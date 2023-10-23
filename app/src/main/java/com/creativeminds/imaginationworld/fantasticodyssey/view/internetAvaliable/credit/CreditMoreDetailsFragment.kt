package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.credit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.creativeminds.imaginationworld.fantasticodyssey.Event
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard
import com.creativeminds.imaginationworld.fantasticodyssey.data.Credit
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentCreditMoreDetailsBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse
import com.yandex.metrica.YandexMetrica

class CreditMoreDetailsFragment : Fragment() {

    lateinit var binding: FragmentCreditMoreDetailsBinding
    lateinit var viewModel: ViewModelResponse
    private val args : CreditMoreDetailsFragmentArgs by lazy {
        CreditMoreDetailsFragmentArgs.fromBundle(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreditMoreDetailsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         bind(args.Credit)
    }

    private fun bind(item: Credit) {
        Glide
            .with(requireContext())
            .load(item.screen)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(com.google.android.material.R.drawable.mtrl_ic_error)
            .fitCenter()
            .into(binding.imBankLogo)
        binding.firstStarIcon.isSelected = true
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.tvInfo.text = item.description.replace(Regex("<.*?>|&\\w+;"), "")
        binding.tvRating.text = item.score.toDouble().toString()
        setPayIconState(item)
        binding.btnDone.setOnClickListener {
            viewModel.createLink(item.order)
            Event().trackEventMyTracker("external_link")
            Event().trackEventAppsFlyer(
                requireContext().applicationContext,
                "external_link",
                null
            )
            YandexMetrica.reportEvent(
                "external_link",
                "{\"from\": \"more_details\", \"offer\": \"${item.itemId}\", \"url\": \"${viewModel.orderLink}\"}"
            )
            findNavController().navigate(R.id.action_creditMoreDetailsFragment_to_webViewFragment)
        }
    }

    fun setPayIconState(item: Credit) {
        binding.visa.isVisible = item.show_visa == "1"
        binding.mastercard.isVisible = item.show_mastercard == "1"
        binding.qiwi.isVisible = item.show_qiwi == "1"
        binding.mir.isVisible = item.show_mir == "1"
        binding.cash.isVisible = item.show_cash == "1"
        binding.yMoney.isVisible = item.show_yandex == "1"
    }

}