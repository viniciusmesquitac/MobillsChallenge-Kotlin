package com.example.financialapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.financialapp.Presenter.InsertPresenter
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.CurrencyTextWatcher
import com.example.financialapp.View.IInsertView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_insert_expenses.*
import kotlinx.android.synthetic.main.activity_insert_incomes.*
import java.io.IOException
import java.sql.Timestamp
import java.text.NumberFormat
import java.util.*

class InsertIncomesActivity : AppCompatActivity(), IInsertView {

    internal lateinit var firebaseRequest: FirebaseRequest
    internal lateinit var pickerDate: Date
    private var selectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_incomes)
        setSupportActionBar(toolbar_incomes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = Color.parseColor("#009c49")

        firebaseRequest = FirebaseRequest()
        val insertPresenter = InsertPresenter(this)

        btnCreateIncome.setOnClickListener {

            val price = editPrice_income.text.toString()
            val descript = editDescription_income.text.toString()
            val date = editDate_income.text.toString()
            val category = mySpinner_income.selectedItem.toString()

            if (price.isEmpty() || date.isEmpty() || descript.isEmpty()) {
                insertPresenter.verifyEditTexts(editPrice_income, editDescription_income, editDate_income)
            }
            else if (selectedUri != null) {
                val filename = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

                ref.putFile(selectedUri!!)
                        .addOnSuccessListener(OnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                val imageUrl = it.toString()
                                saveIncomeInFirebase(price, descript, category, imageUrl)
                                btnCreateIncome?.isClickable = false
                            }
                        })

            } else {
                saveIncomeInFirebase(price,descript,category)
                btnCreateIncome?.isClickable = false
            }
        }

        editPrice_income.addTextChangedListener(CurrencyTextWatcher(editPrice_income))

        editDate_income.setOnClickListener {
            insertPresenter.clickDataPicker(this, it)
        }

        btnAddPhoto_income.setOnClickListener {
            selectPhoto()
        }

    }

    private fun saveIncomeInFirebase(price: String, d: String, c: String, img: String = "") {
        val nf = NumberFormat.getInstance()
        val clean = price.replace("R$", "")
        val cleanPrice = clean.replace(",", "")
        val p = nf.parse(cleanPrice).toDouble()

        firebaseRequest.saveExpenseInFirebase("receitas",p, d, pickerDate, c, img)

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(intent, 1000)
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, 0)
    }

    override fun handlerDataPicker(day: String, month: String, year: String) {
        val calendar = GregorianCalendar(year.toInt(), month.toInt()-1, day.toInt())
        val timestamp = Timestamp(calendar.timeInMillis)
        pickerDate = Date(timestamp.time)

        editDate_income.setText("$day/$month/$year")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            Activity.RESULT_OK -> {
                if(data?.data != null){
                    selectedUri = data.data!!
                } else {
                    finish()
                }
                val bitmap: Bitmap
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(this.contentResolver, selectedUri!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        imageView_attach.setImageBitmap(bitmap)
                        btnAddPhoto.visibility = View.GONE
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver, selectedUri)
                        imageView_attach_income.setImageBitmap(bitmap)
                        imageView_attach_income.visibility = View.VISIBLE
                        txt_image_income.visibility = View.INVISIBLE
                        btnAddPhoto_income.visibility = View.GONE
                    }


                } catch (e: IOException) {
                    finish()
                }
            }
        }
    }

    override fun onErrorResult(message: String) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInformationResult(message: String) {
        Toasty.info(this, message, Toast.LENGTH_SHORT).show()
    }

}
