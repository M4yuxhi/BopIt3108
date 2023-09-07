package com.maybomiTobar.bopit_31_08

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
    //Variable Global - Global Variable
    private var generalCount = 0;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Variable Local - Local Variable
        val localCount = 0

        //Find Views
        /*
        val buttonIncrement = findViewById<Button>(R.id.buttonIncrement)
        val buttonNavigate = findViewById<Button>(R.id.buttonNavigate)

        //Button click listeners
        buttonIncrement.setOnClickListener
        {
            // Increment the global counter
            generalCount++
            // Update the text on a TextView (not shown in this example)
        }

        buttonNavigate.setOnClickListener
        {

        }*/
    }
}