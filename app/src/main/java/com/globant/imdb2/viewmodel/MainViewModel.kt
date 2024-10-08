package com.globant.imdb2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var _movies = MutableLiveData<List<MovieDTO>>()
    val movies = _movies

    private val repo = MovieRepository()

    fun loadMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repo.getPopularMovies()
            _movies.postValue(response.body()?.results)
        }
    }


}