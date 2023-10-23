package com.creativeminds.imaginationworld.fantasticodyssey.data

data class Card(
    val cards_credit: List<AnyCard>,
    val cards_debit: List<AnyCard>,
    val cards_installment: List<AnyCard>
)