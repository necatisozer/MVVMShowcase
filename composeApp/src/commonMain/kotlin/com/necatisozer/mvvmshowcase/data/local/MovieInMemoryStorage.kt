package com.necatisozer.mvvmshowcase.data.local

import com.necatisozer.mvvmshowcase.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MovieInMemoryStorage : MovieLocalDataSource {
  private val moviesStateFlow = MutableStateFlow<List<MovieInMemoryModel>>(emptyList())

  override val movies: Flow<List<Movie>>
    get() = moviesStateFlow.map { movieInMemoryModels -> movieInMemoryModels.map { it.toDomain() } }

  override suspend fun saveMovies(movies: List<Movie>) {
    val movieInMemoryModels: List<MovieInMemoryModel> = movies.map { it.toInMemoryModel() }
    moviesStateFlow.update { movieInMemoryModels }
  }

  override suspend fun getMovieById(movieId: String): Movie {
    val movieInMemoryModels: List<MovieInMemoryModel> = moviesStateFlow.value
    val movieInMemoryModel: MovieInMemoryModel = movieInMemoryModels.first { it.id == movieId }
    return movieInMemoryModel.toDomain()
  }
}

private class MovieInMemoryModel(
  val id: String,
  val title: String,
  val overview: String,
  val posterUrl: String,
  val backdropUrl: String,
)

private fun Movie.toInMemoryModel(): MovieInMemoryModel =
  MovieInMemoryModel(
    id = id,
    title = name,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
  )

private fun MovieInMemoryModel.toDomain(): Movie =
  Movie(
    id = id,
    name = title,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
  )
