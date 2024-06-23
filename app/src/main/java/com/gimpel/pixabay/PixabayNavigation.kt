package com.gimpel.pixabay

import com.gimpel.pixabay.PixabayDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.pixabay.PixabayScreens.DETAIL_SCREEN
import com.gimpel.pixabay.PixabayScreens.SEARCH_SCREEN

private object PixabayScreens {
    const val SEARCH_SCREEN = "list"
    const val DETAIL_SCREEN = "detail"
}

object PixabayDestinationsArgs {
    const val DETAIL_ID_ARG = "imageId"
}

object PixabayDestinations {
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{${DETAIL_ID_ARG}}"
}
