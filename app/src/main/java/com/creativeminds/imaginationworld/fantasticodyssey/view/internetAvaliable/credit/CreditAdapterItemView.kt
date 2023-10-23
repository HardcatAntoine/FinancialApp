package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.credit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.creativeminds.imaginationworld.fantasticodyssey.R
import com.creativeminds.imaginationworld.fantasticodyssey.data.Credit
import com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.loans.LoansClickListener

class CreditAdapterItemView(private val list: List<Credit>) : Adapter<CreditViewHolder>() {
    var clickListener: CreditClickListener? = null
    fun setOnItemClickListener(clickListener: CreditClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_placeholder, parent, false)
        return CreditViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        val item = list[position]
        holder.bindCredit(item, clickListener!!)
    }
}