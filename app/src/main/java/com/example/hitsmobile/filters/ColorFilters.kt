package com.example.hitsmobile.filters

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.activity.PhotoActivity
import kotlin.math.pow

class ColorFilters: PhotoActivity() {
   suspend fun toGreen(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var green = colorPixel[1]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 0f, green, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }

    suspend fun toBlue(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 0f, 0f, blue))
                y++
            }
            x++
        }
        return newBitmap
    }

    suspend fun toRed(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, red, 0f, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }

    suspend fun toYellow(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, red, green, 0f))
                y++
            }
            x++
        }
        return newBitmap
    }
    suspend fun toNegative(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                newBitmap.setPixel(x, y, Color.argb(alfa, 1 - red, 1 - green, 1 - blue))
                y++
            }
            x++
        }
        return newBitmap
    }
    suspend fun toGray(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var x = 0
        var y = 0
        while (x < width) {
            y = 0
            while (y < height) {
                var colorPixel = bitmap.getColor(x, y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var gray = 0.2126f * red + 0.7152f * green + 0.0722f * blue
                newBitmap.setPixel(x, y, Color.argb(alfa, gray, gray, gray))
                y++
            }
            x++
        }
        return newBitmap
    }
     fun gaussian(sigma: Float, y:Int,x:Int): Double {
        var temp =  (- (x*x + y*y) / (2 * sigma.pow(2))).toDouble()
        var ans = (1 / (3.14 * sigma.pow(2)) * 2.71.pow(temp))
        return ans
    }
    suspend fun gausBlur(bitmap: Bitmap, r: Float): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
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

        i = 0
        while (i < bitmap.height) {
            var j = 0
            while (j < bitmap.width) {
                var tempRed = 0f
                var tempGreen = 0f
                var tempBlue = 0f
                var tempAlpha = 0f
                var kernelCoef = 0.0
                var y = 0
                while (y < len) {
                    var x = 0
                    while (x < len) {
                        if ((j - mid + x) >= 0 && (j - mid + x) < bitmap.width && (i - mid + y) >= 0 && (i - mid + y) < bitmap.height) {
                            var tempColors = bitmap.getColor(j - mid + x, i - mid + y).components
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
                newBitmap.setPixel(j,i, Color.argb(tempAlpha, tempRed, tempGreen, tempBlue))
                j++
            }
            i++
        }
        return newBitmap
    }

    fun normalizeColor(color:Float):Float {
        var newColor = color
        if (newColor > 1) {
            newColor = 1f
        }
        if (newColor < 0) {
            newColor = 0f
        }

        return newColor
    }
    suspend fun changeContrast(bitmap: Bitmap, k: Float): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        var newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        var minBright = 1f
        var maxBright = 0f

        var i = 0
        while (i < height) {
            var j = 0
            while (j < width) {
                var colorPixel = bitmap.getColor(j, i).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var bright = 0.2126f * red + 0.7152f * green + 0.0722f * blue

                if (bright > maxBright) {
                    maxBright = bright
                }
                if (bright < minBright) {
                    minBright = bright
                }
                j++
            }
            i++
        }

        var contrast = (maxBright - minBright) / (maxBright + minBright) * 255
        var coef = (contrast + k) / contrast

        i = 0
        while (i < height) {

            var j = 0
            while (j < width) {
                var colorPixel = bitmap.getColor(j, i).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]

                red = (red - 0.5f) * coef + 0.5f
                green = (green - 0.5f) * coef + 0.5f
                blue = (blue - 0.5f) * coef + 0.5f

                red = normalizeColor(red)
                green = normalizeColor(green)
                blue = normalizeColor(blue)

                newBitmap.setPixel(j,i,Color.argb(alfa, red, green, blue))
                j++
            }

            i+=1
        }
        return newBitmap
    }

    fun erosionFilter(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val radius = 4

        for (x in radius until width - radius) {
            for (y in radius until height - radius) {
                var minR = 255
                var minG = 255
                var minB = 255

                for (i in -radius..radius) {
                    for (j in -radius..radius) {
                        val pixel = bitmap.getPixel(x + i, y + j)
                        val r = Color.red(pixel)
                        val g = Color.green(pixel)
                        val b = Color.blue(pixel)

                        if (r < minR) minR = r
                        if (g < minG) minG = g
                        if (b < minB) minB = b
                    }
                }

                resultBitmap.setPixel(x, y, Color.rgb(minR, minG, minB))
            }
        }
        return resultBitmap
    }

}