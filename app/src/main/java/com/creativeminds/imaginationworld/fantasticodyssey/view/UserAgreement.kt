package com.creativeminds.imaginationworld.fantasticodyssey.view

import android.content.SharedPreferences
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.api.PrefHelper
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentUserAgreementBinding

class UserAgreement : Fragment() {
    lateinit var binding: FragmentUserAgreementBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAgreementBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefHelper = PrefHelper.getInstance(requireContext())
        if (prefHelper.getSavedUserAgree()) {
            findNavController().navigate(R.id.action_userAgreement_to_firstFragment)
        } else {
            binding.checkBox.setOnClickListener {
                binding.acceptButton.isEnabled = binding.checkBox.isChecked != false
            }
            binding.acceptButton.setOnClickListener {
                prefHelper.saveUserAgree(true)
                findNavController().navigate(R.id.action_userAgreement_to_firstFragment)
            }
        }
    }

}