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
import kotlin.math.abs
import kotlin.math.pow


class CustomView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()
    private val paths = ArrayList<Stroke>()
    private val points = mutableListOf<Pair<Float, Float>>()
    private val newPoints = mutableListOf<Pair<Float, Float>>()
    private val customPoints = mutableListOf<Pair<Float, Float>>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)
    private var currHeight : Float = 0.0f
    private var currWidth : Float = 0.0f

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
        currWidth = mCanvas!!.width / 200.0f
    }

    fun reset(){
        paths.clear()
        points.clear()
        newPoints.clear()

        invalidate()
    }

    fun back() {
        if(SplineActivity.MyFun.currShape == 5){
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
                newPoints.removeAt(newPoints.size - 1)

                if(newPoints.size > 0 && customPoints.contains(points[points.size - 1])){
                    points.removeAt(points.size - 1)
                    newPoints.removeAt(newPoints.size - 1)
                }

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

            if(SplineActivity.MyFun.currShape == 4){
                if(newPoints.size > 0){
                    val lastX = newPoints[newPoints.size - 1].first

                    val dX = lastX - x / 200.0f

                    if(abs(dX) < 0.3f){
                        val newX = if(x / 200.0f + 0.4f < currWidth){x / 200.0f + 0.4f}else{x / 200.0f - 0.4f}

                        val newY = ((newPoints[newPoints.size - 1].second + (currHeight / 2.0f) - (y / 200.0f)) / 2.0f)

                        newPoints.add(newX to newY)

                        points.add(newX * 200 to ((currHeight / 2.0f) - newY) * 200.0f)

                        customPoints.add(newX * 200 to ((currHeight / 2.0f) - newY) * 200.0f)
                    }
                }
                newPoints.add((x / 200.0f) to (currHeight / 2.0f) - (y / 200.0f))
            }

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

            if(SplineActivity.MyFun.currShape == 4){
                if(customPoints.contains(points[points.size - 2])){
                    mPath!!.moveTo(points[points.size - 3].first, points[points.size - 3].second)
                    mPath!!.lineTo(points[points.size - 1].first, points[points.size - 1].second)
                }
                else{
                    mPath!!.moveTo(points[points.size - 2].first, points[points.size - 2].second)
                    mPath!!.lineTo(points[points.size - 1].first, points[points.size - 1].second)
                }
            }
            else{
                mPath!!.moveTo(points[points.size - 2].first, points[points.size - 2].second)
                mPath!!.lineTo(points[points.size - 1].first, points[points.size - 1].second)
            }

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
            if(SplineActivity.MyFun.currShape == 4){
                /*cubic()*/
                val spline = Spline(newPoints)

                spline.initializingFirstFunction()
                spline.initializingAllFunctions()

                rendering(spline)
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

    private fun cubic(){
        paths.clear()
        invalidate()

        mPath = Path()
        val stroke = Stroke(
            currentColor, 10,
            mPath!!
        )

        paths.add(stroke)

        mPath!!.reset()

        mPath!!.moveTo(points[0].first, points[0].second)

        for(i in 1..< points.size){

            val newY1 = ((points[i].second + points[i - 1].second) / 2.0f + points[i].second) / 2.0f
            val newY2 = ((points[i].second + points[i - 1].second) / 2.0f + points[i].second) / 2.0f

            val newX1 = ((newPoints[i].first + newPoints[i - 1].first) / 2 - 0.8f) * 200
            val newX2 = ((newPoints[i].first + newPoints[i - 1].first) / 2 + 0.8f) * 200

            /*mPath!!.cubicTo(newX1, newY1, newX2, newY2, points[i].first, points[i].second)*/

            val a1 = points[i - 1].first
            val a2 = points[i - 1].second
            val b1 = points[i].first
            val b2 = points[i].second

            var t = 0.0f

            while(t <= 1) {
                val x = (1.0f - t).pow(3) * a1 + 3 * (1.0f - t).pow(2) * t * newX1 +
                        3 * (1.0f - t) * t.pow(2) * newX2 + t.pow(3) * b1

                val y = (1.0f - t).pow(3) * a2 + 3 * (1.0f - t).pow(2) * t * newY1 +
                        3 * (1.0f - t) * t.pow(2) * newY2 + t.pow(3) * b2

                mPath!!.lineTo(x, y)

                t += 0.01f
            }

            invalidate()
        }
    }

    fun cubicInterpolation(path : Path, x1 : Float, y1 : Float, x2 : Float, y2 : Float,
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
            if(customPoints.contains(points[i])){
                continue
            }
            else{
                mPath = Path()
                val stroke = Stroke(
                    currentColor, 10,
                    mPath!!
                )

                paths.add(stroke)

                mPath!!.reset()

                if(customPoints.contains(points[i - 1])){
                    val newY1 = ((points[i].second + points[i - 2].second) / 2.0f + points[i - 2].second) / 2.0f
                    val newY2 = ((points[i].second + points[i - 2].second) / 2.0f + points[i].second) / 2.0f

                    val newX1 = ((newPoints[i].first + newPoints[i - 2].first) / 2 - 0.8f) * 200
                    val newX2 = ((newPoints[i].first + newPoints[i - 2].first) / 2 + 0.8f) * 200

                    mPath!!.moveTo(points[i - 2].first, points[i - 2].second)
                    /*mPath!!.cubicTo(newX1, newY1, newX2, newY2, points[i].first, points[i].second)*/

                    val a1 = points[i - 2].first
                    val a2 = points[i - 2].second
                    val b1 = points[i].first
                    val b2 = points[i].second

                    var t = 0.0f

                    while(t <= 1) {
                        val x = (1.0f - t).pow(3) * a1 + 3 * (1.0f - t).pow(2) * t * newX1 +
                                3 * (1.0f - t) * t.pow(2) * newX2 + t.pow(3) * b1

                        val y = (1.0f - t).pow(3) * a2 + 3 * (1.0f - t).pow(2) * t * newY1 +
                                3 * (1.0f - t) * t.pow(2) * newY2 + t.pow(3) * b2

                        mPath!!.lineTo(x, y)

                        t += 0.01f
                    }

                    invalidate()

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
                    mPath!!.moveTo(points[i - 1].first, points[i - 1].second)

                    var a : Float
                    var b : Float

                    if(points[i - 1].first < points[i].first){
                        a = newPoints[i - 1].first
                        b = newPoints[i].first

                        while(a + 0.0001f < b) {
                            val v1 = a + 0.0001f
                            val v2 = spline.findValue(v1, i - 1)

                            mPath!!.lineTo(v1 * 200.0f, ((currHeight / 2.0f) - v2) * 200.0f)

                            invalidate()

                            a += 0.0001f
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
                        a = newPoints[i - 1].first
                        b = newPoints[i].first

                        while(a - 0.0001f > b) {
                            val v1 = a - 0.0001f
                            val v2 = spline.findValue(v1, i - 1)

                            mPath!!.lineTo(v1 * 200.0f, ((currHeight / 2.0f) - v2) * 200.0f)

                            invalidate()

                            a -= 0.0001f
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
    }
}

