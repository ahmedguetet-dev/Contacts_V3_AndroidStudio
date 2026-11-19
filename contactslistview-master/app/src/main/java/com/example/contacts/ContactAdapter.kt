package com.example.contacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val onClick: (Contact) -> Unit) : RecyclerView.Adapter<ContactAdapter.ContactHolder>() {
    private var items: List<Contact> = emptyList()
    fun submitList(list: List<Contact>) { items = list; notifyDataSetChanged() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ContactHolder, position: Int) = holder.bind(items[position])
    inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val photo: ImageView = view.findViewById(R.id.contactPhoto)
        private val name: TextView = view.findViewById(R.id.contactName)
        private val phone: TextView = view.findViewById(R.id.contactPhone)
        fun bind(contact: Contact) {
            name.text = contact.displayName
            phone.text = contact.telephone
            when {
                contact.photoBitmap != null -> photo.setImageBitmap(contact.photoBitmap)
                contact.photoUri != null -> try { photo.setImageURI(Uri.parse(contact.photoUri)) } catch (_: Exception) { photo.setImageResource(android.R.drawable.ic_menu_myplaces) }
                else -> photo.setImageResource(android.R.drawable.ic_menu_myplaces)
            }
            itemView.setOnClickListener { onClick(contact) }
        }
    }
}
