package com.example.hitsmobile.filters

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.example.hitsmobile.PhotoActivity
import com.example.hitsmobile.R
import kotlin.math.exp
import kotlin.math.pow

class ColorFilters: PhotoActivity() {
    fun toGreen(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var green = colorPixel[1]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 0f, green, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }

    fun toBlue(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 0f, 0f, blue))
                y++
            }
            x++
        }
        return newBitmap
    }

    fun toRed(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, red, 0f, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }

    fun toYellow(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, red, green, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }
    fun toNegative(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 1 - red, 1 - green, 1 - blue))
                y++
            }
            x++
        }
        return newBitmap
    }
    fun toGray(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var gray = 0.2126f * red + 0.7152f * green + 0.0722f * blue
                newBitmap.setPixel(x, y, Color.argb(alfa, gray, gray, gray))
                y++
            }
            x++
        }
        return newBitmap
    }
}