package com.example.contacts

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListeContactsFragment : Fragment(R.layout.fragment_list_contacts) {
    private val vm: ContactsViewModel by activityViewModels()
    private lateinit var adapter: ContactAdapter
    private lateinit var search: EditText
    private lateinit var empty: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search = view.findViewById(R.id.searchContacts)
        empty = view.findViewById(R.id.emptyMessage)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerContacts)
        adapter = ContactAdapter { (activity as MainActivity).openDetail(it.id) }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        vm.contacts.observe(viewLifecycleOwner) { filterAndDisplay(it) }
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { vm.contacts.value?.let { filterAndDisplay(it) } }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }
    private fun filterAndDisplay(all: List<Contact>) {
        val query = search.text.toString().trim().lowercase()
        val result = all.filter { it.nom.lowercase().contains(query) || it.prenom.lowercase().contains(query) || it.telephone.lowercase().contains(query) }
        adapter.submitList(result)
        empty.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
    }
}
