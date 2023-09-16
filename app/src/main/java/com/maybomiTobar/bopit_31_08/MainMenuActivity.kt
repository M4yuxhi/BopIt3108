package com.maybomiTobar.bopit_31_08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.content.Intent

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val aboutButtonIns = findViewById<Button>(R.id.aboutButton)
        val preferencesButtonIns = findViewById<Button>(R.id.preferencesButton)

        aboutButtonIns.setOnClickListener()
        {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        preferencesButtonIns.setOnClickListener()
        {
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }
    }
}