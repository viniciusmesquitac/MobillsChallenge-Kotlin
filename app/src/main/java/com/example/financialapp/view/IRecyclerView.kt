package com.example.financialapp.view

import androidx.recyclerview.widget.RecyclerView

interface IRecyclerView {
    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener)
}