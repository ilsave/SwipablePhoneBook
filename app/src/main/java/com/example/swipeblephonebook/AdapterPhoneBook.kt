package com.example.swipeblephonebook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class AdapterPhoneBook(val MainActivity: MainActivity,private val list: List<Person>)
    : RecyclerView.Adapter<AdapterPhoneBook.ItemsHolder>() {

    inner class ItemsHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_phone, parent, false)
        return ItemsHolder(view)
    }

    private var onItemClickListener: ((Person) -> Unit)? = null

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        val currentItem = list[position]
        holder.itemView.apply {
            findViewById<CustomPhoneItem>(R.id.tvName).text = currentItem.name
            findViewById<Button>(R.id.button).setOnClickListener {
                val intent = Intent(MainActivity, UpdateActivity::class.java)
                intent.putExtra("id", "wqwq")
                intent.putExtra("date", currentItem.name)
                intent.putExtra("text", currentItem.number)
                context.startActivity(Intent(MainActivity, UpdateActivity::class.java))

            }
        }

        setOnItemClickListener { person ->
            onItemClickListener?.let { it(person) }
        }
    }

    fun setOnItemClickListener(listener: (Person) -> Unit) {
        onItemClickListener = listener
    }
}