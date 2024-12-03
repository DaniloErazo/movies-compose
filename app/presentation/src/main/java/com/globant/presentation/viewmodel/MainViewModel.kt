package com.globant.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.model.Movie
import com.globant.domain.model.MovieDetail
import com.globant.domain.repository.MovieRepository
import com.globant.presentation.utils.NetworkUtils.isInternetAvailable

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context): ViewModel() {

    private var _movies = MutableLiveData<List<Movie>>()
    val movies = _movies

    private var _movie = MutableLiveData<MovieDetail>()
    val movie = _movie

    var dataFetched = MutableLiveData(false)

    //work manager para consultar las películas, periodico de tal forma que se consulte
    //primariamente la base de datos local


    fun loadMovies(){
        viewModelScope.launch{

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies()
                response.let { movies ->
                    repository.saveLocalMovies(movies)
                    _movies.postValue(movies)
                }
            }else{
                _movies.postValue(repository.getLocalMovies())
            }
            dataFetched.postValue(true)

        }
    }

    fun loadMovie(id: String){
        viewModelScope.launch {
            val response = repository.getMovieById(id)
            _movie.postValue(response)
        }
    }



}