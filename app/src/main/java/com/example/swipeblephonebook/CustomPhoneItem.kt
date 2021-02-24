package com.example.swipeblephonebook

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import java.lang.Math.abs

class CustomPhoneItem(context: Context, attributes: AttributeSet): androidx.appcompat.widget.AppCompatTextView(context, attributes) {


    companion object {
        private const val DEFAULT_CIRCLE_COLOR = Color.YELLOW
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        const val FOCUSED = 0L
        const val PRESSED = 1L

    }
    private lateinit var valueAnimator: ValueAnimator


    private var icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_baseline_call_24)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circleColor = DEFAULT_CIRCLE_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var size = 320
    private var weight = 0
    set(value){
        field = value
        invalidate()
    }

    public var focusedState = FOCUSED
        set(state) {
            field = state
            invalidate()
        }

    init {
       // icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_baseline_call_24)
        paint.isAntiAlias = true
        setupAttributes(attributes)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomAttribute,
            0, 0)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        drawCirle(canvas)
        super.onDraw(canvas)
        if (this::valueAnimator.isInitialized) {
            if (valueAnimator.isRunning) {
                setBackgroundResource(R.drawable.ic_baseline_call_24)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //для отступов внутри самой вьюшки, будет обращена именно к тексту
        // setPadding((measuredWidth* 0.75).toInt(), 0 , 0, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun drawCirle(canvas: Canvas?){
       paint.style = Paint.Style.FILL
        canvas?.drawRect(0f,0f, weight.toFloat(), measuredHeight.toFloat(), paint)
        canvas?.drawBitmap(icon, 0f, 0f, paint)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action and MotionEvent.ACTION_MASK
        var xUp = 0; var xDown = 0; var yUp = 0; var yDown = 0;

        when(event.action){
            MotionEvent.ACTION_UP -> {
                Log.d("IlsaveUp", " x = ${event.x}  y = ${event.y}")
                xUp = event.x.toInt()
                yUp = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE ->{
                Log.d("IlsaveMove", " x = ${event.x}  y = ${event.y}")
            }
            MotionEvent.ACTION_DOWN -> {
                Log.d("IlsaveDown", " x = ${event.x}  y = ${event.y}")
                xDown = event.x.toInt()
                yDown = event.y.toInt()
            }
        }
        if (abs(xUp - xDown) > 100){
            Log.d("Ilsave", "You swiped! ${abs(xUp - xDown)}")

            valueAnimator = ValueAnimator.ofInt(0, measuredWidth)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                val activeRadius = valueAnimator.animatedValue as Int
                 weight = activeRadius
            }
            valueAnimator.start()
        }
        return true
    }
}