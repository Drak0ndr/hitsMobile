package com.example.hitsmobile.Engine3D

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


class Drawer {
    var width = 1
    var height = 1
    var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    constructor(_width:Int, _height:Int) {
        width = _width
        height = _height
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    fun drawPixel(x:Int, y:Int, r:Float, g:Float, b:Float) {
        var x = x + width/2
        var y = -(y - height/2 * 1.01)
        if (x >= 0 && x < bitmap.width && y > 0 && y < bitmap.height) {
            bitmap.setPixel(x.toInt(),y.toInt(), Color.argb(1f, r, g, b))
        }
    }

    fun clearSurface() {
        bitmap.eraseColor(Color.argb(0, 0, 0 ,0))
    }

    fun drawLine(x1:Float, y1:Float, x2:Float, y2:Float, r:Float, g:Float, b:Float) {
        val c1 = y2 - y1
        val c2 = x2 - x1

        val length = (c1*c1 + c2*c2).toFloat().pow(0.5f)

        val xStep = c2 / length
        val yStep = c1 / length

        var i = 0
        while (i < length) {
            drawPixel((x1 + xStep*i).toInt(), (y1 + yStep*i).toInt(), r, g, b)
            i++
        }
    }

    fun fillPolygon(v1:Vector, v2:Vector, v3:Vector, r:Float, g:Float, b:Float) {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))


        var x = minX
        while (x <= maxX) {
            var y = minY
            while (y <= maxY) {
                var t1 = (v1.x - x) * (v2.y - v1.y) - (v2.x - v1.x) * (v1.y - y)
                var t2 = (v2.x - x) * (v3.y - v2.y) - (v3.x - v2.x) * (v2.y - y)
                var t3 = (v3.x - x) * (v1.y - v3.y) - (v1.x - v3.x) * (v3.y - y)
                if ((t1 <= 0 && t2 <= 0 && t3 <= 0) || (t1 >= 0 && t2 >= 0 && t3 >= 0)) {
                    drawPixel(x.toInt(), y.toInt(),r, g, b)
                }

                y++
            }
            x++
        }
    }
}