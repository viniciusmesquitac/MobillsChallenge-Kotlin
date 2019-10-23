package com.example.financialapp.view

interface ILoginView {
    fun onLoginResult(message: String)
    fun onErrorResult(message: String)
    fun onInformationResult(message: String)
}