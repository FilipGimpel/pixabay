package com.gimpel.pixabay.data

import com.gimpel.pixabay.data.local.LocalHit
import com.gimpel.pixabay.data.network.Hit

fun Hit.toLocal(): LocalHit {
    return LocalHit(
        id = this.id,
        previewURL = this.previewURL,
        user = this.user,
        tags = this.tags.split(","),
        largeImageURL = this.largeImageURL,
        likes = this.likes,
        downloads = this.downloads,
        comments = this.comments
    )
}

fun LocalHit.toNetwork(): Hit {
    return Hit(
        id = this.id,
        previewURL = this.previewURL,
        user = this.user,
        tags = this.tags.joinToString(","),
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
fun List<LocalHit>.toNetwork() = map(LocalHit::toNetwork)

@JvmName("externalToLocal")
fun List<Hit>.toLocal() = map(Hit::toLocal)