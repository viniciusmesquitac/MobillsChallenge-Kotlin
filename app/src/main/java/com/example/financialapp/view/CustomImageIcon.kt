package com.example.financialapp.view

import androidx.appcompat.widget.AppCompatImageView
import com.example.financialapp.R

fun AppCompatImageView.selectByCategory(category: String) {
    when(category) {
        "Casa" -> return this.setImageResource(R.drawable.home)
        "Alimentação" -> return this.setImageResource(R.drawable.food)
        "Transporte" -> return this.setImageResource(R.drawable.transport)
    }
}