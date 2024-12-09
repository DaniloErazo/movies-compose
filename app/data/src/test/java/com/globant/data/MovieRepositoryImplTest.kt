package com.globant.data

import com.globant.data.database.dao.MovieDao
import com.globant.data.network.repository.MovieRepositoryImpl
import com.globant.data.network.services.MovieServices
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MovieRepositoryImplTest {

    @Mock
    lateinit var mockMovieServices: MovieServices

    @Mock
    lateinit var mockMovieDao: MovieDao

    lateinit var movieRepository: MovieRepositoryImpl

    private val fakeMovieResponse = com.globant.data.network.model.MovieResponse(
        listOf(
            com.globant.data.network.model.MovieAPI(
                identifier = "1",
                movieName = "The Shawshank Redemption",
                backImage = "/path1",
                movieImage = "/poster1",
                movieDate = "1994-09-23",
                score = 9.3
            ),
            com.globant.data.network.model.MovieAPI(
                identifier = "2",
                movieName = "The Godfather",
                backImage = "/path2",
                movieImage = "/poster2",
                movieDate = "1972-03-24",
                score = 9.2
            ),
            com.globant.data.network.model.MovieAPI(
                identifier = "3",
                movieName = "The Dark Knight",
                backImage = "/path3",
                movieImage = "/poster3",
                movieDate = "2008-07-18",
                score = 9.0
            )
        )
    )

    private val genresAPI = listOf(
        com.globant.data.network.model.GenreAPI(name = "Drama"),
        com.globant.data.network.model.GenreAPI(name = "Crime")
    )

    private val fakeMovieDetail = com.globant.data.network.model.MovieDetailAPI(
        identifier = "1",
        movieName = "The Godfather",
        movieImage = "/path/to/poster1.jpg",
        score = 9.2,
        backImage = "/path/to/backdrop1.jpg",
        description = "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
        genres = genresAPI
    )

    val genres = listOf(
        com.globant.domain.model.Genre("Drama"),
        com.globant.domain.model.Genre("Crime")
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

    private val mockMovieList = listOf(
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

    private val mockMovieDB = listOf(
        com.globant.data.database.entities.MovieDB(
            identifier = "1",
            movieName = "The Shawshank Redemption",
            backImage = "/path1",
            movieImage = "/poster1",
            movieDate = "1994-09-23",
            score = 9.3
        ),
        com.globant.data.database.entities.MovieDB(
            identifier = "2",
            movieName = "The Godfather",
            backImage = "/path2",
            movieImage = "/poster2",
            movieDate = "1972-03-24",
            score = 9.2
        ),
        com.globant.data.database.entities.MovieDB(
            identifier = "3",
            movieName = "The Dark Knight",
            backImage = "/path3",
            movieImage = "/poster3",
            movieDate = "2008-07-18",
            score = 9.0
        )
    )


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        movieRepository = MovieRepositoryImpl(mockMovieServices, mockMovieDao)
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
