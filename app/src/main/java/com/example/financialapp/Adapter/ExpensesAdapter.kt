package com.example.financialapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.financialapp.Model.Expense
import com.example.financialapp.R
import kotlinx.android.synthetic.main.payment_item.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat

class ExpensesAdapter(val payments: MutableList<Expense>): RecyclerView.Adapter<ExpensesAdapter.ContactViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return ContactViewHolder(inflate)
    }

    override fun getItemCount(): Int = payments.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
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