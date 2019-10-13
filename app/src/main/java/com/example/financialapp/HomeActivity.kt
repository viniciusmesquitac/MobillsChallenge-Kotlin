package com.example.financialapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.financialapp.Fragments.ExpensesFragment
import com.example.financialapp.Presenter.InterfaceLoginPresenter
import com.example.financialapp.Presenter.LoginPresenter
import com.example.financialapp.View.ILoginView
import com.example.financialapp.View.NavigationBottomView
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.view.View
import com.example.financialapp.Fragments.IncomesFragment
import com.example.financialapp.Fragments.InsightFragment
import com.example.financialapp.View.INavBottomView


class HomeActivity : AppCompatActivity(), ILoginView, INavBottomView {


    override fun onLoginResult(message: String) {}

    override fun onErrorResult(message: String) {}

    override fun onInformationResult(message: String) {}

    internal lateinit var loginPresenter: InterfaceLoginPresenter
    internal lateinit var navigationView: NavigationBottomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        // MARK: INIT PRESENTER
        loginPresenter = LoginPresenter(this)

        // MARK: SETUP NAVIGATION BOTTOM VIEW - SUPPORT FRAGMENT MANAGER
        navigationView = NavigationBottomView(this)

        if(savedInstanceState==null || content == null){
            Log.d("null", "deu null")
            navigation.selectedItemId = R.id.home
            val fragment = ExpensesFragment()
            navigationView.addFragment(fragment, supportFragmentManager)
        }

        val navigationListener = navigationView
                .navigationActionListener(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(navigationListener)


        //SET CLICK ACTIONS
        fab.setOnClickListener {
            if(navigationView.currentFragment is ExpensesFragment) {
                val intent = Intent(this, InsertExpensesActivity::class.java)
                startActivity(intent)
            } else if(navigationView.currentFragment is IncomesFragment){
                val intent = Intent(this, InsertIncomesActivity::class.java)
                startActivity(intent)
            } else {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.btnSearch -> {
            if(navigationView.currentFragment is ExpensesFragment) {
                val intent = Intent(this, SearchExpensesActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                true
            } else if(navigationView.currentFragment is IncomesFragment){
                val intent = Intent(this, SearchIncomesActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                true
            } else {
                true
            }

        }
        R.id.logout -> {
            loginPresenter.onLogout(this)
            true
        }

        R.id.settings -> {
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    @SuppressLint("RestrictedApi")
    override fun configureToolbarColor(color: String) {

        if(navigationView.currentFragment is IncomesFragment) {
            //noinspection RestrictedApi
            fab.visibility = View.VISIBLE
            toolbar.setTitle("Receitas")
        } else if(navigationView.currentFragment is ExpensesFragment) {
            fab.visibility = View.VISIBLE
            toolbar.setTitle("Despesas")
        } else if (navigationView.currentFragment is InsightFragment) {
            fab.visibility = View.GONE
            toolbar.setTitle("Insight")
        }

    }
}