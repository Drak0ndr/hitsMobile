package com.example.hitsmobile.Engine3D

import kotlin.math.pow

class Vector {
    var x = 0f
    var y = 0f
    var z = 0f

    constructor(_x:Float,_y:Float,_z:Float) {
        x = _x
        y = _y
        z = _z
    }

    fun add(v1:Vector, v2:Vector):Vector {
        return Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z)
    }

    fun getLength():Float {
        return (x*x + y*y + z*z).pow(0.5f)
    }

    fun normalize(): Vector {
        var lenght = getLength()

        x = x/lenght
        y = y/lenght
        z = z/lenght

        return this

    }

    fun multiplyByScalar(n:Float):Vector {
        x = x*n
        y = y*n
        z = z*n

        return this
    }
}