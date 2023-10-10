package com.sweak.unlockmaster.presentation.main.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.presentation.common.util.RoundedBarChartRenderer
import com.sweak.unlockmaster.presentation.common.util.getShortDayString
import java.time.ZonedDateTime

@Composable
fun WeeklyUnlocksChart(
    lastWeekUnlockEventCountsEntries: List<BarEntry>,
    modifier: Modifier = Modifier
) {
    val barArgbColor: Int = MaterialTheme.colorScheme.secondary.toArgb()
    val textArgbColor = MaterialTheme.colorScheme.onBackground.toArgb()

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                renderer = RoundedBarChartRenderer(this, this.animator, this.viewPortHandler)
                setScaleEnabled(false)
                description.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                isHighlightPerTapEnabled = false
                isHighlightPerDragEnabled = false
                extraBottomOffset = 16f
                extraTopOffset = 24f

                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(false)
                    axisMinimum = 0f
                }

                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    textSize = 10f
                    axisMinimum = -0.5f
                    axisMaximum = 6.5f
                    textColor = textArgbColor
                    typeface = ResourcesCompat.getFont(context, R.font.amiko_regular)
                    valueFormatter = object : IndexAxisValueFormatter() {
                        override fun getAxisLabel(value: Float, axis: AxisBase): String =
                            getShortDayString(
                                ZonedDateTime.now()
                                    .plusDays(value.toLong() + 1)
                                    .toTimeInMillis()
                            )
                    }
                }
            }
        },
        update = {
            val barData =
                BarData(
                    BarDataSet(
                        lastWeekUnlockEventCountsEntries,
                        "lastWeekUnlockEventCounts"
                    ).apply {
                        color = barArgbColor
                    }
                ).apply {
                    barWidth = 0.66f
                    setValueTextSize(10f)
                    setValueTextColor(textArgbColor)
                    setValueTypeface(ResourcesCompat.getFont(it.context, R.font.amiko_regular))
                    setValueFormatter(object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    })
                }

            it.data = barData
            it.invalidate()
        },
        modifier = modifier
    )
}