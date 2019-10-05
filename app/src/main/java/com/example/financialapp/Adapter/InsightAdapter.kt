package com.example.financialapp.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.financialapp.Fragments.ExpensesFragment
import com.example.financialapp.Fragments.IncomesFragment
import com.example.financialapp.Fragments.InsightFragment
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.android.synthetic.main.geral_state.*
import kotlinx.android.synthetic.main.pie_chart_item.*

class InsightAdapter(context: Context): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var incomeList: MutableList<Income>
    private lateinit var expenseList: MutableList<Expense>


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        // dataSource
        incomeList = IncomesFragment.getIncomeList()
        expenseList = ExpensesFragment.getExpenseList()

        var rowView = p1
        if (p0 == 0) {
            rowView = inflater.inflate(R.layout.geral_state, p2, false)
            val expense_state = rowView.findViewById(R.id.expenses_geral_state) as TextView
            val income_state = rowView.findViewById(R.id.incomes_geral_state) as TextView
            setGeralVisionLayout(expense_state, income_state)
        }
        else if(p0 == 1) {
            rowView = inflater.inflate(R.layout.pie_chart_item, p2, false)
            setupPieView(rowView)
        }

        return rowView!!

    }

    private fun setGeralVisionLayout(expense_state: TextView, income_state: TextView) {
        var totalExpense = 0.0
        var totalIncome = 0.0
        expenseList.forEach {
            totalExpense -= it.price
        }
        incomeList.forEach {
            totalIncome += it.price
        }
        income_state.setText(totalIncome.toString())
        expense_state.setText(totalExpense.toString())


    }

    private fun setupPieView(rowView: View?) {
        var category1 = 0
        var category2 = 0
        var category3 = 0
        expenseList.forEach {
             if  (it.category == "Casa") {
                 category1 += 1
             } else if  (it.category == "Alimentação") {
                category2 +=1
             } else if  (it.category == "Transporte") {
                 category3+=1
            }
        }

        var array: ArrayList<PieEntry> = ArrayList()

        array.add(PieEntry(category1.toFloat(), "Casa"))
        array.add(PieEntry(category2.toFloat(), "Alimentação"))
        array.add(PieEntry(category3.toFloat(), "Transporte"))


        var colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)

        val pieChartView = rowView?.findViewById(R.id.pieChartView) as PieChart
        val pieDataSet: PieDataSet = PieDataSet(array, "")

        pieDataSet.setColors(colorArray, 100)

        pieChartView.setDrawEntryLabels(false)
        pieChartView.setUsePercentValues(true)
        pieChartView.description = null
        pieChartView.setNoDataTextColor(Color.WHITE)
        //setting data
        val pieData = PieData(pieDataSet)
        pieChartView.data = pieData

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
}