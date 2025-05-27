package com.necatisozer.mvvmshowcase.data

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import mvvmshowcase.composeapp.generated.resources.Res
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MovieRepository() {
  suspend fun fetchMovies(): List<Movie> {
    delay(1.seconds)
    if (Random.nextBoolean()) error("Network error")
    return loadMovies()
  }

  suspend fun getMovie(movieId: String): Movie {
    delay(1.seconds)
    if (Random.nextBoolean()) error("Network error")
    return loadMovies().find { it.id == movieId } ?: error("Movie not found")
  }

  private suspend fun loadMovies(): List<Movie> {
    val jsonString = Res.readBytes("files/movies.json").decodeToString()
    return Json.decodeFromString(jsonString)
  }

  companion object {
    val instance by lazy { MovieRepository() }
  }
}
