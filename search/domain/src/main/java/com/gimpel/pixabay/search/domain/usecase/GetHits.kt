package com.gimpel.pixabay.search.domain.usecase

import com.gimpel.pixabay.search.domain.repository.ImagesRepository
import javax.inject.Inject

class GetHits @Inject constructor(
    private val imagesRepository: ImagesRepository
) {
    suspend operator fun invoke(query: String, page: Int = 1, perPage: Int = 20) =
        imagesRepository.getHits(query, page, perPage)
}