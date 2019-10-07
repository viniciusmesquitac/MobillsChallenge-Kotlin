package com.example.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.ExpensesAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.NavigationBottomView
import kotlinx.android.synthetic.main.activity_search_expenses.*

class SearchExpensesActivity : AppCompatActivity() {

    private lateinit var adapter: ExpensesAdapter
    private lateinit var expensesList : MutableList<Expense>
    internal lateinit var navigationView: NavigationBottomView
    private lateinit var db: FirebaseRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_expenses)
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        expensesList = mutableListOf<Expense>()
        db = FirebaseRequest()

        db.fetchFirebase("despesas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Expense::class.java)
                            expensesList.add(payment)
                        }
                    }
                }

        adapter = ExpensesAdapter(expensesList)
        rv_search?.adapter = adapter
        rv_search?.layoutManager = LinearLayoutManager(this@SearchExpensesActivity)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }
}
