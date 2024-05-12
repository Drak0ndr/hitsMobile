package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.filters.ColorFilters
class Mask {
    fun UnsharpMask(bitmap:Bitmap ,k:Float):Bitmap {
        var colorFilters = ColorFilters()
        var newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)

        var imgMatrix =  mutableListOf<MutableList<Float>>()

        var filterMatrix = mutableListOf<MutableList<Float>>()

        var resultMatrix = mutableListOf<MutableList<Float>>()
        var r = 1.9f
        var len = (r).toInt() * 2 + 1

        var mid = r.toInt()
        var sigma = r/3
        var sumFilterMatrix = 0f

        var i = 0
        while (i < len) {
            var temp = mutableListOf<Float>()
            var j = 0
            while (j < len) {
                temp.add(0f)
                j++
            }
            imgMatrix.add(temp)
            i++
        }
        imgMatrix[mid][mid] = 1f
        i = 0
        while (i < len) {
            var temp = mutableListOf<Float>()
            var j = 0
            while (j < len) {
                temp.add(colorFilters.gaussian(sigma, i-mid, j-mid).toFloat())
                j++
            }
            filterMatrix.add(temp)
            i++
        }
//        filterMatrix.add(mutableListOf(0f,1f,0f))
//        filterMatrix.add(mutableListOf(1f,1f,1f))
//        filterMatrix.add(mutableListOf(0f,1f,0f))
        i = 0
        while (i < len) {
            var j = 0
            while (j < len) {
                sumFilterMatrix += filterMatrix[i][j]
                j++
            }
            i++
        }
        i = 0
        while (i < len) {
            var j = 0
            while (j < len) {
                filterMatrix[i][j] = -filterMatrix[i][j] / sumFilterMatrix
                j++
            }
            i++
        }

        filterMatrix[mid][mid]+=1f

        i = 0
        while (i < len) {
            var j = 0
            var tempArr = mutableListOf<Float>()
            while (j < len) {
                tempArr.add(imgMatrix[i][j] + filterMatrix[i][j] * k)
                j++
            }
            resultMatrix.add(tempArr)
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
                            tempRed+= (tempColors[0] * resultMatrix[y][x])
                            tempGreen+= (tempColors[1] * resultMatrix[y][x])
                            tempBlue+= (tempColors[2] * resultMatrix[y][x])
                            tempAlpha+= (tempColors[3] * resultMatrix[y][x])
                            kernelCoef+= resultMatrix[y][x]
                        }

                        x++
                    }
                    y++
                }
                tempRed = (tempRed / kernelCoef).toFloat()
                tempGreen = (tempGreen / kernelCoef).toFloat()
                tempBlue = (tempBlue / kernelCoef).toFloat()
                tempAlpha = (tempAlpha / kernelCoef).toFloat()
                if (tempRed <= 0) {
                    tempRed = 0f
                }
                if (tempGreen <= 0) {
                    tempGreen = 0f
                }
                if (tempBlue <=0) {
                    tempBlue = 0f
                }
                if (tempAlpha <= 0) {
                    tempAlpha = 0f
                }
                newBitmap.setPixel(j,i, Color.argb(tempAlpha, tempRed, tempGreen, tempBlue))
                j++
            }
            i++
        }
        return newBitmap
    }
}