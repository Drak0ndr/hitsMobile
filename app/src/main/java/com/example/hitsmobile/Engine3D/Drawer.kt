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
    fun getLeftTopVector(v1:Vector, v2:Vector, v3:Vector):Vector {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))
        var tempn = mutableListOf<Vector>()
        if (v1.x == minX) {
            tempn.add(v1)
        }
        if (v2.x == minX) {
            tempn.add(v2)
        }
        if (v3.x == minX) {
            tempn.add(v3)
        }
        var ans = tempn[0]
        var i = 0
        while (i < tempn.size) {
            if (tempn[i].y > ans.y) {
                ans = tempn[i]
            }
            i++
        }

        return ans
    }

    fun getRightTopVector(v1:Vector, v2:Vector, v3:Vector):Vector {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))
        var tempn = mutableListOf<Vector>()
        if (v1.x == maxX) {
            tempn.add(v1)
        }
        if (v2.x == maxX) {
            tempn.add(v2)
        }
        if (v3.x == maxX) {
            tempn.add(v3)
        }
        var ans = tempn[0]
        var i = 0
        while (i < tempn.size) {
            if (tempn[i].y > ans.y) {
                ans = tempn[i]
            }
            i++
        }

        return ans
    }

    fun getBottomVector(v1:Vector, v2:Vector, v3:Vector):Vector {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))
        var tempn = mutableListOf<Vector>()
        if (v1.y == minY) {
            tempn.add(v1)
        }
        if (v2.y == minY) {
            tempn.add(v2)
        }
        if (v3.y == minY) {
            tempn.add(v3)
        }
        var ans = tempn[0]
        var i = 0
        while (i < tempn.size) {
            if (tempn[i].x < ans.x) {
                ans = tempn[i]
            }
            i++
        }

        return ans
    }
    fun texturePolygon(a:Vector, b:Vector, c:Vector, texture:String, t1x:Float, t1y:Float, t2x:Float, t2y:Float, t3x:Float, t3y:Float) {
        var d = Vector(0f,0f,0f)
        var temp = 0f
        var a = a
        var au = t1x
        var av = t1y
        var b= b
        var bu = t2x
        var bv = t2y
        var c= c
        var cu = t3x
        var cv = t3y
        if (a.y > b.y) {
            d = a
            a = b
            b = d
            temp = au
            au = bu
            bu = temp
            temp = av
            av = bv
            bv = temp
        }

        if (a.y > c.y) {
            d = a
            a = c
            c = d
            temp = au
            au = cu
            cu = temp
            temp = av
            av = cv
            cv = temp
        }

        if (b.y > c.y) {
            d = b
            b = c
            c = d
            temp = bu
            bu = cu
            cu = temp
            temp = bv
            bv = cv
            cv = temp
        }

        if (a.y == c.y) {
            return
        }

        var awz = 1f / a.z
        var auz = au * awz
        var avz = av * awz

        var bwz = 1f / b.z
        var buz = bu * bwz
        var bvz = bv * bwz

        var cwz = 1f / c.z
        var cuz = cu * cwz
        var cvz = cv * cwz

        var x1 = a.x + (c.x - a.x) * (b.y - a.y) / (c.y - a.y)
        var u1 = au + (cu - au) * (b.y - a.y) / (c.y - a.y)
        var v1 = av + (cv - av) * (b.y - a.y) / (c.y - a.y)
        var x2 = b.x
        var u2 = bu
        var v2 = bv
        var du = (u1 - u2) / (x1 - x2)
        var dv = (v1 - v2) / (x1 - x2)
        var x3 = 0f
        var u3 = 0f
        var v3 = 0f

        var u = 0f
        var v = 0f

        var img = Textures.getBitmap(texture)

        var y = a.y.toInt()
        while (y <= c.y.toInt()) {
            x1 = a.x  + (c.x - a.x) * (y - a.y) / (c.y - a.y)
            u1 = au  + (cu - au) * (y - a.y) / (c.y - a.y)
            v1 = av  + (cv - av) * (y - a.y) / (c.y - a.y)

            if (y >= b.y) {
                x2 = b.x + (c.x - b.x) * (y - b.y) / (c.y - b.y)
                u2 = bu + (cu - bu) * (y - b.y) / (c.y - b.y)
                v2 = bv + (cv - bv) * (y - b.y) / (c.y - b.y)
            } else {
                x2 = a.x + (b.x - a.x) * (y - a.y) / (b.y - a.y)
                u2 = au + (bu - au) * (y - a.y) / (b.y - a.y)
                v2 = av + (bv - av) * (y - a.y) / (b.y - a.y)
            }

            if (x1 > x2) {
                x3 = x1
                x1 = x2
                x2 = x3

                u3 = u1
                u1 = u2
                u2 = u3

                v3 = v1
                v1 = v2
                v2 = v3
            }
            u = u1
            v = v1
            var x = x1.toInt()
            while (x <= x2.toInt()) {

                var tempColors = img.getColor(u.toInt(),v.toInt()).components
                var r = tempColors[0]
                var g = tempColors[1]
                var b = tempColors[2]

                drawPixel(x.toInt(), y.toInt(), r, g, b)
                u+= du
                v+=dv
                x++
            }
            y++
        }
    }
}