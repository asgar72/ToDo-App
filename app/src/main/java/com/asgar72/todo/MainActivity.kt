package com.asgar72.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottomNav)
        val navController = findNavController(R.id.navHostfragment)

        // Add a destination changed listener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check the destination id and show/hide views accordingly
            when (destination.id) {
                R.id.listFragment, R.id.StopWatch -> {
                    navView.visibility = View.VISIBLE
                }
                else -> {
                    navView.visibility = View.GONE
                }
            }
        }

        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostfragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
