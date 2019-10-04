package com.example.financialapp.Model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp
import java.util.*

data class Income (
        val id: String?,
        var description: String,
        var price: Double,
        val date: Date,
        val received: Boolean
): Parcelable {

    constructor(): this (
            "",
            "",
            0.0,
            Timestamp(System.currentTimeMillis()),
            false
    )

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString().toString(),
            parcel.readDouble(),
            parcel.readSerializable() as Date,
            parcel.readBoolean()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeSerializable(date)
        parcel.writeByte(if (received) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Income> {
        override fun createFromParcel(parcel: Parcel): Income {
            return Income(parcel)
        }

        override fun newArray(size: Int): Array<Income?> {
            return arrayOfNulls(size)
        }
    }
}