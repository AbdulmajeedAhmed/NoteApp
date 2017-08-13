package com.example.majid_fit5.noteapp

import android.app.AlertDialog
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.ticket.view.*
import android.view.LayoutInflater




class RecyclerView : AppCompatActivity() {
    var listNotes=ArrayList<Note>()
    var myAdapter:CustomAdapter?=null
    var dbManager:DbManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

      /*  Dummy Data:
        listNotes.add(Note(1,"title1","description1"))
        listNotes.add(Note(2,"title2","description2"))
        listNotes.add(Note(3,"title3","description3"))
        listNotes.add(Note(4,"title4","description4"))
        listNotes.add(Note(5,"title5","description5"))
        listNotes.add(Note(6,"title6","description6"))
        listNotes.add(Note(7,"title7","description7"))
        listNotes.add(Note(8,"title8","description8"))
        listNotes.add(Note(9,"title9","description9"))*/

        loadData("%") //by default the title is % which means 'anything'.
        displayRecyclerViewContent()

    }

    private fun displayRecyclerViewContent() {
        // -----------------------------------Recycler View------------------------------------------------------
        //myRecyclerView.setHasFixedSize(true) //true if adapter changes cannot affect the size of the RecyclerView
        //StaggeredGridLayoutManager is  another way of presentation but needs some review..
        //myRecyclerView.layoutManager=GridLayoutManager(this,2) // // other way of presentation.
        myRecyclerView.layoutManager=LinearLayoutManager(this, LinearLayout.VERTICAL, false) // one way of presentation.
        myAdapter=CustomAdapter(this,listNotes)
        myRecyclerView.adapter=myAdapter
    }

    override  fun onResume() {
        super.onResume()
        loadData("%")
           //  Toast.makeText(applicationContext,"View.VISIBLE", Toast.LENGTH_SHORT).show()

        displayRecyclerViewContent()
       // Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show()
    }

    private fun  loadData(title: String) {// by default the title is % which means 'anything'.
       // Toast.makeText(this,"title is $title", Toast.LENGTH_SHORT).show()

        listNotes.clear() // clear old notes
        dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title) // OR arrayOf(title,name)
        val cursor=dbManager!!.fetchData(projections,"Title like ?",selectionArgs,"Title") //? refers to selectionArgs and it is only 'one ? because of the array above'
        if(cursor!!.moveToFirst()){ // if there is records...
            do {
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(noteID = ID,title = Title,description = Description))

            }while (cursor!!.moveToNext())
            noNoteImage.visibility=View.INVISIBLE
            noNoteComment.visibility=View.INVISIBLE // there is notes
            noResultComment.visibility=View.INVISIBLE
            displayRecyclerViewContent() // very important to call this again because the list changes..

        }else{ // no notes found... 2 cases , either no notes or no matches when searching....
            when(fromSearch){
                true -> {
                    noNoteImage.visibility=View.VISIBLE
                    noResultComment.visibility=View.VISIBLE
                    fromSearch=false
                   // loadData("%") // when no matches found, display the previous note again..

                } else ->{
                noNoteComment.visibility=View.VISIBLE
                noNoteImage.visibility=View.VISIBLE

            }
            }

        }




    }

    inner class CustomAdapter(val context:Context,val NoteList: ArrayList<Note>)  : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket, parent, false)
            return ViewHolder(this.context,view)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
            holder.bindItems(NoteList[position])
        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return NoteList.size
        }


         //the class is holding the list view

       inner class ViewHolder(val context:Context,itemView: View) : RecyclerView.ViewHolder(itemView) {

             fun bindItems(note: Note) {
                itemView.txtTitle.text=note.title
                itemView.txtDescription.text=note.description
                itemView.delete.setOnClickListener {
                    try {
                    /*val view: View = layoutInflater.inflate(R.layout.ticket, null); this is for practice on how you can inflate other views in the dialog.*/
                    val alertDialog:AlertDialog.Builder = AlertDialog.Builder(context)
                    alertDialog.setTitle("CONFIRMATION MESSAGE")
                    /*alertDialog.setView(view)*/
                    alertDialog.setMessage("Are you sure you want to delete this note?")
                    alertDialog.setPositiveButton("DELETE", object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            deleteNote(note) // DELETING FROM DATABASE
                            loadData("%") //LOADING NEW DATA AND REFRESHING THE RECYCLER VIEW

                            notifyDataSetChanged()
                        }
                    })
                    alertDialog.setNegativeButton("CANCEL", object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            // do nothing..
                        }
                    })
                    alertDialog.show()

                    }catch (ex:Exception){
                        Log.e("myERROR",ex.message)
                       // ex.printStackTrace()
                    }
                }
                 itemView.edit.setOnClickListener{
                     editNote(context,note)

                 }

             } // bindItems



             private fun deleteNote(note: Note) {
                 var dbManager=DbManager(context = context)
                 var selectionArgs= arrayOf(note.noteID.toString())
                 dbManager.deleteData("ID=?",selectionArgs)
             }

             private fun  editNote(context:Context,note: Note) {
                 // Go to update note activity.....
                 var intent=Intent(context,UpdateNote::class.java)
                 intent.putExtra("ID",note.noteID)
                 intent.putExtra("Title",note.title)
                 intent.putExtra("Description",note.description)
                 startActivity(intent)
             }

             /*   override fun onClick(view: View) { // you must implement the interface (View.OnClickListener) to use the second way.
                NoteList.remove(this.note)
                notifyDataSetChanged()
            }*/
        }} // End of CustomAdapter

    var fromSearch=false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu) // for add note icon

        // now for the search icon..
        val searchView=menu!!.findItem(R.id.searchNote).actionView as SearchView
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnCloseListener { // when closing search bar...
            loadData("%")
            false
        }
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean { // when click search
                fromSearch=true // before querying....
                loadData("%$query%")
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                listNotes.clear()
                myAdapter!!.notifyDataSetChanged()

                fromSearch=true // before querying....
                loadData("%$query%")
               return false
            }

        })



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId){
                R.id.addNoteClick ->{
                    // go to that activity
                    var intent=Intent(this,AddNote::class.java)
                    startActivity(intent)
                }
                R.id.searchNote ->{

            }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
