package com.example.financialapp.View

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.financialapp.Fragments.ExpensesFragment
import com.example.financialapp.Fragments.InsightFragment
import com.example.financialapp.Fragments.IncomesFragment
import com.example.financialapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationBottomView(internal var iNavBottomView: INavBottomView): IHomeView {

    private var content: FrameLayout? = null

    companion object {
        var currentFragment: Fragment = ExpensesFragment()
    }


    override fun addFragment(fragment: Fragment, supportFragmentManager: FragmentManager) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
                .commit()
    }

    override fun navigationActionListener(supportFragmentManager: FragmentManager): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_expense -> {
                    if(currentFragment is ExpensesFragment) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val fragment = ExpensesFragment()
                    currentFragment = fragment
                    addFragment(fragment, supportFragmentManager)
                    iNavBottomView.configureToolbarColor("#4682B4")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_income -> {
                    if(currentFragment is IncomesFragment) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val fragment = IncomesFragment()
                    currentFragment = fragment
                    addFragment(fragment, supportFragmentManager)
                    iNavBottomView.configureToolbarColor("#90EE90")

                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_insight -> {
                    if(currentFragment is InsightFragment) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val fragment = InsightFragment()
                    currentFragment = fragment
                    addFragment(fragment, supportFragmentManager)
                    iNavBottomView.configureToolbarColor("#CCCC00")
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}