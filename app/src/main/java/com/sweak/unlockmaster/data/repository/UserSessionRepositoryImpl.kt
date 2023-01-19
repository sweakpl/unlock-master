package com.sweak.unlockmaster.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserSessionRepositoryImpl(private val context: Context) : UserSessionRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataStore")

    override suspend fun setIntroductionFinished() {
        context.dataStore.edit { preferences ->
            preferences[IS_INTRODUCTION_FINISHED] = true
        }
    }

    override suspend fun isIntroductionFinished(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[IS_INTRODUCTION_FINISHED] ?: false
        }.first()

    companion object {
        val IS_INTRODUCTION_FINISHED = booleanPreferencesKey("isIntroductionFinished")
    }
}