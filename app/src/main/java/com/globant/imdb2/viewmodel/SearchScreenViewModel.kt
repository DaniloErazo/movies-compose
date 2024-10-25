package com.globant.imdb2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDTO>>()
    val filtered = MutableLiveData<List<MovieDTO>>()
    var dataFetched = MutableLiveData<Boolean>()

    private fun loadMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getPopularMovies()
            _movies.postValue(response.body()?.results)
            filtered.postValue(response.body()?.results)
            dataFetched.postValue(true)

        }
    }

    fun filterMovies(query: String) {
        filtered.value = if (query.isBlank()) {
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