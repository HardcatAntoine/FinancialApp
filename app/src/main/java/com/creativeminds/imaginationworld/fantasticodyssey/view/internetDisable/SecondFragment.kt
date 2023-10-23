package com.creativeminds.imaginationworld.fantasticodyssey.view.internetDisable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentSecondBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SecondFragment : Fragment() {
    lateinit var binding: FragmentSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkBoxAgree.setOnClickListener {
            binding.btnAgree.isEnabled = binding.checkBoxAgree.isChecked != false
        }
        binding.btnAgree.setOnClickListener {
            if (checkInputText()) {
                findNavController().navigate(R.id.action_secondFragment_to_timerFragment)
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    fun checkInputText(): Boolean {
        var check: Boolean = false
        when {
            binding.tvSecondName.text.toString() == "" -> errorDialog(
                R.string.error,
                R.string.poleIsNull
            )

            binding.tvName.text.toString() == "" -> errorDialog(
                R.string.error,
                R.string.poleIsNull
            )

            binding.tvFatherName.text.toString() == "" -> errorDialog(
                R.string.error,
                R.string.poleIsNull
            )

            binding.tvPhone.text?.toString() == "" -> errorDialog(
                R.string.error,
                R.string.wrongPhoneFormat
            )

            binding.tvEmail.text.toString() == "" -> errorDialog(
                R.string.error,
                R.string.wrongMailFormat
            )

            else -> check = true
        }
        return check
    }

    fun errorDialog(title: Int, description: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .show()

    }


}