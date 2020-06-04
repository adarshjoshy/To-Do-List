package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.list_item_layout.view.*

class MainAdapter(context: Context): RecyclerView.Adapter<CustomViewHolder>(){

    private val ids: MutableList<Int> = ArrayList()
    private val tasks: MutableList<String> = ArrayList()
    private val date: MutableList<String> = ArrayList()
    private val time: MutableList<String> = ArrayList()
    private var db = DBHandler(context)
    private var tasklist = db.ReadTasks()

    init {
        for (task in 0 until tasklist.size){
            ids.add(tasklist[task].ID)
            tasks.add(tasklist[task].TASK)
            date.add(tasklist[task].DAYOFMONTH + " / " + (tasklist[task].MONTH .toInt()+1).toString() + " / " + tasklist[task].YEAR)
            time.add(tasklist[task].TIME)
        }
    }

    private fun DeleteTask(context: Context, name:String){
        val db = DBHandler(context)
        db.DeleteTask(name)
        db.close()
    }

    private fun DeleteItem(position: Int){
        ids.remove(ids[position])
        tasks.remove(tasks[position])
        date.remove(date[position])
        time.remove(time[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val singleTaskView = inflater.inflate(R.layout.list_item_layout, parent, false)
        return CustomViewHolder(singleTaskView, parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.task_textView.text = tasks[position]
        holder.view.taskdate_textView.text = date[position]
        holder.view.tasktime_textView.text = time[position]

        holder.view.complete_button.setOnClickListener {
            DeleteTask(holder.parent.context, tasks[position])
            DeleteItem(position)
            holder.parent.recyclerView_main.layoutManager = LinearLayoutManager(holder.parent.context)
            holder.parent.recyclerView_main.adapter = MainAdapter(holder.parent.context)
            Toast.makeText(holder.parent.context, "Great!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }
}

class CustomViewHolder(val view: View, val parent: ViewGroup): RecyclerView.ViewHolder(view){
}