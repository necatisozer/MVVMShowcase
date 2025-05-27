package com.necatisozer.mvvmshowcase.data

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
  val id: String,
  val title: String,
  val overview: String,
  val posterUrl: String,
  val backdropUrl: String,
)
