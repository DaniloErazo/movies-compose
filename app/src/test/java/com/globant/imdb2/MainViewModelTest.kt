package com.globant.imdb2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globant.imdb2.entity.Genre
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.repository.MovieRepository
import com.globant.imdb2.viewmodel.MainViewModel
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

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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

        viewModel = MainViewModel(repository, mockContext)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher to the original Main dispatcher
    }

    @Test
    fun loadMovies_withInternet_updatesMoviesLiveData() = runTest {

        // Setup mock data for movies
        val mockMovies = listOf(
            MovieDTO(
                identifier = "1",
                movieName = "The Shawshank Redemption",
                backImage = "/path/to/backdrop1.jpg",
                movieImage = "/path/to/poster1.jpg",
                movieDate = "1994-09-23",
                score = 9.3
            ),
            MovieDTO(
                identifier = "2",
                movieName = "The Godfather",
                backImage = "/path/to/backdrop2.jpg",
                movieImage = "/path/to/poster2.jpg",
                movieDate = "1972-03-24",
                score = 9.2
            ),
            MovieDTO(
                identifier = "3",
                movieName = "The Dark Knight",
                backImage = "/path/to/backdrop3.jpg",
                movieImage = "/path/to/poster3.jpg",
                movieDate = "2008-07-18",
                score = 9.0
            ),
            MovieDTO(
                identifier = "4",
                movieName = "Pulp Fiction",
                backImage = "/path/to/backdrop4.jpg",
                movieImage = "/path/to/poster4.jpg",
                movieDate = "1994-10-14",
                score = 8.9
            ),
            MovieDTO(
                identifier = "5",
                movieName = "The Lord of the Rings: The Return of the King",
                backImage = "/path/to/backdrop5.jpg",
                movieImage = "/path/to/poster5.jpg",
                movieDate = "2003-12-17",
                score = 8.9
            )
        )

        `when`(repository.getPopularMovies()).thenReturn((MovieResponse(mockMovies)))

        viewModel.movies.observeForever { }

        viewModel.loadMovies()

        advanceUntilIdle()

        assertEquals(mockMovies, viewModel.movies.value)
        verify(repository).getPopularMovies()
    }


    @Test
    fun loadMovie_updatesMovieLiveData() = runTest {

        val genres = listOf(
            Genre(name = "Drama"),
            Genre(name = "Crime")
        )

       val mockMovieDetail = MovieDetail(
            identifier = "1",
            movieName = "The Godfather",
            movieImage = "/path/to/poster1.jpg",
            score = 9.2,
            backImage = "/path/to/backdrop1.jpg",
            description = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
            genres = genres
       )
        `when`(repository.getMovieById("1")).thenReturn(mockMovieDetail)

        viewModel.loadMovie("1")

        advanceUntilIdle()

        assertEquals(mockMovieDetail, viewModel.movie.value)
        verify(repository).getMovieById("1")
    }
}