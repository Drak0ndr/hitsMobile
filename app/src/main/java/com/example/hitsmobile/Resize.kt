package com.example.hitsmobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.Color

class Resize: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.test_rotate)
        var img = findViewById<ImageView>(R.id.imageView)
        var bitmap = (img.getDrawable() as BitmapDrawable).bitmap
        var test = bitmap.getPixel(0,0)
        var ans = upScale(bitmap, 10.1f)
        img.setImageBitmap(ans)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

    }
   fun upScale(bitmap: Bitmap, k: Float): Bitmap {
       var width = (bitmap.width * k).toInt() + 1
       var height = (bitmap.height * k).toInt() + 1
       var scaleBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
       var y = 0
       var scaleY = 0
       var yDist = k
       var yfine = 0f
       while (y < bitmap.height) {
           yDist = k - yfine
           var x = 0
           var scaleX = 0
           var fine = 0f
           while (x < bitmap.width) {
               var colorPixel = bitmap.getColor(x,y).components
               var red = colorPixel[0]
               var green = colorPixel[1]
               var blue = colorPixel[2]
               var alfa = colorPixel[3]
               var xDist = k - fine
               var i = 0
               while (i <= xDist-1) {
                    if (scaleX + i < scaleBitmap.width) {
                        scaleBitmap.setPixel(scaleX + i, scaleY, Color.argb(alfa, red, green, blue))
                    }
                   i++
               }
                if (xDist - i > 0) {
                    fine =1 -( xDist - i)
                    if (x+1 < bitmap.width) {
                        var nextColorPixel = bitmap.getColor(x+1,y).components
                        var nextRed = nextColorPixel[0] * fine + red * (1-fine)
                        var nextGreen = nextColorPixel[1] * fine + green * (1-fine)
                        var nextBlue = nextColorPixel[2] * fine + blue * (1-fine)
                        var nextAlfa = nextColorPixel[3] * fine + alfa * (1-fine)
                        scaleBitmap.setPixel(scaleX + i, scaleY, Color.argb(nextAlfa, nextRed, nextGreen, nextBlue))
                    }
                } else {
                    fine = 0f
                }
               if (xDist > xDist.toInt()) {
                   scaleX+= xDist.toInt() + 1
               } else {
                   scaleX += xDist.toInt()
               }

               x++
           }
           var j = 0
           while (j <= yDist-1) {
               if (scaleY + j < scaleBitmap.height) {
                   x = 0
                   while (x < scaleBitmap.width) {
                       var colorPixel = scaleBitmap.getColor(x,scaleY).components
                       var red = colorPixel[0]
                       var green = colorPixel[1]
                       var blue = colorPixel[2]
                       var alfa = colorPixel[3]
                       scaleBitmap.setPixel(x, scaleY +j, Color.argb(alfa, red, green, blue))
                       x++
                   }

               }
               j++
           }
           if (yDist % 1 > 0) {
               yfine =1 -( yDist % 1)
           } else {
               yfine = 0f
           }
           if (yDist > yDist.toInt()) {
               scaleY+= yDist.toInt() + 1
           } else {
               scaleY += yDist.toInt()
           }
           if (scaleY >= scaleBitmap.height -1) {
               scaleY = scaleBitmap.height - 1
           }
           y++
       }
       y = 0
       while (y < scaleBitmap.height) {
           var x = 0
           while (x < scaleBitmap.width) {
               var colorPixel = scaleBitmap.getColor(x,y).components
               var red = colorPixel[0]
               var green = colorPixel[1]
               var blue = colorPixel[2]
               var alfa = colorPixel[3]
               if (alfa <= 0) {
                   if (y-1 >= 0 && y+1 < scaleBitmap.height) {
                       var prevColor = scaleBitmap.getColor(x,y-1).components
                       var prevRed = prevColor[0]
                       var prevGreen = prevColor[1]
                       var prevBlue = prevColor[2]
                       var prevAlfa = prevColor[3]

                       var nextColor = scaleBitmap.getColor(x,y+1).components
                       var nextRed = nextColor[0]
                       var nextGreen = nextColor[1]
                       var nextBlue = nextColor[2]
                       var nextAlfa = nextColor[3]

                       var newRed = 0.5f * prevRed + 0.5f * nextRed
                       var newGreen = 0.5f * prevGreen + 0.5f * nextGreen
                       var newBlue = 0.5f * prevBlue + 0.5f * nextBlue
                       var newAlfa = 0.5f * prevAlfa + 0.5f * nextAlfa
                       if (prevAlfa > 0 && nextAlfa > 0) {
                           scaleBitmap.setPixel(x, y, Color.argb(newAlfa, newRed, newGreen, newBlue))
                       }

                   }
               }
               x++
           }
           y++
       }
       return scaleBitmap
   }

}