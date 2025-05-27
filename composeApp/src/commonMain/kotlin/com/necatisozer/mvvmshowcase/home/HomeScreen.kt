package com.necatisozer.mvvmshowcase.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mvvmshowcase.composeapp.generated.resources.Res
import mvvmshowcase.composeapp.generated.resources.home_movie_list_button
import mvvmshowcase.composeapp.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMovieListClick: () -> Unit, modifier: Modifier = Modifier) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(title = { Text(text = stringResource(Res.string.home_title)) })
    },
    modifier = modifier,
  ) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
      Button(onClick = onMovieListClick) {
        Text(text = stringResource(Res.string.home_movie_list_button))
      }
    }
  }
}
