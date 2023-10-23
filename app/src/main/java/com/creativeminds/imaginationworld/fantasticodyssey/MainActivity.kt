package com.creativeminds.imaginationworld.fantasticodyssey

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.creativeminds.imaginationworld.fantasticodyssey.api.ApiProvider
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.ActivityMainBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.my.tracker.MyTracker
import kotlinx.coroutines.launch
import org.json.JSONObject
import pro.userx.UserX

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ViewModelResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ViewModelResponse::class.java]
        UserX.init(Store.dataMap[Store.USERX_KEY])

        //MyTrackerInit
        if (viewModel.isInternetAvaliable()) {
            viewModel.init()
            val trackerParams = MyTracker.getTrackerParams()
            trackerParams.customUserId = Settings.Secure.ANDROID_ID

            val trackerConfig = MyTracker.getTrackerConfig()
            trackerConfig.isTrackingLaunchEnabled = true

            MyTracker.setAttributionListener { attribution ->
                val deeplink = attribution.deeplink
                Log.d("debugDeeplink", "DEEPLINK: $deeplink")
                lifecycleScope.launch {
                    viewModel.prepareArguments(applicationContext)
                    val response = getAffSub2(payloadMT = deeplink, "")
                    Store.dataSubs.put(Store.AFF_SUB_2_MT, response)
                    Log.d("debugDeeplinkMT", response.toString())
                }

            }
            MyTracker.initTracker(Store.dataMap[Store.MYTRACKER_KEY], this.application)
            MyTracker.trackLaunchManually(this)
            val deviceIdMT = MyTracker.getInstanceId(applicationContext)
            Store.deviceId[Store.MYTRACKER_DEVICE_ID] = deviceIdMT

            //AppsFlyerInit

            val conversionDataListener = object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    val payload_appsflyer = p0?.toMap().let { JSONObject(it).toString() } ?: ""
                    lifecycleScope.launch {
                        val response = getAffSub2("", payload_appsflyer)
                        Store.dataSubs.put(Store.AFF_SUB_2_AF, response)
                        Log.d("debugDeeplinkAF", response.toString())
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

                }

                override fun onAttributionFailure(p0: String?) {

                }
            }
            AppsFlyerLib.getInstance()
                .init(Store.dataMap[Store.APPSFLYER_KEY]!!, conversionDataListener, this)
            AppsFlyerLib.getInstance().start(this)
            val deviceIdAF = AppsFlyerLib.getInstance().getAppsFlyerUID(applicationContext)
            Store.deviceId[Store.APPSFLYER_DEVICE_ID] = deviceIdAF!!
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        MyTracker.handleDeeplink(intent)

    }

    private suspend fun getAffSub2(payloadMT: String, payloadAF: String): String? {
        val bodyForSub2 = HashMap<String, Any>()
        bodyForSub2["application_token"] = Store.dataMap[Store.API_KEY]!!
        bodyForSub2["user_id"] = Store.requestArguments.p8 ?: ""
        bodyForSub2["payload_appsflyer"] = payloadAF
        bodyForSub2["payload_mytracker"] = payloadMT
        val responseAffSub2 = ApiProvider.getInstance(this).subApiService.getAffSub2(bodyForSub2)
        val jsonObject = JSONObject(responseAffSub2)
        val affSub2Value = jsonObject.getString("affsub2")
        Log.d("affsub2", affSub2Value)
        return affSub2Value
    }
}