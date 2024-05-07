package com.example.hitsmobile.filters

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.PhotoActivity
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
    fun gaussian(sigma: Float, y:Int,x:Int): Double {
        var temp =  (- (x*x + y*y) / (2 * sigma.pow(2))).toDouble()
        var ans = (1 / (3.14 * sigma.pow(2)) * 2.71.pow(temp))
        return ans
    }
    fun gausBlur(bitmap: Bitmap, r: Float): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var kernel = mutableListOf<MutableList<Double>>()
        var len = (r).toInt() * 2 + 1
        var sigma = r/3
        var mid = (r).toInt()
        var i = 0
        while (i < len) {
            var temp = mutableListOf<Double>()
            var j = 0
            while (j < len) {
                temp.add(gaussian(sigma, i-mid, j-mid))
                j++
            }
            kernel.add(temp)
            i++
        }

        i = 0
        while (i < bitmap.height) {
            var j = 0
            while (j < bitmap.width) {
                var tempRed = 0f
                var tempGreen = 0f
                var tempBlue = 0f
                var tempAlpha = 0f
                var kernelCoef = 0.0
                var y = 0
                while (y < len) {
                    var x = 0
                    while (x < len) {
                        if ((j - mid + x) >= 0 && (j - mid + x) < bitmap.width && (i - mid + y) >= 0 && (i - mid + y) < bitmap.height) {
                            var tempColors = bitmap.getColor(j - mid + x, i - mid + y).components
                            tempRed+= (tempColors[0] * kernel[y][x]).toFloat()
                            tempGreen+= (tempColors[1] * kernel[y][x]).toFloat()
                            tempBlue+= (tempColors[2] * kernel[y][x]).toFloat()
                            tempAlpha+= (tempColors[3] * kernel[y][x]).toFloat()
                            kernelCoef+= kernel[y][x]
                        }

                        x++
                    }
                    y++
                }
                tempRed = (tempRed / kernelCoef).toFloat()
                tempGreen = (tempGreen / kernelCoef).toFloat()
                tempBlue = (tempBlue / kernelCoef).toFloat()
                tempAlpha = (tempAlpha / kernelCoef).toFloat()
                newBitmap.setPixel(j,i, Color.argb(tempAlpha, tempRed, tempGreen, tempBlue))
                j++
            }
            i++
        }
        return newBitmap
    }
}