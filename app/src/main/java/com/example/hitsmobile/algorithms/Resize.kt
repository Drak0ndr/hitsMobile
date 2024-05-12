package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.activity.PhotoActivity
import kotlin.math.pow

class Resize: PhotoActivity() {
    fun upScale(bitmap: Bitmap, k: Float): Bitmap {
        var width = (bitmap.width * k).toInt() + 1
        var height = (bitmap.height * k).toInt() + 1
        var scaleBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        var y = 0
        var scaleY = 0
        var yDist = k
        var yfine = 0f
        while (y < bitmap.height) {
            yDist = k - yfine
            var x = 0
            var scaleX = 0
            var fine = 0f
            while (x < bitmap.width) {
                var colorPixel = bitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                var xDist = k - fine
                var i = 0
                while (i <= xDist-1) {
                    if (scaleX + i < scaleBitmap.width) {
                        scaleBitmap.setPixel(scaleX + i, scaleY, Color.argb(alfa, red, green, blue))
                    }
                    i++
                }
                if (xDist - i > 0) {
                    fine =1 -( xDist - i)
                    if (x+1 < bitmap.width) {
                        var nextColorPixel = bitmap.getColor(x+1,y).components
                        var nextRed = nextColorPixel[0] * fine + red * (1-fine)
                        var nextGreen = nextColorPixel[1] * fine + green * (1-fine)
                        var nextBlue = nextColorPixel[2] * fine + blue * (1-fine)
                        var nextAlfa = nextColorPixel[3] * fine + alfa * (1-fine)
                        scaleBitmap.setPixel(scaleX + i, scaleY, Color.argb(nextAlfa, nextRed, nextGreen, nextBlue))
                    }
                } else {
                    fine = 0f
                }
                if (xDist > xDist.toInt()) {
                    scaleX+= xDist.toInt() + 1
                } else {
                    scaleX += xDist.toInt()
                }

                x++
            }
            var j = 0
            while (j <= yDist-1) {
                if (scaleY + j < scaleBitmap.height) {
                    x = 0
                    while (x < scaleBitmap.width) {
                        var colorPixel = scaleBitmap.getColor(x,scaleY).components
                        var red = colorPixel[0]
                        var green = colorPixel[1]
                        var blue = colorPixel[2]
                        var alfa = colorPixel[3]
                        scaleBitmap.setPixel(x, scaleY +j, Color.argb(alfa, red, green, blue))
                        x++
                    }

                }
                j++
            }
            if (yDist % 1 > 0) {
                yfine =1 -( yDist % 1)
            } else {
                yfine = 0f
            }
            if (yDist > yDist.toInt()) {
                scaleY+= yDist.toInt() + 1
            } else {
                scaleY += yDist.toInt()
            }
            if (scaleY >= scaleBitmap.height -1) {
                scaleY = scaleBitmap.height - 1
            }
            y++
        }
        y = 0
        while (y < scaleBitmap.height) {
            var x = 0
            while (x < scaleBitmap.width) {
                var colorPixel = scaleBitmap.getColor(x,y).components
                var red = colorPixel[0]
                var green = colorPixel[1]
                var blue = colorPixel[2]
                var alfa = colorPixel[3]
                if (alfa <= 0) {
                    if (y-1 >= 0 && y+1 < scaleBitmap.height) {
                        var prevColor = scaleBitmap.getColor(x,y-1).components
                        var prevRed = prevColor[0]
                        var prevGreen = prevColor[1]
                        var prevBlue = prevColor[2]
                        var prevAlfa = prevColor[3]

                        var nextColor = scaleBitmap.getColor(x,y+1).components
                        var nextRed = nextColor[0]
                        var nextGreen = nextColor[1]
                        var nextBlue = nextColor[2]
                        var nextAlfa = nextColor[3]

                        var newRed = 0.5f * prevRed + 0.5f * nextRed
                        var newGreen = 0.5f * prevGreen + 0.5f * nextGreen
                        var newBlue = 0.5f * prevBlue + 0.5f * nextBlue
                        var newAlfa = 0.5f * prevAlfa + 0.5f * nextAlfa
                        if (prevAlfa > 0 && nextAlfa > 0) {
                            scaleBitmap.setPixel(x, y, Color.argb(newAlfa, newRed, newGreen, newBlue))
                        }

                    }
                }
                x++
            }
            y++
        }
        return scaleBitmap
    }
    fun downScale(bitmap: Bitmap, k: Float): Bitmap {
        var width = (bitmap.width / k).toInt() + 1
        var height = (bitmap.height / k).toInt() + 1
        var scaleBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888)
        var y = 0
        var yfine = 0f
        var scaleY = 0


