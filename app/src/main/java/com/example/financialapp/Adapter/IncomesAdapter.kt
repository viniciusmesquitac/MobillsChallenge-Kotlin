package com.example.financialapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.Model.Expense
import com.example.financialapp.Model.Income
import com.example.financialapp.R
import kotlinx.android.synthetic.main.payment_item.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat

class IncomesAdapter(val incomes: MutableList<Income>): RecyclerView.Adapter<IncomesAdapter.IncomesViewHolder> (), Filterable{

    internal var filterListResult: MutableList<Income>

    init {
        filterListResult = incomes
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomesViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.income_item, parent, false)
        return IncomesViewHolder(inflate)
    }

    override fun getItemCount(): Int = filterListResult.size

    override fun onBindViewHolder(holder: IncomesViewHolder, position: Int) {
        holder.bind(filterListResult[position])
    }

    inner class IncomesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Income) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            with(payment){
                itemView.txt_description.text = description
                //itemView.txt_date.text = sdf.format(date).toString()

                val nf = NumberFormat.getInstance()
                val input = nf.format(price)
                itemView.txt_price.text = "R$"+input
            }
        }
    }


}