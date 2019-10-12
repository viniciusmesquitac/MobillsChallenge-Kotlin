package com.example.financialapp.Service

import android.util.Log
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseRequest: InterfaceFirestoreRequest {

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override suspend fun fetchExpense(): MutableList<Expense> {

        val querySnapshot = db.collection("users/$uid/despesas").get().await()

        val expenses = querySnapshot.toObjects(Expense::class.java)

        return expenses
    }

    override suspend fun fetchIncomes(): MutableList<Income> {

        val querySnapshot = db.collection("users/$uid/receitas").get().await()

        val incomes = querySnapshot.toObjects(Income::class.java)

        return incomes

    }



    // LOAD
    override fun fetchFirebase(collectionPath: String): Task<QuerySnapshot> {

        var paymentsList: MutableList<Expense> = mutableListOf<Expense>()

        return db.collection("users/$uid/$collectionPath").get()

    }

    // SAVE
    override fun saveExpenseInFirebase(collectionPath: String, price: Double, description: String, date: Date, category: String, image: String) {

        val docRef =  db.collection("users/$uid/$collectionPath").document()
        val docId = docRef.id

        var expense = Expense(docId, description, price, date,false, category, "")
        if(!image.isEmpty()) {
           expense.image = image
        }
        docRef.set(expense)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "salvo no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }

    override fun saveIncomeInFirebase(collectionPath: String, price: Double, description: String, date: Date) {

        val docRef =  db.collection("users/$uid/$collectionPath").document()
        val docId = docRef.id

        val income = Income(docId, description, price, date,false)
        docRef.set(income)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "salvo no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }

    // UPDATE
    override fun updateExpenseInFirebase(collectionPath: String, expense: Expense) {

        val docId = expense.id.toString()
        db.collection("users/$uid/$collectionPath/")
                .document(docId)
                .set(expense)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "atualizado no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }

    // DELETE
    override fun deleteExpenseInFirebase(collectionPath: String, expense: Expense) {

        val docId = expense.id.toString()
        db.collection("users/$uid/$collectionPath")
                .document(docId)
                .delete()
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "removido no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }


    override fun updateIncomeInFirebase(collectionPath: String, income: Income) {

        val docId = income.id.toString()
        db.collection("users/$uid/$collectionPath")
                .document(docId)
                .set(income)
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "atualizado no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }

    override fun deleteIncomeInFirebase(collectionPath: String, income: Income) {

        val docId = income.id.toString()
        db.collection("users/$uid/$collectionPath")
                .document(docId)
                .delete()
                .addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("teste", "removido no firebase")
                    }
                }.addOnFailureListener{
                    Log.e("teste", it.message, it)
                }
    }

}
