package com.example.financialapp.presenter

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.financialapp.activities.HomeActivity
import com.example.financialapp.activities.LoginActivity
import com.example.financialapp.view.ILoginView
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter (internal var iLoginView: ILoginView): InterfaceLoginPresenter {

    override fun verifyAuthStatus(context: Activity) {
        val uuid = FirebaseAuth.getInstance().uid
        print(uuid)
        if(uuid!=null) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override fun onLogout(context: Activity) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    override fun onLogin(context: Activity, email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()) {
            iLoginView.onInformationResult("Insira email e senha.")
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "login success")
                        iLoginView.onLoginResult("Login Aprovado!")
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                    iLoginView.onErrorResult("Error no Login!")
                }

    }

}