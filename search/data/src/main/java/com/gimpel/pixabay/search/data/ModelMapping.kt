package com.gimpel.pixabay.search.data

import com.gimpel.pixabay.search.data.local.HitEntity
import com.gimpel.pixabay.search.data.local.HitWithTags
import com.gimpel.pixabay.search.data.network.HitDTO
import com.gimpel.pixabay.search.domain.model.Hit

fun HitDTO.toEntity(): HitEntity {
    return HitEntity(
        hitId = this.id,
        previewURL = this.previewURL,
        user = this.user,
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

fun HitDTO.toHit(): Hit {
    return Hit(
        id = this.id,
        previewURL = this.previewURL,
        user = this.user,
        tags = this.tags,
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

fun Hit.toEntity() : HitEntity {
    return HitEntity(
        hitId = this.id,
        previewURL = this.previewURL,
        user = this.user,
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

fun HitWithTags.toHit(): Hit {
    return Hit(
        id = this.hit.hitId,
        previewURL = this.hit.previewURL,
        user = this.hit.user,
        tags = this.tags.map { it.tagId },
        largeImageURL = this.hit.largeImageURL,
        likes = this.hit.likes,
        downloads = this.hit.downloads,
        comments = this.hit.comments
    )
}

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("HitWithTagToModel")
fun List<HitWithTags>.toHit() = map(HitWithTags::toHit)

@JvmName("HitDTOToModel")
fun List<HitDTO>.toHit() = map(HitDTO::toHit)

@JvmName("HitToEntity")
fun List<Hit>.toEntity() = map(Hit::toEntity)