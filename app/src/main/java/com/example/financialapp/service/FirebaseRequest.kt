package com.example.financialapp.service

import android.util.Log
import com.example.financialapp.model.Expense
import com.example.financialapp.model.Income
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseRequest: InterfaceFirestoreRequest {

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    val expenses = "despesas"
    val incomes = "receitas"

    override suspend fun fetchExpense(): MutableList<Expense> {

        val querySnapshot = db.collection("users/$uid/$expenses").get().await()

        return querySnapshot.toObjects(Expense::class.java)
    }

    override suspend fun fetchIncomes(): MutableList<Income> {

        val querySnapshot = db.collection("users/$uid/$incomes").get().await()

        return querySnapshot.toObjects(Income::class.java)

    }

    // LOAD
    override fun fetchFirebase(collectionPath: String): Task<QuerySnapshot> {

        var paymentsList: MutableList<Expense> = mutableListOf<Expense>()

        return db.collection("users/$uid/$collectionPath").get()

    }

    // SAVE
    override fun saveExpenseInFirebase(price: Double, description: String, date: Date, category: String, image: String) {

        val docRef =  db.collection("users/$uid/$expenses").document()
        val docId = docRef.id

        var expense = Expense(docId, description, price, date,category , "", false)
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

    override fun saveIncomeInFirebase(price: Double, description: String, date: Date, category: String, image: String) {

        val docRef =  db.collection("users/$uid/$incomes").document()
        val docId = docRef.id

        val income = Income(docId, description, price, date,category, image, false)
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
    override fun updateExpenseInFirebase(expense: Expense) {

        val docId = expense.id.toString()
        db.collection("users/$uid/$expenses/")
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
    override fun deleteExpenseInFirebase(expense: Expense) {

        val docId = expense.id.toString()
        db.collection("users/$uid/$incomes")
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


    override fun updateIncomeInFirebase(income: Income) {

        val docId = income.id.toString()
        db.collection("users/$uid/$incomes")
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

    override fun deleteIncomeInFirebase(income: Income) {

        val docId = income.id.toString()
        db.collection("users/$uid/$incomes")
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
