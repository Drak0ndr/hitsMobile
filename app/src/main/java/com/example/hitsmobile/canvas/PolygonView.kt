package com.example.hitsmobile.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow

class PolygonView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var pointSize = 20f
    private var pointMiddleSize = 15f
    private val touchRadius = 50f

    private val points = mutableListOf<PointF>()
    private val middlePoints = mutableListOf<PointF>()

    private var isMoving : Boolean = false
    private var isStart : Boolean = false

    private val paint: Paint = Paint().apply {
        strokeWidth = 10f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(points.size > 0){
            if(!isMoving){
                val path = Path()

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE

                path.moveTo(points[0].x, points[0].y)

                paint.color = Color.parseColor("#9400D3")
                paint.style = Paint.Style.FILL

                canvas.drawCircle(points[0].x, points[0].y, pointSize, paint)

                for(i in 1..< points.size) {
                    paint.color = Color.BLACK
                    paint.style = Paint.Style.STROKE

                    path.lineTo(points[i].x, points[i].y)

                    paint.color = Color.parseColor("#9400D3")
                    paint.style = Paint.Style.FILL

                    canvas.drawCircle(points[i].x, points[i].y, pointSize, paint)
                }

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE

                canvas.drawPath(path, paint)
            }
            else{
                if(!isStart){
                    val middleX = (points[0].x + points[points.size - 1].x) / 2
                    val middleY = (points[0].y + points[points.size - 1].y) / 2

                    middlePoints.add(PointF(middleX, middleY))

                    isStart = true
                }

                val path = Path()

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE

                path.moveTo(points[0].x, points[0].y)

                paint.color = Color.parseColor("#9400D3")
                paint.style = Paint.Style.FILL

                canvas.drawCircle(points[0].x, points[0].y, pointSize, paint)

                for(i in 1..< points.size) {
                    paint.color = Color.BLACK
                    paint.style = Paint.Style.STROKE

                    cubicInterpolation(path, points[i - 1].x, points[i - 1].y,
                        middlePoints[i - 1].x, middlePoints[i - 1].y, points[i].x, points[i].y)

                    paint.color = Color.parseColor("#9400D3")
                    paint.style = Paint.Style.FILL

                    canvas.drawCircle(points[i].x, points[i].y, pointSize, paint)

                    canvas.drawCircle(middlePoints[i - 1].x, middlePoints[i - 1].y, pointMiddleSize, paint)
                }

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE

                cubicInterpolation(path,points[points.size - 1].x, points[points.size - 1].y,
                    middlePoints[middlePoints.size - 1].x, middlePoints[middlePoints.size - 1].y,
                    points[0].x, points[0].y)

                paint.color = Color.parseColor("#9400D3")
                paint.style = Paint.Style.FILL

                canvas.drawCircle(middlePoints[middlePoints.size - 1].x, middlePoints[middlePoints.size - 1].y, pointMiddleSize, paint)

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE

                canvas.drawPath(path, paint)
            }
        }
    }

    private fun checkPoints(x : Float, y : Float):Boolean{
        for(i in 0..< points.size){
            if(points[i].x <= x + 50 && points[i].x >= x - 50
                && points[i].y <= y + 50 && points[i].y >= y - 50){
                return false
            }
        }
        return true
    }

    fun start(){
        isMoving = true

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(!isMoving){
                    if(checkPoints(x, y)){
                        points.add(PointF(x, y))

                        if(points.size > 1){
                            val middleX = (points[points.size - 2].x + points[points.size - 1].x) / 2
                            val middleY = (points[points.size - 2].y + points[points.size - 1].y) / 2

                            middlePoints.add(PointF(middleX, middleY))
                        }
                    }
                }
                else{
                    for(i in 0..< points.size) {
                        if(isTouchingPoint(points[i], x, y)) {
                            points[i].x = x
                            points[i].y = y

                            invalidate()
                        }
                    }

                    for(i in 0..< middlePoints.size) {
                        if(isTouchingPoint(middlePoints[i], x, y)) {
                            middlePoints[i].x = x
                            middlePoints[i].y = y

                            invalidate()
                        }
                    }
                }

                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if(isMoving){
                    for(i in 0..< points.size) {
                        if(isTouchingPoint(points[i], x, y)) {
                            points[i].x = x
                            points[i].y = y

                            invalidate()
                        }
                    }

                    for(i in 0..< middlePoints.size) {
                        if(isTouchingPoint(middlePoints[i], x, y)) {
                            middlePoints[i].x = x
                            middlePoints[i].y = y

                            invalidate()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun isTouchingPoint(point: PointF, touchX: Float, touchY: Float): Boolean {
        return touchX >= point.x - touchRadius && touchX <= point.x + touchRadius &&
                touchY >= point.y - touchRadius && touchY <= point.y + touchRadius
    }

    fun reset(){
        points.clear()
        middlePoints.clear()

        isMoving = false;
        isStart = false;

        invalidate()
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