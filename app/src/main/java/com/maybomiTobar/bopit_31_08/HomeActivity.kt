package com.maybomiTobar.bopit_31_08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class HomeActivity : AppCompatActivity()
{
    private lateinit var sharePref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setOnClickListeners()
    }

    private fun setOnClickListeners()
    {
        val playButtonIns = findViewById<Button>(R.id.playButtonHome)
        val aboutButtonIns = findViewById<Button>(R.id.aboutButtonHome)
        val preferencesButtonIns = findViewById<Button>(R.id.preferencesButtonHome)
        val restartDSButtonIns = findViewById<Button>(R.id.restartSettingsButtonHome)

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

        restartDSButtonIns.setOnClickListener()
        {
            sharePref = PreferenceManager.getDefaultSharedPreferences(this)
            sharePref.edit().putString("highscorePreference", "0").apply()
            sharePref.edit().putString("thresholdDifficultyIncreasePreference", "8").apply()
            sharePref.edit().putString("speedScalarInscreasePreference", "1.2").apply()
        }
    }
}