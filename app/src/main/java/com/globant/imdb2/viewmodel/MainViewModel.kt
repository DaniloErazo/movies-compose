package com.globant.imdb2.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.toEntity
import com.globant.imdb2.entity.toMovieDTO
import com.globant.imdb2.repository.MovieRepository
import com.globant.imdb2.utils.NetworkUtils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context): ViewModel() {

    private var _movies = MutableLiveData<List<MovieDTO>>()
    val movies = _movies

    private var _movie = MutableLiveData<MovieDetail>()
    val movie = _movie


    fun loadMovies(){
        viewModelScope.launch(){

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies()
                response.let { movies ->
                    val saveMovies = movies.results.map {it.toEntity()}
                    repository.saveLocalMovies(saveMovies)
                    _movies.postValue(movies.results)
                }
            }else{
                _movies.postValue(repository.getLocalMovies().map { it.toMovieDTO() })
            }

        }
    }

    fun loadMovie(id: String){
        viewModelScope.launch(){
            val response = repository.getMovieById(id)
            _movie.postValue(response.body())
        }

    }



}