package com.example.financialapp.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

interface IHomeView {
    fun navigationActionListener(supportFragmentManager: FragmentManager): BottomNavigationView.OnNavigationItemSelectedListener
    fun addFragment(fragment: Fragment, supportFragmentManager: FragmentManager)
}