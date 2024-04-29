package com.example.hitsmobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.Color

class ColorFilters: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.test_color_filters)
        var img = findViewById<ImageView>(R.id.imageView)
        var bitmap = (img.getDrawable() as BitmapDrawable).bitmap
        var test = bitmap.getPixel(0,0)
        var ans = toGray(bitmap)
        img.setImageBitmap(ans)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

    }
    fun toGreen(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 0f, green, 0f))
                y++
            }
            x++
        }

        return newBitmap
    }
    fun toNegative(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 1 - red,1- green, 1 - blue))
                y++
            }
            x++
        }

        return newBitmap
    }
    fun toGray(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var gray = 0.2126f * red + 0.7152f * green + 0.0722f * blue
                newBitmap.setPixel(x, y, Color.argb(alfa, gray,gray, gray))
                y++
            }
            x++
        }

        return newBitmap
    }

}