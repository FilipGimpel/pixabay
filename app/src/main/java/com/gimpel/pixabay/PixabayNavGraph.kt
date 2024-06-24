package com.gimpel.pixabay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onItemClick = { navController.navigate(PixabayDestinations.DETAIL_ROUTE) }
            )
        }
        composable(
            PixabayDestinations.DETAIL_ROUTE,
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Text(
                        text = "Detail Screen",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Button(onClick = { navController.popBackStack() }) {
                        Text(
                            text = "Go back to List Screen",
                        )
                    }
                }
            }
        }
    }
}