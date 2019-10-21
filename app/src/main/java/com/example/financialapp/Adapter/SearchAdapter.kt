package com.example.financialapp.Adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Payment
import com.example.financialapp.R
import kotlinx.android.synthetic.main.payment_item.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat


class SearchAdapter<T>(val list: MutableList<T>, internal var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filterListResult: MutableList<*>

    init {
        filterListResult = list
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(p0: CharSequence?): FilterResults {
                val resultList = mutableListOf<Payment>()
                val char = p0.toString()
                list as MutableList<Payment>
                filterListResult
                list.forEach {
                    if (it.description.toString().toLowerCase().contains(char.toLowerCase())) {
                        resultList.add(it)
                    }
                    filterListResult = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterListResult
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterListResult = p1!!.values as MutableList<Payment>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(filterListResult[0] is Expense) {
            val inflate = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
            return ExpenseViewHolder(inflate)
        } else {
            val inflate = LayoutInflater.from(parent.context).inflate(R.layout.income_item, parent, false)
            return ExpenseViewHolder(inflate)
        }

    }

    override fun getItemCount(): Int = filterListResult.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder1 = holder as SearchAdapter<*>.ExpenseViewHolder
        holder1.bind(filterListResult[position] as Payment)
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Payment) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            with(payment) {
                if(payment is Expense) {
                    itemView.txt_description.text = description
                    itemView.txt_category.text = category
                    itemView.txt_date.text = sdf.format(date).toString()
                    val nf = NumberFormat.getInstance()
                    val input = nf.format(price)
                    itemView.txt_price.text = "R$ " + input
                } else {
                    itemView.txt_description.text = description
                    val nf = NumberFormat.getInstance()
                    val input = nf.format(price)
                    itemView.txt_price.text = "R$ $input"
                }
            }
        }
    }

}