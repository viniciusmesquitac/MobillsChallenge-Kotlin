package com.example.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.ExpensesAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Service.FirebaseRequest
import kotlinx.android.synthetic.main.activity_search_expenses.*

class SearchExpensesActivity : AppCompatActivity() {

    private lateinit var adapter: ExpensesAdapter
    private lateinit var expensesList : MutableList<Expense>
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

       searchView_expenses.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

           override fun onQueryTextSubmit(p0: String?): Boolean {
               adapter.filter.filter(p0)
               return false
           }

           override fun onQueryTextChange(p0: String?): Boolean {
               adapter.filter.filter(p0)
               return false
           }

       })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }
}
