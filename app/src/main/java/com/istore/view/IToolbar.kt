package com.istore.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.istore.R

class IToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var onBackClickListener: View.OnClickListener? = null
        set(value) {
            field = value
            findViewById<ImageView>(R.id.iv_back).setOnClickListener(onBackClickListener)
        }

    init {
        inflate(context, R.layout.view_toolbar, this)
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.IToolbar)
            val title = attributes.getString(R.styleable.IToolbar_i_title)
            if (title != null && title.isNotEmpty())
                findViewById<TextView>(R.id.tv_toolbar_title).text = title
            attributes.recycle()
        }
    }
}