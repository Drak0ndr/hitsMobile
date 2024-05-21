package com.example.hitsmobile.Engine3D

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    fun fillPixel (x:Int, y:Int) {
        var x = (x + width/2f).toInt()
        var y = (-(y - height/2 * 1.01)).toInt()
        var r = 0f
        var g = 0f
        var b = 0f
        var a = 0f
        var k = 0f
        if (x >= 0 && x < bitmap.width && y >= 0 && y < bitmap.height) {
            if (x-1 >= 0) {
                var tempColor = bitmap.getColor(x-1,y).components
                if (tempColor[3] > 0) {
                    r+= tempColor[0]
                    g+= tempColor[1]
                    b+= tempColor[2]
                    a += tempColor[3]
                    k+=1
                }
            }
            if (x+1 < bitmap.width) {
                var tempColor = bitmap.getColor(x+1,y).components
                if (tempColor[3] > 0) {
                    r+= tempColor[0]
                    g+= tempColor[1]
                    b+= tempColor[2]
                    a += tempColor[3]
                    k+=1
                }
            }
            if (y-1 >= 0) {
                var tempColor = bitmap.getColor(x,y-1).components
                if (tempColor[3] > 0) {
                    r+= tempColor[0]
                    g+= tempColor[1]
                    b+= tempColor[2]
                    a += tempColor[3]
                    k+=1
                }
            }
            if (y+1 < bitmap.height) {
                var tempColor = bitmap.getColor(x,y+1).components
                if (tempColor[3] > 0) {
                    r+= tempColor[0]
                    g+= tempColor[1]
                    b+= tempColor[2]
                    a += tempColor[3]
                    k+=1
                }
            }
            var tempColor = bitmap.getColor(x,y).components
            if (tempColor[3] > 0) {
                r+= tempColor[0]
                g+= tempColor[1]
                b+= tempColor[2]
                a += tempColor[3]
                k+=1
            }
            if (k < 1) {
                k = 1f
                a = 1f
            }
            bitmap.setPixel(x,y, Color.argb(a/k, r/k, g/k, b/k))
        }
    }
    fun fillLine(x1:Float, y1:Float, x2:Float, y2:Float) {
        val c1 = y2 - y1
        val c2 = x2 - x1

        val length = (c1*c1 + c2*c2).toFloat().pow(0.5f)

        val xStep = c2 / length
        val yStep = c1 / length

        var i = 0
        while (i < length) {
            runBlocking {
                launch {
                    fillPixel((x1 + xStep*i).toInt(), (y1 + yStep*i).toInt())
                }
                launch {
                    fillPixel((x1 + xStep*i).toInt() - 1, (y1 + yStep*i).toInt())
                }
                launch {
                    fillPixel((x1 + xStep*i).toInt() + 1, (y1 + yStep*i).toInt())
                }
                launch {
                    fillPixel((x1 + xStep*i).toInt() - 2, (y1 + yStep*i).toInt())
                }
                launch {
                    fillPixel((x1 + xStep*i).toInt() + 2, (y1 + yStep*i).toInt())
                }
            }
            i++
        }
    }

    fun fillPolygon(v1:Vector, v2:Vector, v3:Vector, r:Float, g:Float, b:Float) {
        var minX = min(v3.x ,min(v1.x, v2.x))
        var minY = min(v3.y ,min(v1.y, v2.y))
        var maxX = max(v3.x ,max(v1.x, v2.x))
        var maxY = max(v3.y ,max(v1.y, v2.y))

        runBlocking {
            launch(Dispatchers.IO) {
                var d = ((maxX - minX) / 2).toInt()
                var x = minX
                while (x <= minX + d) {
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

            launch(Dispatchers.IO) {
                var d = ((maxX - minX) / 2).toInt()
                var x = minX + d
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

        var wz1 = awz + (cwz - awz) * (b.y - a.y) / (c.y - a.y)
        var uz1 = auz + (cuz - auz) * (b.y - a.y) / (c.y - a.y)
        var vz1 = avz + (cvz - avz) * (b.y - a.y) / (c.y - a.y)


        var x2 = b.x
        var uz2 = buz
        var vz2 = bvz
        var wz2 = bwz
        var duz = (uz1 - uz2) / (x1 - x2)
        var dvz = (vz1 - vz2) / (x1 - x2)
        var dwz = (wz1 - wz2) / (x1 - x2)


        var x3 = 0f
        var uz3 = 0f
        var vz3 = 0f
        var wz3 = 0f

        var u = 0f
        var v = 0f

        var img = Textures.getBitmap(texture)

        var y = a.y.toInt()
        while (y <= c.y.toInt()) {
            x1 = a.x  + (c.x - a.x) * (y - a.y) / (c.y - a.y)
            uz1 = auz  + (cuz - auz) * (y - a.y) / (c.y - a.y)
            vz1 = avz  + (cvz - avz) * (y - a.y) / (c.y - a.y)
            wz1 = awz  + (cwz - awz) * (y - a.y) / (c.y - a.y)

            if (y >= b.y) {
                x2 = b.x + (c.x - b.x) * (y - b.y) / (c.y - b.y)
                uz2 = buz + (cuz - buz) * (y - b.y) / (c.y - b.y)
                vz2 = bvz + (cvz - bvz) * (y - b.y) / (c.y - b.y)
                wz2 = bwz + (cwz - bwz) * (y - b.y) / (c.y - b.y)
            } else {
                x2 = a.x + (b.x - a.x) * (y - a.y) / (b.y - a.y)
                uz2 = auz + (buz - auz) * (y - a.y) / (b.y - a.y)
                vz2 = avz + (bvz - avz) * (y - a.y) / (b.y - a.y)
                wz2 = awz + (bwz - awz) * (y - a.y) / (b.y - a.y)
            }

            if (x1 > x2) {
                x3 = x1
                x1 = x2
                x2 = x3

                uz3 = uz1
                uz1 = uz2
                uz2 = uz3

                vz3 = vz1
                vz1 = vz2
                vz2 = vz3

                wz3 = wz1
                wz1 = wz2
                wz2 = wz3

            }

            var lenght = (x2 - x1).toInt()
            var uz_a = uz1
            var vz_a = vz1
            var wz_a = wz1
            var u_a = uz_a / wz_a
            var v_a = vz_a / wz_a
            var x = x1.toInt()
            while (lenght >= 8) {
                var uz_b = uz_a + 8 * duz
                var vz_b = vz_a + 8 * dvz
                var wz_b = wz_a + 8 * dwz
                var u_b = uz_b / wz_b
                var v_b = vz_b / wz_b

                u = u_a
                v = v_a

                var du = (u_b - u_a) / 8
                var dv = (v_b - v_a) / 8

                var len = 8
                while (len > 0) {
                    if (u.toInt() >= 0 && u.toInt() < img.width && v.toInt() >= 0 && v.toInt() < img.height) {
                        var tempColors = img.getColor(u.toInt(),v.toInt()).components
                        var r = tempColors[0]
                        var g = tempColors[1]
                        var b = tempColors[2]

                        drawPixel(x.toInt(), y.toInt(), r, g, b)
                    }
                    x++
                    u += du
                    v += dv
                    len -= 1
                }

                lenght-=8
                uz_a = uz_b
                vz_a = vz_b
                wz_a = wz_b
                u_a = u_b
                v_a = v_b

            }

            if (lenght > 0) {
                var uz_b = uz2
                var vz_b = vz2
                var wz_b = wz2
                var u_b = uz_b / wz_b
                var v_b = vz_b / wz_b

                u = u_a
                v = v_a

                var du = (u_b - u_a) / lenght
                var dv = (v_b - v_a) / lenght

                while (lenght > 0) {
                    if (u.toInt() >= 0 && u.toInt() < img.width && v.toInt() >= 0 && v.toInt() < img.height) {
                        var tempColors = img.getColor(u.toInt(),v.toInt()).components
                        var r = tempColors[0]
                        var g = tempColors[1]
                        var b = tempColors[2]

                        drawPixel(x.toInt(), y.toInt(), r, g, b)
                    }
                    x++
                    u += du
                    v += dv
                    lenght-=1
                }
            }

            y++
        }
    }
}