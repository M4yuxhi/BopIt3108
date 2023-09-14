package com.maybomiTobar.bopit_31_08

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val delayMillis = 3000L //Segundos de espera.
        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intent);
            finish()
        }, delayMillis)
    }


}