package com.creativeminds.imaginationworld.fantasticodyssey.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.Event
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.my.tracker.MyTracker
import com.yandex.metrica.YandexMetrica

class SplashFragment : Fragment() {

    lateinit var viewModel: ViewModelResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isInternetAvaliable()) {
            findNavController().navigate(R.id.action_splashFragment_to_invalidConnection)
        } else {
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (!isLoading) {
                    if (Store.actualBackend?.actualbackend != "null" && Store.actualBackend?.actualbackend != "" && Store.actualBackend != null) {
                        when {
                            viewModel.loans.value != null -> findNavController().navigate(R.id.action_splashFragment_to_loans)
                            viewModel.credits.value != null -> findNavController().navigate(R.id.action_splashFragment_to_credit)
                            viewModel.creditCards.value != null
                                    || viewModel.debitCards.value != null
                                    || viewModel.installmentCards.value != null ->
                                findNavController().navigate(R.id.action_splashFragment_to_cards)

                            else -> findNavController().navigate(R.id.action_splashFragment_to_userAgreement)

                        }
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_userAgreement)
                        val eventParams = mapOf(Pair("GAID", Store.requestArguments.p8.toString()))
                        MyTracker.trackEvent("actualbackend_null", eventParams)
                        Event().trackEventAppsFlyer(
                            requireContext().applicationContext,
                            "actualbackend_null",
                            eventParams
                        )
                        YandexMetrica.reportEvent("actualbackend_null", eventParams)
                    }
                }
            }

        }

    }

    // проверка наличия интернета
    fun isInternetAvaliable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)
        return activeNetwork != null
    }
}