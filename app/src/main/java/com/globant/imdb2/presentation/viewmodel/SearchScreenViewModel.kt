package com.globant.imdb2.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.data.network.repository.MovieRepository
import com.globant.imdb2.domain.model.toDTO
import com.globant.imdb2.presentation.model.MovieDTO
import com.globant.imdb2.utils.NetworkUtils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDTO>>()
    val filtered = MutableLiveData<List<MovieDTO>>()
    var dataFetched = MutableLiveData<Boolean>()

    fun loadMovies(){
        viewModelScope.launch{

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies().map { it.toDTO() }
                _movies.postValue(response)
                filtered.postValue(response)
                dataFetched.postValue(true)
            }else{
                val movies = repository.getLocalMovies().map { it.toDTO() }
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