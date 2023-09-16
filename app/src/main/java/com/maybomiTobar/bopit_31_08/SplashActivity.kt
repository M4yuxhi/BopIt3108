package com.maybomiTobar.bopit_31_08

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val delayMillis = 3000L //3 segundos de espera.
        val intent = Intent(this, MainMenuActivity::class.java);

        var handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intent);
            finish()
        }, delayMillis)
    }


}