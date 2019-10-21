package com.example.financialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.ExpensesAdapter
import com.example.financialapp.Adapter.SearchAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Service.FirebaseRequest
import kotlinx.android.synthetic.main.activity_search_expenses.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchExpensesActivity : AppCompatActivity() {

    private lateinit var adapter: SearchAdapter<Expense>
    private lateinit var expensesList : MutableList<Expense>
    private lateinit var db: FirebaseRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_expenses)
        setSupportActionBar(toolbar_search)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_search.contentInsetStartWithNavigation = 0

        expensesList = mutableListOf<Expense>()
        db = FirebaseRequest()

       CoroutineScope(IO).launch {
           expensesList = db.fetchExpense()
           withContext(Main){
               adapter = SearchAdapter(expensesList, this@SearchExpensesActivity)
               rv_search?.adapter = adapter
               rv_search?.layoutManager = LinearLayoutManager(this@SearchExpensesActivity)
           }
       }

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


        back_home.setOnClickListener {
            onBackPressed()
        }

    }

}
