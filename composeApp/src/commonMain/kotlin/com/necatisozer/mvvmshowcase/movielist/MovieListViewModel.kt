package com.necatisozer.mvvmshowcase.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.necatisozer.mvvmshowcase.data.Movie
import com.necatisozer.mvvmshowcase.data.MovieRepository
import com.necatisozer.mvvmshowcase.data.UiState
import com.necatisozer.mvvmshowcase.data.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModel(
  private val movieRepository: MovieRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val searchQueryState: MutableStateFlow<String> =
    savedStateHandle.getMutableStateFlow(key = "searchQuery", initialValue = "")

  private val moviesState: MutableStateFlow<UiState<List<Movie>>> =
    MutableStateFlow(UiState.Loading)

  val uiState: StateFlow<MovieListUIState> =
    combine(searchQueryState, moviesState) { searchQuery, movies ->
        val queriedMovies: UiState<List<Movie>> =
          if (searchQuery.isBlank()) {
            movies
          } else {
            movies.map { movieList ->
              movieList.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }
          }
        MovieListUIState(searchQuery = searchQuery, movies = queriedMovies)
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MovieListUIState(),
      )

  init {
    fetchAndUpdateMovies()
  }

  fun onSearchQueryChange(query: String) {
    searchQueryState.update { query }
  }

  fun onRetry() {
    fetchAndUpdateMovies()
  }

  private fun fetchAndUpdateMovies() {
    viewModelScope.launch {
      moviesState.update { UiState.Loading }
      runCatching {
          val movies = movieRepository.fetchMovies()
          moviesState.update { UiState.Success(movies) }
        }
        .onFailure { exception ->
          coroutineContext.ensureActive()
          moviesState.update { UiState.Failure(exception) }
        }
    }
  }
}
