package com.example.financialapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.Model.Expense
import com.example.financialapp.R
import kotlinx.android.synthetic.main.payment_item.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat

class ExpensesAdapter(val payments: MutableList<Expense>): RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder> (), Filterable{

    internal var filterListResult: MutableList<Expense>

    init {
        filterListResult = payments
    }

    override fun getFilter(): Filter {
        return object: Filter() {

            override fun performFiltering(p0: CharSequence?): FilterResults {
                val resultList = mutableListOf<Expense>()
                val char = p0.toString()
                payments.forEach {
                    if(it.description.toString().toLowerCase().contains(char.toLowerCase())){
                        resultList.add(it)
                    }
                    filterListResult = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterListResult
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterListResult = p1!!.values as MutableList<Expense>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return ExpenseViewHolder(inflate)
    }

    override fun getItemCount(): Int = filterListResult.size

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(filterListResult[position])
    }

    inner class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Expense) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            with(payment){
                itemView.txt_description.text = description
                itemView.txt_category.text = category
                itemView.txt_date.text = sdf.format(date).toString()

                val nf = NumberFormat.getInstance()
                val input = nf.format(price)
                itemView.txt_price.text = "R$ "+input
            }
        }
    }


}