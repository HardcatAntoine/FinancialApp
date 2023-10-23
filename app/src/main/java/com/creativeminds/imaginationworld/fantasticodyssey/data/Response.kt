package com.creativeminds.imaginationworld.fantasticodyssey.data

data class Response(
    val app_config: AppConfig,
    val cards: List<Card>,
    val credits: List<Credit>,
    val loans: List<Loan>
)