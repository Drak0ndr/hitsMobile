package com.example.hitsmobile.algorithms

import android.graphics.Bitmap
import android.graphics.Color
import com.example.hitsmobile.filters.ColorFilters
class Mask {
    fun UnsharpMask(bitmap:Bitmap ,k:Float):Bitmap {
        val colorFilters = ColorFilters()
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height,Bitmap.Config.ARGB_8888)

        val imgMatrix =  mutableListOf<MutableList<Float>>()

        val filterMatrix = mutableListOf<MutableList<Float>>()

        val resultMatrix = mutableListOf<MutableList<Float>>()
        val r = 1.9f
        val len = (r).toInt() * 2 + 1

        val mid = r.toInt()
        val sigma = r/3
        var sumFilterMatrix = 0f

        var i = 0
        while (i < len) {
            val temp = mutableListOf<Float>()
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
            val temp = mutableListOf<Float>()
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
            val tempArr = mutableListOf<Float>()
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
                            val tempColors = bitmap.getColor(j - mid + x, i - mid + y).components
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
                if (tempAlpha > 1 && (tempAlpha - 1) < 0.01) {
                    tempAlpha = 1f
                }
                if (tempRed > 1 && (tempRed- 1) < 0.01) {
                    tempRed = 1f
                }
                if (tempGreen > 1 && (tempGreen- 1) < 0.01) {
                    tempGreen = 1f
                }
                if (tempBlue > 1 && (tempBlue- 1) < 0.01) {
                    tempBlue = 1f
                }

                if (tempRed < 0 || tempRed > 1 || tempGreen < 0 || tempGreen > 1 || tempBlue < 0 || tempBlue > 1 || tempAlpha < 0 || tempAlpha > 1) {
                    val tempColors = bitmap.getColor(j, i).components
                    tempRed = tempColors[0]
                    tempGreen = tempColors[1]
                    tempBlue = tempColors[2]
                    tempAlpha = tempColors[3]
                }
                newBitmap.setPixel(j,i, Color.argb(tempAlpha, tempRed, tempGreen, tempBlue))
                j++
            }
            i++
        }
        return newBitmap
    }
}