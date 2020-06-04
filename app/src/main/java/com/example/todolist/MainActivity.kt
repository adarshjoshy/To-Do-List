package com.example.todolist

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth->

            val builder = AlertDialog.Builder(this)
            builder.setCancelable(true)

            val addView: View = LayoutInflater.from(this).inflate(R.layout.add_task_layout, null)

            val taskName = addView.findViewById<EditText>(R.id.task_name)
            val taskTime = addView.findViewById<TextView>(R.id.task_time)
            val setTime = addView.findViewById<ImageButton>(R.id.set_task_time)
            val addButton = addView.findViewById<Button>(R.id.add_task_button)

            setTime.setOnClickListener{
                val calendar: Calendar = Calendar.getInstance()
                val hours: Int = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes: Int = calendar.get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(addView.context, R.style.Theme_AppCompat_Light_Dialog,
                    TimePickerDialog.OnTimeSetListener{ _, hourOfDay, minute->
                        val c: Calendar = Calendar.getInstance()
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        c.set(Calendar.MINUTE, minute)
                        c.timeZone = TimeZone.getDefault()
                        val dateFormat =  SimpleDateFormat("K:mm a", Locale.ENGLISH)
                        val tasktime = dateFormat.format(c.time)
                        taskTime.text = tasktime
                    }, hours, minutes, false)
                timePickerDialog.show()
            }

            addButton.setOnClickListener{
                SaveTask(this, taskName.text.toString(), taskTime.text.toString(), dayOfMonth.toString(), month.toString(), year.toString())
                alertDialog.dismiss()
                recyclerView_main.layoutManager = LinearLayoutManager(this)
                recyclerView_main.adapter = MainAdapter(this)
            }

            builder.setView(addView)
            alertDialog = builder.create()
            alertDialog.show()
        }

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = MainAdapter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.pomodoro_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.pomodoro_menu ->{
                val intent = Intent(this, PomodoroActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun SaveTask(context: Context, task: String, time: String, dayofMonth: String, month: String, year: String){
        if(task.isNotEmpty()){
            val newtask = Task(task, time, dayofMonth, month, year)
            val db = DBHandler(context)
            db.SaveTask(newtask)
            db.close()
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}

