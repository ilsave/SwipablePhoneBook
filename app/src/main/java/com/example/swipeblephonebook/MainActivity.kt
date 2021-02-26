package com.example.swipeblephonebook

import android.Manifest
import android.content.Context
import android.content.UriMatcher
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.PhoneLookup
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    companion object {
        const val AUTHORITY = "com.swtecnn.contentproviderlesson.DbContentProvider"
        private val DIARY_ENTRY_TABLE = "DiaryEntry"
        val DIARY_TABLE_CONTENT_URI: Uri = Uri.parse("content://" +
                AUTHORITY + "/" + DIARY_ENTRY_TABLE)
    }

    private val DIARY_ENTRIES = 1
    private val DIARY_ENTRY_ID = 2
    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sUriMatcher.addURI(AUTHORITY, DIARY_ENTRY_TABLE, DIARY_ENTRIES)
        sUriMatcher.addURI(AUTHORITY, "$DIARY_ENTRY_TABLE/#", DIARY_ENTRY_ID)
    }

    private lateinit var phoneRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneRecyclerView = findViewById(R.id.recyclerView)
        phoneRecyclerView.layoutManager = LinearLayoutManager(this)
        phoneRecyclerView.adapter = AdapterPhoneBook(
           getHotChtoTo()
        )
        (phoneRecyclerView.adapter as AdapterPhoneBook).notifyDataSetChanged()
    }

    private fun getHotChtoTo(): List<Person>{
        val contactList = ArrayList<Person>()
        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        val contactUri: Uri = ContactsContract.Contacts.CONTENT_URI
        val listCursor = contentResolver.query(
                DIARY_TABLE_CONTENT_URI, null, null, null, null)
        if (listCursor?.moveToNext()!!){
            val textEntry = listCursor.getString(listCursor.getColumnIndex("entry_text"))
            val dateText = listCursor.getString(listCursor.getColumnIndex("entry_date"))
            contactList.add(Person(textEntry, dateText))
        }
        return contactList
    }


}