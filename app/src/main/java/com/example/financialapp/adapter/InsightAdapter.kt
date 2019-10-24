package com.example.financialapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.R
import com.example.financialapp.model.formatted
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.android.synthetic.main.card_view_insight.view.*
import kotlinx.android.synthetic.main.geral_state.view.*
import kotlinx.android.synthetic.main.pie_chart_item.view.*
import java.text.NumberFormat


class InsightAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var totalExpense = 0.0
    var totalIncome = 0.0
    var pieDataSet: PieDataSet? = null

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {

            0 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.card_view_insight, parent, false)
                return CardViewHolder(inflate)
            }

            1 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.geral_state, parent, false)
                return ViewHolderGeralVision(inflate)
            }

            else -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.pie_chart_item, parent, false)
                return PieChartViewHolder(inflate)
            }
        }
    }

    override fun getItemCount() = 5

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType) {

            0 -> {
                val cardHolder = holder as CardViewHolder
                cardHolder.bind()
            }

            1 -> {
                val holder1 = holder as ViewHolderGeralVision
                holder1.bind(totalExpense, totalIncome)
            }

            2 -> {

                val holder2 = holder as PieChartViewHolder
                if(pieDataSet != null) {
                    holder2.bind(pieDataSet!!)
                }

            }

             else -> {
                 val holder3 = holder as PieChartViewHolder

                 if(pieDataSet != null) {
                     holder3.bind(pieDataSet!!)
                 }
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
            itemView.progress_recycler.visibility = View.GONE

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

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            var total = totalIncome + totalExpense
            itemView.txt_total_insight.text = total.formatted()
        }
    }

}