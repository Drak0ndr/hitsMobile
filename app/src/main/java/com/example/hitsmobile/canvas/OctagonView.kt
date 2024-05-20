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

class OctagonView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var pointSize = 20f
    private val touchRadius = 50f

    private var Point1: PointF = PointF(100f, 950f)
    private var Point2: PointF = PointF(212.5f, 612.5f)
    private var Point3: PointF = PointF(550f, 500f)
    private var Point4: PointF = PointF(887.5f, 612.5f)
    private var Point5: PointF = PointF(1000f, 950f)
    private var Point6: PointF = PointF(887.5f, 1287.5f)
    private var Point7: PointF = PointF(550f, 1400f)
    private var Point8: PointF = PointF(212.5f, 1287.5f)

    private var controlPoint1: PointF = PointF(156.25f, 781.25f)
    private var controlPoint2: PointF = PointF(381.25f, 556.25f)
    private var controlPoint3: PointF = PointF(718.75f, 556.25f)
    private var controlPoint4: PointF = PointF(943.75f, 781.25f)
    private var controlPoint5: PointF = PointF(943.75f, 1118.75f)
    private var controlPoint6: PointF = PointF(718.75f, 1343.75f)
    private var controlPoint7: PointF = PointF(381.25f, 1343.75f)
    private var controlPoint8: PointF = PointF(156.25f, 1118.75f)

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
            Point4.x, Point4.y
        )

        path.quadTo(
            controlPoint4.x, controlPoint4.y,
            Point5.x, Point5.y
        )

        path.quadTo(
            controlPoint5.x, controlPoint5.y,
            Point6.x, Point6.y
        )

        path.quadTo(
            controlPoint6.x, controlPoint6.y,
            Point7.x, Point7.y
        )

        path.quadTo(
            controlPoint7.x, controlPoint7.y,
            Point8.x, Point8.y
        )

        path.quadTo(
            controlPoint8.x, controlPoint8.y,
            Point1.x, Point1.y
        )

        canvas.drawPath(path, paint)

        paint.color = Color.parseColor("#1E90FF")
        paint.style = Paint.Style.FILL
        pointSize = 20f

        canvas.drawCircle(Point1.x, Point1.y, pointSize, paint)
        canvas.drawCircle(Point2.x, Point2.y, pointSize, paint)
        canvas.drawCircle(Point3.x, Point3.y, pointSize, paint)
        canvas.drawCircle(Point4.x, Point4.y, pointSize, paint)
        canvas.drawCircle(Point5.x, Point5.y, pointSize, paint)
        canvas.drawCircle(Point6.x, Point6.y, pointSize, paint)
        canvas.drawCircle(Point7.x, Point7.y, pointSize, paint)
        canvas.drawCircle(Point8.x, Point8.y, pointSize, paint)

        pointSize = 15f

        canvas.drawCircle(controlPoint1.x, controlPoint1.y, pointSize, paint)
        canvas.drawCircle(controlPoint2.x, controlPoint2.y, pointSize, paint)
        canvas.drawCircle(controlPoint3.x, controlPoint3.y, pointSize, paint)
        canvas.drawCircle(controlPoint4.x, controlPoint4.y, pointSize, paint)
        canvas.drawCircle(controlPoint5.x, controlPoint5.y, pointSize, paint)
        canvas.drawCircle(controlPoint6.x, controlPoint6.y, pointSize, paint)
        canvas.drawCircle(controlPoint7.x, controlPoint7.y, pointSize, paint)
        canvas.drawCircle(controlPoint8.x, controlPoint8.y, pointSize, paint)
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
                } else if (isTouchingPoint(Point7, x, y)) {
                    Point7.x = x
                    Point7.y = y
                } else if (isTouchingPoint(Point8, x, y)) {
                    Point8.x = x
                    Point8.y = y
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
                else if (isTouchingPoint(controlPoint7, x, y)) {
                    controlPoint7.x = x
                    controlPoint7.y = y
                }
                else if (isTouchingPoint(controlPoint8, x, y)) {
                    controlPoint8.x = x
                    controlPoint8.y = y
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
                } else if (isTouchingPoint(Point7, x, y)) {
                    Point7.x = x
                    Point7.y = y
                } else if (isTouchingPoint(Point8, x, y)) {
                    Point8.x = x
                    Point8.y = y
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
                else if (isTouchingPoint(controlPoint7, x, y)) {
                    controlPoint7.x = x
                    controlPoint7.y = y
                }
                else if (isTouchingPoint(controlPoint8, x, y)) {
                    controlPoint8.x = x
                    controlPoint8.y = y
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
        Point1 = PointF(100f, 950f)
        Point2 = PointF(212.5f, 612.5f)
        Point3 = PointF(550f, 500f)
        Point4 = PointF(887.5f, 612.5f)
        Point5 = PointF(1000f, 950f)
        Point6 = PointF(887.5f, 1287.5f)
        Point7 = PointF(550f, 1400f)
        Point8 = PointF(212.5f, 1287.5f)

        controlPoint1 = PointF(156.25f, 781.25f)
        controlPoint2 = PointF(381.25f, 556.25f)
        controlPoint3 = PointF(718.75f, 556.25f)
        controlPoint4 = PointF(943.75f, 781.25f)
        controlPoint5 = PointF(943.75f, 1118.75f)
        controlPoint6 = PointF(718.75f, 1343.75f)
        controlPoint7 = PointF(381.25f, 1343.75f)
        controlPoint8 = PointF(156.25f, 1118.75f)

        invalidate()
    }
}