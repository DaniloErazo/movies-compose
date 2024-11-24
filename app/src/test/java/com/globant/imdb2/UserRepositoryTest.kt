package com.globant.imdb2

import com.globant.imdb2.data.database.dao.UserDao
import com.globant.imdb2.data.database.entities.UserDB
import com.globant.imdb2.data.network.repository.UserRepository
import com.globant.imdb2.domain.model.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    private val testEmail = "test@example.com"
    private val testUser = User(id = 1, email = testEmail, name = "Test User", password = "hashedPassword", salt = "salt", color = 123456)
    private val testUserDB = UserDB(id = 1, email = testEmail, name = "Test User", password = "hashedPassword", salt = "salt", color = 123456)


    @Before
    fun setup() {

        userDao = mock(UserDao::class.java)

        userRepository = UserRepository(userDao)
    }

    @Test
    fun `test getUserByEmail returns user when found`(): Unit = runBlocking {
        `when`(userDao.getUserByEmail(testEmail)).thenReturn(testUserDB)

        val result = userRepository.getUserByEmail(testEmail)

        assertNotNull(result)
        assertEquals(testEmail, result.email)
        verify(userDao).getUserByEmail(testEmail)
    }

    @Test
    fun `test addUser adds user to the database`() = runBlocking {
        userRepository.addUser(testUser)

        verify(userDao).addUser(testUserDB)
    }
}
