package com.necatisozer.mvvmshowcase.ui.util

sealed interface UiState<out T> {
  data object Loading : UiState<Nothing>

  data class Success<out T>(val data: T) : UiState<T>

  data class Failure(val exception: Throwable) : UiState<Nothing>
}

fun <T> UiState<T>.getOrNull(): T? =
  when (this) {
    is UiState.Success -> data
    else -> null
  }

fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> =
  when (this) {
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Loading,
    is UiState.Failure -> this
  }
