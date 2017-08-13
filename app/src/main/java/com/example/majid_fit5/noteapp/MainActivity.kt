package com.example.majid_fit5.noteapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNotes=ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listNotes.add(Note(1,"title1","description1"))
        listNotes.add(Note(2,"title2","description2"))
        listNotes.add(Note(3,"title3","description3"))
        listNotes.add(Note(4,"title4","description4"))
        listNotes.add(Note(5,"title5","description5"))
        listNotes.add(Note(6,"title6","description6"))
        listNotes.add(Note(7,"title7","description7"))
        listNotes.add(Note(8,"title8","description8"))
        listNotes.add(Note(9,"title8","description8"))

        var adapter=MyListViewAdapter(listNotes)
        myListView.adapter=adapter


     /*   myRecyclerView.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false) // one way of presentation.
        val myAdapter=MyRecycelerViewAdapter(listNotes)
        myRecyclerView.adapter=myAdapter*/
    }

    inner class MyListViewAdapter(var adapterListNotes: ArrayList<Note>) : BaseAdapter() {

        override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
            val myView=layoutInflater.inflate(R.layout.ticket,null)
            val note=adapterListNotes[position]
            myView.txtTitle.text=note.title
            myView.txtDescription.text=note.title
            return myView
        }

        override fun getItem(p0: Int): Any {
        return adapterListNotes[p0]
        }

        override fun getItemId(p0: Int): Long {
        return p0.toLong()
        }

        override fun getCount(): Int {
            return adapterListNotes.size
        }


    } // list view

    inner class MyRecycelerViewAdapter(val NoteList: ArrayList<Note>)  : RecyclerView.Adapter<MyRecycelerViewAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecycelerViewAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket, parent, false)
            return ViewHolder(view)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: MyRecycelerViewAdapter.ViewHolder, position: Int) {
            holder.bindItems(NoteList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return NoteList.size
        }


        //the class is holding the list view
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) ,View.OnClickListener { // inheritance and implementation of interface together.
            var note:Note?=null
            fun bindItems(note: Note) {
                this.note=note
                itemView.txtTitle.text=note.title
                itemView.txtDescription.text=note.description
                itemView.delete.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                NoteList.remove(this.note)
                notifyDataSetChanged()
            }
        }}// recycler view
}
