package com.example.cropapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

// Import all the fragments and the LoginActivity
import com.example.cropapp.CropFragment
import com.example.cropapp.WeatherFragment
import com.example.cropapp.MarketFragment
import com.example.cropapp.ScannerFragment
import com.example.cropapp.ProfileFragment
import com.example.cropapp.FriendsFragment
import com.example.cropapp.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val btnExitApp = findViewById<Button>(R.id.btnExitApp)

        btnExitApp.setOnClickListener {
            showExitDialog()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_crop -> loadFragment(CropFragment())
                R.id.nav_weather -> loadFragment(WeatherFragment())
                R.id.nav_market -> loadFragment(MarketFragment())
                R.id.nav_scanner -> loadFragment(ScannerFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
                R.id.nav_friends -> loadFragment(FriendsFragment())
            }
            true
        }

        if (savedInstanceState == null) {
            loadFragment(CropFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
