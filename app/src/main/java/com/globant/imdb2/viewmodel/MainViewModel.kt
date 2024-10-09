package com.globant.imdb2.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var _movies = MutableLiveData<List<MovieDTO>>()
    val movies = _movies

    private var _movie = MutableLiveData<MovieDetail>()
    val movie = _movie

    private val repo = MovieRepository()

    fun loadMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repo.getPopularMovies()
            _movies.postValue(response.body()?.results)
        }
    }

    fun loadMovie(id: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = repo.getMovieById(id)
            _movie.postValue(response.body())
        }

    }


}