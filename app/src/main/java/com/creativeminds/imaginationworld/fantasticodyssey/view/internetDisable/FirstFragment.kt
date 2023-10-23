package com.creativeminds.imaginationworld.fantasticodyssey.view.internetDisable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.FragmentFirstBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow


class FirstFragment : Fragment() {

    lateinit var binding: FragmentFirstBinding
    private var loanAmount = 5000
    private var loanTerm = 95

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.seekBarAmount.max = 30 - 5
        binding.seekBarTerm.max = 360 - 95
        binding.seekBarAmount.progress = 11
        binding.seekBarTerm.progress = 120
        updateLoanSummary()

        binding.seekBarAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loanAmount = (progress + 5) * 1000
                updateLoanSummary()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.seekBarTerm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loanTerm = progress + 95
                updateLoanSummary()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.btnAccept.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    fun updateLoanSummary() {
        val interestRate = 0.1
        val loanDays = loanTerm
        val totalAmount = loanAmount * (1 + interestRate / 100).pow(loanDays)
        val decimal = BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_EVEN)
        binding.tvLoan.text = loanAmount.toString()
        binding.tvTerm.text = loanTerm.toString()
        binding.tvSum.text = decimal.toString()
    }


}



