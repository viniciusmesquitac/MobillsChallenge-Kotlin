package com.example.financialapp.activities

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
import com.example.financialapp.model.formatted
import com.example.financialapp.presenter.InsertPresenter
import com.example.financialapp.R
import com.example.financialapp.service.FirebaseRequest
import com.example.financialapp.view.CurrencyTextWatcher
import com.example.financialapp.view.IInsertView
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_insert_expenses.*
import java.io.IOException
import java.sql.Timestamp
import java.util.*

class InsertExpensesActivity : AppCompatActivity(), IInsertView {

    private lateinit var db: FirebaseRequest
    private lateinit var date: Date
    private var selectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_expenses)
        setSupportActionBar(toolbar_expenses)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = Color.parseColor("#720000")


        // MARK: INITIALIZE PRESENTER
        db = FirebaseRequest()
        val insertPresenter = InsertPresenter(this)

        // MARK - SET CLICK EVENTS
        btnCreatePayment.setOnClickListener {
            val price = editPrice.text.toString()
            val description = editDescription.text.toString()
            val date = editDate.text.toString()
            val category = mySpinner.selectedItem.toString()

            if (price.isEmpty() || date.isEmpty() || description.isEmpty()) {
                insertPresenter.verifyEditTexts(editPrice, editDate, editDescription)
            } else if (selectedUri != null) {
                val filename = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
                btnCreatePayment.isEnabled = false
                btnCreatePayment.isClickable = false

                ref.putFile(selectedUri!!)
                        .addOnSuccessListener {
                            ref.downloadUrl.addOnSuccessListener {
                                val imageUrl = it.toString()
                                saveExpenseInFirebase(price, description, category, imageUrl)
                            }
                        }
            } else {
                saveExpenseInFirebase(price, description, category)
                btnCreatePayment.isClickable = false
            }
        }

        editPrice.addTextChangedListener(CurrencyTextWatcher(editPrice))

        editDate.setOnClickListener {
            insertPresenter.clickDataPicker(this, it)
        }

        btnAddPhoto.setOnClickListener {
            selectPhoto()
        }

        delete_image.setOnClickListener {
            imageView_attach.visibility = View.GONE
            btnAddPhoto.visibility = View.VISIBLE
            delete_image.visibility = View.INVISIBLE
            txtAddDescription.visibility = View.VISIBLE
            selectedUri = null
        }
    }

    private fun saveExpenseInFirebase(price: String, description: String, category: String, img: String = "") {

        db.saveExpenseInFirebase(price.formatted(), description, date, category, img)

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun handlerDataPicker(day: String, month: String, year: String) {
        val calendar = GregorianCalendar(year.toInt(), month.toInt()-1, day.toInt())
        val timestamp = Timestamp(calendar.timeInMillis)
        date = Date(timestamp.time)

        editDate.setText(date.formatted())
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
                        bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver, selectedUri)
                        imageView_attach.setImageBitmap(bitmap)
                        imageView_attach.visibility = View.VISIBLE
                        delete_image.visibility = View.VISIBLE
                        txtAddDescription.visibility = View.INVISIBLE
                        btnAddPhoto.visibility = View.INVISIBLE
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
