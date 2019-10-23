package com.example.financialapp.model

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
    val paid: Boolean
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
        SimpleDateFormat("d MM yyyy", Locale("pt", "BR")).format(this)


fun Double.formatted(): String {
    val nf = NumberFormat.getInstance()
    val input = nf.format(this)
    return "R$ $input"
}

fun String.formatted(): Double {
    val nf = NumberFormat.getInstance()
    val clean = this.replace("R$", "").replace(",", "")
    val price = nf.parse(clean).toDouble()
    return price
}