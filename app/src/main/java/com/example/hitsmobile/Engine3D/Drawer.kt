package com.example.hitsmobile.Engine3D

import android.graphics.Bitmap
import android.graphics.Color
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
        bitmap.setPixel(x,y, Color.argb(1f, r, g, b))
    }

    fun clearSurface() {
        bitmap.eraseColor(Color.argb(0, 0, 0 ,0))
    }

    fun drawLine(x1:Int, y1:Int, x2:Int, y2:Int, r:Float, g:Float, b:Float) {
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
}