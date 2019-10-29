package com.example.financialapp.view

import androidx.appcompat.widget.AppCompatImageView
import com.example.financialapp.R

fun AppCompatImageView.selectByCategory(category: String) {
    when(category) {
        "Casa" -> return this.setImageResource(R.drawable.home)
        "Alimentação" -> return this.setImageResource(R.drawable.food)
        "Transporte" -> return this.setImageResource(R.drawable.transport)
        "Salários" -> return this.setImageResource(R.drawable.transport)
        "Emprestimos" -> return this.setImageResource(R.drawable.transport)
        "Investimentos" -> return this.setImageResource(R.drawable.transport)
    }
}