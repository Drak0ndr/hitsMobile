package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.activity.PhotoActivity
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Rotate: PhotoActivity() {
    fun rotateRight(bitmap: Bitmap): Bitmap {
        val rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        val width = bitmap.width
        val height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                val colorPixel = bitmap.getColor(x,y).components
                val red = colorPixel[0]
                val green = colorPixel[1]
                val blue = colorPixel[2]
                val alfa = colorPixel[3]
                rotatedBitmap.setPixel(height -1 -y, x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }

    fun rotateLeft(bitmap: Bitmap): Bitmap {
        val rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        val width = bitmap.width
        val height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                val colorPixel = bitmap.getColor(x,y).components
                val red = colorPixel[0]
                val green = colorPixel[1]
                val blue = colorPixel[2]
                val alfa = colorPixel[3]
                rotatedBitmap.setPixel(y, width -1 - x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }

    fun getFourPixels(arr: MutableList<MutableList<Float>>, x0: Float, y0:Float): MutableList<MutableList<Float>> {
        val mutableCopy = arr.toMutableList()
        val ans = mutableListOf<MutableList<Float>>()
        var i = 0
        while (i < 4) {
            var pixel = mutableListOf<Float>()
            var distPixel = Float.MAX_VALUE
            for (item in mutableCopy) {
                val dist = ((item[0] - x0).pow(2) + (item[1] - y0).pow(2)).pow(0.5f)
                if (dist < distPixel) {
                    distPixel = dist
                    pixel = item
                }
            }
            if (distPixel > 2) {
                pixel[5] = 0f
            }
            ans.add(pixel)
            mutableCopy.remove(pixel)
            i++
        }

        return ans
    }

    fun rotatefloat(bitmap: Bitmap, deg: Int): Bitmap {
        val rotatedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)
        val arr = mutableListOf<MutableList<Float>>()
        val y0 = bitmap.height /2
        val x0 = bitmap.width /2
        val angle = PI / 360 * deg
        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                val colorPixel = bitmap.getColor(x,y).components
                val red = colorPixel[0]
                val green = colorPixel[1]
                val blue = colorPixel[2]
                val alfa = colorPixel[3]
                val newX:Float = ((x0 - x) * cos(angle) - (y0- y) * sin(angle)).toFloat()
                val newY:Float = ((x0 - x) * sin(angle) + (y0- y) * cos(angle)).toFloat()

                val data = mutableListOf(newX, newY, red, green, blue, alfa)
                arr.add(data)
                x++
            }
            y++
        }
        val centerX = rotatedBitmap.width / 2
        val centerY = rotatedBitmap.height / 2
        y = 0
        while (y < rotatedBitmap.height) {
            var x = 0
            while (x < rotatedBitmap.width) {
                val temp = getFourPixels(arr, (centerX - x).toFloat(), (centerY - y).toFloat())
                val red =  0.5f * temp[0][2] + 0.25f * temp[1][2] + 0.13f * temp[2][2] + 0.12f * temp[3][2]
                val green = 0.5f * temp[0][3] + 0.25f * temp[1][3] + 0.13f * temp[2][3] + 0.12f * temp[3][3]
                val blue = 0.5f * temp[0][4] + 0.25f * temp[1][4] + 0.13f * temp[2][4] + 0.12f * temp[3][4]
                val alfa = 0.5f * temp[0][5] + 0.25f * temp[1][5] + 0.13f * temp[2][5] + 0.12f * temp[3][5]
                rotatedBitmap.setPixel(x, y, Color.argb(alfa, red, green, blue))

                x++
            }
            y++
        }

        return rotatedBitmap
    }

    fun rotateAny(bitmap: Bitmap, deg: Int): Bitmap {

        val arr = mutableListOf<MutableList<Float>>()
        val y0 = bitmap.height /2
        val x0 = bitmap.width /2
        val angle = PI / 180 * deg
        val newWidth = (x0 * cos(angle) + y0 * abs(sin(angle))).toInt() * 2
        val newHeight = (x0 * abs(sin(angle)) + y0 * cos(angle)).toInt() * 2
        val rotatedBitmap = Bitmap.createBitmap(newWidth, newHeight ,Bitmap.Config.ARGB_8888)
        var y = 0
        val xCenter = rotatedBitmap.width / 2
        val yCenter = rotatedBitmap.height / 2
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                val colorPixel = bitmap.getColor(x,y).components
                val red = colorPixel[0]
                val green = colorPixel[1]
                val blue = colorPixel[2]
                val alfa = colorPixel[3]
                val newX:Float = ((x0 - x) * cos(angle) - (y0- y) * sin(angle)).toFloat()
                val newY:Float = ((x0 - x) * sin(angle) + (y0- y) * cos(angle)).toFloat()
                var bestDist = Float.MAX_VALUE
                var bestX = 0
                var bestY = 0
                val indX = newX.toInt()
                val indY = newY.toInt()

                var i = -1
                while (i <= 1) {
                    var j = -1
                    while (j <= 1) {
                        val dist = ((indX + i - newX).pow(2) + (indY + j - newY).pow(2)).pow(0.5f)
                        if (dist < bestDist) {
                            bestDist = dist
                            bestX = indX + i
                            bestY = indY + j
                        }
                        j++
                    }
                    i++
                }
                if ((xCenter - bestX) < rotatedBitmap.width && (xCenter - bestX) >= 0 && (yCenter - bestY) < rotatedBitmap.height && (yCenter - bestY) >= 0) {
                    rotatedBitmap.setPixel(xCenter - bestX, yCenter -bestY, Color.argb(alfa, red, green, blue))
                }

                val data = mutableListOf(newX, newY, red, green, blue, alfa)
                arr.add(data)
                x++
            }
            y++
        }
        val ans = fillEmptyPixels(rotatedBitmap)
        return ans
    }

    fun fillEmptyPixels(bitmap: Bitmap): Bitmap {
        val newBitmap = bitmap.copy(bitmap.config, true)
        var x = 0
        var y = 0
        while (x < bitmap.width) {
            y = 0
            while (y < bitmap.height) {
                val colorPixel = bitmap.getColor(x,y).components
                val neighboringPixels = mutableListOf<MutableList<Float>>()

                if (colorPixel[3] <= 0) {
                    var i = -1
                    var j = -1
                    while (i <= 1) {
                        j = -1
                        while (j <=1) {
                            if ((x+i) >= 0 && (x+i) < bitmap.width && (y+j) >=0 && (y+j) < bitmap.height) {
                                val tempColorPixel = bitmap.getColor(x + i,y + j).components
                                val red = tempColorPixel[0]
                                val green = tempColorPixel[1]
                                val blue = tempColorPixel[2]
                                val alfa = tempColorPixel[3]
                                if (alfa > 0) {
                                    val data = mutableListOf(red, green, blue, alfa)
                                    neighboringPixels.add(data)
                                }
                            }
                            j++
                        }
                        i++
                    }
                    var red = 0f
                    var green = 0f
                    var blue = 0f
                    var alfa = 0f
                    if (neighboringPixels.count() >= 5) {
                        for (item in neighboringPixels) {
                            red += item[0] / neighboringPixels.count()
                            green += item[1] / neighboringPixels.count()
                            blue += item[2] / neighboringPixels.count()
                            alfa += item[3] / neighboringPixels.count()
                        }
                    }

                    newBitmap.setPixel(x, y, Color.argb(alfa, red, green, blue))
                }
                y++
            }
            x++
        }
        return newBitmap
    }
}