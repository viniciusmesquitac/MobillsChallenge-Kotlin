package com.example.financialapp.presenter

import android.app.Activity
import android.view.View
import android.widget.EditText

interface InterfaceInsertPresenter {

    fun clickDataPicker(context: Activity, view: View)

    fun verifyEditTexts(et0: EditText, et1: EditText, et2: EditText)

}