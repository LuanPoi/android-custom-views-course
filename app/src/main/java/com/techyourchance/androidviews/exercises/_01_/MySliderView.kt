package com.techyourchance.androidviews.exercises._01_

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import android.view.MotionEvent
import com.techyourchance.androidviews.CustomViewScaffold
import com.techyourchance.androidviews.exercises._03_.SliderChangeListener
import com.techyourchance.androidviews.solutions._04_.SolutionExercise4View
import com.techyourchance.androidviews.utils.Point
import kotlin.math.sqrt


class MySliderView : CustomViewScaffold {
    var sliderChangeListener: SliderChangeListener? = null
    var sliderValue: Float = 0.5f

    private val paint: Paint = Paint()

    private var lineStartPoint: Point = Point()
    private var lineStopPoint: Point = Point()

    private var circleCenterPoint: Point = Point()

    private var isBeingDragged: Boolean = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onSaveInstanceState(): Parcelable {
        val superSavedState = super.onSaveInstanceState()
        return MySliderView.MySavedState(superSavedState, this.sliderValue)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is MySliderView.MySavedState) {
            super.onRestoreInstanceState(state.superSavedState)
            this.sliderValue = state.sliderValue
            this.circleCenterPoint.x = ((this.lineStopPoint.x * this.sliderValue) + this.lineStartPoint.x)
        } else {
            super.onRestoreInstanceState(state)
        }

    }

    private fun calculateSliderValue(start: Point, stop: Point, circle: Point): Float {
        val sliderRangeValue = stop.x - start.x
        val thumbValue = circle.x - start.x
        val result = thumbValue / sliderRangeValue
        return result
    }

    private fun pointIsOverThumb(touchPoint: Point, circleCenter: Point, circleRadius: Float): Boolean {
        val dx = touchPoint.x - circleCenter.x
        val dy = touchPoint.y - circleCenter.y
        val distance = sqrt(dx*dx + dy*dy)
        return if(distance <= circleRadius) true else false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) return super.onTouchEvent(event)

        if(
            event.action == ACTION.DOWN.ordinal
            && pointIsOverThumb(Point(event.x, event.y), this.circleCenterPoint, dpToPx(CIRCLE_RADIUS_DP))
        ){
            this.isBeingDragged = true
        } else if(event.action == ACTION.MOVE.ordinal && this.isBeingDragged){
            if(event.x > this.lineStopPoint.x){
                this.circleCenterPoint.x = this.lineStopPoint.x
            } else if (event.x < this.lineStartPoint.x){
                this.circleCenterPoint.x = this.lineStartPoint.x
            } else {
                this.circleCenterPoint.x = event.x
            }

            this.sliderValue = calculateSliderValue(this.lineStartPoint, this.lineStopPoint, this.circleCenterPoint)
            this.sliderChangeListener?.onValueChanged(this.sliderValue)

//            this.circleCenterPoint.y = event.y
            invalidate()
        } else {
            this.isBeingDragged = false
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.lineStartPoint.x = dpToPx(MARGIN_HORIZONTAL_DP)
        this.lineStartPoint.y = (h / 2f)
        this.lineStopPoint.x = w - dpToPx(MARGIN_HORIZONTAL_DP)
        this.lineStopPoint.y = this.lineStartPoint.y

        this.circleCenterPoint.y = h / 2f
        this.circleCenterPoint.x = ((this.lineStopPoint.x * this.sliderValue) + this.lineStartPoint.x)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setBackgroundColor(Color.parseColor("#FFD9D9D9"))

        this.paint.apply {
            color = Color.parseColor("#FFB9B9B9")
            strokeWidth = dpToPx(LINE_HEIGHT_DP)
            style = Paint.Style.STROKE
        }
        canvas.drawLine(this.lineStartPoint.x, this.lineStartPoint.y, this.lineStopPoint.x, this.lineStopPoint.y, this.paint)

        this.paint.apply {
            color = Color.parseColor("#FF476fd6")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(this.circleCenterPoint.x, this.circleCenterPoint.y, dpToPx(CIRCLE_RADIUS_DP), this.paint)
    }

    private companion object {
        private const val MARGIN_HORIZONTAL_DP = 24f
        private const val LINE_HEIGHT_DP = 4f
        private const val CIRCLE_RADIUS_DP = 24f
    }

    private enum class ACTION {
        DOWN,
        UP,
        MOVE,
        CANCEL
    }

    private class MySavedState: BaseSavedState {

        val superSavedState: Parcelable?
        val sliderValue: Float

        constructor(superSavedState: Parcelable?, sliderValue: Float): super(superSavedState) {
            this.superSavedState = superSavedState
            this.sliderValue = sliderValue
        }

        constructor(parcel: Parcel) : super(parcel) {
            this.superSavedState = parcel.readParcelable(null)
            this.sliderValue = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(superSavedState, flags)
            out.writeFloat(sliderValue)
        }

        companion object CREATOR : Parcelable.Creator<MySavedState> {
            override fun createFromParcel(parcel: Parcel): MySavedState {
                return MySavedState(parcel)
            }

            override fun newArray(size: Int): Array<MySavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}