        while (y < bitmap.height) {
            var x = 0
            var scaleX = 0
            var yDist = k - yfine
            var xfine = 0f
            while (x < bitmap.width) {
                var red = 0f
                var green = 0f
                var blue = 0f
                var alfa = 0f
                var square = 0f
                var xDist = k - xfine
                var i = 0
                if (yfine > 0) {
                    var i = 0
                    while (i <= xDist -1 && x + i < bitmap.width) {
                        var tempColor = bitmap.getColor(x+i, y-1).components
                        red+= tempColor[0] / k.pow(2) * yfine
                        green+= tempColor[1] / k.pow(2) * yfine
                        blue+= tempColor[2] / k.pow(2) * yfine
                        alfa+= tempColor[3] / k.pow(2) * yfine
                        square+= 1f/ k.pow(2) * yfine
                        i++
                    }
                    if (x+i < bitmap.width) {
                        var tempColor = bitmap.getColor(x+i, y-1).components
                        red+= tempColor[0] / k.pow(2) * yfine * (xDist%1)
                        green+= tempColor[1] / k.pow(2) * yfine * (xDist%1)
                        blue+= tempColor[2] / k.pow(2) * yfine * (xDist%1)
                        alfa+= tempColor[3] / k.pow(2) * yfine * (xDist%1)
                        square += 1f/k.pow(2) * yfine * (xDist%1)
                    }



                }
                if (xfine > 0) {
                    var j = 0
                    while (j <= yDist-1 && y+j < bitmap.height) {
                        var tempColor = bitmap.getColor(x-1, y+j).components
                        red+= tempColor[0] / k.pow(2) * xfine
                        green+= tempColor[1] / k.pow(2) * xfine
                        blue+= tempColor[2] / k.pow(2) * xfine
                        alfa+= tempColor[3] / k.pow(2) * xfine
                        square+= 1f/k.pow(2) * xfine
                        j++
                    }
                    if (y+j < bitmap.height) {
                        var tempColor = bitmap.getColor(x-1, y+j).components
                        red+= tempColor[0] / k.pow(2) * xfine * (yDist%1)
                        green+= tempColor[1] / k.pow(2) * xfine * (yDist%1)
                        blue+= tempColor[2] / k.pow(2) * xfine * (yDist%1)
                        alfa+= tempColor[3] / k.pow(2) * xfine * (yDist%1)
                        square+= 1f/k.pow(2) * xfine * (yDist%1)
                    }


                }
                while (i <= xDist -1 && x+i < bitmap.width) {
                    var j = 0
                    while (j <= yDist -1 && y+j < bitmap.height) {
                        var tempColor = bitmap.getColor(x+i, y+j).components
                        red+= tempColor[0] / k.pow(2)
                        green+= tempColor[1] / k.pow(2)
                        blue+= tempColor[2] / k.pow(2)
                        alfa+= tempColor[3] / k.pow(2)
                        square+= 1f/k.pow(2)
                        j++
                    }
                    i++
                }
                var j = 0
                if (x+i < bitmap.width) {
                    while (j <= yDist-1 && y+j < bitmap.height) {
                        var tempColor = bitmap.getColor(x+i, y+j).components
                        red+= tempColor[0] / k.pow(2) * (xDist%1)
                        green+= tempColor[1] / k.pow(2) * (xDist%1)
                        blue+= tempColor[2] / k.pow(2) * (xDist%1)
                        alfa+= tempColor[3] / k.pow(2) * (xDist%1)
                        square+= 1f/k.pow(2) * (xDist%1)
                        j++
                    }
                    if (y+j < bitmap.height) {
                        var tempColor = bitmap.getColor(x+i, y+j).components
                        red+= tempColor[0] / k.pow(2) * (xDist%1) * (yDist%1)
                        green+= tempColor[1] / k.pow(2) * (xDist%1) * (yDist%1)
                        blue+= tempColor[2] / k.pow(2) * (xDist%1) * (yDist%1)
                        alfa+= tempColor[3] / k.pow(2) * (xDist%1) * (yDist%1)
                        square+= 1f/k.pow(2) * (xDist%1) * (yDist%1)
                    }
                }
                j = yDist.toInt()
                i = 0
                if (y+j < bitmap.height) {
                    while (i <= xDist-1 && x+i < bitmap.width) {
                        var tempColor = bitmap.getColor(x+i, y+j).components
                        red+= tempColor[0] / k.pow(2) * (yDist%1)
                        green+= tempColor[1] / k.pow(2) * (yDist%1)
                        blue+= tempColor[2] / k.pow(2) * (yDist%1)
                        alfa+= tempColor[3] / k.pow(2) * (yDist%1)
                        square+=1f/k.pow(2) * (yDist%1)
                        i++
                    }
                    if (x+i < bitmap.width && xDist < 1) {
                        var tempColor = bitmap.getColor(x+i, y+j).components
                        red+= tempColor[0] / k.pow(2) * (yDist%1) * (xDist%1)
                        green+= tempColor[1] / k.pow(2) * (yDist%1) * (xDist%1)
                        blue+= tempColor[2] / k.pow(2) * (yDist%1) * (xDist%1)
                        alfa+= tempColor[3] / k.pow(2) * (yDist%1) * (xDist%1)
                        square+= 1f/k.pow(2) * (yDist%1) * (xDist%1)
                    }
                }
                red = red / square
                green = green/ square
                blue = blue/square
                alfa = alfa/square
                scaleBitmap.setPixel(scaleX, scaleY, Color.argb(alfa, red, green, blue))
                scaleX+=1
                if (xDist > xDist.toInt()) {
                    x+= xDist.toInt()+1
                    xfine = 1 - xDist%1
                } else {
                    x+= xDist.toInt()
                }

            }
            if (yDist > yDist.toInt()) {
                y+= yDist.toInt()+1
                yfine = 1 - yDist%1
            } else {
                y+= yDist.toInt()
            }
            scaleY+=1
            if (scaleY >= scaleBitmap.height) {
                scaleY = scaleBitmap.height-1
            }
        }
        return scaleBitmap
    }

    fun bilinearFilter(bitmap: Bitmap):Bitmap {
        var newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)
        var kernel = mutableListOf<MutableList<Int>>()
        kernel.add(mutableListOf(0,1,0))
        kernel.add(mutableListOf(1,1,1))
        kernel.add(mutableListOf(0,1,0))

        var len = 3
        var mid = 1
        var i = 0
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

    fun trilinearFilter(minBitmap: Bitmap, nextBitmap: Bitmap):Bitmap {
        var ansBitmap = Bitmap.createBitmap(minBitmap.width, minBitmap.height,Bitmap.Config.ARGB_8888)
        var k = nextBitmap.width.toFloat() / minBitmap.width.toFloat()
        var blurMinBitmap = bilinearFilter(minBitmap)
        var blurNextBitmap = bilinearFilter(nextBitmap)
        var x = 0
        while (x < ansBitmap.width) {
            var y = 0
            while (y < ansBitmap.height) {
                var colorMin = blurMinBitmap.getColor(x,y).components
                var nextX = (x*k).toInt()
                var nextY = (y*k).toInt()
                if (nextX >= blurNextBitmap.width) {
                    nextX = blurNextBitmap.width-1
                }
                if (nextY >= blurNextBitmap.height) {
                    nextY = blurNextBitmap.height-1
                }
                var colorMax = blurNextBitmap.getColor(nextX, nextY).components
                var red = (colorMin[0] + colorMax[0]) * 0.5f
                var green = (colorMin[1] + colorMax[1]) * 0.5f
                var blue = (colorMin[2] + colorMax[2]) * 0.5f
                var alpha = (colorMin[3] + colorMax[3]) * 0.5f
                ansBitmap.setPixel(x,y,Color.argb(alpha, red, green, blue))
                y++
            }
            x++
        }
        return ansBitmap
    }
}