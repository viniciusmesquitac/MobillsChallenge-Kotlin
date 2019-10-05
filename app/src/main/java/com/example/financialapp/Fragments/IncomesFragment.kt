package com.example.financialapp.Fragments


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
import com.example.financialapp.Adapter.IncomesAdapter
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income

import com.example.financialapp.R
import com.example.financialapp.Service.FirebaseRequest
import com.example.financialapp.View.IRecyclerView
import com.example.financialapp.View.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_info_expenses.view.*
import kotlinx.android.synthetic.main.fragment_expenses.*
import kotlinx.android.synthetic.main.fragment_incomes.*
import java.text.NumberFormat
import java.text.SimpleDateFormat

class IncomesFragment : Fragment(), IRecyclerView {

    private lateinit var adapter: IncomesAdapter
    private lateinit var incomesList : MutableList<Income>
    private lateinit var firebaseRequest: FirebaseRequest

    companion object {
        var companionIncomeList = mutableListOf<Income>()

        fun setIncomeList(list: MutableList<Income>) {
            companionIncomeList = list
        }

        fun getIncomeList(): MutableList<Income> {
            return companionIncomeList
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_incomes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        incomesList = mutableListOf<Income>()
        firebaseRequest = FirebaseRequest()

        // MARK - FETCH ALL EXPENSES AND SET IN RECYCLER VIEW
        firebaseRequest.fetchFirebase("receitas")
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.exists()) {
                            val payment = document.toObject(Income::class.java)
                            incomesList.add(payment)
                        }
                    }
                    adapter = IncomesAdapter(incomesList)
                    setIncomeList(incomesList)
                    checkAdapterStatus(adapter)
                    recycler_view_income?.adapter = adapter
                    recycler_view_income?.layoutManager = activity?.let { LinearLayoutManager(it) }
                    setTotalValue()
                }



        recycler_view_income.addOnItemClickListener(object: OnItemClickListener{
            override fun onItemClicked(position: Int, view: View) {

                val income = incomesList[position]
                val dialog = activity?.let { BottomSheetDialog(it) }

                val view_dialog = layoutInflater.inflate(R.layout.dialog_info_expenses, null)

                val sdf = SimpleDateFormat("dd/MM/yyyy")

                // MARK - SETTING EXPENSE DATA IN MODAL
                with(income) {
                    view_dialog.edit_price_dialog.setText(price.toString())
                    view_dialog.edit_description_dialog.setText(description.toString())
                    view_dialog.edit_date_dialog.setText(sdf.format(date).toString())
                }
                view_dialog.mySpinner_dialog.setSelection(0)

                val close = view_dialog.findViewById<ImageView>(R.id.iv_close)
                close.setOnClickListener {
                    dialog?.dismiss()
                }
                val btnUpdate_income = view_dialog.findViewById<Button>(R.id.btnUpdateExpense)

                btnUpdate_income.btnUpdateExpense.setOnClickListener {
                    income.price = view_dialog.edit_price_dialog.text.toString().toDouble()
                    income.description = view_dialog.edit_description_dialog.text.toString()

                    firebaseRequest.updateIncomeInFirebase("receitas", income)
                    adapter.notifyDataSetChanged()
                    setTotalValue()
                    dialog?.dismiss()
                }

                val btnDelete_income = view_dialog.findViewById<Button>(R.id.btnDeleteExpense)
                btnDelete_income.setOnClickListener {
                    firebaseRequest.deleteIncomeInFirebase("receitas", income)
                    incomesList.remove(income)
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
        var totalIncomes = 0
        incomesList.forEach {
            totalIncomes += it.price.toInt()
        }
        val nf = NumberFormat.getInstance()
        val input = nf.format(totalIncomes)
        txt_total_incomes?.setText(input)
    }

    fun checkAdapterStatus(adapter: IncomesAdapter) {
        if(adapter.itemCount == 0) {
            backgroundEmpty_text_ic?.visibility = View.VISIBLE
            backgroundEmpty_income?.visibility = View.VISIBLE
        }
    }
}
