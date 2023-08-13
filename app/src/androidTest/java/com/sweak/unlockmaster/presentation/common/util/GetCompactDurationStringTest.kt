package com.sweak.unlockmaster.presentation.common.util

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class GetCompactDurationStringTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun secondsPrecision_1h15min45s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "1h 15min 45s",
                getCompactDurationString(
                    Duration(4545000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_0h55min32s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "55min 32s",
                getCompactDurationString(
                    Duration(3332000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_0h0min56s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "56s",
                getCompactDurationString(
                    Duration(56000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_2h0min27s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "2h 0min 27s",
                getCompactDurationString(
                    Duration(7227000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_3h12min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "3h 12min",
                getCompactDurationString(
                    Duration(11520000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_4h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "4h",
                getCompactDurationString(
                    Duration(14400000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_0h30min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "30min",
                getCompactDurationString(
                    Duration(1800000, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun secondsPrecision_0h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0s",
                getCompactDurationString(
                    Duration(0, Duration.DisplayPrecision.SECONDS)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_1h15min45s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "1h 15min",
                getCompactDurationString(
                    Duration(4545000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_0h55min32s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "55min",
                getCompactDurationString(
                    Duration(3332000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_0h0min56s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0min",
                getCompactDurationString(
                    Duration(56000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_2h0min27s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "2h",
                getCompactDurationString(
                    Duration(7227000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_4h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "4h",
                getCompactDurationString(
                    Duration(14400000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_3h12min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "3h 12min",
                getCompactDurationString(
                    Duration(11520000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_0h30min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "30min",
                getCompactDurationString(
                    Duration(1800000, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun minutesPrecision_0h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0min",
                getCompactDurationString(
                    Duration(0, Duration.DisplayPrecision.MINUTES)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_1h15min45s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "1h",
                getCompactDurationString(
                    Duration(4545000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_0h55min32s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0h",
                getCompactDurationString(
                    Duration(3332000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_0h0min56s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0h",
                getCompactDurationString(
                    Duration(56000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_2h0min27s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "2h",
                getCompactDurationString(
                    Duration(7227000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_4h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "4h",
                getCompactDurationString(
                    Duration(14400000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_3h12min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "3h",
                getCompactDurationString(
                    Duration(11520000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_0h30min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0h",
                getCompactDurationString(
                    Duration(1800000, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }

    @Test
    fun hoursPrecision_0h0min0s() {
        composeTestRule.setContent {
            Assert.assertEquals(
                "0h",
                getCompactDurationString(
                    Duration(0, Duration.DisplayPrecision.HOURS)
                )
            )
        }
    }
}