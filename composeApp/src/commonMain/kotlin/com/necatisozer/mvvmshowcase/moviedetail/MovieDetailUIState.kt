package com.necatisozer.mvvmshowcase.moviedetail

import com.necatisozer.mvvmshowcase.data.Movie
import com.necatisozer.mvvmshowcase.data.UiState

data class MovieDetailUIState(val movieState: UiState<Movie> = UiState.Loading)
