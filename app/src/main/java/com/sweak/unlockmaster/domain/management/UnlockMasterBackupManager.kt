package com.sweak.unlockmaster.domain.management

interface UnlockMasterBackupManager {
    suspend fun createDataBackupFile(): ByteArray
    suspend fun restoreDataFromBackupFile(backupFileBytes: ByteArray)
}