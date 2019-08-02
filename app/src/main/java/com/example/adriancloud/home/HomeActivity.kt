package com.example.adriancloud.home

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.adriancloud.R
import com.example.adriancloud.home.lambda.LambdaReactorFragment
import com.example.adriancloud.home.wrapper.ApiWrapperFragment
import com.example.adriancloud.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth

    private val API_WRAPPER_TAG : String = "Api Wrapper"
    private val PICTURE_STUDIO_TAG : String = "Picture Studio"
    private val LAMBDA_REACTOR_TAG : String = "Lambda Reactor"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val headerView : View = navView.getHeaderView(0)
        val textEmail : TextView = headerView.findViewById(R.id.header_user_email)
        textEmail.text = auth.currentUser?.email

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        navView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
            .add(R.id.home_container, LambdaReactorFragment(), LAMBDA_REACTOR_TAG)
            .commit()
        findViewById<Toolbar>(R.id.home_toolbar).title = LAMBDA_REACTOR_TAG


    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.sign_out -> signOut()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut(): Boolean {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment : Fragment? = null
        var tag : String? = null

        when (item.itemId) {
            R.id.nav_apiwrapper -> {
                fragment = ApiWrapperFragment()
                tag = API_WRAPPER_TAG
            }
            R.id.nav_nav_threading_lab -> {

            }
            R.id.nav_picture_studio -> {
                tag = PICTURE_STUDIO_TAG
            }
            R.id.nav_lambda_reactor -> {
                tag = LAMBDA_REACTOR_TAG
            }
        }

        if (fragment != null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, tag)
                .commit()
            findViewById<Toolbar>(R.id.home_toolbar).title = tag
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
