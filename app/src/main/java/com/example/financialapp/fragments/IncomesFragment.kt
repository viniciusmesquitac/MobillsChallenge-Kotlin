package com.example.financialapp.fragments


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
import com.example.financialapp.adapter.IncomesAdapter
import com.example.financialapp.model.Income

import com.example.financialapp.R
import com.example.financialapp.model.formatted
import com.example.financialapp.service.FirebaseRequest
import com.example.financialapp.view.IRecyclerView
import com.example.financialapp.view.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_insert_incomes.view.*
import kotlinx.android.synthetic.main.dialog_info_expenses.view.*
import kotlinx.android.synthetic.main.fragment_incomes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                checkAdapterStatus(adapter)
            }
        }



        recycler_view_income.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (position <= 0) {
                    return
                }
                val income = incomesList[position - 1]
                val dialog = activity?.let { BottomSheetDialog(it) }
                val viewDialog = layoutInflater.inflate(R.layout.dialog_info_incomes, null)

                // MARK - SETTING EXPENSE DATA IN MODAL
                with(income) {
                    viewDialog.edit_price_dialog.setText(price.formatted())
                    viewDialog.edit_description_dialog.setText(description.toString())
                    viewDialog.edit_date_dialog.setText(date.formatted())
                    setSpinnerSelection(category!!, viewDialog)
                }

                val close = viewDialog.findViewById<ImageView>(R.id.iv_close)
                close.setOnClickListener {
                    dialog?.dismiss()
                }
                val btnUpdateIncome = viewDialog.findViewById<Button>(R.id.btnUpdateExpense)

                btnUpdateIncome.btnUpdateExpense.setOnClickListener {

                    income.price = viewDialog.edit_price_dialog.text.toString().formatted()
                    income.description = viewDialog.edit_description_dialog.text.toString()
                    income.category = viewDialog.mySpinner_dialog.selectedItem.toString()

                    db.updateIncomeInFirebase(income)
                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }

                val btnDeleteIncome = viewDialog.findViewById<Button>(R.id.btnDeleteExpense)
                btnDeleteIncome.setOnClickListener {

                    incomesList.remove(income)
                    db.deleteIncomeInFirebase(income)

                    //SNACK MESSAGE
                    snackMessage(activity!!, "Receita deleta!", income)

                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }
                dialog?.setCancelable(true)
                dialog?.setContentView(viewDialog)
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


    private fun setTotalValue() {
        var totalIncomes = 0.0
        incomesList.forEach {
            totalIncomes += it.price.toInt()
        }
        adapter.totalIncomes = totalIncomes
        adapter.notifyDataSetChanged()
    }

    private fun checkAdapterStatus(adapter: IncomesAdapter) {
        if (adapter.itemCount == 1) {
            backgroundEmpty_text_ic?.visibility = View.VISIBLE
            backgroundEmpty_income?.visibility = View.VISIBLE
        }
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

        snack.setAction("DESFAZER") {
            incomesList.add(income)
            db.updateIncomeInFirebase( income)
            adapter.notifyDataSetChanged()
        }
        snack.show()
    }
}
