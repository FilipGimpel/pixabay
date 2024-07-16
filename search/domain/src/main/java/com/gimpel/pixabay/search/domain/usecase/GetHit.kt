package com.gimpel.pixabay.search.domain.usecase

import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import javax.inject.Inject

class GetHit @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    suspend operator fun invoke(hitId: Int) = imagesRepository.getHit(hitId)
}