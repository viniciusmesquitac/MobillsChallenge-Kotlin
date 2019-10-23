package com.example.financialapp.presenter

interface InterfaceRegisterPresenter {
    fun onCreateUser(username: String, password: String, email: String)
    fun onSaveUserInFirebase(username: String)
}