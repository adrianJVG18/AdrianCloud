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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adriancloud.R
import com.example.adriancloud.home.lambda.LambdaReactorFragment
import com.example.adriancloud.home.settings.UpdateUserActivity
import com.example.adriancloud.home.wrapper.PostFormFragment
import com.example.adriancloud.home.wrapper.ApiWrapperFragment
import com.example.adriancloud.home.wrapper.Post
import com.example.adriancloud.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ApiWrapperFragment.ApiWrapperInterface,
    PostFormFragment.IPostFormReponse {



    private lateinit var auth: FirebaseAuth

    private val API_WRAPPER_TAG: String = "Api Wrapper"
    private val LAMBDA_REACTOR_TAG: String = "Lambda Reactor"
    private val POST_FORM_TAG: String = "Post Form"
    private var ACTIVE_TAG: String = LAMBDA_REACTOR_TAG


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val headerView: View = navView.getHeaderView(0)
        val textEmail: TextView = headerView.findViewById(R.id.header_user_email)
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
        updateCurrentFragment(LAMBDA_REACTOR_TAG)
    }

    fun updateCurrentFragment(fragmentTag: String) {
        ACTIVE_TAG = fragmentTag
        findViewById<Toolbar>(R.id.home_toolbar).title = fragmentTag
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
            R.id.update_profile -> launchEditProfileActivity()
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
    private fun launchEditProfileActivity(): Boolean{
        val intent = Intent(this, UpdateUserActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        var tag: String? = null

        when (item.itemId) {
            R.id.nav_apiwrapper -> {
                fragment = ApiWrapperFragment()
                tag = API_WRAPPER_TAG
            }
            R.id.nav_lambda_reactor -> {
                fragment = LambdaReactorFragment()
                tag = LAMBDA_REACTOR_TAG
            }
        }

        if (fragment != null && tag != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_container, fragment, tag)
                .commit()
            updateCurrentFragment(tag)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // SetListener de las acciones del fragment actual
    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is ApiWrapperFragment -> fragment.setOnCalledPostFormListener(this)
            is PostFormFragment -> fragment.setOnPostResponseListener(this)
        }
    }

    // ApiWrapper action
    override fun callAddPostForm(postFormMode: Int) {

        val postFormFragment = PostFormFragment()
        postFormFragment.FORM_MODE = postFormMode

        supportFragmentManager.beginTransaction()
            .hide(supportFragmentManager.findFragmentByTag(API_WRAPPER_TAG)!!)
            .add(R.id.home_container, postFormFragment, POST_FORM_TAG)
            .addToBackStack(POST_FORM_TAG)
            .commit()
        updateCurrentFragment(POST_FORM_TAG)
    }


    // PostForm action
    override fun onCreatedPost(post: Post) {
        val apiWrapperFrgmnt = supportFragmentManager.findFragmentByTag(API_WRAPPER_TAG) as ApiWrapperFragment
        val postFormFrgmnt = supportFragmentManager.findFragmentByTag(POST_FORM_TAG) as PostFormFragment

        supportFragmentManager.beginTransaction()
            .remove(postFormFrgmnt)
            .show(apiWrapperFrgmnt)
            .commit()

        apiWrapperFrgmnt.onPostCreated(post)
    }

    // PostForm action
    override fun onCanceledPost(mode: Int) {
        var message = "error"
        when (mode) {
            PostFormFragment.ADDING_POST -> message = "Se ha cancelado el nuevo post"
            PostFormFragment.UPDATE_POST -> message = "Se ha cancelado la edicion del post"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    // PostForm action
    override fun onUpdatedPost(post: Post) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callUpdateProfile() {
        launchEditProfileActivity()
    }
}
