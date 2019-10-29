package com.example.financialapp.presenter

import android.util.Log
import com.example.financialapp.model.User
import com.example.financialapp.view.IRegisterView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterPresenter(internal var iLoginView: IRegisterView): InterfaceRegisterPresenter {


    override fun onCreateUser(username: String, password: String, email: String) {

        if(email.isEmpty() || password.isEmpty()){
            iLoginView.onInformationResult("Email e senha devem ser preenchidos!")
            return
        }

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        onSaveUserInFirebase(username)
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                    iLoginView.onErrorResult("Error ao criar a conta, tente novamente!")
                }

    }

    override fun onSaveUserInFirebase(username: String) {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val user = User(uid, username, photoUrl = "")
        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(user)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        iLoginView.onRegisterResult("Usuário Cadastrado!")
                        Log.d("teste", "salvo no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }

    }



}