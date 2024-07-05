package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.HitEntity
import com.gimpel.pixabay.data.local.HitWithTags
import com.gimpel.pixabay.data.network.HitDTO
import com.gimpel.pixabay.model.Hit

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

fun HitDTO.toModel(): Hit {
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