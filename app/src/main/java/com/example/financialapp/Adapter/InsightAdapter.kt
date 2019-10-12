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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class InsightAdapter(internal var context: Context, var totalExpense: Double, var totalIncome: Double, var pieDataSet: PieDataSet): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var expense_state: TextView
    private lateinit var income_state: TextView

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var rowView = p1
        if (p0 == 0) {
            rowView = inflater.inflate(R.layout.geral_state, p2, false)
             expense_state = rowView.findViewById(R.id.expenses_geral_state) as TextView
             income_state = rowView.findViewById(R.id.incomes_geral_state) as TextView

            setIncomesAndExpenses()

        }
        else if(p0 == 1) {
            rowView = inflater.inflate(R.layout.pie_chart_item, p2, false)
            setupPieView(rowView)
        }

        return rowView!!

    }

    private fun setIncomesAndExpenses() {

        val nf = NumberFormat.getInstance()
        val income_value = nf.format(totalIncome)
        val expense_value = nf.format(totalExpense)

        setTextExpenses(expense_state, expense_value)
        setTextIncomes(income_state, income_value)

    }

    private fun setTextExpenses(expense_state: TextView, expense_value: String){
        expense_state.setText(expense_value)
    }

    private fun setTextIncomes(income_state: TextView, income_value: String){
        income_state.setText(income_value)
    }


    private fun setupPieView(rowView: View?) {

        var colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)
        val pieChartView = rowView?.findViewById(R.id.pieChartView) as PieChart

        pieDataSet.setColors(colorArray, 100)
        pieChartView.setDrawEntryLabels(false)
        pieChartView.description = null
        pieChartView.setNoDataTextColor(Color.WHITE)

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