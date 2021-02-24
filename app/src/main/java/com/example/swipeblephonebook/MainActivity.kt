package com.example.swipeblephonebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var phoneRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneRecyclerView = findViewById(R.id.recyclerView)
        phoneRecyclerView.layoutManager = LinearLayoutManager(this)
        phoneRecyclerView.adapter = AdapterPhoneBook(
            listOf<Person>(
                Person("Ilya"),
                Person("Maria"),
                Person("Nikolay"),
                Person("Anna"),
                Person("Elizabeth")
                )
        )
        (phoneRecyclerView.adapter as AdapterPhoneBook).notifyDataSetChanged()
    }
}