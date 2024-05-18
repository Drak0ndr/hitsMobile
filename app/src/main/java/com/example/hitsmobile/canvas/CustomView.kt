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


class CustomView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths = ArrayList<Stroke>()
    private val points = mutableListOf<Pair<Float, Float>>()
    private val newPoints = mutableListOf<Pair<Float, Float>>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)
    private var currHeight : Float = 0.0f

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

        currHeight = mCanvas!!.height / 200.0f
    }

    fun back() {
        if(paths.size != 0){
            if(paths.size > 1){
                paths.removeAt(paths.size - 1)
            }

            paths.removeAt(paths.size - 1)
            points.removeAt(points.size - 1)
            newPoints.removeAt(newPoints.size - 1)

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

            newPoints.add((x / 200.0f) to (currHeight / 2.0f) - (y / 200.0f))

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
        if(newPoints.size > 1){
            val spline = Spline(newPoints)

            spline.initializingFirstFunction()
            spline.initializingAllFunctions()

            rendering(spline)
        }
    }

    private fun rendering(spline : Spline){
        paths.clear()
        invalidate()

        mPath = Path()
        val stroke = Stroke(
            currentColor, 30,
            mPath!!
        )

        paths.add(stroke)

        mPath!!.reset()

        mPath!!.moveTo(points[0].first, points[0].second)
        mPath!!.lineTo(points[0].first, points[0].second)

        for(i in 1..< points.size){
            mPath = Path()
            val stroke = Stroke(
                currentColor, 10,
                mPath!!
            )

            paths.add(stroke)

            mPath!!.reset()

            mPath!!.moveTo(points[i - 1].first, points[i - 1].second)

            var a : Float
            var b : Float

            if(points[i - 1].first < points[i].first){
                a = points[i - 1].first
                b = points[i].first

                a /= 200.0f
                b /= 200.0f

                while(a + 0.01f < b) {
                    val v1 = a + 0.01f
                    val v2 = spline.findValue(v1, i - 1)

                    mPath!!.lineTo(v1 * 200.0f, ((currHeight / 2.0f) - v2) * 200.0f)

                    invalidate()

                    a += 0.01f
                }

                mPath!!.lineTo(points[i].first, points[i].second)

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
            else{
                a = points[i - 1].first
                b = points[i].first

                a /= 200.0f
                b /= 200.0f

                while(a - 0.01f > b) {
                    val v1 = a - 0.01f
                    val v2 = spline.findValue(v1, i - 1)

                    mPath!!.lineTo(v1 * 200.0f, ((currHeight / 2.0f) - v2) * 200.0f)

                    invalidate()

                    a -= 0.01f
                }

                mPath!!.lineTo(points[i].first, points[i].second)

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

