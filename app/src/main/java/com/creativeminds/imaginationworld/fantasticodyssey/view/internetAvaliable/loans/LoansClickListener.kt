package com.creativeminds.imaginationworld.fantasticodyssey.view.internetAvaliable.loans

import com.creativeminds.imaginationworld.fantasticodyssey.data.Loan

interface LoansClickListener {
    fun onDetailsClickListener(position: Int, data: Loan)
    fun onOrderClickListener(position: Int, data: Loan)
}