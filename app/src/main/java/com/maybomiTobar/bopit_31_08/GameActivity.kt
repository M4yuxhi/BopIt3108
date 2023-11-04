package com.maybomiTobar.bopit_31_08

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import kotlin.math.abs

class GameActivity : AppCompatActivity(), SensorEventListener
{
    private lateinit var backgroundMediaPlayer : MediaPlayer
    private lateinit var fxMediaPlayer : MediaPlayer
    private lateinit var playbackParams: PlaybackParams
    private var bgMusicVolume : Float = 0.2f
    private lateinit var volumeValueTV : TextView

    private lateinit var gestureDetector : GestureDetector

    private lateinit var sensorManager : SensorManager
    private var accelerometerSensor : Sensor? = null
    private var prevValueAcceleration = 0.0f
    private var currentInstruction = " "
    private lateinit var instructionTV: TextView
    private lateinit var instructionTimeTV : TextView
    private lateinit var possibleIntructions : Array<String>
    private var instructionTimer : CountDownTimer? = null
    private val instructionTime = 5000

    private lateinit var scoreTV : TextView
    private var score = 0
    private var asignedScore = 100

    private var actionWasPerformed = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gestureDetector = GestureDetector(this, GestureListener())

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        instructionTV = findViewById(R.id.instructionTVG)
        instructionTimeTV = findViewById(R.id.instructionTimeTVG)
        scoreTV = findViewById(R.id.scoreTVG2)
        possibleIntructions = arrayOf("Toca Bop It", "Desliza", "Agita")

        initializeBGMediaPlayer()

        handlers()
        setOnClickListeners()
    }

    override fun onPause()
    {
        super.onPause()
        backgroundMediaPlayer.pause()
        fxMediaPlayer.stop()

        sensorManager.unregisterListener(this)
    }

    override fun onResume()
    {
        super.onResume()
        backgroundMediaPlayer.start()

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)
    {

    }

    override fun onSensorChanged(event: SensorEvent)
    {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val diff = abs(x + y + z - prevValueAcceleration)
            val scaleFactor = Math.pow(10.0, 10.0).toFloat()
            val acceleration = (diff / event.timestamp) * scaleFactor

            prevValueAcceleration = x + y + z

            val threshold = 0.01f

            if(acceleration > threshold && currentInstruction == possibleIntructions[2])
                correctPerformance()

        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun bopItLogic()
    {
        scoreTV.text = score.toString()

        val random = Random
        val randomNumber = random.nextInt(1, 4)

        currentInstruction = possibleIntructions[randomNumber - 1]
        instructionTV.text = currentInstruction

        instructionTimer?.cancel()

        instructionTimer = object : CountDownTimer(instructionTime.toLong(), 1000)
        {

            override fun onTick(millisUntilFinished : Long)
            {
                instructionTimeTV.text = (1 + millisUntilFinished / 1000).toString()
            }

            override fun onFinish()
            {
                if(!actionWasPerformed)
                {
                    setMusicOnFXMP(R.raw.lose_sound)
                }

                fxMediaPlayer.start()
                bopItLogic()
            }

        }

        instructionTimer?.start()
        actionWasPerformed = false
    }

    private fun changeSoundSpeed(scalar : Float)
    {
        backgroundMediaPlayer.playbackParams.setSpeed(playbackParams.speed * scalar)
    }

    private fun correctPerformance()
    {
        //showToast(R.string.correctActionMessage.toString())
        actionWasPerformed = true
        score += asignedScore
        setMusicOnFXMP(R.raw.win_sound)
        bopItLogic()
    }

    private fun handlers()
    {
        val handlerLogic = Handler()
        val milliseconds = 500

        handlerLogic.postDelayed({ bopItLogic() }, milliseconds.toLong())
    }

    private fun initializeBGMediaPlayer()
    {
        backgroundMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        backgroundMediaPlayer.start()
        backgroundMediaPlayer.setVolume(bgMusicVolume, bgMusicVolume)

        playbackParams = backgroundMediaPlayer.playbackParams
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
        val buttonBopIt : Button = findViewById(R.id.bopItButtonG)

        buttonBopIt.setOnClickListener()
        {
            if(currentInstruction == possibleIntructions[0])
            {
                correctPerformance()
            }
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener()
    {

        /*
        override fun onDown(e : MotionEvent) : Boolean
        {
            showToast("onDown")

            if(currentInstruction == possibleIntructions[0])
            {
                correctPerformance()
            }

            return true
        }*/

        /*
        override fun onSingleTapUp(e : MotionEvent) : Boolean
        {
            showToast("onSingleTapUp")
            return true
        }*/

        /*
        override fun onLongPress(e : MotionEvent)
        {
            showToast("onLongPress")
        }
        */
        /*
        override fun onFling(
            e1: MotionEvent, e2: MotionEvent,
            velocityX : Float, velocityY: Float
        ) : Boolean
        {
            showToast("onFling")
            return true
        }
        */

        override fun onScroll(
            e1 : MotionEvent, e2 : MotionEvent,
            distanceX : Float, distanceY : Float
        ) : Boolean
        {
            showToast("onScroll")

            if(currentInstruction == possibleIntructions[1])
            {
                correctPerformance()
            }

            return true
        }
    }

    private fun showToast(message : String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}