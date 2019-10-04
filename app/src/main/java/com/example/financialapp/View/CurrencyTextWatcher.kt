package com.example.financialapp.View

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.Exception
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.NumberFormat
import java.util.*
import java.lang.NumberFormatException as NumberFormatException

class CurrencyTextWatcher(internal var editText: EditText): TextWatcher {

    private var current = ""
    override fun afterTextChanged(p0: Editable?) {
        return
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        return
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(!p0.toString().equals("")) {

            val nf = NumberFormat.getInstance()
            editText.removeTextChangedListener(this)

            // MARK - STRING CLEAN
            val clean = p0.toString().replace("R$", "")
            val clean_dot = clean.replace(".", "")
            val cleanString = clean_dot.replace(",", "")

            try {
                // Converting to local format
                val input = nf.format(cleanString.toDouble())

                // Adding Symbol
                val formatted = "R$"+input

                editText.setText(formatted)
                editText.setSelection(formatted.length)
                editText.addTextChangedListener(this)
            }catch (e: NumberFormatException) {

            }

        }
    }
}