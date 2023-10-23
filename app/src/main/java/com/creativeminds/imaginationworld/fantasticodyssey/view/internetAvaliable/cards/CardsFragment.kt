package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.cards

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
import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentCardsBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.my.tracker.MyTracker
import com.yandex.metrica.YandexMetrica

class CardsFragment : Fragment() {
    lateinit var binding: FragmentCardsBinding
    lateinit var viewModel: ViewModelResponse
    private val adapter = AnyCardAdapter()
    private val itemClickListener = object : CardsClickListener {
        override fun onInfoClickListener(position: Int, data: AnyCard) {
            val action = CardsFragmentDirections.actionCardsToAnyCardMoreDetailsFragment(data)
            findNavController().navigate(action)
        }

        override fun onOrderClickListener(position: Int, data: AnyCard) {
            viewModel.createLink(data.order)
            val url = viewModel.orderLink.value ?: ""
            val action = CardsFragmentDirections.actionCardsToWebViewFragment(url)
            MyTracker.trackEvent("Cards", mapOf(Pair("URL", url)))
            Event().trackEventAppsFlyer(
                requireContext().applicationContext,
                "Cards",
                mapOf(Pair("URL", url))
            )
            YandexMetrica.reportEvent("Cards", url)
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
        binding = FragmentCardsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureAdapter()
        if (viewModel.loans.value == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_loans)
        }
        if (viewModel.credits.value == null) {
            binding.bottomNavigation.menu.removeItem(R.id.page_credits)
        }
        when {
            viewModel.creditCards.value != null -> viewModel.creditCards.observe(viewLifecycleOwner) { creditCard ->
                adapter.updateData(creditCard)
            }

            viewModel.debitCards.value != null -> viewModel.debitCards.observe(viewLifecycleOwner) { debitCard ->
                adapter.updateData(debitCard)
            }

            viewModel.installmentCards.value != null -> viewModel.installmentCards.observe(
                viewLifecycleOwner
            ) { installmetCard ->
                adapter.updateData(installmetCard)
            }
        }

        chipChecked()
        navigation()
    }

    private fun chipChecked() {
        if (viewModel.creditCards.value == null) {
            binding.chipCreditCards.visibility = View.GONE
            binding.chipDebitCards.isChecked = true
        } else {
            binding.chipCreditCards.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    adapter.updateData(viewModel.creditCards.value!!)
                }
            }
        }
        if (viewModel.debitCards.value == null) {
            binding.chipDebitCards.visibility = View.GONE
            binding.chipInstallmentCards.isChecked = true
        } else {
            binding.chipDebitCards.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    adapter.updateData(viewModel.debitCards.value!!)
                }
            }
        }
        if (viewModel.installmentCards.value == null) {
            binding.chipInstallmentCards.visibility = View.GONE
            binding.chipCreditCards.isChecked = true
        } else {
            binding.chipInstallmentCards.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    adapter.updateData(viewModel.installmentCards.value!!)
                }
            }
        }
    }

    fun navigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_loans -> {
                    findNavController().navigate(R.id.action_cards_to_loans)
                    true
                }

                R.id.page_credits -> {
                    findNavController().navigate(R.id.action_cards_to_credit)
                    true
                }

                else -> false
            }
        }

    }

    private fun configureAdapter() {
        binding.rvCards.adapter = adapter
        adapter.setOnItemClickListener(itemClickListener)
        binding.rvCards.layoutManager = LinearLayoutManager(requireContext())
    }
}