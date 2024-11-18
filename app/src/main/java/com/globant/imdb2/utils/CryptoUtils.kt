package com.globant.imdb2.utils

import java.util.Base64 // Import Java Base64 instead
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils {

    private val ALGORITHM = "AES"
    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private val IV_SIZE = 16
    private val SECRET_KEY = "tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ="

    private val ITERATION_COUNT = 65536
    private val KEY_LENGTH = 256
    private val SALT_SIZE = 16


    fun generateIv(): ByteArray {
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)
        return iv
    }

    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_SIZE)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun getSecretKey(): SecretKeySpec {
        return SecretKeySpec(SECRET_KEY.toByteArray(Charsets.UTF_8), ALGORITHM)
    }

    fun hashPassword(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(hash)
    }

    fun checkPassword(enteredPassword: String, storedHash: String, storedSalt: ByteArray): Boolean {
        val hashedEnteredPassword = hashPassword(enteredPassword, storedSalt)
        return hashedEnteredPassword == storedHash
    }

    fun encrypt(password: String, iv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        return Base64.getEncoder().encodeToString(iv + encryptedBytes)
    }

    fun decrypt(encryptedData: String): String {
        val decodedData = Base64.getDecoder().decode(encryptedData)
        val iv = decodedData.sliceArray(0 until IV_SIZE) // Extract IV
        val encryptedBytes = decodedData.sliceArray(IV_SIZE until decodedData.size) // Extract encrypted data

        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return Base64.getEncoder().encodeToString(decryptedBytes)
    //return String(decryptedBytes)
    }


}