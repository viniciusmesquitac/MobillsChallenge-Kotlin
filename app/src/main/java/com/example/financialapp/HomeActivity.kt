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
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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

        val mOnNavigationItemSelectedListener = navigationView.navigationActionListener(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


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
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                true
            } else if(navigationView.currentFragment is IncomesFragment){
                val intent = Intent(this, SearchActivity::class.java)
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

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun configureToolbarColor(color: String) {

//        toolbar.setBackgroundColor(Color.parseColor(color))
//        fab.setBackgroundColor(Color.parseColor(color))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(navigationView.currentFragment is IncomesFragment) {
                fab.visibility = View.VISIBLE
                toolbar.setTitle("Receitas")
//                getWindow().setStatusBarColor(Color.parseColor("#32CD32"))
            } else if(navigationView.currentFragment is ExpensesFragment) {
                fab.visibility = View.VISIBLE
                toolbar.setTitle("Despesas")
//                getWindow().setStatusBarColor(Color.parseColor("#054F77"))
            } else if (navigationView.currentFragment is InsightFragment) {
                fab.visibility = View.GONE
                toolbar.setTitle("Insight")
//                getWindow().setStatusBarColor(Color.parseColor("#CCCC00"))
            }
//
        }
    }
}