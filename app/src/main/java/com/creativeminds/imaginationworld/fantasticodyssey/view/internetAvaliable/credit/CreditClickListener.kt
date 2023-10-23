package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.credit

import com.creativeminds.imaginationworld.fantasticodyssey.data.Credit

interface CreditClickListener {
    fun onDetailsClickListener(position: Int, data: Credit)
    fun onOrderClickListener(position: Int, data: Credit)
}