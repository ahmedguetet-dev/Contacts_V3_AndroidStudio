package com.example.contacts

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CarteContactFragment : Fragment(R.layout.fragment_map_contact), OnMapReadyCallback {
    private val vm: ContactsViewModel by activityViewModels()
    private var contactId = -1
    private var map: GoogleMap? = null
    companion object { fun newInstance(id: Int) = CarteContactFragment().apply { arguments = Bundle().apply { putInt("contactId", id) } } }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contactId = arguments?.getInt("contactId") ?: -1
        val contact = vm.getContact(contactId)
        val status = view.findViewById<TextView>(R.id.mapStatus)
        if (contact?.latitude == null || contact.longitude == null) { status.text = "Position GPS absente. Ajoutez latitude et longitude dans le détail."; return }
        status.visibility = View.GONE
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapContainer) as? SupportMapFragment
            ?: SupportMapFragment.newInstance().also { childFragmentManager.beginTransaction().replace(R.id.mapContainer, it).commit() }
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val c = vm.getContact(contactId) ?: return
        val lat = c.latitude ?: return; val lng = c.longitude ?: return
        val position = LatLng(lat, lng)
        googleMap.addMarker(MarkerOptions().position(position).title(c.displayName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
    }
}
