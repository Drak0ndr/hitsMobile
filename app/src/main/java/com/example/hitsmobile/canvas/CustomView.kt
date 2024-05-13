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

    fun back() {
        if(paths.size != 0){
            if(paths.size > 1){
                paths.removeAt(paths.size - 1)
            }
            paths.removeAt(paths.size - 1)
            points.removeAt(points.size - 1)
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
        for(i in 0..points.size - 1){
            if(points[i].first <= x + 50 && points[i].first >= x - 50
                && points[i].second <= y + 50 && points[i].second >= y - 50){
                return false
            }
        }
        return true
    }
    fun connection(){
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

    fun alg(){

    }

    fun approximation(x: Double): Double{
        val apoints = listOf(Pair(1.0f, 2.0f), Pair(3.0f, 1.0f), Pair(5.0f, 3.0f),
            Pair(6.0f, 4.0f), Pair(8.0, 2.0f))
        val newpoints = mutableListOf<Pair<Double, Double>>()

        for(i in 0..apoints.size - 1){
            var p = Pair((apoints[i].first).toDouble(), (apoints[i].second).toDouble())
            newpoints.add(p)
        }


        // Решаем систему уравнений для линейной функции y = ax + b
        val sumX = newpoints.sumByDouble { it.first }
        val sumY = newpoints.sumByDouble { it.second }
        val sumXY = newpoints.sumByDouble { it.first * it.second }
        val sumXSquare = newpoints.sumByDouble { it.first * it.first }

        val n = newpoints.size.toDouble()

        val a = (n * sumXY - sumX * sumY) / (n * sumXSquare - sumX * sumX)
        val b = (sumY - a * sumX) / n

        // Функция аппроксимации
        fun linearFunction(x: Double): Double {
            return a * x + b
        }


        return linearFunction(x)
    }
}

