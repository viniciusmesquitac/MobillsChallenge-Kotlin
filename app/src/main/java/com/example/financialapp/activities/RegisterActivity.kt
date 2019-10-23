package com.example.financialapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialapp.presenter.InterfaceRegisterPresenter
import com.example.financialapp.presenter.RegisterPresenter
import com.example.financialapp.R
import com.example.financialapp.view.IRegisterView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editEmail
import kotlinx.android.synthetic.main.activity_register.editPassword

class RegisterActivity : AppCompatActivity(), IRegisterView {

    private lateinit var registerPresenter: InterfaceRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        // MARK: INIT PRESENTER
        registerPresenter = RegisterPresenter(this)

        // MARK: START EVENTS
        btnRegister.setOnClickListener {
            registerPresenter.onCreateUser(editUsername.text.toString(), editPassword.text.toString(), editEmail.text.toString())
        }

        btnBackLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    // MARK: SETTING CONFIGURATION OF TOAST
    override fun onRegisterResult(message: String) {
        Toasty.success(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onErrorResult(message: String) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInformationResult(message: String) {
        Toasty.info(this, message, Toast.LENGTH_SHORT).show()
    }
}
