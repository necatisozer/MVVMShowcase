package com.necatisozer.mvvmshowcase.ui.moviedetail

import com.necatisozer.mvvmshowcase.model.Movie
import com.necatisozer.mvvmshowcase.ui.util.UiState

data class MovieDetailUIState(val movieState: UiState<Movie> = UiState.Loading)
