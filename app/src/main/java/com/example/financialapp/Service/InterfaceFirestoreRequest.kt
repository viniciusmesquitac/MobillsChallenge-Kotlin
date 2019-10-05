package com.example.financialapp.Service

import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

interface InterfaceFirestoreRequest {
    fun fetchFirebase(collectionPath: String): Task<QuerySnapshot>

    fun saveExpenseInFirebase(collectionPath: String, price: Double, description: String, date: Date, category: String, image: String)
    fun saveIncomeInFirebase(collectionPath: String, price: Double, description: String, date: Date)

    fun updateExpenseInFirebase(collectionPath: String, expense: Expense)
    fun updateIncomeInFirebase(collectionPath: String, income: Income)

    fun deleteExpenseInFirebase(collectionPath: String, expense: Expense)
    fun deleteIncomeInFirebase(collectionPath: String, income: Income)
}