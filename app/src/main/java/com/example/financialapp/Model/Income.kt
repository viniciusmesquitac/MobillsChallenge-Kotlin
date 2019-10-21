package com.example.financialapp.Model


import java.util.*

data class Income (
        override val id: String?,
        override var description: String?,
        override var price: Double,
        override val date: Date,
        override var category: String?,
        override var image: String?,
        val received: Boolean
): Payment {
    constructor(): this ("",
            "",
            0.0,
            Date(),
            "",
            "",
            false)
}