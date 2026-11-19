package com.example.contacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit { replace(R.id.fragment_container, ListeContactsFragment()); setReorderingAllowed(true) }
        }
    }
    fun openDetail(contactId: Int) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, DetailContactFragment.newInstance(contactId))
            addToBackStack("detail")
            setReorderingAllowed(true)
        }
    }
    fun openMap(contactId: Int) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, CarteContactFragment.newInstance(contactId))
            addToBackStack("map")
            setReorderingAllowed(true)
        }
    }
}
