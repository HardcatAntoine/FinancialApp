package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard
import com.creativeminds.imaginationworld.fantasticodyssey.databinding.ItemPlaceholderBinding

class AnyCardAdapter : Adapter<AnyCardViewHolder>() {

    private var list: List<AnyCard> = emptyList()
    var clickListener: CardsClickListener? = null
    fun setOnItemClickListener(clickListener: CardsClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyCardViewHolder {
        val binding =
            ItemPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnyCardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AnyCardViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data, clickListener)
    }

    fun updateData(list: List<AnyCard>) {
        this.list = list
        notifyDataSetChanged()
    }
}