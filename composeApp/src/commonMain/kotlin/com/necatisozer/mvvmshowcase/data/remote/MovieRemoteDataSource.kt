package com.necatisozer.mvvmshowcase.data.remote

import com.necatisozer.mvvmshowcase.model.Movie

interface MovieRemoteDataSource {
    suspend fun getMovies(): List<Movie>
}