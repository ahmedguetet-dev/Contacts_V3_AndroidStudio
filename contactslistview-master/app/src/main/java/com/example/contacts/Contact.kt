package com.example.contacts

import android.graphics.Bitmap

data class Contact(
    val id: Int,
    var nom: String,
    var prenom: String,
    var telephone: String,
    var email: String,
    var photoUri: String? = null,
    var photoBitmap: Bitmap? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
) {
    val displayName: String get() = "$prenom $nom".trim()
}
