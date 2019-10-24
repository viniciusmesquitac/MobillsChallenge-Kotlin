package com.example.financialapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.model.Income
import com.example.financialapp.R
import com.example.financialapp.activities.SearchIncomesActivity
import com.example.financialapp.model.formatted
import kotlinx.android.synthetic.main.card_view_insight.view.*
import kotlinx.android.synthetic.main.payment_item.view.*

class IncomesAdapter(val incomes: MutableList<Income>, internal var context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder> (), Filterable{

    internal var filterListResult: MutableList<Income>

    init {
        filterListResult = incomes
    }

    var totalIncomes = 0.0

    override fun getFilter(): Filter {
        return object: Filter() {

            override fun performFiltering(p0: CharSequence?): FilterResults {
                val resultList = mutableListOf<Income>()
                val char = p0.toString()
                incomes.forEach {
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
                filterListResult = p1!!.values as MutableList<Income>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.card_view_payments, parent, false)
                CardViewHolder(inflate)
            } else -> {
                val inflate = LayoutInflater.from(parent.context).inflate(R.layout.income_item, parent, false)
                IncomesViewHolder(inflate)
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
                val holder1 = holder as IncomesViewHolder
                holder1.bind(filterListResult[position - 1])
            }
        }

    }

    inner class IncomesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Income) {

            with(payment){
                itemView.txt_description.text = description
                itemView.txt_price.text = price.formatted()
            }
        }
    }

    inner class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.txt_total_insight.text = totalIncomes.formatted()


            itemView.toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {

                    R.id.btnSearch -> {
                        val intent = Intent(context, SearchIncomesActivity::class.java)
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