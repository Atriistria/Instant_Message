package com.example.instant_message.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NoScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr){

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return  e?.action == MotionEvent.ACTION_MOVE
    }
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return  e?.action != MotionEvent.ACTION_MOVE
    }
}