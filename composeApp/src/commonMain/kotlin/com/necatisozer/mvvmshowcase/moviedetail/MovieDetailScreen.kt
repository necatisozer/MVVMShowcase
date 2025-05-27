package com.necatisozer.mvvmshowcase.moviedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.necatisozer.mvvmshowcase.data.Movie
import com.necatisozer.mvvmshowcase.data.UiState
import com.necatisozer.mvvmshowcase.data.getOrNull
import com.necatisozer.mvvmshowcase.icon.ArrowBack
import mvvmshowcase.composeapp.generated.resources.Res
import mvvmshowcase.composeapp.generated.resources.back_icon_content_description
import mvvmshowcase.composeapp.generated.resources.movie_detail_fetch_failure
import mvvmshowcase.composeapp.generated.resources.retry_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
  uiState: MovieDetailUIState,
  onBackClick: () -> Unit,
  onRetryClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = uiState.movieState.getOrNull()?.title.orEmpty()) },
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
    when (val movieState = uiState.movieState) {
      UiState.Loading -> {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator()
        }
      }
      is UiState.Failure -> {
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxSize(),
        ) {
          Text(text = stringResource(Res.string.movie_detail_fetch_failure))

          OutlinedButton(onClick = onRetryClick) {
            Text(text = stringResource(Res.string.retry_button))
          }
        }
      }
      is UiState.Success -> {
        val movie: Movie = movieState.data

        Column(
          verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier =
            Modifier.verticalScroll(rememberScrollState())
              .fillMaxSize()
              .padding(padding)
              .padding(16.dp),
        ) {
          AsyncImage(
            model = movie.backdropUrl,
            contentDescription = movie.title,
            modifier =
              Modifier.widthIn(max = 550.dp)
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(MaterialTheme.shapes.medium),
          )

          Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge)
        }
      }
    }
  }
}
