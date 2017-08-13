package com.example.majid_fit5.noteapp

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddNote : AppCompatActivity() {
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
    }

   fun buAddNote(view:View){
       var dbManager=DbManager(this)
       var values= ContentValues()
       values.put("Title",noteTitle.text.toString())
       values.put("Description",noteDescription.text.toString())
           val ID = dbManager.insertData(values)
           if (ID > 0) {
           } else {
               Toast.makeText(this, " cannot add note ", Toast.LENGTH_LONG).show()
           }

           finish()
   }
   }


