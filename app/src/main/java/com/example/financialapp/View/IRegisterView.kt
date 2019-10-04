package com.example.financialapp.View

interface IRegisterView {
    fun onRegisterResult(message: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}