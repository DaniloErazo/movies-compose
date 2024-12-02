package com.globant.imdb2

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.globant.data.network.repository.MovieRepository
import com.globant.domain.model.Movie
import com.globant.presentation.viewmodel.SearchScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchScreenViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: com.globant.presentation.viewmodel.SearchScreenViewModel

    @Mock
    private lateinit var repository: com.globant.data.network.repository.MovieRepository

    private val movieList = listOf(
        com.globant.domain.model.Movie(
            id = "1",
            name = "The Shawshank Redemption",
            originalTitle = "The Shawshank Redemption",
            backImage = "/path1",
            image = "/poster1",
            date = "1994-09-23",
            score = 9.3
        ),
        com.globant.domain.model.Movie(
            id = "2",
            name = "The Godfather",
            originalTitle = "The Godfather",
            backImage = "/path2",
            image = "/poster2",
            date = "1972-03-24",
            score = 9.2
        ),
        com.globant.domain.model.Movie(
            id = "3",
            name = "The Dark Knight",
            originalTitle = "The Dark Knight",
            backImage = "/path3",
            image = "/poster3",
            date = "2008-07-18",
            score = 9.0
        )
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

        `when`(repository.getPopularMovies()).thenReturn(movieList)

        viewModel =
            com.globant.presentation.viewmodel.SearchScreenViewModel(repository, mockContext)

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

        viewModel =
            com.globant.presentation.viewmodel.SearchScreenViewModel(repository, mockContext)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loadMovies when internet is available`() = runTest {

        setup()

        `when`(repository.getPopularMovies()).thenReturn(movieList)

        viewModel.filtered.observeForever { }

        viewModel.loadMovies()

        advanceUntilIdle()

        assertEquals(movieList, viewModel.filtered.value)

    }

    @Test
    fun `test loadMovies when no internet is available`() = runTest {

        setupNoConnection()

        //`when`(repository.getLocalMovies()).thenReturn(movieList)

        viewModel.filtered.observeForever { }

        assertEquals(movieList, viewModel.filtered.value)

        verify(repository).getLocalMovies()
    }

    @Test
    fun `test filterMovies with query`() = runTest {
        setup()

        `when`(repository.getPopularMovies()).thenReturn(movieList)

        val filteredObserver = mock(Observer::class.java) as Observer<List<com.globant.domain.model.Movie>>
        viewModel.filtered.observeForever(filteredObserver)

        viewModel.loadMovies()

        viewModel.filterMovies("Godfather")

        advanceUntilIdle()

        val filteredMovies = viewModel.filtered.value
        assertNotNull(filteredMovies)
        assertEquals(1, filteredMovies?.size)
        assertTrue(filteredMovies?.any { it.name.contains("Godfather", ignoreCase = true) } == true)
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
