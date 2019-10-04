package com.example.financialapp.Presenter

interface InterfaceRegisterPresenter {
    fun onCreateUser(username: String, password: String, email: String)
    fun onSaveUserInFirebase(username: String)
}