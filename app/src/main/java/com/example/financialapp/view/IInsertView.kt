package com.example.financialapp.view

interface IInsertView {
    fun handlerDataPicker(day: String, month: String, year: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}