package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.cards

import com.creativeminds.imaginationworld.fantasticodyssey.data.AnyCard

interface CardsClickListener {
    fun onInfoClickListener(position: Int, data: AnyCard)
    fun onOrderClickListener(position: Int, data: AnyCard)
}