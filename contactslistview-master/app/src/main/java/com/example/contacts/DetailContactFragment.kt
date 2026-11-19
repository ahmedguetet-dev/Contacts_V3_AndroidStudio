package com.example.contacts

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailContactFragment : Fragment(R.layout.fragment_detail_contact) {
    private val vm: ContactsViewModel by activityViewModels()
    private var contactId = -1
    private lateinit var photo: ImageView
    private lateinit var nom: EditText
    private lateinit var prenom: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var lat: EditText
    private lateinit var lng: EditText
    private var current: Contact? = null
    private var selectedPhotoUri: String? = null
    private var selectedPhotoBitmap: Bitmap? = null
    private val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) { selectedPhotoUri = uri.toString(); selectedPhotoBitmap = null; photo.setImageURI(uri) }
    }
    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) { selectedPhotoBitmap = bitmap; selectedPhotoUri = null; photo.setImageBitmap(bitmap) }
    }
    companion object { fun newInstance(id: Int) = DetailContactFragment().apply { arguments = Bundle().apply { putInt("contactId", id) } } }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contactId = arguments?.getInt("contactId") ?: -1
        current = vm.getContact(contactId)
        if (current == null) { Toast.makeText(requireContext(), "Contact introuvable", Toast.LENGTH_SHORT).show(); parentFragmentManager.popBackStack(); return }
        photo = view.findViewById(R.id.detailPhoto); nom = view.findViewById(R.id.editNom); prenom = view.findViewById(R.id.editPrenom)
        phone = view.findViewById(R.id.editTelephone); email = view.findViewById(R.id.editEmail); lat = view.findViewById(R.id.editLatitude); lng = view.findViewById(R.id.editLongitude)
        selectedPhotoUri = current!!.photoUri
        selectedPhotoBitmap = current!!.photoBitmap
        fillFields(current!!)
        view.findViewById<Button>(R.id.btnGallery).setOnClickListener { gallery.launch("image/*") }
        view.findViewById<Button>(R.id.btnCamera).setOnClickListener { camera.launch(null) }
        view.findViewById<Button>(R.id.btnSave).setOnClickListener { save() }
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener { parentFragmentManager.popBackStack() }
        view.findViewById<Button>(R.id.btnCall).setOnClickListener { call() }
        view.findViewById<Button>(R.id.btnEmail).setOnClickListener { sendEmail() }
        view.findViewById<Button>(R.id.btnMap).setOnClickListener { saveCoordinatesThenMap() }
    }
    private fun fillFields(c: Contact) {
        nom.setText(c.nom); prenom.setText(c.prenom); phone.setText(c.telephone); email.setText(c.email)
        lat.setText(c.latitude?.toString().orEmpty()); lng.setText(c.longitude?.toString().orEmpty())
        when { c.photoBitmap != null -> photo.setImageBitmap(c.photoBitmap); c.photoUri != null -> photo.setImageURI(Uri.parse(c.photoUri)); else -> photo.setImageResource(android.R.drawable.ic_menu_myplaces) }
    }
    private fun readCoordinates(): Pair<Double?, Double?>? {
        val la = lat.text.toString().trim(); val lo = lng.text.toString().trim()
        if (la.isEmpty() && lo.isEmpty()) return Pair(null, null)
        val latitude = la.toDoubleOrNull(); val longitude = lo.toDoubleOrNull()
        if (latitude == null || longitude == null || latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
            lat.error = "Latitude: -90 à 90"; lng.error = "Longitude: -180 à 180"; return null
        }
        return Pair(latitude, longitude)
    }
    private fun save(): Boolean {
        val c = current ?: return false
        if (nom.text.isBlank() || prenom.text.isBlank() || phone.text.isBlank()) { Toast.makeText(requireContext(), "Nom, prénom et téléphone obligatoires", Toast.LENGTH_SHORT).show(); return false }
        val coords = readCoordinates() ?: if (lat.text.isBlank() && lng.text.isBlank()) Pair(null, null) else return false
        c.nom = nom.text.toString().trim(); c.prenom = prenom.text.toString().trim(); c.telephone = phone.text.toString().trim(); c.email = email.text.toString().trim()
        c.photoUri = selectedPhotoUri; c.photoBitmap = selectedPhotoBitmap
        c.latitude = coords.first; c.longitude = coords.second
        vm.update(c); Toast.makeText(requireContext(), "Contact enregistré", Toast.LENGTH_SHORT).show(); return true
    }
    private fun saveCoordinatesThenMap() {
        val c = current ?: return
        if (!save()) return
        (activity as MainActivity).openMap(c.id)
    }
    private fun call() { val n = phone.text.toString().trim(); if (n.isBlank()) return; startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$n"))) }
    private fun sendEmail() { val address = email.text.toString().trim(); if (address.isBlank()) { email.error = "Adresse e-mail obligatoire"; return }; val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address")); try { startActivity(intent) } catch (_: Exception) { Toast.makeText(requireContext(), "Aucune application e-mail disponible", Toast.LENGTH_SHORT).show() } }
}
