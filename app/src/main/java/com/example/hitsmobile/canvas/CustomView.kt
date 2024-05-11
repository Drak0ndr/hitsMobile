package com.example.hitsmobile.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class CustomView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null

    private val mPaint: Paint

    private val paths = ArrayList<Stroke>()
    private val points = mutableListOf<Pair<Float, Float>>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.alpha = 0xff
    }

    fun init(height: Int, width: Int) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)

        currentColor = Color.BLACK
        strokeWidth = 25
    }

    fun setColor(color: Int) {
        currentColor = color
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun back() {
        paths.removeAt(paths.size - 1)
        invalidate()
    }

    fun save(): Bitmap? {
        return mBitmap
    }
    override fun onDraw(canvas: Canvas) {
        canvas.save()

        val backgroundColor = Color.WHITE
        mCanvas!!.drawColor(backgroundColor)

        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mCanvas!!.drawPath(fp.path, mPaint)
        }

        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    fun alg(){
        mPath = Path()
        val stroke = Stroke(
            currentColor, 10,
            mPath!!
        )
        paths.add(stroke)

        mPath!!.reset()

        mPath!!.moveTo(points[0].first, points[0].second)

        for(i in 1..points.size - 1) {
            var mx = (points[i].first + points[i - 1].first) / 2
            var my = (points[i].second + points[i - 1].second) / 2

            mPath!!.quadTo(mx - 100, my + 410,points[i].first, points[i].second)
        }
        invalidate()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val stroke = Stroke(
            currentColor, 30,
            mPath!!
        )
        if(checkPoints(points, x, y)){
            paths.add(stroke)

            mPath!!.reset()

            mPath!!.moveTo(x, y)

            points.add(x to y)

            mX = x
            mY = y
        }

    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
        neig()
    }

    private fun checkPoints(points: MutableList<Pair<Float, Float>>, x : Float, y : Float):Boolean{
        for(i in 0..points.size - 1){
            if(points[i].first <= x + 50 && points[i].first >= x - 50
                && points[i].second <= y + 50 && points[i].second >= y - 50){
                return false
            }
        }
        return true
    }
    fun neig(){
        if(points.size > 1){
            mPath = Path()
            val stroke = Stroke(
                currentColor, 10,
                mPath!!
            )
            paths.add(stroke)

            mPath!!.reset()

            mPath!!.moveTo(points[points.size - 2].first, points[points.size - 2].second)
            mPath!!.lineTo(points[points.size - 1].first, points[points.size - 1].second)


            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
        var isSpline : Boolean = false
    }
}

