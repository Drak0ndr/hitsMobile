package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color

class Affinis {

    fun calcValues(x11:Float, y11:Float, x12:Float, y12:Float, x13:Float, y13:Float, x01:Float, x02:Float, x03:Float, y01:Float, y02:Float, y03:Float): MutableList<Float> {
        val d = x11 * (y12 - y13) + x12 * (y13 - y11) + x13 * (y11 - y12)
        val dax = x01 * (y12 - y13) + x02 * (y13 - y11) + x03 * (y11 - y12)
        val dbx = x11 * (x02 - x03) + x12 * (x03 - x01) + x13 * (x01 - x02)

        val ax = dax / d
        val bx = dbx / d
        val cx = x01 - ax * x11 - bx * y11

        val day = y01 * (y12 - y13) + y02 * (y13 - y11) + y03 * (y11 - y12)
        val dby = x11 * (y02 - y03) + x12 * (y03 - y01) + x13 * (y01 - y02)

        val ay = day / d
        val by = dby / d
        val cy = y01 - ay * x11 - by * y11

        return mutableListOf(ax, bx, cx, ay, by, cy)
    }

    fun transformBitmap(bitmap: Bitmap, x11:Float, y11:Float, x12:Float, y12:Float, x13:Float, y13:Float, x01:Float, x02:Float, x03:Float, y01:Float, y02:Float, y03:Float): Bitmap {
        val data = calcValues(x11, y11, x12, y12, x13, y13, x01, x02, x03, y01, y02, y03)

        val ax = data[0]
        val bx = data[1]
        val cx = data[2]
        val ay = data[3]
        val by = data[4]
        val cy = data[5]

        val width = bitmap.width
        val height = bitmap.height
        var resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        var x = 0
        while (x < width) {
            var y = 0
            while (y < height) {
                var newX = (ax * x + bx * y + cx).toInt()
                var newY = (ay * x + by * y + cy).toInt()
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    val tempColors = bitmap.getColor(x,y).components
                    val r = tempColors[0]
                    val g = tempColors[1]
                    val b = tempColors[2]
                    val a = tempColors[3]
                    resultBitmap.setPixel(newX, newY, Color.argb(a, r, g, b))
                }
                y++
            }
            x++
        }

        return resultBitmap
    }
}