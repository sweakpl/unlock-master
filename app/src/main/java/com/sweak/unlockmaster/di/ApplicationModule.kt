package com.sweak.unlockmaster.di

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNotificationManager(app: Application): NotificationManagerCompat =
        NotificationManagerCompat.from(app)
}