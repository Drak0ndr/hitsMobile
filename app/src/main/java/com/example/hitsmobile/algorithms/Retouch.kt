package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.pow

class Retouch {
    fun gaussian(sigma: Float, y:Int,x:Int): Double {
        val temp =  (- (x*x + y*y) / (2 * sigma.pow(2))).toDouble()
        val ans = (1 / (3.14 * sigma.pow(2)) * 2.71.pow(temp))
        return ans
    }
    fun blurPixel(bitmap: Bitmap, x0:Int, y0:Int, r:Float): MutableList<Float> {
        val kernel = mutableListOf<MutableList<Double>>()
        val len = (r).toInt() * 2 + 1
        val sigma = r/3
        val mid = (r).toInt()
        var i = 0
        while (i < len) {
            val temp = mutableListOf<Double>()
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
                    val tempColors = bitmap.getColor(x0 - mid + x, y0 - mid + y).components
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
        val ans = mutableListOf(tempAlpha, tempRed, tempGreen, tempBlue)
        return ans
    }
    fun blur(bitmap: Bitmap, r:Float, intensive:Float, x0:Int, y0:Int):Bitmap {
        val testBitmap = bitmap.copy(bitmap.config, true)
//        var i = 0
//        while (i < bitmap.width) {
//            var j = 0
//            while (j < bitmap.height) {
//                var tempColor = bitmap.getColor(i,j).components
//                var red = tempColor[0]
//                var green = tempColor[1]
//                var blue = tempColor[2]
//                var alpha = tempColor[3]
//                testBitmap.setPixel(i,j,Color.argb(alpha, red, green, blue))
//                j++
//            }
//            i++
//        }
        var startX = x0 - r.toInt() -1
        var endX = x0 + r.toInt() + 1
        var startY = y0 - r.toInt() - 1
        var endY = y0 + r.toInt() + 1

        if (startX < 0) {
            startX = 0
        }
        if (endX >= bitmap.width) {
            endX = bitmap.width-1
        }
        if (startY < 0) {
            startY = 0
        }
        if (endY >= bitmap.height) {
            endY = bitmap.height-1
        }
        var y = startY
        while (y <= endY && y < bitmap.height) {
            var x = startX
            while (x <= endX && x < bitmap.width) {
                val dist = ((x-x0) * (x-x0) + (y-y0) * (y-y0)).toFloat().pow(0.5f)
                if (dist < r) {
                    val delta = (dist).toInt() * intensive/r
                    val data = blurPixel(bitmap, x,y, intensive - delta)
                    testBitmap.setPixel(x, y, Color.argb(data[0], data[1], data[2], data[3]))
                }
                x++
            }
            y++
        }
        return testBitmap
    }
}