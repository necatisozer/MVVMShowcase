package com.necatisozer.mvvmshowcase.di

import com.necatisozer.mvvmshowcase.data.MovieRepository
import com.necatisozer.mvvmshowcase.data.local.MovieInMemoryStorage
import com.necatisozer.mvvmshowcase.data.local.MovieLocalDataSource
import com.necatisozer.mvvmshowcase.data.remote.MovieRemoteDataSource
import com.necatisozer.mvvmshowcase.data.remote.MovieServer

object AppModule {
  val movieLocalDataSource: MovieLocalDataSource by lazy { MovieInMemoryStorage() }

  val movieRemoteDataSource: MovieRemoteDataSource by lazy { MovieServer() }

  val movieRepository: MovieRepository by lazy {
    MovieRepository(movieLocalDataSource, movieRemoteDataSource)
  }
}
