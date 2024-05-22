package com.techyourchance.androidviews.exercises._01_

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techyourchance.androidviews.general.BaseFragment
import com.techyourchance.androidviews.R
import com.techyourchance.androidviews.exercises._03_.SliderChangeListener

class MySliderFragment : BaseFragment(), SliderChangeListener {
    lateinit var slider: MySliderView
    lateinit var textView: TextView

    override val screenName get() = getString(R.string.screen_name_my_slider)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.layout_my_slider, container, false).apply {
            slider = findViewById(R.id.customSlider)
            textView = findViewById(R.id.sliderValueTextView)
            slider.sliderChangeListener = this@MySliderFragment
        }
    }

    override fun onStart() {
        super.onStart()
        this.textView.text = this.slider.sliderValue.toString()
    }

    override fun onValueChanged(value: Float){
        this.textView.text = value.toString()
    }

    companion object {
        fun newInstance(): MySliderFragment {
            return MySliderFragment()
        }
    }
}