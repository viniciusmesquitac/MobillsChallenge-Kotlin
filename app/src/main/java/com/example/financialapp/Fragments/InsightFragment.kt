package com.example.financialapp.Fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.financialapp.Adapter.InsightAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income

import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_incomes.*
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.android.synthetic.main.geral_state.*
import kotlinx.android.synthetic.main.pie_chart_item.*
import java.text.NumberFormat

class InsightFragment : Fragment() {

    private lateinit var firebaseRequest: FirebaseRequest
    private lateinit var incomesList : MutableList<Income>
    private lateinit var expensesList : MutableList<Expense>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseRequest = FirebaseRequest()
        val ex = ExpensesFragment()
        fetchData()
        setTotalValue()

        var array: ArrayList<PieEntry> = ArrayList()

        array.add(PieEntry(2F, "Alimentação"))
        array.add(PieEntry(3F, "transporte"))
        array.add(PieEntry(3F, "transporte"))


        var colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)

        val pieDataSet: PieDataSet = PieDataSet(array, "")

        pieDataSet.setColors(colorArray, 100)

        val pieData = PieData(pieDataSet)

        val adapter = InsightAdapter(activity!!, pieData)
        list_insight.adapter = adapter
    }

    private fun setTotalValue() {




    }

    private fun fetchData() {
        incomesList = mutableListOf<Income>()
        expensesList = mutableListOf<Expense>()
        var totalValue = 0

        firebaseRequest
                .fetchFirebase("receitas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Income::class.java)
                            incomesList.add(payment)
                        }
                    }
                    incomesList.forEach {
                        totalValue += it.price.toInt()
                    }
                    if(incomes_geral_state !=null) {
                        formatTextCurrency(incomes_geral_state, totalValue)
                        fetchDataExpenses(totalValue)
                    }
                }

    }

    fun fetchDataExpenses(tv: Int) {
        var totalPrice = 0
        var totalValue = tv
        firebaseRequest
                .fetchFirebase("despesas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Expense::class.java)
                            expensesList.add(payment)
                        }
                    }
                    expensesList.forEach {
                        totalPrice -= it.price.toInt()
                    }
                    if (expenses_geral_state !=null || txt_total_insight!=null) {
                        formatTextCurrency(expenses_geral_state, totalPrice)
                        totalValue = tv + totalPrice
                        formatTextCurrency(txt_total_insight, totalValue)
                        setPieChart(expensesList)
                    }
                }
    }


    fun setPieChart(list: MutableList<Expense>) {

    }


    fun formatTextCurrency(textView: TextView, value: Int) {
        val nf = NumberFormat.getInstance()
        val input = nf.format(value)
        textView?.setText(input)
    }

}
