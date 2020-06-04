package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

const val DATABASE_NAME = "MyDB"
const val TABLE_TASKS = "Tasks"
const val COL_ID = "Id"
const val COL_TASK = "task"
const val COL_TIME = "time"
const val COL_DAYOFMONTH = "day"
const val COL_MONTH = "month"
const val COL_YEAR = "year"

class DBHandler(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1){

    private val createTaskTable = ("CREATE TABLE " + TABLE_TASKS + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TASK + " TEXT, " +  COL_TIME + " TEXT, " + COL_DAYOFMONTH + " TEXT, "
            + COL_MONTH + " TEXT, " + COL_YEAR + " TEXT)")
    private val dropTasksTable = "DROP TABLE IF EXISTS $TABLE_TASKS"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTaskTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropTasksTable)
        onCreate(db)
    }

    fun SaveTask(task: Task){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_TASK, task.TASK)
        cv.put(COL_TIME, task.TIME)
        cv.put(COL_DAYOFMONTH, task.DAYOFMONTH)
        cv.put(COL_MONTH, task.MONTH)
        cv.put(COL_YEAR, task.YEAR)
        val result = db.insert(TABLE_TASKS, null, cv)

        if(result == (-1).toLong()){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun ReadTasks(): MutableList<Task>{
        val list: MutableList<Task> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                val task = Task("", "", "", "", "")
                task.TASK = result.getString(result.getColumnIndex(COL_TASK))
                task.TIME = result.getString(result.getColumnIndex(COL_TIME))
                task.DAYOFMONTH = result.getString(result.getColumnIndex(COL_DAYOFMONTH))
                task.MONTH = result.getString(result.getColumnIndex(COL_MONTH))
                task.YEAR = result.getString(result.getColumnIndex(COL_YEAR))
                list.add(task)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun DeleteTask(taskName: String){
        val db = this.writableDatabase
        db.delete(TABLE_TASKS, "$COL_TASK=?", arrayOf(taskName))
        db.close()
    }

}

