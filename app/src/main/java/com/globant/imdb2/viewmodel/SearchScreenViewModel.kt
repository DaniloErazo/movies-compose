package com.globant.imdb2.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.toMovieDTO
import com.globant.imdb2.repository.MovieRepository
import com.globant.imdb2.utils.NetworkUtils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDTO>>()
    val filtered = MutableLiveData<List<MovieDTO>>()
    var dataFetched = MutableLiveData<Boolean>()

    fun loadMovies(){
        viewModelScope.launch(Dispatchers.IO){

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies()
                _movies.postValue(response.body()?.results)
                filtered.postValue(response.body()?.results)
                dataFetched.postValue(true)
            }else{
                val movies = repository.getLocalMovies().map { it.toMovieDTO() }
                _movies.postValue(movies)
                filtered.postValue(movies)
                dataFetched.postValue(true)
            }
        }
    }

    fun filterMovies(query: String) {
        filtered.value = if (query.isEmpty()) {
            _movies.value
        } else {
            filtered.value?.filter {
                it.movieName.contains(query, ignoreCase = true)
            }
        }
    }

    init{
        loadMovies()
    }
}