package com.creativeminds.imaginationworld.fantasticodyssey.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creativeminds.imaginationworld.fantasticodyssey.BankApplication
import com.creativeminds.imaginationworld.fantasticodyssey.Event
import com.creativeminds.imaginationworld.fantasticodyssey.api.ApiProvider
import com.creativeminds.imaginationworld.fantasticodyssey.api.ParamsHelper
import com.creativeminds.imaginationworld.fantasticodyssey.api.PrefHelper
import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard
import com.creativeminds.imaginationworld.fantasticodyssey.data.Backend
import com.creativeminds.imaginationworld.fantasticodyssey.data.Credit
import com.creativeminds.imaginationworld.fantasticodyssey.data.LastModified
import com.creativeminds.imaginationworld.fantasticodyssey.data.Loan
import com.creativeminds.imaginationworld.fantasticodyssey.data.ParamsForQuery
import com.creativeminds.imaginationworld.fantasticodyssey.data.Response
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.AFF_SUB_1
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.AFF_SUB_2
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.AFF_SUB_3
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.APPMETRICA_DEVICE_ID
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.actualBackend
import com.creativeminds.imaginationworld.fantasticodyssey.data.Store.requestArguments
import com.google.gson.GsonBuilder
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.launch
import org.json.JSONObject

class ViewModelResponse(application: Application) : AndroidViewModel(application) {

    val prefHelper = PrefHelper.getInstance(application.applicationContext)

    private val gson = GsonBuilder().create()
    private val apiProvider = ApiProvider.getInstance(application.applicationContext)

    private var _loans = MutableLiveData<List<Loan>>()
    val loans: LiveData<List<Loan>>
        get() = _loans
    private var _credits = MutableLiveData<List<Credit>>()
    val credits: LiveData<List<Credit>>
        get() = _credits
    private var _creditCards = MutableLiveData<List<AnyCard>>()
    val creditCards: LiveData<List<AnyCard>>
        get() = _creditCards
    private var _debitCards = MutableLiveData<List<AnyCard>>()
    val debitCards: LiveData<List<AnyCard>>
        get() = _debitCards
    private var _installmentCards = MutableLiveData<List<AnyCard>>()
    val installmentCards: LiveData<List<AnyCard>>
        get() = _installmentCards

    private var _orderLink = MutableLiveData<String>()
    val orderLink: LiveData<String>
        get() = _orderLink
    private var _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun init() {
        Log.d("DEBUG SPIDER NET", "ViewModelResponse init")
        viewModelScope.launch {
            if (isInternetAvaliable()) {
                prepareArguments(getApplication<BankApplication>().applicationContext)
                getActualBackend()
                if (!needToRequestRemote()) {
                    apiProvider.clearCache()
                    getProducts()
                } else {
                    getProducts()
                }
                getAffSub1()
                getAffSub3()
                getAffSub5()
                _isLoading.value = false
            } else {
                _isLoading.value = false
                return@launch
            }
        }
    }

    init {

    }

    private suspend fun getActualBackend() {
        val actualBackend: Backend? =
            apiProvider.apiService.getDbJson(
                p1 = requestArguments.p1,
                p2 = requestArguments.p2,
                p3 = requestArguments.p3,
                p4 = requestArguments.p4,
                p5 = requestArguments.p5,
                p6 = requestArguments.p6,
                p7 = requestArguments.p7,
                p8 = requestArguments.p8,
                p9 = requestArguments.p9,
                p10 = requestArguments.p10,
                p11 = requestArguments.p11,
                p12 = requestArguments.p12,
                p13 = requestArguments.p13,
                p14 = requestArguments.p14,
            )
        Store.actualBackend = actualBackend
    }


    private suspend fun getProducts() {
        if (actualBackend == null || actualBackend?.actualbackend == "" || actualBackend?.actualbackend == "null") return

        val response: Response? = actualBackend?.actualbackend?.let {
            apiProvider.apiService.getResponse(
                it
            )
        }

        if (response == null) {
            Event().trackEventMyTracker("requestdb")
            Event().trackEventAppsFlyer(
                getApplication<Application>().applicationContext,
                "requestdb",
                null
            )
            YandexMetrica.reportEvent("requestdb")
            return
        } else {
            if (response?.loans != null) {
                _loans.value = response.loans
            }
            if (response?.credits != null) {
                _credits.value = response.credits
            }
            if (response?.cards != null) {
                response.cards.map { card ->
                    if (card.cards_credit != null)
                        _creditCards.value = card.cards_credit
                    if (card.cards_debit != null)
                        _debitCards.value = card.cards_debit
                    if (card.cards_installment != null)
                        _installmentCards.value = card.cards_installment
                }
            }
        }
    }

    suspend fun prepareArguments(context: Context) {
        val auth = ParamsHelper.generateLink(context)
        requestArguments = ParamsForQuery(
            auth.p1,
            auth.p2,
            auth.p3,
            auth.p4,
            auth.p5,
            auth.p6,
            auth.p7,
            auth.p8,
            auth.p9,
            auth.p10,
            auth.p11,
            auth.p12,
            auth.p13,
            auth.p14
        )
    }

