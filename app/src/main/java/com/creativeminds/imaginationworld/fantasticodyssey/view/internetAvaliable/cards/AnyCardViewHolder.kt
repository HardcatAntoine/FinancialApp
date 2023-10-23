package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.cards

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.ItemPlaceholderBinding

class AnyCardViewHolder(val binding: ItemPlaceholderBinding) : ViewHolder(binding.root) {

    fun bind(data: AnyCard, clickListener: CardsClickListener?) {
        Glide
            .with(itemView)
            .load(data.screen)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(com.google.android.material.R.drawable.mtrl_ic_error)
            .fitCenter()
            .into(binding.imBankLogo)
        binding.tvBank.text = data.name
        binding.tvSumRange.text =
            "${data.summPrefix} ${data.summMin} ${data.summMid} ${data.summMax} ${data.summPostfix}"
        binding.tvPercent.text = "${data.percentPrefix} ${data.percent}${data.percentPostfix}"
        binding.tvTerm.text =
            "${data.termPrefix} ${data.termMin} ${data.termMid} ${data.termMax} ${data.termPostfix}"
        binding.tvRating.text = data.score.toDouble().toString()

        binding.btnArrange.setOnClickListener {
            clickListener?.onOrderClickListener(position, data)
        }
        binding.imBankLogo.setOnClickListener {
            clickListener?.onInfoClickListener(position, data)
        }
        binding.buttonDetails.setOnClickListener {
            clickListener?.onInfoClickListener(position, data)
        }
        setStarIconState(data.score)
        setPayIconState(data)
    }

    fun setStarIconState(score: String) {
        val i = score.toFloat()
        when {
            i >= 1 && i < 2 -> {
                binding.firstStarIcon.isSelected = true
                binding.secondStarIcon.isSelected = false
                binding.thirdStarIcon.isSelected = false
                binding.fourthStarIcom.isSelected = false
                binding.fifthStarIcon.isSelected = false
            }

            i >= 2 && i < 3 -> {
                binding.firstStarIcon.isSelected = true
                binding.secondStarIcon.isSelected = true
                binding.thirdStarIcon.isSelected = false
                binding.fourthStarIcom.isSelected = false
                binding.fifthStarIcon.isSelected = false
            }

            i >= 3 && i < 4 -> {
                binding.firstStarIcon.isSelected = true
                binding.secondStarIcon.isSelected = true
                binding.thirdStarIcon.isSelected = true
                binding.fourthStarIcom.isSelected = false
                binding.fifthStarIcon.isSelected = false
            }

            i >= 4 && i < 5 -> {
                binding.firstStarIcon.isSelected = true
                binding.secondStarIcon.isSelected = true
                binding.thirdStarIcon.isSelected = true
                binding.fourthStarIcom.isSelected = true
                binding.fifthStarIcon.isSelected = false
            }

            i == 5f -> {
                binding.firstStarIcon.isSelected = true
                binding.secondStarIcon.isSelected = true
                binding.thirdStarIcon.isSelected = true
                binding.fourthStarIcom.isSelected = true
                binding.fifthStarIcon.isSelected = true
            }
        }
    }

    fun setPayIconState(item: AnyCard) {
        binding.visa.isVisible = item.show_visa == "1"
        binding.mastercard.isVisible = item.show_mastercard == "1"
        binding.qiwi.isVisible = item.show_qiwi == "1"
        binding.mir.isVisible = item.show_mir == "1"
        binding.cash.isVisible = item.show_cash == "1"
        binding.yMoney.isVisible = item.show_yandex == "1"
    }
}