package com.techyourchance.androidviews.exercises._01_

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.techyourchance.androidviews.CustomViewScaffold

class MySliderView : CustomViewScaffold {

    private val paint: Paint = Paint()

    private var lineStartX = 0f
    private var lineStartY = 0f
    private var lineStopX = 0f
    private var lineStopY = 0f

    private var circlePositionX = 0f
    private var circlePositionY = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        lineStartX = dpToPx(MARGIN_HORIZONTAL_DP)
        lineStartY = (h / 2f)
        lineStopX = w - dpToPx(MARGIN_HORIZONTAL_DP)
        lineStopY = lineStartY

        circlePositionY = h / 2f
        circlePositionX = w / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setBackgroundColor(Color.parseColor("#FFD9D9D9"))

        paint.apply {
            color = Color.parseColor("#FFB9B9B9")
            strokeWidth = dpToPx(LINE_HEIGHT_DP)
            style = Paint.Style.STROKE
        }
        canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, this.paint)

        paint.apply {
            color = Color.parseColor("#FF476fd6")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(circlePositionX, circlePositionY, dpToPx(CIRCLE_RADIUS_DP), paint)
    }

    companion object {
        private const val MARGIN_HORIZONTAL_DP = 24f
        private const val LINE_HEIGHT_DP = 4f
        private const val CIRCLE_RADIUS_DP = 12f
    }
}