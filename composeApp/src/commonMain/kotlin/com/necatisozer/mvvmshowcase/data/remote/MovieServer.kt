package com.necatisozer.mvvmshowcase.data.remote

import com.necatisozer.mvvmshowcase.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

class MovieServer : MovieRemoteDataSource {
  private val httpClient = HttpClient {
    install(ContentNegotiation) { json(contentType = ContentType.Text.Plain) }
  }

  override suspend fun getMovies(): List<Movie> {
    delay(1.seconds)
    val movieServerModels: List<MovieServerModel> = httpClient.get(MOVIES_URL).body()
    return movieServerModels.map { it.toDomain() }
  }
}

private const val MOVIES_URL =
  "https://raw.githubusercontent.com/necatisozer/MVVMShowcase/refs/heads/master/composeApp/src/commonMain/composeResources/files/movies.json"

@Serializable
private class MovieServerModel(
  val id: String,
  val title: String,
  val overview: String,
  val posterUrl: String,
  val backdropUrl: String,
)

private fun MovieServerModel.toDomain(): Movie =
  Movie(
    id = id,
    name = title,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
  )
