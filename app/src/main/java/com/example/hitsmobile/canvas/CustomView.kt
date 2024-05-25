package com.example.hitsmobile.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.hitsmobile.activity.SplineActivity
import kotlin.math.pow


class CustomView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths = ArrayList<Stroke>()
    private val points = mutableListOf<Pair<Float, Float>>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    init {
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

    fun reset(){
        paths.clear()
        points.clear()

        invalidate()
    }

    fun back() {
        if(SplineActivity.MyVarSpline.currShape == 5){
            reset()
            invalidate()
        }
        else{
            if(paths.size != 0){
                if(paths.size > 1){
                    paths.removeAt(paths.size - 1)
                }

                paths.removeAt(paths.size - 1)
                points.removeAt(points.size - 1)

                invalidate()
            }
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
        connection()
    }

    private fun checkPoints(points: MutableList<Pair<Float, Float>>, x : Float, y : Float):Boolean{
        for(i in 0..< points.size){
            if(points[i].first <= x + 50 && points[i].first >= x - 50
                && points[i].second <= y + 50 && points[i].second >= y - 50){
                return false
            }
        }
        return true
    }

    private fun connection(){
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

    @SuppressLint("ClickableViewAccessibility")
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

    fun start(){
        if(points.size > 1){
            if(SplineActivity.MyVarSpline.currShape == 4){
                points.add(points[points.size - 1])
                points.add(0, points[0])

                paths.clear()

                mPath = Path()
                val stroke = Stroke(
                    currentColor, 30,
                    mPath!!
                )

                paths.add(stroke)

                mPath!!.reset()

                mPath!!.moveTo(points[1].first, points[1].second)
                mPath!!.lineTo(points[1].first, points[1].second)

                invalidate()

                for(i in 3..< points.size){
                    mPath = Path()
                    val stroke = Stroke(
                        currentColor, 10,
                        mPath!!
                    )

                    paths.add(stroke)

                    mPath!!.reset()

                    mPath!!.moveTo(points[i - 2].first, points[i - 2].second)

                    catmullRom(mPath!!, points[i - 3], points[i - 2], points[i - 1], points[i])

                    mPath = Path()
                    val stroke2 = Stroke(
                        currentColor, 30,
                        mPath!!
                    )

                    paths.add(stroke2)

                    mPath!!.reset()

                    mPath!!.moveTo(points[i - 1].first, points[i - 1].second)
                    mPath!!.lineTo(points[i - 1].first, points[i - 1].second)

                    invalidate()
                }

                points.removeAt(0)
                points.removeAt(points.size - 1)
            }
            else{
                paths.clear()

                mPath = Path()

                val stroke = Stroke(
                    currentColor, 10,
                    mPath!!
                )

                paths.add(stroke)

                mPath!!.reset()

                mPath!!.moveTo(points[0].first, points[0].second)

                cubicInterpolation(
                    mPath!!,
                    points[0].first, points[0].second,
                    points[1].first, points[1].second,
                    (points[1].first + points[2].first) /2,
                    (points[1].second + points[2].second) / 2
                )

                invalidate()

                for(i in 2..< points.size - 2){
                    cubicInterpolation(
                        mPath!!,
                        (points[i].first + points[i - 1].first) /2,
                        (points[i].second + points[i - 1].second) / 2,
                        points[i].first, points[i].second,
                        (points[i].first + points[i + 1].first) / 2,
                        (points[i].second + points[i + 1].second) / 2
                    )

                    invalidate()
                }

                cubicInterpolation(
                    mPath!!,
                    (points[points.size - 2].first + points[points.size - 3].first) / 2,
                    (points[points.size - 3].second + points[points.size - 2].second) / 2,
                    points[points.size - 2].first, points[points.size - 2].second,
                    points[points.size - 1].first, points[points.size - 1].second
                )

                invalidate()

                for(i in 0..< points.size) {

                    mPath = Path()
                    val stroke = Stroke(
                        currentColor, 30,
                        mPath!!
                    )

                    paths.add(stroke)

                    mPath!!.reset()

                    mPath!!.moveTo(points[i].first, points[i].second)
                    mPath!!.lineTo(points[i].first, points[i].second)

                    invalidate()
                }
            }
        }
    }

    private fun catmullRom(path: Path, pair1: Pair<Float,Float>, pair2: Pair<Float,Float>,
                           pair3: Pair<Float,Float>, pair4: Pair<Float,Float>){
        var t = 0.0f

        while(t <= 1.0f){
            val x = 0.5f * (-1.0f * t * (1.0f - t).pow(2) * pair1.first
                    + (2.0f - 5.0f * t.pow(2) +  3.0f * t.pow(3)) * pair2.first
                    + t * (1.0f + 4.0f * t - 3.0f * t.pow(2)) * pair3.first
                    - t.pow(2) * (1.0f - t) * pair4.first)

            val y = 0.5f * (-1.0f * t * (1.0f - t).pow(2) * pair1.second
                    + (2.0f - 5.0f * t.pow(2) +  3.0f * t.pow(3)) * pair2.second
                    + t * (1.0f + 4.0f * t - 3.0f * t.pow(2)) * pair3.second
                    - t.pow(2) * (1.0f - t) * pair4.second)

            path.lineTo(x, y)

            invalidate()

            t += 0.01f
        }
    }

    private fun cubicInterpolation(path : Path, x1 : Float, y1 : Float, x2 : Float, y2 : Float,
                                   x3 : Float, y3 : Float){
        var t = 0.0f

        while(t <= 1) {
            val x = (1.0f - t).pow(2) * x1 + 2 * (1.0f - t) * t * x2 + t.pow(2) * x3

            val y = (1.0f - t).pow(2) * y1 + 2 * (1.0f - t) * t * y2 + t.pow(2) * y3

            path.lineTo(x, y)

            t += 0.01f
        }

        invalidate()
    }
}

