package com.globant.data

import com.globant.data.database.dao.UserDao
import com.globant.data.database.entities.UserDB
import com.globant.data.network.repository.UserRepositoryImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.Base64

class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepositoryImpl

    private val testEmail = "test@example.com"
    private val testName = "Test user"
    private val testSaltString = Base64.getEncoder().encodeToString("salt".toByteArray())
    private val testHashedPassword = "hashedPassword"

    private val testUserDB = UserDB(
        id = 0,
        email = testEmail,
        name = testName,
        password = testHashedPassword,
        salt = testSaltString,
        color = 0
    )


    @Before
    fun setup() {

        userDao = mock(UserDao::class.java)

        userRepository = UserRepositoryImpl(userDao)
    }

    @Test
    fun `test getUserByEmail returns user when found`() = runTest {
        `when`(userDao.getUserByEmail(testEmail)).thenReturn(testUserDB)

        val result = userRepository.getUserByEmail(testEmail)

        assertNotNull(result)
        assertEquals(testEmail, result.email)
        verify(userDao).getUserByEmail(testEmail)
    }

    @Test
    fun `test addUser adds user to the database`() = runTest {
        `when`(userDao.getUserByEmail(testEmail)).thenReturn(testUserDB)

        userRepository.addUser(testEmail, testName, testHashedPassword, testSaltString, 0)

        verify(userDao).addUser(testUserDB)
    }
}
