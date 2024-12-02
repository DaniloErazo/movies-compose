package com.globant.imdb2
import com.globant.presentation.utils.CryptoUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CryptoUtilsTest {

    private val cryptoUtils = CryptoUtils()

    @Test
    fun testHashPassword() {
        val password = "mySecurePassword"

        // Generate a salt
        val salt = cryptoUtils.generateSalt()

        // Hash the password with the salt
        val hashedPassword = cryptoUtils.hashPassword(password, salt)

        // Verify that hashing the same password with the same salt produces the same result
        val hashedPassword2 = cryptoUtils.hashPassword(password, salt)
        assertTrue(hashedPassword == hashedPassword2)

        // Verify that hashing a different password produces a different result
        val differentPassword = "anotherPassword"
        val hashedDifferentPassword = cryptoUtils.hashPassword(differentPassword, salt)
        assertFalse(hashedPassword == hashedDifferentPassword)

        // Verify that using a different salt produces a different result
        val differentSalt = cryptoUtils.generateSalt()
        val hashedWithDifferentSalt = cryptoUtils.hashPassword(password, differentSalt)
        assertFalse(hashedPassword == hashedWithDifferentSalt)
    }

    @Test
    fun testCheckPassword() {
        val password = "mySecurePassword"

        // Generate a salt
        val salt = cryptoUtils.generateSalt()

        // Hash the password with the salt
        val hashedPassword = cryptoUtils.hashPassword(password, salt)

        // Check that the password verification works correctly
        assertTrue(cryptoUtils.checkPassword(password, hashedPassword, salt))

        // Test with an incorrect password
        val incorrectPassword = "wrongPassword"
        assertFalse(cryptoUtils.checkPassword(incorrectPassword, hashedPassword, salt))

        // Test with a different salt (should fail)
        val differentSalt = cryptoUtils.generateSalt()
        assertFalse(cryptoUtils.checkPassword(password, hashedPassword, differentSalt))
    }
}

