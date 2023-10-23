package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeminds.imaginationworld.fantasticodyssey.Event
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.Credit
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentCreditBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.my.tracker.MyTracker
import com.yandex.metrica.YandexMetrica

class CreditFragment : Fragment() {
    lateinit var binding: FragmentCreditBinding
    lateinit var viewModel: ViewModelResponse
    private val itemClickListener = object : CreditClickListener {
        override fun onDetailsClickListener(position: Int, data: Credit) {
            val action = CreditFragmentDirections.actionCreditToCreditMoreDetailsFragment(data)
            findNavController().navigate(action)
        }

        override fun onOrderClickListener(position: Int, data: Credit) {
            viewModel.createLink(data.order)
            val url = viewModel.orderLink.value ?: ""
            val action = CreditFragmentDirections.actionCreditToWebViewFragment(url)
            MyTracker.trackEvent("Credit", mapOf(Pair("URL", url)))
            Event().trackEventAppsFlyer(
                requireContext().applicationContext,
                "Credit",
                mapOf(Pair("URL", url))
            )
            YandexMetrica.reportEvent("Credit", url)
            Event().trackEventMyTracker("external_link")
            Event().trackEventAppsFlyer(
                requireContext().applicationContext,
                "external_link",
                null
            )
            YandexMetrica.reportEvent(
                "external_link",
                "{\"from\": \"offerwall\", \"offer\": \"${data.itemId}\", \"url\": \"$url\"}"
            )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreditBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.loans.value == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_loans)
        }
        if (viewModel.creditCards.value == null && viewModel.debitCards.value == null && viewModel.installmentCards == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_cards)
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_loans -> {
                    findNavController().navigate(R.id.action_credit_to_loans)
                    true
                }

                R.id.page_cards -> {
                    findNavController().navigate(R.id.action_credit_to_cards)
                    true
                }

                else -> false
            }
        }
        viewModel.credits.observe(viewLifecycleOwner) { credits ->
            val adapter = CreditAdapterItemView(credits)
            binding.rvCredits.adapter = adapter
            adapter.setOnItemClickListener(itemClickListener)
            binding.rvCredits.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}