package com.necatisozer.mvvmshowcase.ui.movielist

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.necatisozer.mvvmshowcase.model.Movie
import com.necatisozer.mvvmshowcase.ui.util.UiState
import com.necatisozer.mvvmshowcase.ui.res.ArrowBack
import com.necatisozer.mvvmshowcase.ui.res.Search
import mvvmshowcase.composeapp.generated.resources.Res
import mvvmshowcase.composeapp.generated.resources.back_icon_content_description
import mvvmshowcase.composeapp.generated.resources.movie_list_fetch_failure
import mvvmshowcase.composeapp.generated.resources.movie_list_search_label
import mvvmshowcase.composeapp.generated.resources.movie_list_title
import mvvmshowcase.composeapp.generated.resources.retry_button
import mvvmshowcase.composeapp.generated.resources.search_icon_content_description
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
  uiState: MovieListUIState,
  onBackClick: () -> Unit,
  onSearchQueryChange: (query: String) -> Unit,
  onMovieClick: (movieId: String) -> Unit,
  onRetryClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
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
        value = uiState.searchQuery,
        onValueChange = onSearchQueryChange,
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

      when (uiState.movies) {
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

            OutlinedButton(onClick = onRetryClick) {
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
            items(uiState.movies.data, key = { it.id }) { movie ->
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
      contentDescription = movie.name,
      modifier = Modifier.fillMaxWidth().aspectRatio(2 / 3f).clip(MaterialTheme.shapes.medium),
    )
    Text(movie.name, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleSmall)
  }
}
