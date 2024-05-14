package com.example.hitsmobile.canvas

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.example.hitsmobile.R
import com.example.hitsmobile.activity.PhotoActivity
import com.example.hitsmobile.algorithms.Retouch
import kotlin.math.floor


class PhotoImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private var paint: Paint = Paint()
    private var path: Path = Path()

    private var pX = 0f
    private var pY = 0f

    var seekBarRetouchRadius = findViewById<SeekBar>(R.id.seekBarRetouchRadius)
    var seekBarRetouchSharpness = findViewById<SeekBar>(R.id.seekBarRetouchSharpness)

    var firstProgress = (seekBarRetouchRadius.progress).toFloat()

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(PhotoActivity.MyVariables.isRetouch){
            var imageView = findViewById<View>(R.id.photoEditorView) as ImageView

            var PictureWidth = imageView.drawable.intrinsicWidth
            var PictureHeight = imageView.drawable.intrinsicHeight

            var retouch = Retouch()
            var retouchImg : Bitmap

            /*var ViewWidth = imageView.width
            var ViewHeight = imageView.height*/

            val otl: OnTouchListener = object : OnTouchListener {
                var inverse = Matrix()

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    imageView.imageMatrix.invert(inverse)

                    val pts = floatArrayOf(event.x, event.y)

                    inverse.mapPoints(pts)

                    if(pX != floor(pts[0]) && pY != floor(pts[1]) &&
                        floor(pts[0]) > 0 && floor(pts[1]) > 0 &&
                        floor(pts[0]) < PictureWidth && floor(pts[1]) < PictureHeight){

                        Log.d(TAG, "on x: " + floor(pts[0].toDouble()) + ", y: " + floor(pts[1].toDouble()))

                        retouchImg = retouch.blur(PhotoActivity.MyVariables.currImg, 5f,
                            5f, floor(pts[0]).toInt(), floor(pts[1]).toInt())

                        imageView.setImageBitmap(retouchImg)
                        PhotoActivity.MyVariables.rotateImg = retouchImg

                        pX = floor(pts[0])
                        pY = floor(pts[1])
                    }

                    return false
                }
            }

            imageView.setOnTouchListener(otl)


            /*when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    var x = event.x
                    var y = event.y

                    Log.d("jj", "$PictureWidth, $PictureHeight, $ViewWidth, $ViewHeight")

                    var k = PictureWidth.toDouble() / ViewWidth.toDouble()
                    var cX = (k * x.toDouble()) - (ViewWidth - PictureWidth) / 2
                    var cY = k * y.toDouble() - (ViewHeight - PictureHeight) / 2
                    Log.d("нажатие", "X: $cX, Y: $cY")

                    var retoush = Retouch()

                    var r = retoush.blur(PhotoActivity.MyVariables.currImg,20f, 20f, x.toInt(), y.toInt())
                    imageView.setImageBitmap(r)

                    return true
                }
            }*/

            invalidate()
            return true
        }

        return true
    }
}
