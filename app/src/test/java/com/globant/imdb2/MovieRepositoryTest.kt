package com.globant.imdb2

import com.globant.imdb2.data.database.dao.MovieDao
import com.globant.imdb2.data.database.entities.MovieDB
import com.globant.imdb2.data.network.model.GenreAPI
import com.globant.imdb2.data.network.model.MovieAPI
import com.globant.imdb2.data.network.model.MovieDetailAPI
import com.globant.imdb2.data.network.model.MovieResponse
import com.globant.imdb2.data.network.repository.MovieRepository
import com.globant.imdb2.data.network.services.MovieServices
import com.globant.imdb2.domain.model.Genre
import com.globant.imdb2.domain.model.Movie
import com.globant.imdb2.domain.model.MovieDetail
import com.globant.imdb2.presentation.model.GenreDTO
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MovieRepositoryTest {

    @Mock
    lateinit var mockMovieServices: MovieServices

    @Mock
    lateinit var mockMovieDao: MovieDao

    lateinit var movieRepository: MovieRepository

    private val fakeMovieResponse = MovieResponse(listOf(
        MovieAPI(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        MovieAPI(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        MovieAPI(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
    ))

    private val genresAPI = listOf(
        GenreAPI(name = "Drama"),
        GenreAPI(name = "Crime")
    )

    private val fakeMovieDetail = MovieDetailAPI(
        identifier = "1",
        movieName = "The Godfather",
        movieImage = "/path/to/poster1.jpg",
        score = 9.2,
        backImage = "/path/to/backdrop1.jpg",
        description = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
        genres = genresAPI
    )

    val genres = listOf(
        Genre("Drama"),
        Genre("Crime")
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

    private val mockMovieList = listOf(
        Movie(id = "1", name = "The Shawshank Redemption", originalTitle = "The Shawshank Redemption", backImage = "/path1", image = "/poster1", date = "1994-09-23", score = 9.3),
        Movie(id = "2", name = "The Godfather", originalTitle = "The Godfather", backImage = "/path2", image = "/poster2", date = "1972-03-24", score = 9.2),
        Movie(id = "3", name = "The Dark Knight", originalTitle = "The Dark Knight", backImage = "/path3", image = "/poster3", date = "2008-07-18", score = 9.0)
    )

    private val mockMovieDB = listOf(
        MovieDB(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        MovieDB(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        MovieDB(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
    )



    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)  // Initialize mocks
        movieRepository = MovieRepository(mockMovieServices, mockMovieDao)
    }

    @Test
    fun `test getPopularMovies returns correct response`() = runTest {
        `when`(mockMovieServices.getTopMovies()).thenReturn(fakeMovieResponse)

        val result = movieRepository.getPopularMovies()

        assertNotNull(result)
        assertEquals(result, mockMovieList)
        verify(mockMovieServices).getTopMovies()
    }

    @Test
    fun `test getMovieById returns correct movie detail`() = runTest {
        `when`(mockMovieServices.getMovieById("1")).thenReturn(fakeMovieDetail)

        // Act
        val result = movieRepository.getMovieById("1")

        // Assert
        assertNotNull(result)
        assertEquals(result, mockMovieDetail)
        verify(mockMovieServices).getMovieById("1")
    }

    @Test
    fun `test getLocalMovies returns correct local movies`() = runTest {
        `when`(mockMovieDao.getAllMovies()).thenReturn(mockMovieDB)
        val result = movieRepository.getLocalMovies()

        assertNotNull(result)
        assertEquals(result, mockMovieList)
        verify(mockMovieDao).getAllMovies()
    }

    @Test
    fun `test saveLocalMovies saves movies correctly`() = runTest {

        `when`(mockMovieDao.saveAll(mockMovieDB)).thenReturn(Unit)
        `when`(mockMovieDao.deleteAll()).thenReturn(Unit)

        movieRepository.saveLocalMovies(mockMovieList)

        verify(mockMovieDao).deleteAll()
        verify(mockMovieDao).saveAll(mockMovieDB)
    }
}
