package com.pontini.food.delivery.impl.android.presentation.menu

import java.text.NumberFormat
import java.util.Locale

private val BRAZILIAN_CURRENCY_FORMAT: NumberFormat =
    NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

fun formatPrice(value: Double): String = BRAZILIAN_CURRENCY_FORMAT.format(value)
