package com.sweak.unlockmaster.presentation.main.statistics.components

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.common.util.RoundedBarChartRenderer
import com.sweak.unlockmaster.presentation.common.util.getShortDayString
import com.sweak.unlockmaster.presentation.main.home.components.SemiTransparentBlueRectangleMarkerView
import java.time.ZonedDateTime

@Composable
fun AllTimeUnlocksChart(
    allTimeUnlockEventCountsEntries: List<BarEntry>,
    modifier: Modifier = Modifier
) {
    var currentlyHighlightedXValue = remember { 0f }
    val barArgbColor: Int = MaterialTheme.colors.primaryVariant.toArgb()

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                renderer = RoundedBarChartRenderer(this, this.animator, this.viewPortHandler)
                setScaleEnabled(false)
                setDrawMarkers(true)
                isAutoScaleMinMaxEnabled = true
                description.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                isHighlightPerTapEnabled = true
                isHighlightPerDragEnabled = false

                val marker = SemiTransparentBlueRectangleMarkerView(context)
                marker.chartView = this
                setMarker(marker)

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
                    textColor = Color.BLACK
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

                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        if (e != null) {
                            val xValueToBeHighlighted = e.x

                            if (currentlyHighlightedXValue == xValueToBeHighlighted) {
                                return
                            }

                            currentlyHighlightedXValue = xValueToBeHighlighted

                            // TODO call highlight callback here
                        }
                    }

                    override fun onNothingSelected() {
                        highlightValue(currentlyHighlightedXValue, 0)
                    }
                })
            }
        },
        update = {
            val xMax: Float

            val barData =
                BarData(
                    BarDataSet(
                        allTimeUnlockEventCountsEntries,
                        "allTimeUnlockEventCounts"
                    ).apply {
                        color = barArgbColor
                    }
                ).apply {
                    xMax = getXMax()

                    it.xAxis.apply {
                        axisMinimum = -0.5f
                        axisMaximum = xMax + 0.5f
                    }

                    barWidth = 0.66f
                    setValueTextSize(10f)
                    setValueTextColor(Color.BLACK)
                    setValueTypeface(ResourcesCompat.getFont(it.context, R.font.amiko_regular))
                    setValueFormatter(object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    })
                }

            it.data = barData
            it.setVisibleXRange(7f, 7f)
            it.highlightValue(xMax, 0)
            it.moveViewToAnimated(xMax, 0f, YAxis.AxisDependency.LEFT, 500)
        },
        modifier = modifier
    )
}