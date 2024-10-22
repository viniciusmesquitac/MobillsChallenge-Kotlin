@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.financialapp.model

import java.lang.Exception
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class Expense (
        override val id: String?,
        override var description: String?,
        override var price: Double,
        override val date: Date,
        override var category: String?,
        override var image: String?,
        var paid: Boolean
) : Payment {
    constructor(): this("",
            "",
            0.0,
            Date(),
            "",
            "",
            false)
}


fun Date.formatted(): String =
        SimpleDateFormat("d/MM/yyyy", Locale("pt", "BR")).format(this)


fun Double.formatted(): String {
    val nf = NumberFormat.getInstance()
    val input = nf.format(this)
    return "R$ $input"
}

fun String.formatted() : Double {
    val nf = NumberFormat.getInstance()
    return try {
        val clean = this
                .replace("R$", "")
                .replace(",", "")
                .replace(".", "")
                .replace(" ", "")

        val toDouble = nf.parse(clean).toDouble()
        toDouble
    } catch (e: Exception) {
        print(e.message)
        0.0
    }
}