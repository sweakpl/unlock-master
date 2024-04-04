package com.sweak.unlockmaster.presentation.daily_wrap_up

import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenOnEventsDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenTimeDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenUnlocksDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpUnlockLimitDetailsData

data class DailyWrapUpScreenState(
    val isInitializing: Boolean = true,

    val screenUnlocksPreviewData: DailyWrapUpCriterionPreviewType.ScreenUnlocks? = null,
    val screenTimePreviewData: DailyWrapUpCriterionPreviewType.ScreenTime? = null,
    val unlockLimitPreviewData: DailyWrapUpCriterionPreviewType.UnlockLimit? = null,
    val screenTimeLimitPreviewData: DailyWrapUpCriterionPreviewType.ScreenTimeLimit? = null,
    val screenOnEventsPreviewData: DailyWrapUpCriterionPreviewType.ScreenOnEvents? = null,

    val screenUnlocksDetailsData: DailyWrapUpScreenUnlocksDetailsData? = null,
    val screenTimeDetailsData: DailyWrapUpScreenTimeDetailsData? = null,
    val unlockLimitDetailsData: DailyWrapUpUnlockLimitDetailsData? = null,
    val screenOnEventsDetailsData: DailyWrapUpScreenOnEventsDetailsData? = null,

    val isScreenOnEventsInformationDialogVisible: Boolean = false
)
