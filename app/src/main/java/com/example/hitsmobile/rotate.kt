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
import androidx.core.graphics.set

class Rotate: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.test_rotate)
        var img = findViewById<ImageView>(R.id.imageView)
        var bitmap = (img.getDrawable() as BitmapDrawable).bitmap
        var test = bitmap.getPixel(0,0)
        var ans = rotateLeft(bitmap)
//        img.setImageBitmap(ans)
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


}