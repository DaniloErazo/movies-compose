package com.globant.imdb2.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.data.network.repository.MovieRepository
import com.globant.imdb2.domain.model.toDTO
import com.globant.imdb2.presentation.model.MovieDTO
import com.globant.imdb2.presentation.model.MovieDetailDTO
import com.globant.imdb2.utils.NetworkUtils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context): ViewModel() {

    private var _movies = MutableLiveData<List<MovieDTO>>()
    val movies = _movies

    private var _movie = MutableLiveData<MovieDetailDTO>()
    val movie = _movie


    fun loadMovies(){
        viewModelScope.launch{

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies()
                response.let { movies ->
                    repository.saveLocalMovies(movies)
                    _movies.postValue(movies.map {it.toDTO()})
                }
            }else{
                _movies.postValue(repository.getLocalMovies().map { it.toDTO()})
            }

        }
    }

    fun loadMovie(id: String){
        viewModelScope.launch {
            val response = repository.getMovieById(id)
            _movie.postValue(response.toDTO())
        }

    }



}