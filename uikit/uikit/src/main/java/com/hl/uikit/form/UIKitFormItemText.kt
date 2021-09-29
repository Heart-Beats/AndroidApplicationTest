package com.hl.uikit.form

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.hl.uikit.R
import com.hl.uikit.onClick

open class UIKitFormItemText : UIKitFormItemLabel {

    private var tvText: TextView? = null
    private var mTextBold: Boolean = false
    var mTextMaxLines: Int = 0
    private var mTextCustom: Boolean = false
    internal var mText: CharSequence? = null
    private var mTextSize: Int = 0
    private var mTextColor: ColorStateList? = null
    private var mTextGravity = Gravity.END
    private var mHintTextColor: ColorStateList? = null
    private var mHintText: String? = null

    internal var mRightImageView: ImageView? = null
    private var mRightIcon: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.uikit_formItemTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun dispatchSetSelected(selected: Boolean) {
        mRightImageView?.isSelected = selected
        mRightImageView?.refreshDrawableState()
    }

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.UIKitFormItemText, defStyleAttr, defStyleRes)
        ta.also {
            mText = it.getString(R.styleable.UIKitFormItemText_uikit_formText)
            mTextSize =
                it.getDimensionPixelSize(R.styleable.UIKitFormItemText_uikit_formTextSize, 14)
            mTextColor = it.getColorStateList(R.styleable.UIKitFormItemText_uikit_formTextColor)
            mTextMaxLines =
                it.getInt(R.styleable.UIKitFormItemText_uikit_formTextMaxLines, mTextMaxLines)
            mTextBold = it.getBoolean(R.styleable.UIKitFormItemText_uikit_formTextBold, false)
            mTextCustom = it.getBoolean(R.styleable.UIKitFormItemText_uikit_formTextCustom, false)
            mTextGravity = it.getInt(R.styleable.UIKitFormItemText_uikit_formTextGravity, -1)
            mHintText = it.getString(R.styleable.UIKitFormItemText_uikit_formTextHint)
            mHintTextColor = it.getColorStateList(R.styleable.UIKitFormItemText_uikit_formTextHintColor)
            mRightIcon = it.getDrawable(R.styleable.UIKitFormItemText_uikit_formRightIcon)
        }.recycle()
    }

    override fun createRightChildViews(): List<View?> {
        return mutableListOf(createText(), createRightIcon())
    }

    private fun createText(): TextView? {
        if (!mText.isNullOrEmpty() || !mHintText.isNullOrEmpty() || hasHintText()) {
            tvText = onCreateTextView(mTextCustom).apply {
                this.text = mText ?: ""
                this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize * 1.0f)
                if (mTextColor != null) {
                    setTextColor(mTextColor)
                }

                if (mTextMaxLines != 0) {
                    this.maxLines = mTextMaxLines
                }
                mHintText?.let { hint ->
                    setHint(hint)
                }
                mHintTextColor?.let { colors ->
                    setHintTextColor(colors)
                }
                layoutParams = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            setTextBold(mTextBold)
            setTextGravity(mTextGravity)
        }
        return tvText
    }

    protected open fun hasHintText() = false

    fun setTextGravity(@GravityFlag gravity: Int) {
        mTextGravity = gravity
        tvText?.gravity = mTextGravity
    }

    protected open fun createRightIcon(): ImageView? {
        if (mRightIcon != null) {
            mRightImageView = AppCompatImageView(context, null).apply {
                this.setImageDrawable(mRightIcon)
            }
        }
        return mRightImageView
    }

    fun setTextBold(isBold: Boolean) {
        mTextBold = isBold
        val tvText = tvText ?: return
        tvText.typeface = if (isBold) {
            Typeface.DEFAULT_BOLD
        } else {
            null
        }
    }

    open fun setText(text: CharSequence?) {
        mText = text
        val setMethod: ((CharSequence) -> Unit)? = tvText?.run { this::setText }
        commonHandelChangeView(this::mText, this::tvText, this::createText, setMethod)
    }

    open fun onCreateTextView(textCustom: Boolean): TextView {
        return AppCompatTextView(context, null, android.R.attr.textViewStyle)
    }

    fun setRightIcon(icon: Drawable?, onClickListener: ((v: View) -> Unit)? = null) {
        mRightIcon = icon

        val index = if (tvText == null) {
            0
        } else {
            1
        }

        val setMethod: ((Drawable) -> Unit)? = mRightImageView?.run { this::setImageDrawable }
        commonHandelChangeView(
            this::mRightIcon, this::mRightImageView, this::createRightIcon, setMethod, changViewOpeListener = {
                if (onClickListener != null) {
                    mRightImageView?.onClick(onClickListener)
                }
            }, changeViewIndex = index
        )
    }

    fun setTextColor(@ColorInt color: Int) {
        tvText?.setTextColor(color)
    }

}