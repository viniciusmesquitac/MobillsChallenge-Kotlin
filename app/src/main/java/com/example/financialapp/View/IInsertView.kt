package com.example.financialapp.View

import java.util.Date
import java.sql.Timestamp

interface IInsertView {
    fun handlerDataPicker(day: String, month: String, year: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}