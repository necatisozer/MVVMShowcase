package com.necatisozer.mvvmshowcase

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.necatisozer.mvvmshowcase.data.MovieRepository
import com.necatisozer.mvvmshowcase.home.HomeScreen
import com.necatisozer.mvvmshowcase.moviedetail.MovieDetailScreen
import com.necatisozer.mvvmshowcase.moviedetail.MovieDetailUIState
import com.necatisozer.mvvmshowcase.moviedetail.MovieDetailViewModel
import com.necatisozer.mvvmshowcase.movielist.MovieListScreen
import com.necatisozer.mvvmshowcase.movielist.MovieListUIState
import com.necatisozer.mvvmshowcase.movielist.MovieListViewModel
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
        val viewModel = viewModel {
          MovieListViewModel(
            movieRepository = MovieRepository.instance,
            savedStateHandle = createSavedStateHandle(),
          )
        }
        val uiState: MovieListUIState by viewModel.uiState.collectAsStateWithLifecycle()

        MovieListScreen(
          uiState = uiState,
          onBackClick = { navController.popBackStack() },
          onSearchQueryChange = { query -> viewModel.onSearchQueryChange(query) },
          onMovieClick = { movieId -> navController.navigate(MovieDetailRoute(movieId)) },
          onRetryClick = { viewModel.onRetry() },
        )
      }

      composable<MovieDetailRoute> { navBackStackEntry ->
        val route: MovieDetailRoute = navBackStackEntry.toRoute()
        val viewModel = viewModel {
          MovieDetailViewModel(movieId = route.movieId, movieRepository = MovieRepository.instance)
        }
        val uiState: MovieDetailUIState by viewModel.uiState.collectAsStateWithLifecycle()

        MovieDetailScreen(
          uiState = uiState,
          onBackClick = { navController.popBackStack() },
          onRetryClick = { viewModel.onRetry() },
        )
      }
    }
  }
}

@Serializable data object HomeRoute

@Serializable data object MovieListRoute

@Serializable data class MovieDetailRoute(val movieId: String)
