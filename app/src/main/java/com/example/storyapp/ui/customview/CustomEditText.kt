package com.example.storyapp.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R

class CustomEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable
        errorIcon.setBounds(0, 0, errorIcon.intrinsicWidth, errorIcon.intrinsicHeight)

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = (s?.length ?: 0)
                if (length <= 0) return
                if (length < PASSWORD_MAX_LENGTH) {
                    setError(
                        "Password tidak boleh lebih dari $PASSWORD_MAX_LENGTH karakter",
                        errorIcon
                    )
                } else {
                    setError(null, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (text?.isEmpty() == true) {
            setError(null, null)
        }
    }

    companion object {
        private const val PASSWORD_MAX_LENGTH = 8
    }

}
