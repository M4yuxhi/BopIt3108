package com.maybomiTobar.bopit_31_08

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.roundToInt

class GameActivity : AppCompatActivity()
{
    private lateinit var backgroundMediaPlayer : MediaPlayer
    private lateinit var fxMediaPlayer : MediaPlayer
    private lateinit var playbackParams: PlaybackParams
    private var bgMusicVolume : Float = 0.3f
    private lateinit var volumeValueTV : TextView

    private lateinit var gestureDetector : GestureDetector

    private lateinit var sensorManager : SensorManager
    private lateinit var accelerometerSensor : Sensor
    private lateinit var sensorActiveList : ArrayList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gestureDetector = GestureDetector(this, GestureListener())

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        initializeBGMediaPlayer()

        handlers()
        setOnClickListeners()
    }

    override fun onPause()
    {
        super.onPause()
        backgroundMediaPlayer.pause()
        fxMediaPlayer.stop()
    }

    override fun onResume()
    {
        super.onResume()
        backgroundMediaPlayer.start()
    }
/*
    override fun onSensorChanged(event : SensorEvent)
    {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            var dx = Math.pow(event.values[0].toDouble(), 2.0)
            var dy = Math.pow(event.values[1].toDouble(), 2.0)
            var dz = Math.pow(event.values[2].toDouble(), 2.0)

            var acceleration = Math.sqrt(dx + dy + dz).toFloat()

            val threshold = 10.0f

            if(acceleration > threshold)
            {
                //Revisa si la aceleración es valida para ser una agitación.

            }
            else
            {

            }
        }
    }
    */

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun changeSoundSpeed(option : Int)
    {
        if(option == 1)
        {
            backgroundMediaPlayer.playbackParams.setSpeed(playbackParams.speed + 0.1f)
            return
        }

        backgroundMediaPlayer.playbackParams.setSpeed(playbackParams.speed - 0.1f)
    }

    private fun handlers()
    {

    }

    private fun initializeBGMediaPlayer()
    {
        backgroundMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        backgroundMediaPlayer.start()
        backgroundMediaPlayer.setVolume(bgMusicVolume, bgMusicVolume)

        playbackParams = backgroundMediaPlayer.playbackParams

        volumeValueTV = findViewById(R.id.textViewVolumeValueG)
        volumeValueTV.text = (bgMusicVolume * 10).roundToInt().toString()
    }

    private fun restartSoundSpeed()
    {
        backgroundMediaPlayer.playbackParams.setSpeed(1.0f)
    }

    private fun setMusicOnFXMP(id : Int)
    {
        fxMediaPlayer = MediaPlayer.create(this, id)
        fxMediaPlayer.start()
    }

    private fun setOnClickListeners()
    {
        val buttonLessVolume : Button = findViewById(R.id.buttonLessVolume)
        val buttonMoreVolume : Button = findViewById(R.id.buttonMoreVolume)

        buttonLessVolume.setOnClickListener()
        {
            if(bgMusicVolume >= 0.1f)
            {
                bgMusicVolume -= 0.1f
                backgroundMediaPlayer.setVolume(bgMusicVolume, bgMusicVolume)
                volumeValueTV.text = (bgMusicVolume * 10).roundToInt().toString()
            }
        }

        buttonMoreVolume.setOnClickListener()
        {
            if(bgMusicVolume <= 0.9f)
            {
                bgMusicVolume += 0.1f
                backgroundMediaPlayer.setVolume(bgMusicVolume, bgMusicVolume)
                volumeValueTV.text = (bgMusicVolume * 10).roundToInt().toString()
            }
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener()
    {
        override fun onDown(e : MotionEvent) : Boolean
        {
            showToast("onDown")
            return true
        }

        override fun onSingleTapUp(e : MotionEvent) : Boolean
        {
            showToast("onSingleTapUp")
            return true
        }

        override fun onLongPress(e : MotionEvent)
        {
            showToast("onLongPress")
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX : Float, velocityY: Float
        ) : Boolean
        {
            showToast("onFling")
            return true
        }

        override fun onScroll(
            e1 : MotionEvent, e2 : MotionEvent,
            distanceX : Float, distanceY : Float
        ) : Boolean
        {
            showToast("onScroll")
            return true
        }
    }

    private fun showToast(message : String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}