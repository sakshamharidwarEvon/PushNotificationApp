package com.example.pushnotificationfirebase

class Artist(id: String, name: String, genre: String) {

    var artistId: String? = null

    var artistName: String? = null
    var spinnerGenres: String? = null

    fun Artist() {}

    fun Artist(artistId: String?, artistName: String?, spinnerGenres: String?) {
        this.artistId = artistId
        this.artistName = artistName
        this.spinnerGenres = spinnerGenres
    }
}
