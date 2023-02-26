package com.sweak.unlockmaster.presentation.common.util

import android.graphics.Canvas
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.Range
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.abs
import kotlin.math.ceil

class RoundedBarChartRenderer(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer = RectF()

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans: Transformer = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)
        val drawBorder = dataSet.barBorderWidth > 0f
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor
            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            var i = 0
            val count = ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt()
                .coerceAtMost(dataSet.entryCount)

            while (i < count) {
                val e = dataSet.getEntryForIndex(i)
                x = e.x
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) {
                    break
                }

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                c.drawRoundRect(mBarShadowRectBuffer, 10f, 10f, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        if (mBarBuffers == null) {
            return
        }

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)
        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        var j = 0

        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) {
                break
            }

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
            }

            val rounding = abs(buffer.buffer[j] - buffer.buffer[j + 2]) / 8

            c.drawRoundRect(
                buffer.buffer[j],
                buffer.buffer[j + 1],
                buffer.buffer[j + 2],
                buffer.buffer[j + 3],
                rounding,
                rounding,
                mRenderPaint
            )

            if (drawBorder) {
                val borderRounding = abs(buffer.buffer[j] - buffer.buffer[j + 2]) / 8
                c.drawRoundRect(
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    buffer.buffer[j + 2],
                    buffer.buffer[j + 3],
                    borderRounding,
                    borderRounding,
                    mBarBorderPaint
                )
            }

            j += 4
        }
    }

    override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
        val barData = mChart.barData

        for (high in indices) {
            val set = barData.getDataSetByIndex(high.dataSetIndex)

            if (set == null || !set.isHighlightEnabled) {
                continue
            }

            val e = set.getEntryForXValue(high.x, high.y)

            if (!isInBoundsX(e, set)) {
                continue
            }

            val trans: Transformer = mChart.getTransformer(set.axisDependency)
            mHighlightPaint.color = set.highLightColor
            mHighlightPaint.alpha = set.highLightAlpha
            val isStack = high.stackIndex >= 0 && e.isStacked
            val y1: Float
            val y2: Float

            if (isStack) {
                if (mChart.isHighlightFullBarEnabled) {
                    y1 = e.positiveSum
                    y2 = -e.negativeSum
                } else {
                    val range: Range = e.ranges[high.stackIndex]
                    y1 = range.from
                    y2 = range.to
                }
            } else {
                y1 = e.y
                y2 = 0f
            }

            prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
            setHighlightDrawPos(high, mBarRect)
            val rounding = abs(mBarRect.left - mBarRect.right) / 8
            c.drawRoundRect(mBarRect, rounding, rounding, mHighlightPaint)
        }
    }
}