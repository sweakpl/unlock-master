package com.sweak.unlockmaster.presentation.main.screen_time.components

import android.graphics.Color
import android.text.format.DateFormat
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sweak.unlockmaster.R
import kotlin.math.roundToInt

@Composable
fun DailyScreenTimeChart(
    screenTimeMinutesPerHourEntries: List<Entry>,
    modifier: Modifier = Modifier
) {
    val lineArgbColor: Int = MaterialTheme.colorScheme.secondary.toArgb()

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                setScaleEnabled(false)
                description.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                isHighlightPerTapEnabled = false
                isHighlightPerDragEnabled = false

                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(false)
                    axisMinimum = -10f
                    axisMaximum = 70f
                }

                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    labelCount = 24
                    position = XAxis.XAxisPosition.BOTTOM
                    textSize = 10f
                    textColor = Color.BLACK
                    typeface = ResourcesCompat.getFont(context, R.font.amiko_regular)
                    valueFormatter = object : IndexAxisValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase): String =
                            value.roundToInt().run {
                                val is24HoursFormat = DateFormat.is24HourFormat(context)

                                when (this) {
                                    6 -> if (is24HoursFormat) "6:00" else "6:00 AM"
                                    12 -> if (is24HoursFormat) "12:00" else "12:00 PM"
                                    18 -> if (is24HoursFormat) "18:00" else "6:00 PM"
                                    else -> ""
                                }
                            }
                    }
                }
            }
        },
        update = {
            val lineData = LineData(
                LineDataSet(screenTimeMinutesPerHourEntries, "screenTimeMinutesPerHourEntries")
                    .apply {
                        lineWidth = 8f
                        color = lineArgbColor
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawCircles(false)
                        setDrawValues(false)
                        setDrawHighlightIndicators(false)
                    }
            )

            it.data = lineData
            it.invalidate()
        },
        modifier = modifier
    )
}