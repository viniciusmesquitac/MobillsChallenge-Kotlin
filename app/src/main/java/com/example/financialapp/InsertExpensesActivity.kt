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
import java.sql.Timestamp
import java.util.*

class InsertExpensesActivity : AppCompatActivity(), IInsertView {

    internal lateinit var firebaseRequest: FirebaseRequest
    internal lateinit var pickerDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expenses)
        setSupportActionBar(toolbar_expenses)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = Color.parseColor("#720000")


        // MARK: INITIALIZE PRESENTER

        firebaseRequest = FirebaseRequest()
        val insertPresenter = InsertPresenter(this)

        // MARK - SET CLICK EVENTS
        btnCreatePayment.setOnClickListener {
            val price = editPrice.text.toString()
            val descript = editDescription.text.toString()
            val date = editDate.text.toString()
            val category = mySpinner.selectedItem.toString()

            if (price.isEmpty() || date.isEmpty() || descript.isEmpty()){
                insertPresenter.verifyEditTexts(editPrice, editDate, editDescription)
            }else {

                val clean = price.replace("R$", "")
                val cleanPrice = clean.replace(",", "")
                val p = cleanPrice.toDouble()

                firebaseRequest.saveExpenseInFirebase("despesas",p,
                        descript,
                        pickerDate,
                        category)

                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            }
        }

         editPrice.addTextChangedListener(CurrencyTextWatcher(editPrice))

        editDate.setOnClickListener {
            insertPresenter.clickDataPicker(this, it)
        }
    }

    override fun handlerDataPicker(day: String, month: String, year: String) {
        val calendar = GregorianCalendar(year.toInt(), month.toInt()-1, day.toInt())
        val timestamp = Timestamp(calendar.timeInMillis)
        pickerDate = Date(timestamp.time)

        editDate.setText("" + day+"" + "-" + month + "-" + year)
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
