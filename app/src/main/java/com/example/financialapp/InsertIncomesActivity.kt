package com.example.financialapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialapp.Presenter.InsertPresenter
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.CurrencyTextWatcher
import com.example.financialapp.View.IInsertView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_insert_expenses.*
import kotlinx.android.synthetic.main.activity_insert_incomes.*
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.*

class InsertIncomesActivity : AppCompatActivity(), IInsertView {

    internal lateinit var firebaseRequest: FirebaseRequest
    internal lateinit var pickerDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_incomes)
        setSupportActionBar(toolbar_incomes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = Color.parseColor("#32CD32")

        firebaseRequest = FirebaseRequest()
        val insertPresenter = InsertPresenter(this)

        btnCreateIncome.setOnClickListener {

            val price = editPrice_income.text.toString()
            val descript = editDescription_income.text.toString()
            val date = editDate_income.text.toString()

            if (price.isEmpty() || date.isEmpty() || descript.isEmpty()) {
                insertPresenter.verifyEditTexts(editPrice_income, editDescription_income, editDate_income)
            }
            else {
                val nf = NumberFormat.getInstance()
                val clean = price.replace("R$", "")
                val cleanPrice = clean.replace(",", "")
                val p = nf.parse(cleanPrice).toDouble()

                firebaseRequest.saveIncomeInFirebase("receitas",p, descript, pickerDate)

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        editPrice_income.addTextChangedListener(CurrencyTextWatcher(editPrice_income))
        editDate_income.setOnClickListener {
            insertPresenter.clickDataPicker(this, it)
        }

    }

    override fun handlerDataPicker(day: String, month: String, year: String) {
        val calendar = GregorianCalendar(year.toInt(), month.toInt()-1, day.toInt())
        val timestamp = Timestamp(calendar.timeInMillis)
        pickerDate = Date(timestamp.time)

        editDate_income.setText("" + day+"" + "-" + month + "-" + year)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onErrorResult(message: String) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInformationResult(message: String) {
        Toasty.info(this, message, Toast.LENGTH_SHORT).show()
    }

}
