package com.example.majid_fit5.noteapp

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class UpdateNote : AppCompatActivity() {
    var noteID=0
    var title=""
    var description=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        var bundle=intent.extras
        noteID=bundle.getInt("ID",0)
        title=bundle.getString("Title") // i want to use them later as variables
        description=bundle.getString("Description")

        noteTitle.setText(title)
        noteDescription.setText(description)

    }
    fun buUpdateNote(view: View){
        if(noteTitle.text.toString().contentEquals(title)  && noteDescription.text.toString().contentEquals(description)) finish() // if the user changed noting.
        else {

            var dbManager = DbManager(this)
            var values = ContentValues()
            values.put("Title", noteTitle.text.toString())
            values.put("Description", noteDescription.text.toString())

            val selectionArs = arrayOf(noteID.toString())

            val ID = dbManager.updateData(values, "ID=?", selectionArs)
            if (ID > 0) {
                Toast.makeText(this, " The Note is updated successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, " Cannot updated the note ", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

    fun cancel(view: View){
        finish()
    }
}
