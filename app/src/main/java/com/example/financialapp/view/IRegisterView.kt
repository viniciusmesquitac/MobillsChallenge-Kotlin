package com.example.financialapp.view

interface IRegisterView {
    fun onRegisterResult(message: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}