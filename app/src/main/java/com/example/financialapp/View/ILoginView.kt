package com.example.financialapp.View

interface ILoginView {
    fun onLoginResult(message: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}