package com.gimpel.pixabay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gimpel.pixabay.PixabayDestinationsArgs.DETAIL_ID_ARG
import com.gimpel.pixabay.PixabayScreens.DETAIL_SCREEN
import com.gimpel.pixabay.detail.DetailScreen
import com.gimpel.pixabay.list.SearchScreen

@Composable
fun PixabayNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PixabayDestinations.SEARCH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(
            PixabayDestinations.SEARCH_ROUTE,
        ) {
            SearchScreen(
                modifier = modifier,
                onHitClick = { id -> navController.navigate("$DETAIL_SCREEN/$id") }
            )
        }
        composable(
            PixabayDestinations.DETAIL_ROUTE,
            arguments = listOf(navArgument(DETAIL_ID_ARG) { type = NavType.IntType })
        ) {
            DetailScreen()
        }
    }
}