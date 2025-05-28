package com.necatisozer.mvvmshowcase.movielist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.necatisozer.mvvmshowcase.data.Movie
import com.necatisozer.mvvmshowcase.data.UiState
import com.necatisozer.mvvmshowcase.data.map
import com.necatisozer.mvvmshowcase.icon.ArrowBack
import com.necatisozer.mvvmshowcase.icon.Search
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import mvvmshowcase.composeapp.generated.resources.Res
import mvvmshowcase.composeapp.generated.resources.back_icon_content_description
import mvvmshowcase.composeapp.generated.resources.movie_list_fetch_failure
import mvvmshowcase.composeapp.generated.resources.movie_list_search_label
import mvvmshowcase.composeapp.generated.resources.movie_list_title
import mvvmshowcase.composeapp.generated.resources.retry_button
import mvvmshowcase.composeapp.generated.resources.search_icon_content_description
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
  onBackClick: () -> Unit,
  onMovieClick: (movieId: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  var searchQuery by rememberSaveable { mutableStateOf("") }

  var reproducer by remember { mutableStateOf(0) }

  val movies: UiState<List<Movie>> by
    produceState<UiState<List<Movie>>>(initialValue = UiState.Loading, reproducer) {
      value = UiState.Loading
      delay(1.seconds)
      if (Random.nextBoolean()) {
        value = UiState.Failure(RuntimeException("Network error"))
        return@produceState
      }
      val jsonString = Res.readBytes("files/movies.json").decodeToString()
      val movieList: List<Movie> = Json.decodeFromString(jsonString)
      value = UiState.Success(movieList)
    }

  val queriedMovies: UiState<List<Movie>> =
    remember(searchQuery, movies) {
      if (searchQuery.isBlank()) {
        movies
      } else {
        movies.map { movieList ->
          movieList.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
      }
    }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = stringResource(Res.string.movie_list_title)) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = ArrowBack,
              contentDescription = stringResource(Res.string.back_icon_content_description),
            )
          }
        },
      )
    },
    modifier = modifier,
  ) { padding ->
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
        Modifier.fillMaxSize()
          .padding(
            top = padding.calculateTopPadding(),
            start = padding.calculateStartPadding(LocalLayoutDirection.current),
            end = padding.calculateEndPadding(LocalLayoutDirection.current),
          ),
    ) {
      TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text(text = stringResource(Res.string.movie_list_search_label)) },
        trailingIcon = {
          Icon(
            imageVector = Search,
            contentDescription = stringResource(Res.string.search_icon_content_description),
          )
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
      )

      when (val currentQueriedMovies: UiState<List<Movie>> = queriedMovies) {
        UiState.Loading -> {
          Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
            CircularProgressIndicator()
          }
        }
        is UiState.Failure -> {
          Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
          ) {
            Text(text = stringResource(Res.string.movie_list_fetch_failure))

            OutlinedButton(onClick = { reproducer++ }) {
              Text(text = stringResource(Res.string.retry_button))
            }
          }
        }
        is UiState.Success -> {
          LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            contentPadding =
              PaddingValues(
                top = 16.dp,
                start = 16.dp + padding.calculateStartPadding(LocalLayoutDirection.current),
                end = 16.dp + padding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = 16.dp + padding.calculateBottomPadding(),
              ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f),
          ) {
            items(currentQueriedMovies.data, key = { it.id }) { movie ->
              MovieCell(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.animateItem(),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MovieCell(movie: Movie, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier.clip(MaterialTheme.shapes.medium).clickable(onClick = onClick),
  ) {
    AsyncImage(
      model = movie.posterUrl,
      contentDescription = movie.title,
      modifier = Modifier.fillMaxWidth().aspectRatio(2 / 3f).clip(MaterialTheme.shapes.medium),
    )
    Text(movie.title, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleSmall)
  }
}
