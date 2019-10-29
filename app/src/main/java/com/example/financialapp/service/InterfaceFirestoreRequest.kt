package com.example.financialapp.service

import com.example.financialapp.model.Expense
import com.example.financialapp.model.Income
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

interface InterfaceFirestoreRequest {

    fun fetchFirebase(collectionPath: String): Task<QuerySnapshot>

    suspend fun fetchExpense(): MutableList<Expense>
    suspend fun fetchIncomes(): MutableList<Income>

    fun saveExpenseInFirebase(price: Double, description: String, date: Date, category: String, image: String)
    fun saveIncomeInFirebase(price: Double, description: String, date: Date, category: String, image: String)

    fun updateExpenseInFirebase(expense: Expense)
    fun updateIncomeInFirebase(income: Income)

    fun deleteExpenseInFirebase(expense: Expense)
    fun deleteIncomeInFirebase(income: Income)
}