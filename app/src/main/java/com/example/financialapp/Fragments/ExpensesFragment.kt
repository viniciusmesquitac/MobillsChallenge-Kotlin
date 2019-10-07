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
import com.example.financialapp.Adapter.ExpensesAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Service.FirebaseRequest

import com.example.financialapp.R
import com.example.financialapp.View.IRecyclerView
import com.example.financialapp.View.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_info_expenses.view.*
import kotlinx.android.synthetic.main.fragment_expenses.*
import java.text.NumberFormat
import java.text.SimpleDateFormat


class ExpensesFragment : Fragment(), IRecyclerView {

    private lateinit var adapter: ExpensesAdapter
    private lateinit var expensesList : MutableList<Expense>
    private lateinit var db: FirebaseRequest

    companion object {
        var companionExpenseList = mutableListOf<Expense>()

        fun setExpenseList(number: MutableList<Expense>) {
            companionExpenseList = number
        }

        fun getExpenseList(): MutableList<Expense> {
            return companionExpenseList
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expenses, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        expensesList = mutableListOf<Expense>()
        db = FirebaseRequest()

        // MARK - FETCH ALL EXPENSES AND SET IN RECYCLER VIEW
        db.fetchFirebase("despesas")
                .addOnSuccessListener { documents ->
                for (document in documents) {
                    if(document.exists()) {
                        val payment = document.toObject(Expense::class.java)
                        expensesList.add(payment)
                    }
                }
            adapter = ExpensesAdapter(expensesList)
            setExpenseList(expensesList)
            checkAdapterStatus(adapter)
            recycler_view?.adapter = adapter
            recycler_view?.layoutManager = activity?.let { LinearLayoutManager(it) }
            setTotalValue()
        }

        // MARK - EVENT CLICK
        recycler_view.addOnItemClickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {

                val expense = expensesList[position]
                val dialog = activity?.let { BottomSheetDialog(it) }
                val view_dialog = layoutInflater.inflate(R.layout.dialog_info_expenses, null)
                val sdf = SimpleDateFormat("dd/MM/yyyy")

                // MARK - SETTING EXPENSE DATA IN MODAL
                with(expense) {
                    view_dialog.edit_price_dialog.setText(price.toString())
                    view_dialog.edit_description_dialog.setText(description.toString())
                    view_dialog.edit_date_dialog.setText(sdf.format(date).toString())
                    setSpinnerSelection(category!!, view_dialog)
                }

                val close = view_dialog.findViewById(R.id.iv_close) as ImageView
                close.setOnClickListener {
                    dialog?.dismiss()
                }
                val btnUpdate = view_dialog.findViewById(R.id.btnUpdateExpense) as Button

                btnUpdate.btnUpdateExpense.setOnClickListener {
                    expense.price = view_dialog.edit_price_dialog.text.toString().toDouble()
                    expense.description = view_dialog.edit_description_dialog.text.toString()
                    expense.category = view_dialog.mySpinner_dialog.selectedItem.toString()
                    db.updateExpenseInFirebase("despesas", expense)
                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }

                val btnDelete = view_dialog.findViewById(R.id.btnDeleteExpense) as Button
                btnDelete.setOnClickListener {
                    expensesList.remove(expense)
                    db.deleteExpenseInFirebase("despesas", expense)

                    // SNACK MESSAGE
                    snackMessage(activity!!, "Despesa deletada!", expense)


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

    private fun setSpinnerSelection(category: String, view: View) {
        when(category) {
            "Casa" -> view.mySpinner_dialog.setSelection(0)
            "Alimentação" -> view.mySpinner_dialog.setSelection(1)
            "Transporte" -> view.mySpinner_dialog.setSelection(2)
            "Outras receitas" -> view.mySpinner_dialog.setSelection(3)
        }
    }

    override fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener {
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

        val nf = NumberFormat.getInstance()
        val input = nf.format(totalExpenses)
        txt_total_expenses?.setText(input)
    }

    fun checkAdapterStatus(adapter: ExpensesAdapter) {
        if(adapter.itemCount == 0) {
            backgroundEmpty_text?.visibility = View.VISIBLE
            backgroundEmpty?.visibility = View.VISIBLE
        }
    }

    fun snackMessage(activity: Activity, msg: String, expense: Expense) {
        val rootView = activity.window.decorView.findViewById(android.R.id.content) as View
        val snack = Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT)

        snack.setAction("DESFAZER", View.OnClickListener {
            expensesList.add(expense)
            db.updateExpenseInFirebase("despesas", expense)
            adapter.notifyDataSetChanged()
        })
        snack.show()
    }

}
