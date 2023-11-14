package com.maybomiTobar.bopit_31_08

import android.content.Context
import android.content.SharedPreferences
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
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlin.random.Random
import kotlin.math.sqrt
import android.app.AlertDialog
import android.os.Looper

class GameActivity : AppCompatActivity(), SensorEventListener
{
    private lateinit var backgroundMediaPlayer : MediaPlayer
    private lateinit var fxMediaPlayer : MediaPlayer
    private lateinit var playbackParams: PlaybackParams
    private var bgMusicVolume : Float = 0.2f

    private lateinit var gestureDetector : GestureDetector

    private lateinit var sensorManager : SensorManager
    private var accelerometerSensor : Sensor? = null
    private var currentInstruction = " "
    private lateinit var instructionTV: TextView
    private lateinit var instructionTimeTV : TextView
    private lateinit var possibleInstructions : Array<String>
    private var instructionTimer : CountDownTimer? = null
    private var instructionTime = 5000

    private lateinit var scoreTV : TextView
    private var score = 0
    private var asignedScore = 100

    private var actionWasPerformed = false
    private var actionsWellPerformed = 0
    private var prevRandomNum = -1

    private var thresholdDifficultyIncrease = 8
    private var highscore = 0
    private var speedScalar = 1.2f
    private var continueLoop = true
    private lateinit var sharePref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gestureDetector = GestureDetector(this, GestureListener())

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        getTextViews()

        getValuesFromPreferences()

        possibleInstructions = arrayOf(
            applicationContext.getString(R.string.BotIpPressInstruct),
            applicationContext.getString(R.string.SwipeInstruct),
            applicationContext.getString(R.string.ShakeInstruct)
        )

        initializeBGMediaPlayer()

        handlers()
        setOnClickListeners()
    }

    override fun onPause()
    {
        backgroundMediaPlayer.pause()
        fxMediaPlayer.pause()

        sensorManager.unregisterListener(this)
        instructionTimer?.cancel()
        super.onPause()
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

            val acceleration = sqrt(x * x + y * y + z * z.toDouble()).toFloat()

            val threshold = 25f

            if(acceleration > threshold && currentInstruction == possibleInstructions[2])
            {
                Log.i("ValidationOnSensorChange", acceleration.toString() + " > " + threshold.toString())
                correctPerformance()
            }
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

        if (actionsWellPerformed >= thresholdDifficultyIncrease && instructionTime > 1)
        {
            changeSoundSpeedByScalar(2.0f)
            instructionTime -= 250
            actionsWellPerformed = 0
        }

        val randomNumber = getRandomInstruction()
        prevRandomNum = randomNumber


        currentInstruction = possibleInstructions[randomNumber - 1]
        instructionTV.text = currentInstruction

        instructionTimer?.cancel()

        instructionTimer = object : CountDownTimer(instructionTime.toLong(), 1000)
        {

            override fun onTick(millisUntilFinished : Long)
            {
                (1 + millisUntilFinished / 1000).toString().also { instructionTimeTV.text = it }
            }

            override fun onFinish()
            {
                if(!actionWasPerformed)
                {
                    if(!checkHighscore())
                    {
                        setMusicOnFXMP(R.raw.lose_sound, false)
                        fxMediaPlayer.start()
                    }
                }

                if(continueLoop) bopItLogic()
            }

        }

        instructionTimer?.start()
        actionWasPerformed = false
    }

    private fun changeSoundSpeedByScalar(scalar : Float)
    {
        val currentSpeed = backgroundMediaPlayer.playbackParams.speed

        if(currentSpeed * scalar > 4) return

        val params = backgroundMediaPlayer?.playbackParams?.setSpeed(currentSpeed * scalar)
        backgroundMediaPlayer.playbackParams = params!!
    }

    private fun checkHighscore() : Boolean
    {
        var value = false
        continueLoop = false

        sharePref = PreferenceManager.getDefaultSharedPreferences(this)
        val highscorePref : String? = sharePref.getString("highscorePreference", "0")
        highscore = Integer.parseInt(highscorePref)
        val builder = AlertDialog.Builder(this)

        if(score > highscore)
        {
            sharePref.edit().putString("highscorePreference", score.toString()).apply()
            setMusicOnFXMP(R.raw.newhighscore_sound, false)

            builder.setTitle(applicationContext.getString(R.string.HighScoreDialogTitle) + " " + score)
            value = true
        }
        else
        {
            builder.setTitle(applicationContext.getString(R.string.ScoreDialogTitle) + " " + score)
        }

        builder.setPositiveButton("OK")
        {
                dialog,
                which -> finish()
        }

        val dialog = builder.create()
        dialog.show()

        return value
    }

    private fun correctPerformance()
    {
        //showToast(R.string.correctActionMessage.toString())
        actionWasPerformed = true
        actionsWellPerformed++
        score += asignedScore
        setMusicOnFXMP(R.raw.win_sound, false)
        bopItLogic()
    }

    private fun getRandomInstruction() : Int
    {
        //var randomValue : Int
        val random = Random
        return random.nextInt(1, 4)
        /*
        do{ randomValue = random.nextInt(1, 4) }while(randomValue == prevRandomNum)
        return randomValue
        */
    }

    private fun getTextViews()
    {
        instructionTV = findViewById(R.id.instructionTVG)
        instructionTimeTV = findViewById(R.id.instructionTimeTVG)
        scoreTV = findViewById(R.id.scoreTVG2)
    }

    private fun getValuesFromPreferences()
    {
        sharePref = PreferenceManager.getDefaultSharedPreferences(this)
        val tDIPref = sharePref.getString("thresholdDifficultyIncreasePreference", "8")
        thresholdDifficultyIncrease = Integer.parseInt(tDIPref)
        val speedSPref = sharePref.getString("speedScalarInscreasePreference", "1.2")
        speedScalar = speedSPref!!.toFloat()

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
        backgroundMediaPlayer.isLooping = true
        backgroundMediaPlayer.setVolume(bgMusicVolume, bgMusicVolume)

        playbackParams = backgroundMediaPlayer.playbackParams
    }

    /*
    private fun restartSoundSpeed()
    {
        backgroundMediaPlayer.playbackParams.setSpeed(1.0f)
    }
    */

    private fun setMusicOnFXMP(id : Int, loop : Boolean)
    {
        fxMediaPlayer = MediaPlayer.create(this, id)
        fxMediaPlayer.isLooping = loop
        fxMediaPlayer.start()
    }

    private fun setOnClickListeners()
    {
        val buttonBopIt : Button = findViewById(R.id.bopItButtonG)

        buttonBopIt.setOnClickListener()
        {
            if(currentInstruction == possibleInstructions[0])
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
            //showToast("onScroll")

            if(currentInstruction == possibleInstructions[1])
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