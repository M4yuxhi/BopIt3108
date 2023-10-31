package com.maybomiTobar.bopit_31_08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class HomeActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val playButtonIns = findViewById<Button>(R.id.playButtonHome)
        val aboutButtonIns = findViewById<Button>(R.id.aboutButtonHome)
        val preferencesButtonIns = findViewById<Button>(R.id.preferencesButtonHome)

        playButtonIns.setOnClickListener()
        {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

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