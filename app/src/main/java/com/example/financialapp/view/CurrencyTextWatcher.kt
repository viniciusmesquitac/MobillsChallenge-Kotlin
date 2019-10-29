package com.example.financialapp.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.financialapp.model.formatted
import java.text.NumberFormat
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
        if(p0.toString() != "") {

            editText.removeTextChangedListener(this)

            try {
                // Converting to local format
                val formatted = p0.toString().formatted().formatted()

                editText.setText(formatted)
                editText.setSelection(formatted.length)
                editText.addTextChangedListener(this)
            }catch (e: NumberFormatException) {

            }

        }
    }
}