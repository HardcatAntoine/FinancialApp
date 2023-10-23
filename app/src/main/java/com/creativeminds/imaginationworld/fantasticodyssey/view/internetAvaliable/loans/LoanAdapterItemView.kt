package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.loans

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.Loan

class LoanAdapterItemView(private val list: List<Loan>) : RecyclerView.Adapter<LoanViewHolder>() {
    var clickListener: LoansClickListener? = null
    fun setOnItemClickListener(clickListener: LoansClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_placeholder, parent, false)
        return LoanViewHolder(holder)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val item = list[position]
        holder.bindLoan(item,clickListener!!)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}