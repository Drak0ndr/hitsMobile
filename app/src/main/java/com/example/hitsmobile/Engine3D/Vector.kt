package com.example.hitsmobile.Engine3D

import kotlin.math.pow

class Vector {
    var x = 0f
    var y = 0f
    var z = 0f
    var w = 0f

    constructor(_x:Float,_y:Float,_z:Float, _w: Float = 1f) {
        x = _x
        y = _y
        z = _z
        w = _w
    }
    companion object {
        fun add(v1:Vector, v2:Vector):Vector {
            return Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z)
        }

        fun substruct(v1:Vector, v2:Vector):Vector {
            return Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z)
        }

        fun crossProduct(v1:Vector, v2:Vector):Vector {
            return Vector(v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x)
        }

        fun scalarProduct(a:Vector, b:Vector): Float {
            var ans = a.x * b.x + a.y * b.y + a.z * b.z
            return ans
        }

        fun calculateCos(a:Vector, b:Vector): Float {
            var ans = Vector.scalarProduct(a,b) / (a.getLength() * b.getLength())

            return ans
        }
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