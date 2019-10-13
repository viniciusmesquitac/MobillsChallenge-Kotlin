package com.example.financialapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.android.synthetic.main.geral_state.view.*
import java.text.NumberFormat


class InsightAdapter(var totalExpense: Double, var totalIncome: Double, var pieDataSet: PieDataSet): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {

            0 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.geral_state, parent, false)
                return ViewHolderGeralVision(inflate)
            }

            1 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.pie_chart_item, parent, false)
                return PieChartViewHolder(inflate)
            }

            else -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.geral_state, parent, false)
                return ViewHolderGeralVision(inflate)
            }
        }
    }

    override fun getItemCount() = 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType) {

            0 -> {
                val holder1 = holder as ViewHolderGeralVision
                holder1.bind(totalExpense, totalIncome)
            }

            1 -> {
                val holder2 = holder as PieChartViewHolder
                holder2.bind(pieDataSet)
            }

        }
    }

    inner class PieChartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(pieDataSet: PieDataSet) {

            val colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)
            val pieChartView = itemView.findViewById(R.id.pieChartView) as PieChart

            pieDataSet.setColors(colorArray, 100)
            pieChartView.setDrawEntryLabels(false)
            pieChartView.description = null
            pieChartView.setNoDataTextColor(Color.WHITE)

            val pieData = PieData(pieDataSet)
            pieChartView.data = pieData
        }
    }

    inner class ViewHolderGeralVision(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(totalExpense: Double, totalIncome: Double) {

            val nf = NumberFormat.getInstance()
            val income_value = nf.format(totalIncome)
            val expense_value = nf.format(totalExpense)

            with(itemView) {
                expenses_geral_state.setText(expense_value)
                incomes_geral_state.setText(income_value)
            }

        }
    }

}