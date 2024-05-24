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
import kotlin.math.pow

class HexagonView (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var pointSize = 20f
    private val touchRadius = 50f

    private var Point1: PointF = PointF(80f, 948f)
    private var Point2: PointF = PointF(310f, 550f)
    private var Point3: PointF = PointF(770f, 550f)
    private var Point4: PointF = PointF(1000f, 948f)
    private var Point5: PointF = PointF(770f, 1346f)
    private var Point6: PointF = PointF(310f, 1346f)

    private var controlPoint1: PointF = PointF(195f, 749f)
    private var controlPoint2: PointF = PointF(540f, 550f)
    private var controlPoint3: PointF = PointF(885f, 749f)
    private var controlPoint4: PointF = PointF(885f, 1147f)
    private var controlPoint5: PointF = PointF(540f, 1346f)
    private var controlPoint6: PointF = PointF(195f, 1147f)

    private val paint: Paint = Paint().apply {
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE

        val path = Path()

        path.moveTo(Point1.x, Point1.y)

        cubicInterpolation(path, Point1.x, Point1.y, controlPoint1.x, controlPoint1.y,
            Point2.x, Point2.y)

        cubicInterpolation(path, Point2.x, Point2.y, controlPoint2.x, controlPoint2.y,
            Point3.x, Point3.y)

        cubicInterpolation(path, Point3.x, Point3.y, controlPoint3.x, controlPoint3.y,
            Point4.x, Point4.y)

        cubicInterpolation(path, Point4.x, Point4.y, controlPoint4.x, controlPoint4.y,
            Point5.x, Point5.y)

        cubicInterpolation(path, Point5.x, Point5.y, controlPoint5.x, controlPoint5.y,
            Point6.x, Point6.y)

        cubicInterpolation(path, Point6.x, Point6.y, controlPoint6.x, controlPoint6.y,
            Point1.x, Point1.y)

        canvas.drawPath(path, paint)

        paint.color = Color.parseColor("#FF00FF")
        paint.style = Paint.Style.FILL
        pointSize = 20f

        canvas.drawCircle(Point1.x, Point1.y, pointSize, paint)
        canvas.drawCircle(Point2.x, Point2.y, pointSize, paint)
        canvas.drawCircle(Point3.x, Point3.y, pointSize, paint)
        canvas.drawCircle(Point4.x, Point4.y, pointSize, paint)
        canvas.drawCircle(Point5.x, Point5.y, pointSize, paint)
        canvas.drawCircle(Point6.x, Point6.y, pointSize, paint)

        pointSize = 15f

        canvas.drawCircle(controlPoint1.x, controlPoint1.y, pointSize, paint)
        canvas.drawCircle(controlPoint2.x, controlPoint2.y, pointSize, paint)
        canvas.drawCircle(controlPoint3.x, controlPoint3.y, pointSize, paint)
        canvas.drawCircle(controlPoint4.x, controlPoint4.y, pointSize, paint)
        canvas.drawCircle(controlPoint5.x, controlPoint5.y, pointSize, paint)
        canvas.drawCircle(controlPoint6.x, controlPoint6.y, pointSize, paint)
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
                } else if (isTouchingPoint(Point4, x, y)) {
                    Point4.x = x
                    Point4.y = y
                } else if (isTouchingPoint(Point5, x, y)) {
                    Point5.x = x
                    Point5.y = y
                } else if (isTouchingPoint(Point6, x, y)) {
                    Point6.x = x
                    Point6.y = y
                } else if (isTouchingPoint(controlPoint1, x, y)) {
                    controlPoint1.x = x
                    controlPoint1.y = y
                } else if (isTouchingPoint(controlPoint2, x, y)) {
                    controlPoint2.x = x
                    controlPoint2.y = y
                } else if (isTouchingPoint(controlPoint3, x, y)) {
                    controlPoint3.x = x
                    controlPoint3.y = y
                } else if (isTouchingPoint(controlPoint4, x, y)) {
                    controlPoint4.x = x
                    controlPoint4.y = y
                } else if (isTouchingPoint(controlPoint5, x, y)) {
                    controlPoint5.x = x
                    controlPoint5.y = y
                } else if (isTouchingPoint(controlPoint6, x, y)) {
                    controlPoint6.x = x
                    controlPoint6.y = y
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
                } else if (isTouchingPoint(Point4, x, y)) {
                    Point4.x = x
                    Point4.y = y
                } else if (isTouchingPoint(Point5, x, y)) {
                    Point5.x = x
                    Point5.y = y
                } else if (isTouchingPoint(Point6, x, y)) {
                    Point6.x = x
                    Point6.y = y
                } else if (isTouchingPoint(controlPoint1, x, y)) {
                    controlPoint1.x = x
                    controlPoint1.y = y
                } else if (isTouchingPoint(controlPoint2, x, y)) {
                    controlPoint2.x = x
                    controlPoint2.y = y
                } else if (isTouchingPoint(controlPoint3, x, y)) {
                    controlPoint3.x = x
                    controlPoint3.y = y
                } else if (isTouchingPoint(controlPoint4, x, y)) {
                    controlPoint4.x = x
                    controlPoint4.y = y
                } else if (isTouchingPoint(controlPoint5, x, y)) {
                    controlPoint5.x = x
                    controlPoint5.y = y
                } else if (isTouchingPoint(controlPoint6, x, y)) {
                    controlPoint6.x = x
                    controlPoint6.y = y
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
        Point1 = PointF(80f, 948f)
        Point2 = PointF(310f, 550f)
        Point3 = PointF(770f, 550f)
        Point4 = PointF(1000f, 948f)
        Point5 = PointF(770f, 1346f)
        Point6 = PointF(310f, 1346f)

        controlPoint1 = PointF(195f, 749f)
        controlPoint2 = PointF(540f, 550f)
        controlPoint3 = PointF(885f, 749f)
        controlPoint4 = PointF(885f, 1147f)
        controlPoint5 = PointF(540f, 1346f)
        controlPoint6 = PointF(195f, 1147f)

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