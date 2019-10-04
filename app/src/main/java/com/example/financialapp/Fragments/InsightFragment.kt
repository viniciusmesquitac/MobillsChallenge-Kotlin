package com.example.financialapp.Fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.financialapp.Adapter.InsightAdapter

import com.example.financialapp.R
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_insight.*
import kotlinx.android.synthetic.main.pie_chart_item.*

class InsightFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val ic = IncomesFragment()
        val ex = ExpensesFragment()

        var array: ArrayList<PieEntry> = ArrayList()

        array.add(PieEntry(2F, "comida"))
        array.add(PieEntry(3F, "transporte"))
        array.add(PieEntry(5F, "carro"))
        array.add(PieEntry(1F, "vendas"))
        array.add(PieEntry(0F, "leite"))


        var colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)

        val pieDataSet: PieDataSet = PieDataSet(array, "")

        pieDataSet.setColors(colorArray, 100)

        val pieData = PieData(pieDataSet)

        val adapter = InsightAdapter(activity!!, pieData)
        list_insight.adapter = adapter

    }


    fun setPieChart( ) {
        var array: ArrayList<PieEntry> = ArrayList()

        array.add(PieEntry(2F, "comida"))
        array.add(PieEntry(3F, "transporte"))
        array.add(PieEntry(5F, "carro"))
        array.add(PieEntry(1F, "vendas"))
        array.add(PieEntry(0F, "leite"))


        var colorArray = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED, Color.MAGENTA)

        val pieDataSet: PieDataSet = PieDataSet(array, "")

        pieDataSet.setColors(colorArray, 100)

        val pieData = PieData(pieDataSet)

        // setting view
        pieChartView.setDrawEntryLabels(false)
        pieChartView.setUsePercentValues(true)
        pieChartView.description = null
        pieChartView.setNoDataTextColor(Color.WHITE)
         //setting data
        pieChartView.data = pieData
    }


}
