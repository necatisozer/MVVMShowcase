package com.necatisozer.mvvmshowcase.movielist

import com.necatisozer.mvvmshowcase.data.Movie
import com.necatisozer.mvvmshowcase.data.UiState

data class MovieListUIState(
  val searchQuery: String = "",
  val movies: UiState<List<Movie>> = UiState.Loading,
)
