package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.HitEntity
import com.gimpel.pixabay.data.network.HitDTO

fun HitDTO.toEntity(): HitEntity {
    return HitEntity(
        hitId = this.id,
        previewURL = this.previewURL,
        user = this.user,
        tags = this.tags.joinToString(","),
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

fun HitEntity.toDTO(): HitDTO {
    return HitDTO(
        id = this.hitId,
        previewURL = this.previewURL,
        user = this.user,
        //tags = this.tags,TODO FIXME
        tags = this.tags.split(",").map { it.trim() },
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("localToExternal")
fun List<HitEntity>.toDTO() = map(HitEntity::toDTO)

@JvmName("externalToLocal")
fun List<HitDTO>.toEntity() = map(HitDTO::toEntity)