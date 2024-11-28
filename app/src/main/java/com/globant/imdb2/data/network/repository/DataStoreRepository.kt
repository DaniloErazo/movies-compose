package com.globant.imdb2.data.network.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val USERNAME_KEY = stringPreferencesKey("username")
    private val LOGGED_KEY = booleanPreferencesKey("is_logged_in")

    suspend fun logInUser(username:String){
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[LOGGED_KEY] = true
        }
    }

    suspend fun logOutUser() {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = ""
            preferences[LOGGED_KEY] = false
        }
    }

    suspend fun getLoggedUser(): String{
        return dataStore.data
            .map { preferences ->
                preferences[USERNAME_KEY] ?: ""
            }.first()
    }

    fun isLogged(): Boolean = runBlocking {
        dataStore.data
            .map { preferences ->
                preferences[LOGGED_KEY] ?: false
            }.first()
    }
}