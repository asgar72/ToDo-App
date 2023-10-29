package com.asgar72.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar?.hide()

        //action bar name throw nav controller
        setupActionBarWithNavController(findNavController(R.id.navHostfragment))

    }

    //this is function for setupActionBarWithNavController
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostfragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}