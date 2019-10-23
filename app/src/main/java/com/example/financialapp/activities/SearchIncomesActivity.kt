package com.example.financialapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.adapter.SearchAdapter
import com.example.financialapp.model.Income
import com.example.financialapp.R
import com.example.financialapp.service.FirebaseRequest
import kotlinx.android.synthetic.main.activity_search_incomes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchIncomesActivity : AppCompatActivity() {

    private lateinit var adapter: SearchAdapter<Income>
    private lateinit var incomesList : MutableList<Income>
    private lateinit var db: FirebaseRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_incomes)
        setSupportActionBar(toolbar_search_incomes)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_search_incomes.contentInsetStartWithNavigation = 0


        incomesList = mutableListOf<Income>()
        db = FirebaseRequest()

        CoroutineScope(IO).launch {
            incomesList = db.fetchIncomes()

            withContext(Main){
                adapter = SearchAdapter(incomesList, this@SearchIncomesActivity)
                rv_search_incomes?.adapter = adapter
                rv_search_incomes?.layoutManager = LinearLayoutManager(this@SearchIncomesActivity)

                searchView_incomes.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

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
        }

        back_income.setOnClickListener {
            onBackPressed()
        }
    }

}
