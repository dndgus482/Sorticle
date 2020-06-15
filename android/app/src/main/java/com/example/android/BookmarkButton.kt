package com.example.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class BookmarkButton(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageButton(context, attrs) {

    var checkTrueListener : (() -> Unit)? = null
    var checkFalseListener : (() -> Unit)? = null

    var checked: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        background = if (newValue) {
            checkTrueListener?.invoke()
            ResourcesCompat.getDrawable(resources, R.drawable.ic_bookmark_pink_24dp, null)
        } else {
            checkFalseListener?.invoke()
            ResourcesCompat.getDrawable(resources, R.drawable.ic_bookmark_black_24dp, null)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (event?.action == MotionEvent.ACTION_BUTTON_PRESS) {
            performClick();
        }
        return true
    }

    override fun performClick(): Boolean {
        checked = !checked

        return super.performClick()
    }
}