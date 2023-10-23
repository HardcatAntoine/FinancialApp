package com.creativeminds.imaginationworld.fantasticodyssey.view.internetDisable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentInvalidConnectionBinding
import com.creativeminds.imaginationworld.fantasticodyssey.viewModels.ViewModelResponse

class InvalidConnection : Fragment() {
    lateinit var binding: FragmentInvalidConnectionBinding
    lateinit var viewModel: ViewModelResponse
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ViewModelResponse::class.java]
        binding = FragmentInvalidConnectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            viewModel.init()
            findNavController().navigate(R.id.action_invalidConnection_to_splashFragment)
        }
    }
}