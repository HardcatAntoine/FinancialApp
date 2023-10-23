package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.loans

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativeminds.imaginationworld.fantasticodyssey.Event
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.Loan
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentLoansBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.my.tracker.MyTracker
import com.yandex.metrica.YandexMetrica
import okhttp3.CacheControl

class LoansFragment : Fragment() {

    lateinit var viewModel: ViewModelResponse
    lateinit var binding: FragmentLoansBinding
    private val itemClickListener = object : LoansClickListener {
        override fun onDetailsClickListener(position: Int, data: Loan) {
            val action = LoansFragmentDirections.actionLoansToLoanMoreDetailsFragment(data)
            findNavController().navigate(action)
        }

        override fun onOrderClickListener(position: Int, data: Loan) {
            viewModel.createLink(data.order)
            val url = viewModel.orderLink.value ?: ""
            val action = LoansFragmentDirections.actionLoansToWebViewFragment(url)
            MyTracker.trackEvent("Loan", mapOf(Pair("URL", url)))
            Event().trackEventAppsFlyer(
                requireContext().applicationContext,
                "Loan",
                mapOf(Pair("URL", url))
            )
            YandexMetrica.reportEvent("Loan", url)
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
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        binding = FragmentLoansBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.credits.value == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_credits)
        }
        if (viewModel.creditCards.value == null && viewModel.debitCards.value == null && viewModel.installmentCards == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_cards)
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.page_cards -> {
                    findNavController().navigate(R.id.action_loans_to_cards)
                    true
                }

                R.id.page_credits -> {
                    findNavController().navigate(R.id.action_loans_to_credit)
                    true
                }

                else -> false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loans.observe(viewLifecycleOwner) { loans ->
            val adapter = LoanAdapterItemView(loans)
            binding.rvLoans.adapter = adapter
            adapter.setOnItemClickListener(itemClickListener)
            binding.rvLoans.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}