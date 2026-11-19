package com.example.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableLiveData(mutableListOf(
        Contact(1, "Ben Ali", "Ahmed", "22123456", "ahmed@example.com", latitude = 36.8008, longitude = 10.1800),
        Contact(2, "Trabelsi", "Sarra", "98123456", "sarra@example.com", latitude = 35.8256, longitude = 10.6369),
        Contact(3, "Gharbi", "Mohamed", "55123456", "mohamed@example.com", latitude = 33.8869, longitude = 10.0975),
        Contact(4, "Jaziri", "Ines", "27123456", "ines@example.com", latitude = 36.8188, longitude = 10.1658),
        Contact(5, "Mansouri", "Youssef", "44123456", "youssef@example.com", latitude = 34.7406, longitude = 10.7603),
        Contact(6, "Chaabane", "Amira", "29123456", "amira@example.com"),
        Contact(7, "Bouazizi", "Karim", "53123456", "karim@example.com", latitude = 35.5030, longitude = 11.0622),
        Contact(8, "Hammami", "Nour", "20123456", "nour@example.com", latitude = 36.8065, longitude = 10.1815)
    ))
    val contacts: LiveData<MutableList<Contact>> = _contacts
    private var nextId = 9

    fun getContact(id: Int): Contact? = _contacts.value?.firstOrNull { it.id == id }
    fun update(contact: Contact) {
        val list = _contacts.value ?: mutableListOf()
        val index = list.indexOfFirst { it.id == contact.id }
        if (index >= 0) list[index] = contact
        _contacts.value = list.toMutableList()
    }
    fun add(contact: Contact) {
        val list = (_contacts.value ?: mutableListOf()).toMutableList()
        list.add(contact.copy(id = nextId++))
        _contacts.value = list
    }
}
