package com.example.financialapp.Fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.financialapp.Adapter.IncomesAdapter
import com.example.financialapp.Adapter.InsightAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income

import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.android.synthetic.main.geral_state.*
import java.text.NumberFormat

class InsightFragment : Fragment() {

    private lateinit var db: FirebaseRequest
    private lateinit var adapter: InsightAdapter
    private lateinit var incomesList : MutableList<Income>
    private lateinit var expensesList : MutableList<Expense>

    companion object {
        var totalValue = 0.0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        db = FirebaseRequest()
        val ex = ExpensesFragment()

        incomesList = mutableListOf<Income>()
        expensesList = mutableListOf<Expense>()

        if (IncomesFragment.getIncomeList().isEmpty()) {
            fetchDataIncomes()
        } else
        if(ExpensesFragment.getExpenseList().isEmpty()) {
            fetchDataExpenses()
        } else {
            setTotalValue()
        }


        adapter = InsightAdapter(activity!!)
        list_insight.adapter = adapter
    }

    private fun setTotalValue() {
        expensesList = ExpensesFragment.getExpenseList()
        incomesList = IncomesFragment.getIncomeList()
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
        txt_total_insight?.setText(total)
    }

    private fun fetchDataIncomes() {
        db.fetchFirebase("receitas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Income::class.java)
                            incomesList.add(payment)
                        }
                    }
                    IncomesFragment.setIncomeList(incomesList)
                    adapter.notifyDataSetChanged()

                    val nf = NumberFormat.getInstance()
                    val total = nf.format(totalValue)
                    txt_total_insight?.setText(total)
                }
    }

    fun fetchDataExpenses() {
        db.fetchFirebase("despesas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Expense::class.java)
                            expensesList.add(payment)
                        }
                    }
                    ExpensesFragment.setExpenseList(expensesList)
                    adapter.notifyDataSetChanged()

                    val nf = NumberFormat.getInstance()
                    val total = nf.format(totalValue)
                    txt_total_insight?.setText(total)
                }
    }

    fun formatTextCurrency(textView: TextView, value: Double) {
        val nf = NumberFormat.getInstance()
        val input = nf.format(value)
        textView?.setText(input)
    }

}
