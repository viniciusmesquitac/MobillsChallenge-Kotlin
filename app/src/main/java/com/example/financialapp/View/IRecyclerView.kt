package com.example.financialapp.View

import androidx.recyclerview.widget.RecyclerView

interface IRecyclerView {
    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener)
}