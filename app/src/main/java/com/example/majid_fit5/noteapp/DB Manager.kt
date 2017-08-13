package com.example.majid_fit5.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log

/**
* Created by Eng. Abdulmajid Alyafey on 7/2/2017.
*/
class DbManager{
    val dbName="MyFirstDatabase"
    val dbTable="Notes"
    val colID="ID"
    val colTitle="Title"
    val colDescription="Description"
    val dbVersion = 1

    val sqlQuery="CREATE TABLE IF NOT EXISTS $dbTable($colID INTEGER PRIMARY KEY,$colTitle TEXT,$colDescription TEXT)"

    var sqLiteDatabase: SQLiteDatabase?=null

    constructor(context:Context){
        Log.d("msg",sqlQuery)
        val db=DatabaseHelperNote(context)
        sqLiteDatabase =db.writableDatabase
    }

    inner class DatabaseHelperNote(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        override fun onCreate(p0: SQLiteDatabase?) { // create the table immediately after calling the super().
            try {
                p0!!.execSQL(sqlQuery)
            }catch (ex:SQLiteException){
                ex.printStackTrace()
                Log.e("Error#1",sqlQuery)
            }

        }
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            try {
                p0!!.execSQL("Drop table IF EXISTS $dbTable")
            }catch (ex:SQLiteException){
            ex.printStackTrace()
             }
         }

    }

    fun insertData(values:ContentValues)= sqLiteDatabase!!.insert(dbTable,"",values)

    fun fetchData(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor?{
        var cursor:Cursor? =null
        try {
            val sqLiteBuilder=SQLiteQueryBuilder()
            sqLiteBuilder.tables=dbTable
            cursor=sqLiteBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder)
            return cursor
        }catch (ex:SQLiteException){
            ex.printStackTrace()
        }
        return cursor
    }

    fun deleteData(selection:String,selectionArgs:Array<String>)=sqLiteDatabase!!.delete(dbTable,selection,selectionArgs)

    fun updateData(values:ContentValues,selection:String,selectionArgs:Array<String>)=sqLiteDatabase!!.update(dbTable,values,selection,selectionArgs)
}