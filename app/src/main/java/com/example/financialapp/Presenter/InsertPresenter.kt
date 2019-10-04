package com.example.financialapp.Presenter

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.view.View
import android.widget.EditText
import com.example.financialapp.View.IInsertView
import java.util.*

class InsertPresenter(internal var iInsertView: IInsertView): InterfaceInsertPresenter {

    override fun verifyEditTexts(et0: EditText, et1: EditText, et2: EditText) {
        iInsertView.onInformationResult("Preencha todos os campos!")
    }

    // MARK - SET DATE OF CALENDAR PICKER FOR SEND TO FIREBASE
    override fun clickDataPicker(context: Activity, view: View) {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            iInsertView.handlerDataPicker(dayOfMonth.toString(), (monthOfYear + 1).toString(), year.toString() )
        }, year, month, day)
        dpd.show()
    }
}