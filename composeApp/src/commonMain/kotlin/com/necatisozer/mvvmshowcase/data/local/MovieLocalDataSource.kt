package com.necatisozer.mvvmshowcase.data.local

import com.necatisozer.mvvmshowcase.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
  val movies: Flow<List<Movie>>

  suspend fun saveMovies(movies: List<Movie>)

  suspend fun getMovieById(movieId: String): Movie
}