package com.globant.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.domain.model.Movie
import com.globant.domain.repository.MovieRepository
import com.globant.presentation.utils.NetworkUtils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: MovieRepository, @ApplicationContext private val context: Context) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val filtered = MutableLiveData<List<Movie>>()
    var dataFetched = MutableLiveData<Boolean>()

    fun loadMovies(){
        viewModelScope.launch{

            if(isInternetAvailable(context)){
                val response = repository.getPopularMovies()
                _movies.postValue(response)
                filtered.postValue(response)
                dataFetched.postValue(true)
            }else{
                val movies = repository.getLocalMovies()
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
                it.name.contains(query, ignoreCase = true)
            }
        }
    }

    init{
        loadMovies()
    }
}