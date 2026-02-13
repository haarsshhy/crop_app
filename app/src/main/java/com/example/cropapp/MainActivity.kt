package com.example.cropapp

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    private val CHANNEL_ID = "crop_notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

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

        handleIntent(intent)

        // Show a big text notification when app opens, only if not navigating from a notification
        if (intent.getStringExtra("navigateTo") == null) {
            showBigTextNotification(
                "Welcome to Crop App",
                "Your personal farming assistant is ready! We have updated the latest market prices and weather forecasts for your region. Check out the recommendation screen to optimize your harvest."
            )
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (intent.getStringExtra("navigateTo") == "market") {
            loadFragment(MarketFragment())
            bottomNav.selectedItemId = R.id.nav_market
        } else {
            if (supportFragmentManager.findFragmentById(R.id.container) == null) {
                loadFragment(CropFragment())
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Crop App Notifications"
            val descriptionText = "Channel for Crop App updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showActionableNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "market")
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            // Adding the Action Button
            .addAction(android.R.drawable.ic_menu_view, "VIEW MARKET", pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                notify(103, builder.build())
            }
        }
    }

    fun showBigTextNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                notify(101, builder.build())
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
