package com.necatisozer.mvvmshowcase.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.necatisozer.mvvmshowcase.data.MovieRepository
import com.necatisozer.mvvmshowcase.ui.util.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModel(
  private val movieId: String,
  private val movieRepository: MovieRepository,
) : ViewModel() {
  private val _uiState = MutableStateFlow(MovieDetailUIState())
  val uiState: StateFlow<MovieDetailUIState>
    get() = _uiState

  init {
    fetchAndUpdateMovie()
  }

  fun onRetry() {
    fetchAndUpdateMovie()
  }

  private fun fetchAndUpdateMovie() {
    viewModelScope.launch {
      _uiState.update { it.copy(movieState = UiState.Loading) }
      runCatching {
          val movie = movieRepository.getMovie(movieId)
          _uiState.update { it.copy(UiState.Success(movie)) }
        }
        .onFailure { exception ->
          coroutineContext.ensureActive()
          _uiState.update { it.copy(movieState = UiState.Failure(exception)) }
        }
    }
  }
}
