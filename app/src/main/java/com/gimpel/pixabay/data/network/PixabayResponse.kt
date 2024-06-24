package com.gimpel.pixabay.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PixabayResponse (
    val total: Int,
    val totalHits: Int,
    val hits: List<Hit>
)

@Serializable
data class Hit(
    val id: Int,
    val previewURL: String,
    val user: String,
    val tags: String,

    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,

)