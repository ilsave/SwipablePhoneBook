package com.example.swipeblephonebook

import android.content.Intent
import android.content.UriMatcher
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    companion object {
        const val AUTHORITY = "com.swtecnn.contentproviderlesson.DbContentProvider"
        private val DIARY_ENTRY_TABLE = "DiaryEntry"
        val DIARY_TABLE_CONTENT_URI: Uri = Uri.parse("content://" +
                AUTHORITY + "/" + DIARY_ENTRY_TABLE)
    }

    private val DIARY_ENTRIES = "#"
    private val DIARY_ENTRY_ID = 2
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
      //  sUriMatcher.addURI(AUTHORITY, DIARY_ENTRY_TABLE, DIARY_ENTRIES)
     //   sUriMatcher.addURI(AUTHORITY, "$DIARY_ENTRY_TABLE/#", DIARY_ENTRY_ID)
    }

    private lateinit var phoneRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneRecyclerView = findViewById(R.id.recyclerView)
        phoneRecyclerView.layoutManager = LinearLayoutManager(this)
        phoneRecyclerView.adapter = AdapterPhoneBook(this,
           getDbFromLesson()
        )
        (phoneRecyclerView.adapter as AdapterPhoneBook).notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //вот здесь мы возвращаемся из старой активности и получаем данные

    }
    private fun getDbFromLesson(): List<Person>{
        val contactList = ArrayList<Person>()
        Log.d("Ilsave", DIARY_TABLE_CONTENT_URI.toString())
        val listCursor = contentResolver.query(
                DIARY_TABLE_CONTENT_URI, null, null, null, null)
        while (listCursor?.moveToNext()!!){
            val textEntry = listCursor.getString(listCursor.getColumnIndex("entry_text"))
            val dateText = listCursor.getString(listCursor.getColumnIndex("entry_date"))
            val dateTextId = listCursor.getString(listCursor.getColumnIndex("id"))
            contactList.add(Person(textEntry + dateTextId , dateText))
        }
        return contactList
    }


}