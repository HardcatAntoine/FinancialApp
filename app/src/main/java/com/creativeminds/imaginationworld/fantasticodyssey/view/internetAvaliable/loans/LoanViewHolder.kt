package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.loans

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.Loan

class LoanViewHolder(itemView: View) : ViewHolder(itemView) {

    private val bankLogo = itemView.findViewById<ImageView>(R.id.imBankLogo)
    private val bank = itemView.findViewById<TextView>(R.id.tvBank)
    private val sum = itemView.findViewById<TextView>(R.id.tvSumRange)
    private val percent = itemView.findViewById<TextView>(R.id.tvPercent)
    private val term = itemView.findViewById<TextView>(R.id.tvTerm)
    private val rating = itemView.findViewById<TextView>(R.id.tvRating)
    private val firstStar = itemView.findViewById<ImageView>(R.id.firstStarIcon)
    private val secondStar = itemView.findViewById<ImageView>(R.id.secondStarIcon)
    private val thirdStar = itemView.findViewById<ImageView>(R.id.thirdStarIcon)
    private val fourthStar = itemView.findViewById<ImageView>(R.id.fourthStarIcom)
    private val fifthStar = itemView.findViewById<ImageView>(R.id.fifthStarIcon)
    private val visa = itemView.findViewById<ImageView>(R.id.visa)
    private val masterCard = itemView.findViewById<ImageView>(R.id.mastercard)
    private val yMoney = itemView.findViewById<ImageView>(R.id.y_money)
    private val qiwi = itemView.findViewById<ImageView>(R.id.qiwi)
    private val mir = itemView.findViewById<ImageView>(R.id.mir)
    private val cash = itemView.findViewById<ImageView>(R.id.cash)
    private val btnDetails = itemView.findViewById<Button>(R.id.buttonDetails)
    private val btnOrder = itemView.findViewById<Button>(R.id.btnArrange)


    fun bindLoan(item: Loan, clickListener: LoansClickListener) {
        Glide
            .with(itemView)
            .load(item.screen)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(com.google.android.material.R.drawable.mtrl_ic_error)
            .fitCenter()
            .into(bankLogo)
        bank.text = item.name
        sum.text =
            "${item.summPrefix} ${item.summMin} ${item.summMid} ${item.summMax} ${item.summPostfix}"
        percent.text = "${item.percentPrefix} ${item.percent}${item.percentPostfix}"
        term.text =
            "${item.termPrefix} ${item.termMin} ${item.termMid} ${item.termMax} ${item.termPostfix}"
        rating.text = item.score.toDouble().toString()
        setStarIconState(item.score)
        setPayIconState(item)
        btnDetails.setOnClickListener {
            clickListener?.onDetailsClickListener(position, item)
        }
        bankLogo.setOnClickListener {
            clickListener?.onDetailsClickListener(position, item)
        }
        btnOrder.setOnClickListener {
            clickListener?.onOrderClickListener(position, item)
        }

    }

    private fun setStarIconState(score: String) {
        val i = score.toFloat()
        when {
            i >= 1 && i < 2 -> {
                firstStar.isSelected = true
                secondStar.isSelected = false
                thirdStar.isSelected = false
                fourthStar.isSelected = false
                fifthStar.isSelected = false
            }

            i >= 2 && i < 3 -> {
                firstStar.isSelected = true
                secondStar.isSelected = true
                thirdStar.isSelected = false
                fourthStar.isSelected = false
                fifthStar.isSelected = false
            }

            i >= 3 && i < 4 -> {
                firstStar.isSelected = true
                secondStar.isSelected = true
                thirdStar.isSelected = true
                fourthStar.isSelected = false
                fifthStar.isSelected = false
            }

            i >= 4 && i < 5 -> {
                firstStar.isSelected = true
                secondStar.isSelected = true
                thirdStar.isSelected = true
                fourthStar.isSelected = true
                fifthStar.isSelected = false
            }

            i == 5f -> {
                firstStar.isSelected = true
                secondStar.isSelected = true
                thirdStar.isSelected = true
                fourthStar.isSelected = true
                fifthStar.isSelected = true
            }
        }
    }

    private fun setPayIconState(item: Loan) {
        visa.isVisible = item.show_visa == "1"
        masterCard.isVisible = item.show_mastercard == "1"
        qiwi.isVisible = item.show_qiwi == "1"
        mir.isVisible = item.show_mir == "1"
        cash.isVisible = item.show_cash == "1"
        yMoney.isVisible = item.show_yandex == "1"
    }
}