package com.example.financialapp.presenter

import android.app.Activity

interface InterfaceLoginPresenter {
    fun onLogin(context: Activity, email: String, password: String)
    fun verifyAuthStatus(context: Activity)
    fun onLogout(context: Activity)

}