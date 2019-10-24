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
import com.example.financialapp.adapter.ExpensesAdapter
import com.example.financialapp.model.Expense
import com.example.financialapp.service.FirebaseRequest

import com.example.financialapp.R
import com.example.financialapp.model.formatted
import com.example.financialapp.view.IRecyclerView
import com.example.financialapp.view.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_info_expenses.view.*
import kotlinx.android.synthetic.main.fragment_expenses.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ExpensesFragment : Fragment(), IRecyclerView {

    private lateinit var adapter: ExpensesAdapter
    private lateinit var expensesList: MutableList<Expense>
    private lateinit var db: FirebaseRequest


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expenses, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        db = FirebaseRequest()
        expensesList = mutableListOf()
        adapter = ExpensesAdapter(expensesList, activity!!)

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(activity!!)

        CoroutineScope(Dispatchers.IO).launch {
            expensesList = db.fetchExpense()
            adapter.filterListResult = expensesList

            withContext(Dispatchers.Main) {
                progress_expenses?.visibility = View.GONE
                adapter.notifyDataSetChanged()
                setTotalValue()
                checkAdapterStatus(adapter)
            }
        }

        recycler_view.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (position <= 0) {
                    return
                }
                val expense = expensesList[position - 1]
                val dialog = activity?.let { BottomSheetDialog(it) }
                val viewDialog = layoutInflater.inflate(R.layout.dialog_info_expenses, null)


                // MARK - SETTING EXPENSE DATA IN MODAL
                with(expense) {
                    viewDialog.edit_price_dialog.setText(price.formatted())
                    viewDialog.edit_description_dialog.setText(description.toString())
                    viewDialog.edit_date_dialog.setText(date.formatted())
                    setSpinnerSelection(category!!, viewDialog)
                }

                val close = viewDialog.findViewById(R.id.iv_close) as ImageView
                close.setOnClickListener {
                    dialog?.dismiss()
                }
                val btnUpdate = viewDialog.findViewById(R.id.btnUpdateExpense) as Button

                btnUpdate.btnUpdateExpense.setOnClickListener {
                    
                    expense.price = viewDialog.edit_price_dialog.text.toString().formatted()
                    expense.description = viewDialog.edit_description_dialog.text.toString()
                    expense.category = viewDialog.mySpinner_dialog.selectedItem.toString()

                    db.updateExpenseInFirebase(expense)
                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }

                val btnDelete = viewDialog.findViewById(R.id.btnDeleteExpense) as Button
                btnDelete.setOnClickListener {
                    expensesList.remove(expense)
                    db.deleteExpenseInFirebase(expense)

                    // SNACK MESSAGE
                    snackMessage(activity!!, "Despesa deletada!", expense)


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

    private fun setSpinnerSelection(category: String, view: View) {
        when (category) {
            "Casa" -> view.mySpinner_dialog.setSelection(0)
            "Alimentação" -> view.mySpinner_dialog.setSelection(1)
            "Transporte" -> view.mySpinner_dialog.setSelection(2)
            "Outras receitas" -> view.mySpinner_dialog.setSelection(3)
        }
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
        var totalExpenses = 0.0
        expensesList.forEach {
            totalExpenses -= it.price.toInt()
        }
        adapter.totalInsight = totalExpenses
        adapter.notifyDataSetChanged()
    }

    private fun checkAdapterStatus(adapter: ExpensesAdapter) {
        if (adapter.itemCount == 0) {
            backgroundEmpty_text?.visibility = View.VISIBLE
            backgroundEmpty?.visibility = View.VISIBLE
        }
    }

    private fun snackMessage(activity: Activity, msg: String, expense: Expense) {
        val rootView = activity.window.decorView.findViewById(android.R.id.content) as View
        val snack = Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT)

        snack.setAction("DESFAZER") {
            expensesList.add(expense)
            db.updateExpenseInFirebase(expense)
            adapter.notifyDataSetChanged()
        }
        snack.show()
    }

}
