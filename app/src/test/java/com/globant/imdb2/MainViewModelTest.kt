package com.globant.imdb2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.data.network.repository.MovieRepository
import com.globant.domain.model.Genre
import com.globant.domain.model.Movie
import com.globant.domain.model.MovieDetail
import com.globant.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var viewModel: com.globant.presentation.viewmodel.MainViewModel

    @Mock
    private lateinit var repository: com.globant.data.network.repository.MovieRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val mockMovies = listOf(
        com.globant.domain.model.Movie(
            id = "1",
            name = "The Shawshank Redemption",
            originalTitle = "The Shawshank Redemption",
            backImage = "/path/to/backdrop1.jpg",
            image = "/path/to/poster1.jpg",
            date = "1994-09-23",
            score = 9.3
        ),
        com.globant.domain.model.Movie(
            id = "2",
            name = "The Godfather",
            originalTitle = "The Godfather",
            backImage = "/path/to/backdrop2.jpg",
            image = "/path/to/poster2.jpg",
            date = "1972-03-24",
            score = 9.2
        ),
        com.globant.domain.model.Movie(
            id = "3",
            name = "The Dark Knight",
            originalTitle = "The Dark Knight",
            backImage = "/path/to/backdrop3.jpg",
            image = "/path/to/poster3.jpg",
            date = "2008-07-18",
            score = 9.0
        ),
        com.globant.domain.model.Movie(
            id = "4",
            name = "Pulp Fiction",
            originalTitle = "Pulp Fiction",
            backImage = "/path/to/backdrop4.jpg",
            image = "/path/to/poster4.jpg",
            date = "1994-10-14",
            score = 8.9
        ),
        com.globant.domain.model.Movie(
            id = "5",
            name = "The Lord of the Rings: The Return of the King",
            originalTitle = "The Lord of the Rings: The Return of the King",
            backImage = "/path/to/backdrop5.jpg",
            image = "/path/to/poster5.jpg",
            date = "2003-12-17",
            score = 8.9
        )
    )

    val genres = listOf(
        com.globant.domain.model.Genre(name = "Drama"),
        com.globant.domain.model.Genre(name = "Crime")
    )


    val mockMovieDetail = com.globant.domain.model.MovieDetail(
        identifier = "1",
        movieName = "The Godfather",
        movieImage = "/path/to/poster1.jpg",
        score = 9.2,
        backImage = "/path/to/backdrop1.jpg",
        description = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
        genres = genres
    )


    @Before
    fun setUp() {

        val mockContext = mock(Context::class.java)
        val mockConnectivityManager = mock(ConnectivityManager::class.java)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork))
            .thenReturn(mockNetworkCapabilities)
        `when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(true)

        viewModel = com.globant.presentation.viewmodel.MainViewModel(repository, mockContext)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher to the original Main dispatcher
    }

    @Test
    fun loadMovies_withInternet_updatesMoviesLiveData() = runTest {

        `when`(repository.getPopularMovies()).thenReturn((mockMovies))

        viewModel.movies.observeForever { }

        viewModel.loadMovies()

        assertEquals(mockMovies, viewModel.movies.value)
        verify(repository).getPopularMovies()
    }


    @Test
    fun loadMovie_updatesMovieLiveData() = runTest {

        `when`(repository.getMovieById("1")).thenReturn(mockMovieDetail)

        viewModel.loadMovie("1")

        assertEquals(mockMovieDetail, viewModel.movie.value)
        verify(repository).getMovieById("1")
    }
}