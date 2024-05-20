package com.example.hitsmobile.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TriangleView (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var pointSize = 20f
    private val touchRadius = 50f

    private var Point1: PointF = PointF(200f, 1280f)
    private var Point2: PointF = PointF(540f, 692f)
    private var Point3: PointF = PointF(880f, 1280f)

    private var controlPoint1: PointF = PointF(370f, 986f)
    private var controlPoint2: PointF = PointF(710f, 986f)
    private var controlPoint3: PointF = PointF(540f, 1280f)

    private val paint: Paint = Paint().apply {
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE

        val path = Path()

        path.moveTo(Point1.x, Point1.y)

        path.quadTo(
            controlPoint1.x, controlPoint1.y,
            Point2.x, Point2.y
        )

        path.quadTo(
            controlPoint2.x, controlPoint2.y,
            Point3.x, Point3.y
        )

        path.quadTo(
            controlPoint3.x, controlPoint3.y,
            Point1.x, Point1.y
        )

        canvas.drawPath(path, paint)

        paint.color = Color.parseColor("#FF0000")
        paint.style = Paint.Style.FILL
        pointSize = 20f

        canvas.drawCircle(Point1.x, Point1.y, pointSize, paint)
        canvas.drawCircle(Point2.x, Point2.y, pointSize, paint)
        canvas.drawCircle(Point3.x, Point3.y, pointSize, paint)

        pointSize = 15f

        canvas.drawCircle(controlPoint1.x, controlPoint1.y, pointSize, paint)
        canvas.drawCircle(controlPoint2.x, controlPoint2.y, pointSize, paint)
        canvas.drawCircle(controlPoint3.x, controlPoint3.y, pointSize, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTouchingPoint(Point1, x, y)) {
                    Point1.x = x
                    Point1.y = y
                } else if (isTouchingPoint(Point2, x, y)) {
                    Point2.x = x
                    Point2.y = y
                } else if (isTouchingPoint(Point3, x, y)) {
                    Point3.x = x
                    Point3.y = y
                } else if (isTouchingPoint(controlPoint1, x, y)) {
                    controlPoint1.x = x
                    controlPoint1.y = y
                } else if (isTouchingPoint(controlPoint2, x, y)) {
                    controlPoint2.x = x
                    controlPoint2.y = y
                } else if (isTouchingPoint(controlPoint3, x, y)) {
                    controlPoint3.x = x
                    controlPoint3.y = y
                }

                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (isTouchingPoint(Point1, x, y)) {
                    Point1.x = x
                    Point1.y = y
                } else if (isTouchingPoint(Point2, x, y)) {
                    Point2.x = x
                    Point2.y = y
                } else if (isTouchingPoint(Point3, x, y)) {
                    Point3.x = x
                    Point3.y = y
                } else if (isTouchingPoint(controlPoint1, x, y)) {
                    controlPoint1.x = x
                    controlPoint1.y = y
                } else if (isTouchingPoint(controlPoint2, x, y)) {
                    controlPoint2.x = x
                    controlPoint2.y = y
                } else if (isTouchingPoint(controlPoint3, x, y)) {
                    controlPoint3.x = x
                    controlPoint3.y = y
                }

                invalidate()
            }
        }
        return true
    }

    private fun isTouchingPoint(point: PointF, touchX: Float, touchY: Float): Boolean {
        return touchX >= point.x - touchRadius && touchX <= point.x + touchRadius &&
                touchY >= point.y - touchRadius && touchY <= point.y + touchRadius
    }

    fun reset(){
        Point1 = PointF(200f, 1280f)
        Point2 = PointF(540f, 692f)
        Point3 = PointF(880f, 1280f)

        controlPoint1 = PointF(370f, 986f)
        controlPoint2 = PointF(710f, 986f)
        controlPoint3 = PointF(540f, 1280f)

        invalidate()
    }
}