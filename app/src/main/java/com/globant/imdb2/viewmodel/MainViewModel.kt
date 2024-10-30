package com.globant.imdb2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.toEntity
import com.globant.imdb2.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {

    private var _movies = MutableLiveData<List<MovieDTO>>()
    val movies = _movies

    private var _movie = MutableLiveData<MovieDetail>()
    val movie = _movie


    fun loadMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getPopularMovies()
            response.body()?.results?.let { movies ->
                repository.saveLocalMovies(movies.map { it.toEntity() })
            }
            _movies.postValue(response.body()?.results)
        }
    }

    fun loadMovie(id: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getMovieById(id)
            _movie.postValue(response.body())
        }

    }


}