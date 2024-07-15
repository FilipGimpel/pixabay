package com.gimpel.pixabay.search.domain.model

data class Hit(
    val id: Int,
    val previewURL: String,
    val user: String,

    val tags: List<String>,
    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,
)


