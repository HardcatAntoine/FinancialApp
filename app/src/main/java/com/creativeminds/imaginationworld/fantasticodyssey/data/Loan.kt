package com.creativeminds.imaginationworld.fantasticodyssey.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Loan(
    val id: Int,
    val description: String,
    val extra_field_0: String?,
    val extra_field_1: String?,
    val hide_PercentFields: String,
    val hide_TermFields: String,
    val itemId: String,
    val name: String,
    val order: String,
    val orderButtonText: String,
    val percent: String,
    val percentPostfix: String?,
    val percentPrefix: String,
    val position: Int,
    val score: String,
    val screen: String,
    val show_cash: String,
    val show_mastercard: String,
    val show_mir: String,
    val show_qiwi: String,
    val show_visa: String?,
    val show_yandex: String,
    val summMax: String,
    val summMid: String,
    val summMin: String,
    val summPostfix: String,
    val summPrefix: String,
    val termMax: String,
    val termMid: String,
    val termMin: String,
    val termPostfix: String,
    val termPrefix: String
) : Parcelable