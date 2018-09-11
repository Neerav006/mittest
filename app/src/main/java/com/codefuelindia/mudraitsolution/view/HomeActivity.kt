package com.codefuelindia.mudraitsolution.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.codefuelindia.mudraitsolution.Utils.Utils
import com.codefuelindia.mudraitsolution.cons.Constants
import kotlinx.android.synthetic.main.content_home.*
import com.codefuelindia.mudraitsolution.R

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var name = ""
    private val PERMISSSION_WRITE_EXTERNAL = 420


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Home"
        val sharedPref = Utils.getSharedPreference(Constants.MY_PREF, this@HomeActivity)

        name = sharedPref.getString(Constants.NAME, "")

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        navigationView.getHeaderView(0).findViewById<TextView>(R.id.tvTitle).text = name



        if (ContextCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@HomeActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                ActivityCompat.requestPermissions(this@HomeActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSSION_WRITE_EXTERNAL)

                Toast.makeText(this@HomeActivity, "Write storage permission needed for pdf", Toast.LENGTH_LONG).show()

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this@HomeActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSSION_WRITE_EXTERNAL)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // permission is granted for




        }


        cvAddInvoice.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddInvoiceActivity::class.java))
        }


        cvViewInvoice.setOnClickListener {

            startActivity(Intent(this@HomeActivity, ViewTransactionActivity::class.java))

        }

        cvTodayInvoice.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
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
        val id = item.itemId
        return when (id) {
            R.id.action_change_pwd -> {
                val intent = Intent(this@HomeActivity, ChangePwdActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_log_out -> {
                val editor = Utils.writeToPreference(Constants.MY_PREF, this@HomeActivity)
                editor.putBoolean(Constants.IS_LOGIN, false)

                editor.putString(Constants.USER_NAME, null)
                editor.putString(Constants.NAME, null)
                editor.putString(Constants.ID, null)
                editor.putString(Constants.ROLE, null)
                editor.putBoolean(Constants.IS_REGISTERED, false)
                editor.putString(Constants.MEMBER_ID, null)
                editor.putString(Constants.MEMBER_NO, null)
                editor.putString(Constants.GOR, null)
                editor.putString(Constants.MEMBER_STATUS, null)
                editor.putString("family_count", "0")
                editor.putString("addr", "")
                editor.putString("gr_count", "0")
                editor.apply()


                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_about_developer -> {
                // Handle the camera action
                startActivity(Intent(this@HomeActivity, AboutDevActivity::class.java))

            }

        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
