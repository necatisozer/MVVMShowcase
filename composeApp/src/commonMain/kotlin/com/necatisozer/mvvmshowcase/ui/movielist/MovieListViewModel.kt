package com.necatisozer.mvvmshowcase.ui.movielist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.necatisozer.mvvmshowcase.data.MovieRepository
import com.necatisozer.mvvmshowcase.model.Movie
import com.necatisozer.mvvmshowcase.ui.util.UiState
import com.necatisozer.mvvmshowcase.ui.util.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModel(
  private val movieRepository: MovieRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  private val searchQueryState: MutableStateFlow<String> =
    savedStateHandle.getMutableStateFlow(key = "searchQuery", initialValue = "")

  private val retryTrigger = MutableStateFlow(0)

  private val moviesState: StateFlow<UiState<List<Movie>>> =
    retryTrigger
      .flatMapLatest {
        movieRepository.movies
          .map<List<Movie>, UiState<List<Movie>>> { movies -> UiState.Success(movies) }
          .onStart { emit(UiState.Loading) }
          .catch { exception -> emit(UiState.Failure(exception)) }
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = UiState.Loading,
      )

  val uiState: StateFlow<MovieListUIState> =
    combine(searchQueryState, moviesState) { searchQuery, movies ->
        val queriedMovies: UiState<List<Movie>> =
          if (searchQuery.isBlank()) {
            movies
          } else {
            movies.map { movieList ->
              movieList.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }
          }
        MovieListUIState(searchQuery = searchQuery, movies = queriedMovies)
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MovieListUIState(),
      )

  fun onSearchQueryChange(query: String) {
    searchQueryState.update { query }
  }

  fun onRetry() {
    retryTrigger.update { it + 1 }
  }
}
