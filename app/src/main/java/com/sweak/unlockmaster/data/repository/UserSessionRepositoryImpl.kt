package com.sweak.unlockmaster.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sweak.unlockmaster.domain.DEFAULT_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT
import com.sweak.unlockmaster.domain.DEFAULT_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE
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

    override suspend fun setUnlockCounterPaused(isPaused: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_UNLOCK_COUNTER_PAUSED] = isPaused
        }
    }

    override suspend fun isUnlockCounterPaused(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[IS_UNLOCK_COUNTER_PAUSED] ?: false
        }.first()

    override suspend fun setMobilizingNotificationsFrequencyPercentage(percentage: Int) {
        context.dataStore.edit { preferences ->
            preferences[MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE] = percentage
        }
    }

    override suspend fun getMobilizingNotificationsFrequencyPercentage(): Int =
        context.dataStore.data.map { preferences ->
            preferences[MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE]
                ?: DEFAULT_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE
        }.first()

    override suspend fun setDailyWrapUpsNotificationsTimeInMinutesAfterMidnight(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT] = minutes
        }
    }

    override suspend fun getDailyWrapUpsNotificationsTimeInMinutesAfterMidnight(): Int =
        context.dataStore.data.map { preferences ->
            preferences[DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT]
                ?: DEFAULT_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT
        }.first()

    companion object {
        val IS_INTRODUCTION_FINISHED = booleanPreferencesKey("isIntroductionFinished")
        val IS_UNLOCK_COUNTER_PAUSED = booleanPreferencesKey("isUnlockCounterPaused")
        val MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE =
            intPreferencesKey("mobilizingNotificationsFrequencyPercentage")
        val DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT =
            intPreferencesKey("dailyWrapUpNotificationsTimeInMinutesPastMidnight")
    }
}