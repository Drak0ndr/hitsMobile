package com.example.hitsmobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.set
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Rotate: PhotoActivity() {
    fun rotateRight(bitmap: Bitmap): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        var width = bitmap.width
        var height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                rotatedBitmap.setPixel(height -1 -y, x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }

    fun rotateLeft(bitmap: Bitmap): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.height, bitmap.width,Bitmap.Config.ARGB_8888)
        var width = bitmap.width
        var height = bitmap.height
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y< height) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                rotatedBitmap.setPixel(y, width -1 - x, Color.argb(alfa, red, green, blue))
                y++
            }
            x++
        }
        return rotatedBitmap
    }

    fun getFourPixels(arr: MutableList<MutableList<Float>>, x0: Float, y0:Float): MutableList<MutableList<Float>> {
        val mutableCopy = arr.toMutableList()
        var ans = mutableListOf<MutableList<Float>>()
        var i = 0
        while (i < 4) {
            var pixel = mutableListOf<Float>()
            var distPixel = Float.MAX_VALUE
            for (item in mutableCopy) {
                var dist = ((item[0] - x0).pow(2) + (item[1] - y0).pow(2)).pow(0.5f)
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
        var rotatedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)
        var arr = mutableListOf<MutableList<Float>>()
        var y0 = bitmap.height /2
        var x0 = bitmap.width /2
        var angle = PI / 360 * deg
        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var newX:Float = ((x0 - x) * cos(angle) - (y0- y) * sin(angle)).toFloat()
                var newY:Float = ((x0 - x) * sin(angle) + (y0- y) * cos(angle)).toFloat()

                var data = mutableListOf(newX, newY, red, green, blue, alfa)
                arr.add(data)
                x++
            }
            y++
        }
        var centerX = rotatedBitmap.width / 2
        var centerY = rotatedBitmap.height / 2
        y = 0
        while (y < rotatedBitmap.height) {
            var x = 0
            while (x < rotatedBitmap.width) {
                var temp = getFourPixels(arr, (centerX - x).toFloat(), (centerY - y).toFloat())
                var red =  0.5f * temp[0][2] + 0.25f * temp[1][2] + 0.13f * temp[2][2] + 0.12f * temp[3][2]
                var green = 0.5f * temp[0][3] + 0.25f * temp[1][3] + 0.13f * temp[2][3] + 0.12f * temp[3][3]
                var blue = 0.5f * temp[0][4] + 0.25f * temp[1][4] + 0.13f * temp[2][4] + 0.12f * temp[3][4]
                var alfa = 0.5f * temp[0][5] + 0.25f * temp[1][5] + 0.13f * temp[2][5] + 0.12f * temp[3][5]
                rotatedBitmap.setPixel(x, y, Color.argb(alfa, red, green, blue))

                x++
            }
            y++
        }

        return rotatedBitmap
    }

    fun rotateAny(bitmap: Bitmap, deg: Int): Bitmap {
        var rotatedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)
        var arr = mutableListOf<MutableList<Float>>()
        var y0 = bitmap.height /2
        var x0 = bitmap.width /2
        var angle = PI / 180 * deg
        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var newX:Float = ((x0 - x) * cos(angle) - (y0- y) * sin(angle)).toFloat()
                var newY:Float = ((x0 - x) * sin(angle) + (y0- y) * cos(angle)).toFloat()
                var bestDist = Float.MAX_VALUE
                var bestX = 0
                var bestY = 0
                var indX = newX.toInt()
                var indY = newY.toInt()

                var i = -1
                while (i <= 1) {
                    var j = -1
                    while (j <= 1) {
                        var dist = ((indX + i - newX).pow(2) + (indY + j - newY).pow(2)).pow(0.5f)
                        if (dist < bestDist) {
                            bestDist = dist
                            bestX = indX + i
                            bestY = indY + j
                        }
                        j++
                    }
                    i++
                }
                if ((x0 - bestX) < bitmap.width && (x0 - bestX) >= 0 && (y0 - bestY) < bitmap.height && (y0 - bestY) >= 0) {
                    rotatedBitmap.setPixel(x0 - bestX, y0 -bestY, Color.argb(alfa, red, green, blue))
                }

                var data = mutableListOf(newX, newY, red, green, blue, alfa)
                arr.add(data)
                x++
            }
            y++
        }
        var ans = fillEmptyPixels(rotatedBitmap)
        return ans
    }

    fun fillEmptyPixels(bitmap: Bitmap): Bitmap {
        var newBitmap = bitmap.copy(bitmap.config, true)
        var x = 0
        var y = 0
        while (x < bitmap.width) {
            y = 0
            while (y < bitmap.height) {
                var colorPixel = bitmap.getColor(x,y).components
                var neighboringPixels = mutableListOf<MutableList<Float>>()

                if (colorPixel[3] <= 0) {
                    var i = -1
                    var j = -1
                    while (i <= 1) {
                        j = -1
                        while (j <=1) {
                            if ((x+i) >= 0 && (x+i) < bitmap.width && (y+j) >=0 && (y+j) < bitmap.height) {
                                var tempColorPixel = bitmap.getColor(x + i,y + j).components
                                var red = tempColorPixel[0]
                                var green = tempColorPixel[1]
                                var blue = tempColorPixel[2]
                                var alfa = tempColorPixel[3]
                                if (alfa > 0) {
                                    var data = mutableListOf(red, green, blue, alfa)
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