package com.sweak.unlockmaster.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sweak.unlockmaster.domain.DEFAULT_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT
import com.sweak.unlockmaster.domain.DEFAULT_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE
import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState
import com.sweak.unlockmaster.domain.model.UiThemeMode
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserSessionRepositoryImpl(private val context: Context) : UserSessionRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "dataStore",
        corruptionHandler = ReplaceFileCorruptionHandler {
            preferencesOf(
                IS_INTRODUCTION_FINISHED to true
            )
        }
    )

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

    override suspend fun setDailyWrapUpNotificationsTimeInMinutesAfterMidnight(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT] = minutes
        }
    }

    override suspend fun getDailyWrapUpNotificationsTimeInMinutesAfterMidnight(): Int =
        context.dataStore.data.map { preferences ->
            preferences[DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT]
                ?: DEFAULT_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT
        }.first()

    override suspend fun setUnlockMasterServiceProperlyClosed(wasProperlyClosed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WAS_UNLOCK_MASTER_SERVICE_PROPERLY_CLOSED] = wasProperlyClosed
        }
    }

    override suspend fun wasUnlockMasterServiceProperlyClosed(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[WAS_UNLOCK_MASTER_SERVICE_PROPERLY_CLOSED] ?: true
        }.first()

    override suspend fun setShouldShowUnlockMasterBlockedWarning(shouldShowWarning: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOULD_SHOW_UNLOCK_MASTER_BLOCKED_WARNING] = shouldShowWarning
        }
    }

    override suspend fun shouldShowUnlockMasterBlockedWarning(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[SHOULD_SHOW_UNLOCK_MASTER_BLOCKED_WARNING] ?: false
        }.first()

    override suspend fun setUiThemeMode(uiThemeMode: UiThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[UI_THEME_MODE] = uiThemeMode.name
        }
    }

    override fun getUiThemeModeFlow(): Flow<UiThemeMode> =
        context.dataStore.data.map { preferences ->
            when (preferences[UI_THEME_MODE]) {
                "LIGHT" -> UiThemeMode.LIGHT
                "DARK" -> UiThemeMode.DARK
                "SYSTEM" -> UiThemeMode.SYSTEM
                else -> UiThemeMode.SYSTEM
            }
        }

    override suspend fun setOverUnlockLimitMobilizingNotificationsEnabled(areEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ARE_OVER_UNLOCK_LIMIT_MOBILIZING_NOTIFICATIONS_ENABLED] = areEnabled
        }
    }

    override suspend fun areOverUnlockLimitMobilizingNotificationsEnabled(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[ARE_OVER_UNLOCK_LIMIT_MOBILIZING_NOTIFICATIONS_ENABLED] ?: true
        }.first()

    override suspend fun setScreenTimeLimitEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SCREEN_TIME_LIMIT_ENABLED] = isEnabled
        }
    }

    override suspend fun isScreenTimeLimitEnabled(): Boolean =
        context.dataStore.data.map { preferences ->
            preferences[IS_SCREEN_TIME_LIMIT_ENABLED] ?: true
        }.first()

    override suspend fun setScreenTimeLimitWarningState(state: ScreenTimeLimitWarningState) {
        context.dataStore.edit { preferences ->
            preferences[SCREEN_TIME_LIMIT_WARNING_STATE] = state.ordinal
        }
    }

    override suspend fun getScreenTimeLimitWarningState(): ScreenTimeLimitWarningState {
        val screenTimeLimitWarningStateOrdinal = context.dataStore.data.map { preferences ->
            preferences[SCREEN_TIME_LIMIT_WARNING_STATE]
                ?: ScreenTimeLimitWarningState.NO_WARNINGS_FIRED.ordinal
        }.first()

        return ScreenTimeLimitWarningState.entries[screenTimeLimitWarningStateOrdinal]
    }

    companion object {
        val IS_INTRODUCTION_FINISHED = booleanPreferencesKey("isIntroductionFinished")
        val IS_UNLOCK_COUNTER_PAUSED = booleanPreferencesKey("isUnlockCounterPaused")
        val MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE =
            intPreferencesKey("mobilizingNotificationsFrequencyPercentage")
        val DAILY_WRAP_UP_NOTIFICATIONS_TIME_IN_MINUTES_PAST_MIDNIGHT =
            intPreferencesKey("dailyWrapUpNotificationsTimeInMinutesPastMidnight")
        val WAS_UNLOCK_MASTER_SERVICE_PROPERLY_CLOSED =
            booleanPreferencesKey("wasUnlockMasterServiceProperlyClosed")
        val SHOULD_SHOW_UNLOCK_MASTER_BLOCKED_WARNING =
            booleanPreferencesKey("shouldShowUnlockMasterBlockedWarning")
        val UI_THEME_MODE = stringPreferencesKey("uiThemeMode")
        val ARE_OVER_UNLOCK_LIMIT_MOBILIZING_NOTIFICATIONS_ENABLED =
            booleanPreferencesKey("areOverUnlockLimitMobilizingNotificationsEnabled")
        val IS_SCREEN_TIME_LIMIT_ENABLED = booleanPreferencesKey("isScreenTimeLimitEnabled")
        val SCREEN_TIME_LIMIT_WARNING_STATE = intPreferencesKey("screenTimeLimitWarningState")
    }
}