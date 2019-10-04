package com.example.financialapp.Presenter

import android.app.Activity
import android.os.Build
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.util.*

interface InterfaceInsertPresenter {

    fun clickDataPicker(context: Activity, view: View)

    fun verifyEditTexts(et0: EditText, et1: EditText, et2: EditText)

}