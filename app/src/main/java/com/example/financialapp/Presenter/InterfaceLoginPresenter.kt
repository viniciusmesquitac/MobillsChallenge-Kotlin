package com.example.financialapp.Presenter

import android.app.Activity
import android.content.Intent

interface InterfaceLoginPresenter {
    fun onLogin(context: Activity, email: String, password: String)
    fun verifyAuthStatus(context: Activity)
    fun onLogout(context: Activity)

}