package com.example.hitsmobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.set
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Rotate: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.test_rotate)
        var img = findViewById<ImageView>(R.id.imageView)
        var bitmap = (img.getDrawable() as BitmapDrawable).bitmap
        var test = bitmap.getPixel(0,0)
        var ans = rotateLeft(bitmap)
        ans = rotatefloat(bitmap, 45)
        img.setImageBitmap(ans)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

    }
    fun rotateRight(bitmap: Bitmap): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        var width = bitmap.width
        var height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                rotatedBitmap.setPixel(height -1 -y, x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }
    fun rotateLeft(bitmap: Bitmap): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        var width = bitmap.width
        var height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                rotatedBitmap.setPixel(y, width -1 - x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }

    fun getFourPixels(arr: MutableList<MutableList<Float>>, x0: Float, y0:Float): MutableList<MutableList<Float>> {
        val mutableCopy = arr.toMutableList()
        var ans = mutableListOf<MutableList<Float>>()
        var i = 0
        while (i < 4) {
            var pixel = mutableListOf<Float>()
            var distPixel = Float.MAX_VALUE
            for (item in mutableCopy) {
                var dist = ((item[0] - x0).pow(2) + (item[1] - y0).pow(2)).pow(0.5f)
                if (dist < distPixel) {
                    distPixel = dist
                    pixel = item
                }
            }
            if (distPixel > 2) {
                pixel[5] = 0f
            }
            ans.add(pixel)
//            mutableCopy.remove(pixel)
            i++
        }

        return ans
    }
    fun rotatefloat(bitmap: Bitmap, deg: Int): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.width * 3, bitmap.height * 3,Bitmap.Config.ARGB_8888)
        var arr = mutableListOf<MutableList<Float>>()
        var y0 = bitmap.height - 1
        var x0 = bitmap.width - 1
        var angle = PI / 360 * deg
        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var newX:Float = ((x0 - x) * cos(angle) - (y0- y) * sin(angle)).toFloat()
                var newY:Float = ((x0 - x) * sin(angle) + (y0- y) * cos(angle)).toFloat()

                var data = mutableListOf(newX, newY, red, green, blue, alfa)
                arr.add(data)
                x++
            }
            y++
        }
        var centerX = bitmap.width * 2
        var centerY = bitmap.height * 2 - 1
        y = 0
        while (y < rotatedBitmap.height) {
            var x = 0
            while (x < rotatedBitmap.width) {
                var temp = getFourPixels(arr, (centerX - x).toFloat(), (centerY - y).toFloat())
                var red = 0.25f * (temp[0][2] + temp[1][2] + temp[2][2] + temp[3][2])
                var green = 0.25f * (temp[0][3] + temp[1][3] + temp[2][3] + temp[3][3])
                var blue = 0.25f * (temp[0][4] + temp[1][4] + temp[2][4] + temp[3][4])
                var alfa = 0.25f * (temp[0][5] + temp[1][5] + temp[2][5] + temp[3][5])
                rotatedBitmap.setPixel(x, y, Color.argb(alfa, red, green, blue))

                x++
            }
            y++
        }

        return rotatedBitmap
    }

}