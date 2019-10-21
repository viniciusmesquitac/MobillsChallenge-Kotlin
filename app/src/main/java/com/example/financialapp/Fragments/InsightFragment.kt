package com.example.financialapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financialapp.Adapter.InsightAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_insight, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //progress_recycler.visibility = View.GONE
        db = FirebaseRequest()
        adapter = InsightAdapter()

        rv_insight.adapter = adapter
        rv_insight.layoutManager = LinearLayoutManager(activity!!)

        CoroutineScope(IO).launch {
            setTotalValue()
        }

    }

    private suspend fun setAdapter(totalExpenses: Double, totalIncomes: Double) {
        try {
            withContext(Main) {
                val dataSet =  createPieDataSet()
                adapter.pieDataSet = dataSet
                adapter.totalExpense = totalExpenses
                adapter.totalIncome = totalIncomes
                adapter.notifyDataSetChanged()

            }
        } catch (error: Throwable) {
            Log.d("error", error.toString())
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

        setAdapter(totalExpense, totalIncome)

        val nf = NumberFormat.getInstance()
        val total = nf.format(totalIncome + totalExpense)
        setText(total)
    }


    private fun createPieDataSet(): PieDataSet {

        var category1 = 0
        var category2 = 0
        var category3 = 0

        expensesList.forEach {
            if (it.category == "Casa") {
                category1 += 1
            } else if (it.category == "Alimentação") {
                category2 += 1
            } else if (it.category == "Transporte") {
                category3 += 1
            }
        }

        var array: ArrayList<PieEntry> = ArrayList()

        array.add(PieEntry(category1.toFloat(), "Casa"))
        array.add(PieEntry(category2.toFloat(), "Alimentação"))
        array.add(PieEntry(category3.toFloat(), "Transporte"))
        val pieDataSet = PieDataSet(array, "")
        return pieDataSet
    }


    private suspend fun setText(input: String) {
        withContext(Main) {
            //txt_total_insight?.setText(input)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_fragment_insight, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}
