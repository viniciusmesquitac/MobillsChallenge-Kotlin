package com.example.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.ExpensesAdapter
import com.example.financialapp.Adapter.IncomesAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.NavigationBottomView
import kotlinx.android.synthetic.main.activity_search_expenses.*
import kotlinx.android.synthetic.main.activity_search_incomes.*

class SearchIncomesActivity : AppCompatActivity() {

    private lateinit var adapter: IncomesAdapter
    private lateinit var incomesList : MutableList<Income>
    private lateinit var db: FirebaseRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_incomes)
        setSupportActionBar(toolbar_search_incomes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        incomesList = mutableListOf<Income>()
        db = FirebaseRequest()

        db.fetchFirebase("receitas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Income::class.java)
                            incomesList.add(payment)
                        }
                    }
                }

        adapter = IncomesAdapter(incomesList)
        rv_search_incomes?.adapter = adapter
        rv_search_incomes?.layoutManager = LinearLayoutManager(this)

    }
}
