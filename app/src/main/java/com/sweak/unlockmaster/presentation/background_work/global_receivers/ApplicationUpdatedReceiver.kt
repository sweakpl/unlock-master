package com.sweak.unlockmaster.presentation.background_work.global_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ApplicationUpdatedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            runBlocking {
                userSessionRepository.setShouldShowUnlockMasterBlockedWarning(false)
            }
        }
    }
}