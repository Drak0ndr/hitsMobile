package com.example.hitsmobile.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.example.hitsmobile.R


class PhotoImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var paint: Paint = Paint()
    private var path: Path = Path()

    init {
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var imageView = findViewById<View>(R.id.photoEditorView) as ImageView

        var PictureWidth = imageView.drawable.intrinsicWidth
        var PictureHeight = imageView.drawable.intrinsicHeight

        var ViewWidth = imageView.width
        var ViewHeight = imageView.height


        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var x = event.x
                var y = event.y

                Log.d("jj", "$PictureWidth, $PictureHeight, $ViewWidth, $ViewHeight")

                var k = PictureWidth.toDouble() / ViewWidth.toDouble()
                var cX = k * x.toDouble()
                var cY = k * y.toDouble()
                Log.d("нажатие", "X: $cX, Y: $cY")


                return true
            }
        }

        invalidate()
        return true
    }
}
