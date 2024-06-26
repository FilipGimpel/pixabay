package com.gimpel.pixabay.data.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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
    @Serializable(with = TagsDeserializer::class)
    val tags: List<String>,
    val largeImageURL: String,
    val likes: Int,
    val downloads: Int,
    val comments: Int,

)

object TagsDeserializer : KSerializer<List<String>> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Tag", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<String> {
        return decoder.decodeString().split(",").map { it.trim() }
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeString(value.joinToString(","))
    }
}