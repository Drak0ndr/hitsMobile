package com.example.hitsmobile

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.*

class Retouch {
    fun gaussian(sigma: Float, y:Int,x:Int): Double {
        var temp =  (- (x*x + y*y) / (2 * sigma.pow(2))).toDouble()
        var ans = (1 / (3.14 * sigma.pow(2)) * 2.71.pow(temp))
        return ans
    }
    fun blurPixel(bitmap: Bitmap, x0:Int, y0:Int, r:Float): MutableList<Float> {
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
        var tempRed = 0f
        var tempGreen = 0f
        var tempBlue = 0f
        var tempAlpha = 0f
        var kernelCoef = 0.0
        var y = 0
        while (y < len) {
            var x = 0
            while (x < len) {
                if ((x0 - mid + x) >= 0 && (x0 - mid + x) < bitmap.width && (y0 - mid + y) >= 0 && (y0 - mid + y) < bitmap.height) {
                    var tempColors = bitmap.getColor(x0 - mid + x, y0 - mid + y).components
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
        var ans = mutableListOf(tempAlpha, tempRed, tempGreen, tempBlue)
        return ans
    }
    fun blur(bitmap: Bitmap, r:Float, intensive:Float, x0:Int, y0:Int):Bitmap {
        val startX = x0 - r.toInt() -1
        val endX = x0 + r.toInt() + 1
        val startY = y0 - r.toInt() - 1
        val endY = y0 + r.toInt() + 1
        var y = startY
        while (y <= endY && y < bitmap.height) {
            var x = startX
            while (x <= endX && x < bitmap.width) {
                var dist = ((x-x0) * (x-x0) + (y-y0) * (y-y0)).toFloat().pow(0.5f)
                if (dist <= r) {
                    var delta = (r - dist).toInt()
                    var data = blurPixel(bitmap, x,y, intensive - delta)
                    bitmap.setPixel(x, y, Color.argb(data[0], data[1], data[2], data[3]))
                }
                x++
            }
            y++
        }
        return bitmap
    }
}