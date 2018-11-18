package com.gz.jey.realestatemanager.models

data class Amortizations(
        var row: Int? = null,
        var monthly_payed: Float = 0f,
        var interest_refunded: Float = 0f,
        var capital_refunded: Float = 0f,
        var capital_fee: Float = 0f
)