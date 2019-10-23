package com.example.financialapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.model.Expense
import com.example.financialapp.R
import com.example.financialapp.activities.SearchExpensesActivity
import com.example.financialapp.model.formatted
import kotlinx.android.synthetic.main.card_view_insight.view.*
import kotlinx.android.synthetic.main.payment_item.view.*

class ExpensesAdapter(val payments: MutableList<Expense>, internal  var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder> (), Filterable {

    var filterListResult: MutableList<Expense>
    var totalInsight = 0f

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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            0 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.card_view_payments, parent, false)
                return CardViewHolder(inflate)
            }

            else -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
                return ExpenseViewHolder(inflate)
            }

        }
    }

    override fun getItemCount(): Int = filterListResult.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {

            0 -> {
                val cardHolder = holder as CardViewHolder
                cardHolder.bind()
            } else -> {
                val holder1 = holder as ExpenseViewHolder
                holder1.bind(filterListResult[position - 1])
            }

        }
    }

    inner class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Expense) {

            with(payment){
                itemView.txt_description.text = description
                itemView.txt_category.text = category
                itemView.txt_date.text = date.formatted()
                itemView.txt_price.text = price.formatted()
            }
        }
    }

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.txt_total_insight.text = totalInsight.toString()

            itemView.toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {

                    R.id.btnSearch -> {
                        val intent = Intent(context, SearchExpensesActivity::class.java)
                        context.startActivity(intent)
                        true

                    } else -> {
                        true
                    }
                }
            }
        }
    }


}