package com.example.financialapp.Fragments


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.Adapter.IncomesAdapter
import com.example.financialapp.Model.Income

import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.IRecyclerView
import com.example.financialapp.View.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_info_expenses.view.*
import kotlinx.android.synthetic.main.fragment_incomes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat

class IncomesFragment : Fragment(), IRecyclerView {

    private lateinit var adapter: IncomesAdapter
    private lateinit var incomesList: MutableList<Income>
    private lateinit var db: FirebaseRequest

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_incomes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        db = FirebaseRequest()
        incomesList = mutableListOf()

        adapter = IncomesAdapter(incomesList, activity!!)
        recycler_view_income?.adapter = adapter
        recycler_view_income?.layoutManager = LinearLayoutManager(activity!!)

        CoroutineScope(IO).launch {
            incomesList = db.fetchIncomes()
            adapter.filterListResult = incomesList


            withContext(Main) {
                progress_incomes?.visibility = View.GONE
                setTotalValue()
            }
        }



        recycler_view_income.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (position <= 0) {
                    return
                }
                val income = incomesList[position - 1]
                val dialog = activity?.let { BottomSheetDialog(it) }

                val view_dialog = layoutInflater.inflate(R.layout.dialog_info_incomes, null)

                val sdf = SimpleDateFormat("dd/MM/yyyy")

                // MARK - SETTING EXPENSE DATA IN MODAL
                with(income) {
                    view_dialog.edit_price_dialog.setText(price.toString())
                    view_dialog.edit_description_dialog.setText(description.toString())
                    view_dialog.edit_date_dialog.setText(sdf.format(date).toString())
                    // setSpinnerSelection(category!!, view_dialog)
                }

                val close = view_dialog.findViewById<ImageView>(R.id.iv_close)
                close.setOnClickListener {
                    dialog?.dismiss()
                }
                val btnUpdate_income = view_dialog.findViewById<Button>(R.id.btnUpdateExpense)

                btnUpdate_income.btnUpdateExpense.setOnClickListener {

                    income.price = view_dialog.edit_price_dialog.text.toString().toDouble()
                    income.description = view_dialog.edit_description_dialog.text.toString()

                    db.updateIncomeInFirebase("receitas", income)
                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }

                val btnDelete_income = view_dialog.findViewById<Button>(R.id.btnDeleteExpense)
                btnDelete_income.setOnClickListener {

                    incomesList.remove(income)
                    db.deleteIncomeInFirebase("receitas", income)

                    //SNACK MESSAGE
                    snackMessage(activity!!, "Receita deleta!", income)

                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }
                dialog?.setCancelable(true)
                dialog?.setContentView(view_dialog)
                dialog?.show()
            }
        })
    }


    override fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }


    fun setTotalValue() {
        var totalIncomes = 0f
        incomesList.forEach {
            totalIncomes += it.price.toInt()
        }
        adapter.totalIncomes = totalIncomes
        adapter.notifyDataSetChanged()
    }

    private fun setSpinnerSelection(category: String, view: View) {
        when (category) {
            "Salários" -> view.mySpinner_dialog.setSelection(0)
            "Emprestimos" -> view.mySpinner_dialog.setSelection(1)
            "Investimentos" -> view.mySpinner_dialog.setSelection(2)
            "Outro" -> view.mySpinner_dialog.setSelection(3)
        }
    }

    fun snackMessage(activity: Activity, msg: String, income: Income) {
        val rootView = activity.window.decorView.findViewById(android.R.id.content) as View
        val snack = Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT)

        snack.setAction("DESFAZER", View.OnClickListener {
            incomesList.add(income)
            db.updateIncomeInFirebase("receitas", income)
            adapter.notifyDataSetChanged()
        })
        snack.show()
    }
}
