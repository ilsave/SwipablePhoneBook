package com.example.swipeblephonebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class AdapterPhoneBook(private val list: List<Person>)
    : RecyclerView.Adapter<AdapterPhoneBook.ItemsHolder>() {

    inner class ItemsHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_phone, parent, false)
        return ItemsHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        val currentItem = list[position]
        holder.itemView.apply {
            findViewById<CustomPhoneItem>(R.id.tvName).text = currentItem.name
        }
    }
}