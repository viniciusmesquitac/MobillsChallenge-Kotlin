package com.example.financialapp.Model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.sql.Time
import java.sql.Timestamp
import java.util.*

interface Payment {
    val id: String?
    var description: String?
    var price: Double
    val date: Date
    var category: String?
    var image: String?
}

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
