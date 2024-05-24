package com.example.hitsmobile.algorithms

class Affinis {

    fun CalcValues(x11:Float, y11:Float, x12:Float, y12:Float, x13:Float, y13:Float, x01:Float, x02:Float, x03:Float, y01:Float, y02:Float, y03:Float): MutableList<Float> {
        val d = x11 * (y12 - y13) + x12 * (y13 - y11) + x13 * (y11 - y12)
        val dax = x01 * (y12 - y13) + x02 * (y13 - y11) + x03 * (y11 - y12)
        val dbx = x11 * (x02 - x03) + x12 * (x03 - x01) + x13 * (x01 - x02)

        val ax = dax / d
        val bx = dbx / d
        val cx = x01 - ax * x11 - bx * y11

        val day = y01 * (y12 - y13) + y02 * (y13 - y11) + y03 * (y11 - y12)
        val dby = x11 * (y02 - y03) + x12 * (y03 - y01) + x13 * (y01 - y02)

        val ay = day / d
        val by = dby / d
        val cy = y01 - ay * x11 - by * y11

        return mutableListOf(ax, bx, cx, ay, by, cy)
    }
}