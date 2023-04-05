package com.sweak.unlockmaster.presentation.main.home.components

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF
import com.sweak.unlockmaster.R

class SemiTransparentBlueRectangleMarkerView(context: Context) :
    MarkerView(context, R.layout.semi_transparent_blue_rect_marker_view) {

    override fun getOffset(): MPPointF = MPPointF(-(width / 2.0f), height.toFloat())
}