package com.example.swipeblephonebook

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator


class CustomPhoneItem(context: Context, attributes: AttributeSet): androidx.appcompat.widget.AppCompatTextView(context, attributes) {


    companion object {
        private const val DEFAULT_CIRCLE_COLOR = Color.YELLOW
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        const val FOCUSED = 0L
        const val PRESSED = 1L

    }
    private lateinit var valueAnimator: ValueAnimator

    private var viewState: ViewState = ViewState.DEFAULT
    private var viewStateAroundDefault: ViewStateAroundDefault? = null
    private var icon = resources.getDrawable(R.drawable.ic_baseline_call_24)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circleColor = DEFAULT_CIRCLE_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    private var xmDown = 0
    private var ymDown = 0
    private var xmUp = 0
    private var ymUp = 0


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

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomAttribute,
            0, 0)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCirle(canvas)
        if (this::valueAnimator.isInitialized) {
            if (valueAnimator.isRunning) {
              //  setBackgroundResource(R.drawable.ic_baseline_call_24)
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
        paint.color = Color.BLUE
        if (this.viewState == ViewState.SWIPED_RIGHT){
            canvas?.drawRect(0f,0f, weight.toFloat(), measuredHeight.toFloat(), paint)
            canvas?.drawBitmap(drawableToBitmap(icon)!!, (measuredWidth/2.2).toFloat(), (measuredHeight/2.2).toFloat(), paint)
        }

        if (this.viewState == ViewState.DEFAULT){
            canvas?.drawRect(0f,0f, weight.toFloat(), measuredHeight.toFloat(), paint)
        }

        if (this.viewState == ViewState.DEFAULT_FROM_LEFT){
            canvas?.drawRect(weight.toFloat(),0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        }


        if (this.viewState == ViewState.SWIPED_LEFT){
            canvas?.drawRect(weight.toFloat(),0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
            canvas?.drawBitmap(drawableToBitmap(icon)!!, (measuredWidth/2.2).toFloat(), (measuredHeight/2.2).toFloat(), paint)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event!!.action and MotionEvent.ACTION_MASK
        var xUp = 0; var xDown = 0; var yUp = 0; var yDown = 0;

        when(event.action){
            MotionEvent.ACTION_UP -> {
                Log.d("IlsaveUp", " x = ${event.x} xUpCustom = $xmUp y = ${event.y}")
                xUp = event.x.toInt()
                xmUp = event.x.toInt()
                yUp = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE ->{
                Log.d("IlsaveMove", " x = ${event.x}  y = ${event.y}")
            }
            MotionEvent.ACTION_DOWN -> {
                Log.d("IlsaveDown", " x = ${event.x} xDowncustom = $xmDown y = ${event.y}")
                xmDown = event.x.toInt()
                xDown = event.x.toInt()
                yDown = event.y.toInt()
            }
        }
        if (((xmUp - xmDown) > 100) && this.viewState == ViewState.DEFAULT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Right! xUp = ${xUp} xDown = $xDown xUp - xDown = ${(xUp - xDown)}  xUpCustom = $xmUp xDowncustom = $xmDown xUp - xDown = ${(xmUp - xmDown)} ")

            this.viewState = ViewState.SWIPED_RIGHT
            valueAnimator = ValueAnimator.ofInt(0, measuredWidth)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                val activeRadius = valueAnimator.animatedValue as Int
                 weight = activeRadius
            }
            valueAnimator.start()
            xmUp = 0
            xmDown = 0
        }
        if ((xmDown - xmUp) > 100 && this.viewState == ViewState.SWIPED_RIGHT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Left! xUp = ${xUp} xDown = $xDown    xDown - xUp =  ${(xDown - xUp)}   xUpCustom = $xmUp xDowncustom = $xmDown xDown - xUp = ${(xmDown - xmUp)} ")
            this.viewState = ViewState.DEFAULT
            valueAnimator = ValueAnimator.ofInt(measuredWidth, 0)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                val activeRadius = valueAnimator.animatedValue as Int
                weight = activeRadius
            }
            valueAnimator.start()

            xmUp = 0
            xmDown = 0
        }

        if ((xmDown - xmUp) > 100 && this.viewState == ViewState.DEFAULT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Left! xUp = ${xUp} xDown = $xDown    xDown - xUp =  ${(xDown - xUp)}   xUpCustom = $xmUp xDowncustom = $xmDown xDown - xUp = ${(xmDown - xmUp)} ")

            valueAnimator = ValueAnimator.ofInt(measuredWidth, 0)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                val activeRadius = valueAnimator.animatedValue as Int
                weight = activeRadius
            }
            valueAnimator.start()
            this.viewState = ViewState.SWIPED_LEFT
            this.viewStateAroundDefault = ViewStateAroundDefault.FROM_LEFT_TO_DEFAULT
            xmUp = 0
            xmDown = 0
        }


        if (((xmUp - xmDown) > 100) && this.viewState == ViewState.SWIPED_LEFT && xmDown != 0 && xmUp != 0 ){
            Log.d("IlsaveImportant", "You swiped Right! xUp = ${xUp} xDown = $xDown xUp - xDown = ${(xUp - xDown)}  xUpCustom = $xmUp xDowncustom = $xmDown xUp - xDown = ${(xmUp - xmDown)} ")
            this.viewState = ViewState.DEFAULT_FROM_LEFT
            valueAnimator = ValueAnimator.ofInt(0, measuredWidth)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                val activeRadius = valueAnimator.animatedValue as Int
                weight = activeRadius
            }
            valueAnimator.start()
            xmUp = 0
            xmDown = 0
        }

        return true
    }
}