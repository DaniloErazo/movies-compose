package com.globant.imdb2

import com.globant.imdb2.database.dao.MovieDao
import com.globant.imdb2.database.entities.Movie
import com.globant.imdb2.entity.Genre
import com.globant.imdb2.entity.MovieDTO
import com.globant.imdb2.entity.MovieDetail
import com.globant.imdb2.entity.MovieResponse
import com.globant.imdb2.repository.MovieRepository
import com.globant.imdb2.services.MovieServices
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import retrofit2.Response
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

    private val fakeMovieResponse = MovieResponse(listOf())

    private val genres = listOf(
        Genre(name = "Drama"),
        Genre(name = "Crime")
    )

    private val fakeMovieDetail = MovieDetail(
        identifier = "1",
        movieName = "The Godfather",
        movieImage = "/path/to/poster1.jpg",
        score = 9.2,
        backImage = "/path/to/backdrop1.jpg",
        description = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
        genres = genres
    )

    private val mockMovieListDTO = listOf(
        MovieDTO(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        MovieDTO(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        MovieDTO(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
    )

    private val mockMovieList = listOf(
        Movie(identifier = "1", movieName = "The Shawshank Redemption", backImage = "/path1", movieImage = "/poster1", movieDate = "1994-09-23", score = 9.3),
        Movie(identifier = "2", movieName = "The Godfather", backImage = "/path2", movieImage = "/poster2", movieDate = "1972-03-24", score = 9.2),
        Movie(identifier = "3", movieName = "The Dark Knight", backImage = "/path3", movieImage = "/poster3", movieDate = "2008-07-18", score = 9.0)
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
        assertEquals(result, fakeMovieResponse)
        verify(mockMovieServices).getTopMovies()
    }

    @Test
    fun `test getMovieById returns correct movie detail`() = runTest{
        `when`(mockMovieServices.getMovieById("1")).thenReturn(fakeMovieDetail)

        val result = movieRepository.getMovieById("1")

        assertNotNull(result)
        assertEquals(result, fakeMovieDetail)
        verify(mockMovieServices).getMovieById("1")
    }

    @Test
    fun `test getLocalMovies returns correct local movies`() = runTest {
        `when`(mockMovieDao.getAllMovies()).thenReturn(mockMovieList)
        val result = movieRepository.getLocalMovies()

        assertNotNull(result)
        assertEquals(result, mockMovieList)
        verify(mockMovieDao).getAllMovies()
    }

    @Test
    fun `test saveLocalMovies saves movies correctly`() = runTest {

        `when`(mockMovieDao.saveAll(mockMovieList)).thenReturn(Unit)
        `when`(mockMovieDao.deleteAll()).thenReturn(Unit)

        movieRepository.saveLocalMovies(mockMovieList)

        verify(mockMovieDao).deleteAll()
        verify(mockMovieDao).saveAll(mockMovieList)
    }
}
