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
    fun texturePolygon(v1:Vector, v2:Vector, v3:Vector, texture:String, t1x:Float, t1y:Float, t2x:Float, t2y:Float, t3x:Float, t3y:Float) {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))

        var n1 = getLeftTopVector(v1,v2,v3)
        var n1tx = 0f
        var n1ty = 0f
        if (n1.x == v1.x && n1.y == v1.y) {
            n1tx = t1x
            n1ty = t1y
        } else if (n1.x == v2.x && n1.y == v2.y) {
            n1tx = t2x
            n1ty = t2y
        } else {
            n1tx = t3x
            n1ty = t3y
        }

        var n2 = getRightTopVector(v1,v2,v3)
        var n2tx = 0f
        var n2ty = 0f
        if (n2.x == v1.x && n2.y == v1.y) {
            n2tx = t1x
            n2ty = t1y
        } else if (n2.x == v2.x && n2.y == v2.y) {
            n2tx = t2x
            n2ty = t2y
        } else {
            n2tx = t3x
            n2ty = t3y
        }

        var n3 = getBottomVector(v1, v2, v3)
        var n3tx = 0f
        var n3ty = 0f
        if (n3.x == v1.x && n3.y == v1.y) {
            n3tx = t1x
            n3ty = t1y
        } else if (n3.x == v2.x && n3.y == v2.y) {
            n3tx = t2x
            n3ty = t2y
        } else {
            n3tx = t3x
            n3ty = t3y
        }
        var image = Textures.getBitmap(texture)
        var kx = (image.width - 1) / (maxX - minX)
        var ky = (image.height - 1) / (maxY - minY)
        var x = minX
        while (x <= maxX) {
            var y = minY
            while (y <= maxY) {
                var t1 = (v1.x - x) * (v2.y - v1.y) - (v2.x - v1.x) * (v1.y - y)
                var t2 = (v2.x - x) * (v3.y - v2.y) - (v3.x - v2.x) * (v2.y - y)
                var t3 = (v3.x - x) * (v1.y - v3.y) - (v1.x - v3.x) * (v3.y - y)
                if ((t1 <= 0 && t2 <= 0 && t3 <= 0) || (t1 >= 0 && t2 >= 0 && t3 >= 0)) {
                    var ox = Vector.substruct(n2, n1)
                    var oy = Vector.substruct(n3,n1)
                    var pointX = Vector(x, y, 0f)
                    var dir = Vector.substruct(pointX, n1)
                    var tempCosX = Vector.calculateCos(ox, dir)
                    var tempLen = dir.getLength()
                    var tx = tempLen * tempCosX

                    var textureX = tx / ox.getLength() * (image.width - 1)
                    if (textureX < 0) {
                        textureX = 0f
                    }
                    if (textureX > image.width - 1) {
                        textureX = (image.width - 1).toFloat()
                    }

                    var tempCosY = Vector.calculateCos(oy, dir)
                    var ty = tempLen * tempCosY

                    var textureY = ty / oy.getLength() * (image.height - 1)
                    if (textureY < 0) {
                        textureY = 0f
                    }
                    if (textureY > image.height - 1) {
                        textureY = (image.height - 1).toFloat()
                    }
                    var colors = image.getColor(((x - minX) * kx).toInt(), ((y - minY) * ky).toInt()).components
                    var r = colors[0]
                    var g = colors[1]
                    var b = colors[2]
                    drawPixel(x.toInt(), y.toInt(),r, g, b)
                }

                y++
            }
            x++
        }
    }
}