package com.necatisozer.mvvmshowcase

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.necatisozer.mvvmshowcase.home.HomeScreen
import com.necatisozer.mvvmshowcase.moviedetail.MovieDetailScreen
import com.necatisozer.mvvmshowcase.movielist.MovieListScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
  MaterialTheme(
    colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
  ) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HomeRoute) {
      composable<HomeRoute> {
        HomeScreen(onMovieListClick = { navController.navigate(MovieListRoute) })
      }

      composable<MovieListRoute> {
        MovieListScreen(
          onBackClick = { navController.popBackStack() },
          onMovieClick = { movieId -> navController.navigate(MovieDetailRoute(movieId)) },
        )
      }

      composable<MovieDetailRoute> { navBackStackEntry ->
        val route: MovieDetailRoute = navBackStackEntry.toRoute()

        MovieDetailScreen(movieId = route.movieId, onBackClick = { navController.popBackStack() })
      }
    }
  }
}

@Serializable data object HomeRoute

@Serializable data object MovieListRoute

@Serializable data class MovieDetailRoute(val movieId: String)
