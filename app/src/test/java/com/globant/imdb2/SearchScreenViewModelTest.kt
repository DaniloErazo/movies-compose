package com.globant.imdb2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.repository.MovieRepository
import com.globant.imdb2.utils.NetworkUtils
import com.globant.imdb2.viewmodel.SearchScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchScreenViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData updates happen instantly during testing

    private lateinit var viewModel: SearchScreenViewModel

    @Mock
    private lateinit var repository: MovieRepository

    private val movieListDTO = listOf(
        MovieDTO(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        MovieDTO(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        MovieDTO(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
    )

    private val movieList = listOf(
        Movie(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        Movie(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        Movie(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
    )

    private suspend fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val mockContext = mock(Context::class.java)
        val mockConnectivityManager = mock(ConnectivityManager::class.java)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork))
            .thenReturn(mockNetworkCapabilities)
        `when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(true)

        `when`(repository.getPopularMovies()).thenReturn((MovieResponse(movieListDTO)))

        viewModel = SearchScreenViewModel(repository, mockContext)

    }

    private suspend fun setupNoConnection(){
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val mockContext = mock(Context::class.java)
        val mockConnectivityManager = mock(ConnectivityManager::class.java)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)

        val mockNetworkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(mockConnectivityManager.getNetworkCapabilities(mockConnectivityManager.activeNetwork))
            .thenReturn(mockNetworkCapabilities)
        `when`(mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            .thenReturn(false)

        `when`(repository.getLocalMovies()).thenReturn(movieList)

        viewModel = SearchScreenViewModel(repository, mockContext)


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loadMovies when internet is available`() = runTest {

        setup()

        `when`(repository.getPopularMovies()).thenReturn((MovieResponse(movieListDTO)))

        viewModel.filtered.observeForever { }

        viewModel.loadMovies()

        advanceUntilIdle()

        assertEquals(movieListDTO, viewModel.filtered.value)

    }

    @Test
    fun `test loadMovies when no internet is available`() = runTest {

        setupNoConnection()

        //`when`(repository.getLocalMovies()).thenReturn(movieList)

        viewModel.filtered.observeForever { }

        assertEquals(movieListDTO, viewModel.filtered.value)

        verify(repository).getLocalMovies()
    }

    @Test
    fun `test filterMovies with query2`() = runTest {
        setup()

        `when`(repository.getPopularMovies()).thenReturn(MovieResponse(movieListDTO))

        val filteredObserver = mock(Observer::class.java) as Observer<List<MovieDTO>>
        viewModel.filtered.observeForever(filteredObserver)

        viewModel.loadMovies()

        viewModel.filterMovies("Godfather")

        advanceUntilIdle()

        val filteredMovies = viewModel.filtered.value
        assertNotNull(filteredMovies)
        assertEquals(1, filteredMovies?.size)
        assertTrue(filteredMovies?.any { it.movieName.contains("Godfather", ignoreCase = true) } == true)
    }

    @Test
    fun `test filterMovies with empty query`() = runTest {
        setup()

        viewModel.filtered.observeForever {  }

        viewModel.filterMovies(" ")
        advanceUntilIdle()

        val filteredMovies = viewModel.filtered.value
        assertNotNull(filteredMovies)
        assertEquals(movieList.size, filteredMovies?.size)
    }
}
