package com.sweak.unlockmaster.domain.use_case.mobilizing_notifications

import com.sweak.unlockmaster.domain.AVAILABLE_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGES
import javax.inject.Inject

class GetAvailableMobilizingNotificationsFrequencyPercentages @Inject constructor() {

    operator fun invoke(): List<Int> {
        return AVAILABLE_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGES
    }
}