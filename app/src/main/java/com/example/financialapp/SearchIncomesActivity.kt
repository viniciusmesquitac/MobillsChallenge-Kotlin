package com.example.financialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.IncomesAdapter
import com.example.financialapp.Model.Income
import com.example.financialapp.Service.FirebaseRequest
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }
}
