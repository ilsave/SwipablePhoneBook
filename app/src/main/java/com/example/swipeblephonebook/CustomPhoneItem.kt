package com.example.swipeblephonebook

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import java.lang.Math.abs


class CustomPhoneItem(context: Context, attributes: AttributeSet): androidx.appcompat.widget.AppCompatTextView(context, attributes) {

    private lateinit var valueAnimator: ValueAnimator

    private var viewState: ViewState = ViewState.DEFAULT
    private var viewStateAroundDefault: ViewStateAroundDefault? = null
    private var iconCall = resources.getDrawable(R.drawable.ic_baseline_call_24)
    private var iconMessage = resources.getDrawable(R.drawable.ic_baseline_message_24)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var xmDown = 0
    private var xmUp = 0


    private var weight = 0
    set(value){
        field = value
        invalidate()
    }

    init {
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
        if (this.viewState == ViewState.SWIPED_RIGHT){
            paint.color = Color.GREEN
            canvas?.drawRect(0f,0f, weight.toFloat(), measuredHeight.toFloat(), paint)
            canvas?.drawBitmap(drawableToBitmap(iconCall)!!, (weight/2).toFloat(), (measuredHeight/2).toFloat(), paint)
        }

        if (this.viewState == ViewState.DEFAULT && this.viewStateAroundDefault == ViewStateAroundDefault.FROM_LEFT_TO_DEFAULT){
            paint.color = Color.GREEN
            canvas?.drawRect(0f,0f, weight.toFloat(), measuredHeight.toFloat(), paint)
            if ((weight/2) > measuredWidth*0.1){
                Log.d("IlsaveMat", "wight ${weight/2} measured ${measuredWidth/2}     ${(width/2) == (measuredWidth/2)}")
                canvas?.drawBitmap(drawableToBitmap(iconCall)!!, (weight/2).toFloat(), (measuredHeight/2).toFloat(), paint)
            }
        }

        if (this.viewState == ViewState.DEFAULT && this.viewStateAroundDefault == ViewStateAroundDefault.FROM_RIGHT_TO_DEFAULT){
            paint.color = Color.RED
            Log.d("IlsaveInfo","You are here!")
            //measuredWight .. 0
            canvas?.drawRect(weight.toFloat(),0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
            if (weight  < measuredWidth/2){
                Log.d("IlsaveNotMat", " $weight  $measuredWidth")
                canvas?.drawBitmap(drawableToBitmap(iconMessage)!!, (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat(), paint)
            }else {
                canvas?.drawBitmap(drawableToBitmap(iconMessage)!!, (weight).toFloat(), (measuredHeight/2).toFloat(), paint)
            }
        }

        if (this.viewState == ViewState.SWIPED_LEFT){
            paint.color = Color.RED
            canvas?.drawRect(weight.toFloat(),0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
            if (weight > measuredWidth/2){
                canvas?.drawBitmap(drawableToBitmap(iconMessage)!!, (weight).toFloat(), (measuredHeight/2).toFloat(), paint)
            } else {
                canvas?.drawBitmap(drawableToBitmap(iconMessage)!!, (measuredWidth/2).toFloat(), (measuredHeight/2).toFloat(), paint)
            }
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
            }
            MotionEvent.ACTION_MOVE ->{
                Log.d("IlsaveMove", " x = ${event.x}  y = ${event.y}")
            }
            MotionEvent.ACTION_CANCEL -> {
                xmUp = 0
                xmDown = 0
            }
            MotionEvent.ACTION_DOWN -> {
                Log.d("IlsaveDown", " x = ${event.x} xDowncustom = $xmDown y = ${event.y}")
                xmDown = event.x.toInt()
                xDown = event.x.toInt()
            }
        }
        if (((xmUp - xmDown) > 100) && this.viewState == ViewState.DEFAULT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Right! xUp = ${xUp} xDown = $xDown xUp - xDown = ${(xUp - xDown)}  xUpCustom = $xmUp xDowncustom = $xmDown xUp - xDown = ${(xmUp - xmDown)} ")

            this.viewState = ViewState.SWIPED_RIGHT
            valueAnimator = ValueAnimator.ofInt(0, measuredWidth)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                weight = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
            xmUp = 0
            xmDown = 0
        }
        if ((xmDown - xmUp) > 100 && this.viewState == ViewState.SWIPED_RIGHT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Left! xUp = ${xUp} xDown = $xDown    xDown - xUp =  ${(xDown - xUp)}   xUpCustom = $xmUp xDowncustom = $xmDown xDown - xUp = ${(xmDown - xmUp)} ")
            this.viewState = ViewState.DEFAULT
            this.viewStateAroundDefault = ViewStateAroundDefault.FROM_LEFT_TO_DEFAULT
            valueAnimator = ValueAnimator.ofInt(measuredWidth, 0)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                weight = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()

            xmUp = 0
            xmDown = 0
        }

        if ((xmDown - xmUp) > 100 && this.viewState == ViewState.DEFAULT && xmDown != 0 && xmUp != 0 ){
            Log.d("Ilsave", "You swiped Left! xUp = ${xUp} xDown = $xDown    xDown - xUp =  ${(xDown - xUp)}   xUpCustom = $xmUp xDowncustom = $xmDown xDown - xUp = ${(xmDown - xmUp)} ")

            valueAnimator = ValueAnimator.ofInt(measuredWidth, 0)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = AccelerateInterpolator()
            valueAnimator.addUpdateListener {
                weight = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
            this.viewState = ViewState.SWIPED_LEFT
            xmUp = 0
            xmDown = 0
        }


        if (((xmUp - xmDown) > 100) && this.viewState == ViewState.SWIPED_LEFT && xmDown != 0 && xmUp != 0 ){
            Log.d("IlsaveImportant", "You swiped Right! xUp = ${xUp} xDown = $xDown xUp - xDown = ${(xUp - xDown)}  xUpCustom = $xmUp xDowncustom = $xmDown xUp - xDown = ${(xmUp - xmDown)} ")
            this.viewStateAroundDefault = ViewStateAroundDefault.FROM_RIGHT_TO_DEFAULT
            this.viewState = ViewState.DEFAULT
            valueAnimator = ValueAnimator.ofInt(0, measuredWidth)
            valueAnimator.duration = 1000
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener {
                weight = valueAnimator.animatedValue as Int
            }
            valueAnimator.start()
            xmUp = 0
            xmDown = 0
        }

        return true
    }
}