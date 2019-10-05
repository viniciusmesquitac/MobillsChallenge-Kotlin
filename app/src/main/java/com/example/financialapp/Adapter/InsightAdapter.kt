package com.example.financialapp.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.financialapp.Fragments.InsightFragment
import com.example.financialapp.Model.Expense
import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.android.synthetic.main.geral_state.*
import kotlinx.android.synthetic.main.pie_chart_item.*

class InsightAdapter(context: Context, internal var dataSource: PieData): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var firebaseRequest: FirebaseRequest

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var rowView = p1
        if (p0 == 0) {
            rowView = inflater.inflate(R.layout.geral_state, p2, false)
        } else {
            rowView = inflater.inflate(R.layout.pie_chart_item, p2, false)
            val pieChartView = rowView.findViewById(R.id.pieChartView) as PieChart

            //getDataSource()
            pieChartView.setDrawEntryLabels(false)
            pieChartView.setUsePercentValues(true)
            pieChartView.description = null
            pieChartView.setNoDataTextColor(Color.WHITE)
            //setting data
            pieChartView.data = dataSource
        }

        return rowView!!

    }

    private fun getDataSource() {

    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        var view = 1
        if (position == 2) {
            view = 0
        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 1
    }

    override fun getCount(): Int {
        return 2
    }


    fun fetchDataExpenses(tv: Int) {
        val expensesList = mutableListOf<Expense>()
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

                    }
                }
    }
}