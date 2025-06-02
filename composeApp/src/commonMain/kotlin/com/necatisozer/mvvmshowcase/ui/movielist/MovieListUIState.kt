package com.necatisozer.mvvmshowcase.ui.movielist

import com.necatisozer.mvvmshowcase.model.Movie
import com.necatisozer.mvvmshowcase.ui.util.UiState

data class MovieListUIState(
  val searchQuery: String = "",
  val movies: UiState<List<Movie>> = UiState.Loading,
)
