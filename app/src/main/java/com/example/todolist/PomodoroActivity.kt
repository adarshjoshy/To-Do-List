package com.example.todolist

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PomodoroActivity : AppCompatActivity() {

    lateinit var countdownText: TextView
    lateinit var countdownButton: Button
    lateinit var restartButton: Button

    lateinit var countdownTimer: CountDownTimer
    var timeleftinms: Long = 1500000                                                                //25 minutes
    private var pausetimer: Boolean = false
    var switchTimer: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pomodoro_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pomodoro Technique"

        countdownText = findViewById(R.id.countdown_text)
        countdownButton = findViewById(R.id.countdown_button)
        restartButton = findViewById(R.id.restart_button)

        countdownButton.setOnClickListener {
            startPause(this)
        }

        updateTimer()

        restartButton.setOnClickListener {
            restart()
        }
    }

    private fun startPause(context: Context){
        if(pausetimer){
            pauseTimer()
        }
        else{
            startTimer(context)
        }
    }

    private fun restart(){
        if(pausetimer) countdownTimer.cancel()
        timeleftinms = 1500000                                                                      //25 minutes
        countdownText.text = getString(R.string.default_time_string)
        countdownButton.text = getString(R.string.start_task_string)
        pausetimer = false
    }

    private fun startTimer(context: Context){
        countdownTimer = object: CountDownTimer(timeleftinms, 1000){
            override fun onFinish() {
                if(switchTimer){
                    Toast.makeText(context, "Great!", Toast.LENGTH_SHORT).show()
                    timeleftinms = 300000                                                           //5 minutes
                    countdownText.text = getString(R.string.break_time_string)
                    countdownButton.text = getString(R.string.break_string)
                    switchTimer = false
                }
                else{
                    Toast.makeText(context, "Great!", Toast.LENGTH_SHORT).show()
                    timeleftinms = 1500000                                                          //25 minutes
                    countdownText.text = getString(R.string.default_time_string)
                    countdownButton.text = getString(R.string.start_task_string)
                    switchTimer = true
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                timeleftinms = millisUntilFinished
                updateTimer()
            }
        }.start()

        countdownButton.text = getString(R.string.pause_string)
        pausetimer = true
    }

    private fun pauseTimer(){
        countdownTimer.cancel()
        countdownButton.text = getString(R.string.start_task_string)
        pausetimer = false
    }

    fun updateTimer(){
        val minutes = (timeleftinms/60000).toInt()
        val seconds = ((timeleftinms%60000)/1000).toInt()
        var timeLeftText = "$minutes:"
        if(seconds < 10) timeLeftText += "0"
        timeLeftText += seconds

        countdownText.text = timeLeftText
    }
}