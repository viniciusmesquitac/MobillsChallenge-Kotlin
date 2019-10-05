package com.example.financialapp.Model

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.sql.Time
import java.sql.Timestamp
import java.util.*

data class Expense (
    val id: String?,
    var description: String?,
    var price: Double,
    val date: Date,
    val paid: Boolean,
    var category: String?,
    var image: String?
) : Parcelable {

    constructor() : this("","",0.0,Timestamp(System.currentTimeMillis()),false,"","")

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readSerializable() as Date,
            parcel.readBoolean(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeSerializable(date)
        parcel.writeBoolean(paid)
        parcel.writeString(category)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }
}