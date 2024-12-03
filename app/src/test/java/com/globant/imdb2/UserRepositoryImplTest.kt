package com.globant.imdb2

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class UserRepositoryImplTest {

    private lateinit var userDao: com.globant.data.database.dao.UserDao
    private lateinit var userRepository: com.globant.data.network.repository.UserRepositoryImpl

    private val testEmail = "test@example.com"
    private val testUser = com.globant.domain.model.User(
        id = 1,
        email = testEmail,
        name = "Test User",
        password = "hashedPassword",
        salt = "salt",
        color = 123456
    )
    private val testUserDB = com.globant.data.database.entities.UserDB(
        id = 1,
        email = testEmail,
        name = "Test User",
        password = "hashedPassword",
        salt = "salt",
        color = 123456
    )


    @Before
    fun setup() {

        userDao = mock(com.globant.data.database.dao.UserDao::class.java)

        userRepository = com.globant.data.network.repository.UserRepositoryImpl(userDao)
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
