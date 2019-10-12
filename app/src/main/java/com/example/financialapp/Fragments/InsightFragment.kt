package com.example.financialapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialapp.Adapter.InsightAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class InsightFragment : Fragment() {

    private lateinit var db: FirebaseRequest
    private lateinit var adapter: InsightAdapter
    private lateinit var incomesList : MutableList<Income>
    private lateinit var expensesList : MutableList<Expense>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        db = FirebaseRequest()

        incomesList = mutableListOf<Income>()
        expensesList = mutableListOf<Expense>()

        CoroutineScope(IO).launch {
            setTotalValue()

            adapter = InsightAdapter(activity!!, incomesList, expensesList)
            list_insight.adapter = adapter
        }

    }

    private suspend fun setTotalValue() {

        expensesList = db.fetchExpense()
        incomesList = db.fetchIncomes()

        var totalExpense = 0.0
        var totalIncome = 0.0
        expensesList.forEach {
            totalExpense -= it.price
        }
        incomesList.forEach {
            totalIncome += it.price
        }

        val nf = NumberFormat.getInstance()
        val total = nf.format(totalIncome + totalExpense)
        setText(total)
    }


    private suspend fun setText(input: String) {
        withContext(Main) {
            txt_total_insight?.setText(input)
        }
    }

}
