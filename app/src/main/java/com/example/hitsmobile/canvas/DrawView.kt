package com.example.hitsmobile.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import kotlin.math.abs
import kotlin.properties.Delegates


class DrawView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null

    private val mPaint: Paint

    private val paths = ArrayList<Stroke>()
    private val points = mutableListOf<Pair<Float, Float>>()
    private var index : Int = -1
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
        strokeWidth = 20
    }
    
    fun setColor(color: Int) {
        currentColor = color
    }
    
    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun back() {
        if (paths.size != 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
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

    private fun checkPoints(points: MutableList<Pair<Float, Float>>, x : Float, y : Float):Boolean{
        for(i in 0..points.size - 1){
            if(points[i].first <= x + 50 && points[i].first >= x - 50
                && points[i].second <= y + 50 && points[i].second >= y - 50){
                return false
            }
        }
        return true
    }

    private fun touchPoint(x: Float, y: Float) {
        mPath = Path()
        val stroke = Stroke(
            currentColor, 40,
            mPath!!
        )

        if(checkPoints(points, x, y)){
            paths.add(stroke)

            points.add(x to y)
            val nx = points[index + 1].first
            val ny = points[index + 1].second
            Log.d("coord", "$nx, $ny")

            mPath!!.reset()

            mPath!!.moveTo(x, y)

            index += 1

            mX = x
            mY = y
        }
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val stroke = Stroke(
            currentColor, strokeWidth,
            mPath!!
        )
        paths.add(stroke)

        mPath!!.reset()

        mPath!!.moveTo(x, y)

        mX = x
        mY = y
    }
    private fun touchMove(x: Float, y: Float) {
        val dx = abs((x - mX).toDouble()).toFloat()
        val dy = abs((y - mY).toDouble()).toFloat()
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }
    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(!isSpline){
                    touchStart(x, y)
                    invalidate()
                }
                else{
                    touchPoint(x, y)
                    invalidate()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if(!isSpline) {
                    touchMove(x, y)
                    invalidate()
                }
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

