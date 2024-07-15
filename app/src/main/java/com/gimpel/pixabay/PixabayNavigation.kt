package com.gimpel.pixabay

import com.gimpel.pixabay.PixabayScreens.DETAIL_SCREEN
import com.gimpel.pixabay.PixabayScreens.SEARCH_SCREEN
import com.gimpel.pixabay.search.presentation.ui.DetailScreenNavArgs.DETAIL_ID_ARG

object PixabayScreens {
    const val SEARCH_SCREEN = "list"
    const val DETAIL_SCREEN = "detail"
}

object PixabayDestinations {
    const val SEARCH_ROUTE = SEARCH_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{${DETAIL_ID_ARG}}"
}
