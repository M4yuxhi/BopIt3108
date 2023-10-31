package com.maybomiTobar.bopit_31_08

import android.gesture.GestureOverlayView.OnGestureListener
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import org.w3c.dom.Text

class GameActivity : AppCompatActivity()
{
    private lateinit var backgroundMediaPlayer : MediaPlayer
    private lateinit var fxMediaPlayer : MediaPlayer
    private lateinit var playbackParams: PlaybackParams
    private var musicVolume : Float = 0.3f
    private lateinit var volumeValueTV : TextView
    private lateinit var gestureDetector : GestureDetector

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gestureDetector = GestureDetector(this, GestureListener())

        backgroundMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        backgroundMediaPlayer.start()
        backgroundMediaPlayer.setVolume(musicVolume, musicVolume)

        playbackParams = backgroundMediaPlayer.playbackParams

        volumeValueTV = findViewById(R.id.textViewVolumeValueG)
        volumeValueTV.text = musicVolume.toString()

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
        val buttonWin : Button = findViewById(R.id.buttonWinG)
        val buttonLose : Button = findViewById(R.id.buttonLoseG)
        val buttonLessVolume : Button = findViewById(R.id.buttonLessVolume)
        val buttonMoreVolume : Button = findViewById(R.id.buttonMoreVolume)

        buttonWin.setOnClickListener()
        {
            setMusicOnFXMP(R.raw.win_sound)
        }

        buttonLose.setOnClickListener()
        {
            setMusicOnFXMP(R.raw.lose_sound)
        }

        buttonLessVolume.setOnClickListener()
        {
            if(musicVolume >= 0.1f)
            {
                musicVolume -= 0.1f
                backgroundMediaPlayer.setVolume(musicVolume, musicVolume)
                volumeValueTV.text = musicVolume.toString()
            }
        }

        buttonMoreVolume.setOnClickListener()
        {
            if(musicVolume <= 0.9f)
            {
                musicVolume += 0.1f
                backgroundMediaPlayer.setVolume(musicVolume, musicVolume)
                volumeValueTV.text = musicVolume.toString()
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