package com.globant.imdb2
import com.globant.imdb2.utils.CryptoUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CryptoUtilsTest {

    @Test
    fun testHashPassword() {
        val password = "mySecurePassword"

        // Generate a salt
        val salt = CryptoUtils.generateSalt()

        // Hash the password with the salt
        val hashedPassword = CryptoUtils.hashPassword(password, salt)

        // Verify that hashing the same password with the same salt produces the same result
        val hashedPassword2 = CryptoUtils.hashPassword(password, salt)
        assertTrue(hashedPassword == hashedPassword2)

        // Verify that hashing a different password produces a different result
        val differentPassword = "anotherPassword"
        val hashedDifferentPassword = CryptoUtils.hashPassword(differentPassword, salt)
        assertFalse(hashedPassword == hashedDifferentPassword)

        // Verify that using a different salt produces a different result
        val differentSalt = CryptoUtils.generateSalt()
        val hashedWithDifferentSalt = CryptoUtils.hashPassword(password, differentSalt)
        assertFalse(hashedPassword == hashedWithDifferentSalt)
    }

    @Test
    fun testCheckPassword() {
        val password = "mySecurePassword"

        // Generate a salt
        val salt = CryptoUtils.generateSalt()

        // Hash the password with the salt
        val hashedPassword = CryptoUtils.hashPassword(password, salt)

        // Check that the password verification works correctly
        assertTrue(CryptoUtils.checkPassword(password, hashedPassword, salt))

        // Test with an incorrect password
        val incorrectPassword = "wrongPassword"
        assertFalse(CryptoUtils.checkPassword(incorrectPassword, hashedPassword, salt))

        // Test with a different salt (should fail)
        val differentSalt = CryptoUtils.generateSalt()
        assertFalse(CryptoUtils.checkPassword(password, hashedPassword, differentSalt))
    }
}

