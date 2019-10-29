package com.example.financialapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.financialapp.presenter.InterfaceLoginPresenter
import com.example.financialapp.presenter.LoginPresenter
import com.example.financialapp.R
import com.example.financialapp.view.ILoginView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ILoginView {

    private lateinit var loginPresenter: InterfaceLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // MARK: INIT PRESENTER
        loginPresenter = LoginPresenter(this)
        loginPresenter.verifyAuthStatus(this)


        // MARK: START EVENTS
        btnLogin.setOnClickListener {
            loginPresenter.onLogin(this, editEmail.text.toString(), editPassword.text.toString())
        }

        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onErrorResult(message: String) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onInformationResult(message: String) {
        Toasty.info(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginResult(message: String) {
        Toasty.success(this, message, Toast.LENGTH_SHORT).show()
    }
}
