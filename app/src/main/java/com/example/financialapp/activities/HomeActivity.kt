package com.example.financialapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.financialapp.fragments.ExpensesFragment
import com.example.financialapp.presenter.InterfaceLoginPresenter
import com.example.financialapp.presenter.LoginPresenter
import com.example.financialapp.view.ILoginView
import com.example.financialapp.view.NavigationBottomView
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.view.View
import androidx.core.view.forEach
import com.example.financialapp.fragments.IncomesFragment
import com.example.financialapp.fragments.InsightFragment
import com.example.financialapp.R
import com.example.financialapp.view.INavBottomView
import kotlinx.android.synthetic.main.card_view_insight.*


class HomeActivity : AppCompatActivity(), ILoginView, INavBottomView {

    override fun resetIcons() {
        val menu = navigation.menu
        menu.findItem(R.id.navigation_insight).setIcon(R.drawable.lightbulb_outlined)
    }


    override fun onLoginResult(message: String) {}

    override fun onErrorResult(message: String) {}

    override fun onInformationResult(message: String) {}

    private lateinit var loginPresenter: InterfaceLoginPresenter
    private lateinit var navigationView: NavigationBottomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        // MARK: INIT PRESENTER
        loginPresenter = LoginPresenter(this)

        // MARK: SETUP NAVIGATION BOTTOM VIEW - SUPPORT FRAGMENT MANAGER
        navigationView = NavigationBottomView(this)

        if(savedInstanceState==null || content == null) {

            val fragment = NavigationBottomView.currentFragment

            when(fragment) {
                is ExpensesFragment -> navigation.selectedItemId = R.id.navigation_expense
                is IncomesFragment -> navigation.selectedItemId = R.id.navigation_income
            }

            navigationView.addFragment(fragment, supportFragmentManager)
        }

        val navigationListener = navigationView
                .navigationActionListener(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(navigationListener)


        //SET CLICK ACTIONS
        fab.setOnClickListener {
            when {
                NavigationBottomView.currentFragment is ExpensesFragment -> {
                    val intent = Intent(this, InsertExpensesActivity::class.java)
                    startActivity(intent)
                }
                NavigationBottomView.currentFragment is IncomesFragment -> {
                    val intent = Intent(this, InsertIncomesActivity::class.java)
                    startActivity(intent)
                }
                else -> {  }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setFabVisibility() {

        when (NavigationBottomView.currentFragment) {
             is IncomesFragment -> fab.visibility = View.VISIBLE
             is ExpensesFragment -> fab.visibility = View.VISIBLE
             is InsightFragment -> fab.visibility = View.GONE
        }

    }
}