    private suspend fun needToRequestRemote(): Boolean {
        var lastModifiedDate: LastModified? = null
        try {
            lastModifiedDate = actualBackend?.actualbackend?.let {
                apiProvider.apiService.getRefreshDate(
                    it
                )
            }
        } catch (e: Exception) {
            Log.e("error", e.message ?: "")
        }
        if (lastModifiedDate == null) return false

        val isDatesMatch: Boolean =
            prefHelper.getLastModifiedDate() == lastModifiedDate.date
        if (!isDatesMatch) {
            prefHelper.saveLastDateModified(
                lastModifiedDate.date
            )
        }
        Log.d("myTag", lastModifiedDate.date)
        Event().trackEventMyTracker("requestdate")
        Event().trackEventAppsFlyer(
            getApplication<Application>().applicationContext,
            "requestdate",
            null
        )
        YandexMetrica.reportEvent("requestdate")
        return isDatesMatch
    }

    fun createLink(order: String) {
        viewModelScope.launch {
            checkResponseForAffSub2()
            val link =
                "${order}&aff_sub1=${Store.dataSubs[AFF_SUB_1]}&aff_sub2=${Store.dataSubs[AFF_SUB_2]}&aff_sub3=${Store.dataSubs[AFF_SUB_3]}&aff_sub4=not_available&aff_sub5=${requestArguments.p8}"
            _orderLink.value = link
            Log.d("myTag", "ORDER_LINK: ${_orderLink.value!!}")
        }
    }

    private suspend fun getAffSub1() {
        val mapForRequestSub1 =
            mapOf(
                Pair("AppMetricaDeviceID", Store.deviceId[APPMETRICA_DEVICE_ID]),
                Pair("Appsflyer", Store.deviceId[Store.APPSFLYER_DEVICE_ID]),
                Pair("FireBase", requestArguments.p7?.substring(0, 30)),
                Pair("MyTracker", Store.deviceId[Store.MYTRACKER_DEVICE_ID])
            )
        val payload_affsub1 = gson.toJson(mapForRequestSub1)
        val bodyForSub1 = HashMap<String, Any>()
        bodyForSub1["application_token"] = Store.dataMap[Store.API_KEY]!!
        bodyForSub1["user_id"] = requestArguments.p8!!
        bodyForSub1["payload_affsub1"] = payload_affsub1

        val responseAffSub1 = apiProvider.subApiService.getAffSub1(bodyForSub1)
        val jsonObject = JSONObject(responseAffSub1)
        val affSub1Value = jsonObject.getString("affsub1")
        Store.dataSubs.put(Store.AFF_SUB_1, affSub1Value)
        Log.d("affsub1", affSub1Value)
    }

    private suspend fun getAffSub3() {
        val mapForRequestSub3 =
            mapOf(Pair("FireBaseToken", requestArguments.p7))
        val payload_affsub3 = gson.toJson(mapForRequestSub3)
        val bodyForSub3 = HashMap<String, Any>()
        bodyForSub3["application_token"] = Store.dataMap[Store.API_KEY]!!
        bodyForSub3["user_id"] = requestArguments.p8!!
        bodyForSub3["payload_affsub3"] = payload_affsub3
        val responseAffSub3 = apiProvider.subApiService.getAffSub3(bodyForSub3)
        val jsonObject = JSONObject(responseAffSub3)
        val affSub3Value = jsonObject.getString("affsub3")
        Store.dataSubs.put(Store.AFF_SUB_3, affSub3Value)
        Log.d("affsub3", affSub3Value)
    }

    private suspend fun getAffSub5() {
        val mapForRequestSub5 = mapOf(Pair("GAID", requestArguments.p8))
        val payload_sub5 = gson.toJson(mapForRequestSub5)
        val bodyForSub5 = HashMap<String, Any>()
        bodyForSub5["application_token"] = Store.dataMap[Store.API_KEY]!!
        bodyForSub5["user_id"] = requestArguments.p8!!
        bodyForSub5["payload_affsub5"] = payload_sub5
        val responseAffSub5 = apiProvider.subApiService.getAffSub5(bodyForSub5)
        val jsonObject = JSONObject(responseAffSub5)
        val affSub5Value = jsonObject.getString("affsub5")
        Store.dataSubs.put(Store.AFF_SUB_5, affSub5Value)
        Log.d("affsub5", affSub5Value)
    }

    private fun checkResponseForAffSub2() {
        if (Store.dataSubs[Store.AFF_SUB_2_MT] == null
            || Store.dataSubs[Store.AFF_SUB_2_MT] == "null"
            || Store.dataSubs[Store.AFF_SUB_2_MT] == ""
            || Store.dataSubs[Store.AFF_SUB_2_MT].isNullOrBlank()
        ) {
            Store.dataSubs[Store.AFF_SUB_2] = Store.dataSubs[Store.AFF_SUB_2_AF]
        } else {
            Store.dataSubs[Store.AFF_SUB_2] = Store.dataSubs[Store.AFF_SUB_2_MT]
        }
    }

    fun isInternetAvaliable(): Boolean {
        val connectivityManager =
            getApplication<BankApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)
        return activeNetwork != null
    }
}