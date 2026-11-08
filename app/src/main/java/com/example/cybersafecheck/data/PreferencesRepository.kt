package com.example.cybersafecheck.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

// One DataStore for the whole app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Jetpack Preferences DataStore keeps the app's settings, even after the app is closed
class PreferencesRepository(context: Context) {

    private val dataStore = context.applicationContext.dataStore

    private val rememberSearchKey = booleanPreferencesKey("remember_last_search")
    private val lastSearchKey = stringPreferencesKey("last_search")

    val rememberLastSearch: Flow<Boolean> = dataStore.data
        .map { it[rememberSearchKey] ?: false }
        .distinctUntilChanged()

    val lastSearch: Flow<String> = dataStore.data
        .map { it[lastSearchKey] ?: "" }
        .distinctUntilChanged()

    suspend fun setRememberLastSearch(remember: Boolean) {
        dataStore.edit { it[rememberSearchKey] = remember }
    }

    suspend fun setLastSearch(query: String) {
        dataStore.edit { it[lastSearchKey] = query }
    }
}

//Author: Mangesana E
//Student number: 2030630053
