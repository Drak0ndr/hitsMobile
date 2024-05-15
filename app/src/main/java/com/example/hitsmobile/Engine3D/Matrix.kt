package com.example.hitsmobile.Engine3D

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix {
    companion object {
        fun multiply(a:MutableList<MutableList<Float>>, b:MutableList<MutableList<Float>>):MutableList<MutableList<Float>> {
            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(0f,0f,0f,0f))
            matrix.add(mutableListOf(0f,0f,0f,0f))
            matrix.add(mutableListOf(0f,0f,0f,0f))
            matrix.add(mutableListOf(0f,0f,0f,0f))

            var i = 0
            while (i < 4) {
                var j = 0
                while (j < 4) {
                    matrix[i][j] = a[i][0] * b[0][j] + a[i][1] * b[1][j] + a[i][2] * b[2][j] + a[i][3] * b[3][j]
                    j++
                }
                i++
            }

            return matrix
        }

        fun multiplyVector(m: MutableList<MutableList<Float>>, v: Vector):Vector {
            return Vector(m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3] * v.w,
                m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3] * v.w,
                m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3] * v.w,
                m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3] * v.w
            )
        }

        fun getTranslation(dx:Float, dy:Float, dz:Float):MutableList<MutableList<Float>> {
            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(1f,0f,0f,dx))
            matrix.add(mutableListOf(0f,1f,0f,dy))
            matrix.add(mutableListOf(0f,0f,1f,dz))
            matrix.add(mutableListOf(0f,0f,0f,1f))

            return matrix
        }

        fun getScale(sx:Float, sy:Float, sz:Float): MutableList<MutableList<Float>>{
            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(sx,0f,0f,0f))
            matrix.add(mutableListOf(0f,sy,0f,0f))
            matrix.add(mutableListOf(0f,0f,sz,0f))
            matrix.add(mutableListOf(0f,0f,0f,1f))

            return matrix
        }

        fun getRotationX(angle:Float):MutableList<MutableList<Float>> {
            var rad = PI / 180 * angle

            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(1f,0f,0f,0f))
            matrix.add(mutableListOf(0f, cos(rad).toFloat(), -sin(rad).toFloat() ,0f))
            matrix.add(mutableListOf(0f, sin(rad).toFloat(), cos(rad).toFloat() ,0f))
            matrix.add(mutableListOf(0f,0f,0f,1f))

            return matrix
        }

        fun getRotationY(angle:Float):MutableList<MutableList<Float>> {
            var rad = PI / 180 * angle

            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(cos(rad).toFloat(), 0f, sin(rad).toFloat() ,0f))
            matrix.add(mutableListOf(0f,1f,0f,0f))
            matrix.add(mutableListOf(-sin(rad).toFloat(), 0f, cos(rad).toFloat() ,0f))
            matrix.add(mutableListOf(0f,0f,0f,1f))

            return matrix
        }

        fun getRotationZ(angle:Float):MutableList<MutableList<Float>> {
            var rad = PI / 180 * angle

            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(cos(rad).toFloat(), -sin(rad).toFloat() ,0f, 0f))
            matrix.add(mutableListOf(sin(rad).toFloat(), cos(rad).toFloat() , 0f, 0f))
            matrix.add(mutableListOf(0f,0f,1f,0f))
            matrix.add(mutableListOf(0f,0f,0f,1f))

            return matrix
        }

        fun getLookAt(eye:Vector, target: Vector, up:Vector): MutableList<MutableList<Float>> {
            var vz = Vector.substruct(eye, target).normalize()
            var vx = Vector.crossProduct(up, vz).normalize()
            var vy = Vector.crossProduct(vz, vx).normalize()

            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(vx.x, vx.y, vx.z, 0f))
            matrix.add(mutableListOf(vy.x, vy.y, vy.z, 0f))
            matrix.add(mutableListOf(vz.x, vz.y, vz.z, 0f))
            matrix.add(mutableListOf(0f, 0f, 0f, 1f))

            return Matrix.multiply(Matrix.getTranslation(-eye.x, -eye.y, -eye.z), matrix)
        }

        fun getPerspectiveProjection(fov:Float, aspect:Float, n:Float, f:Float): MutableList<MutableList<Float>> {
            var rad = PI / 180 * fov

            var sx = ((1/ tan(rad / 2)) / aspect).toFloat()
            var sy = (1 / tan(rad / 2)).toFloat()
            var sz = (f + n) / (f - n)
            var dz = (-2 * f * n) / (f - n)

            var matrix = mutableListOf<MutableList<Float>>()
            matrix.add(mutableListOf(sx, 0f, 0f, 0f))
            matrix.add(mutableListOf(0f, sy, 0f, 0f))
            matrix.add(mutableListOf(0f, 0f, sz, dz))
            matrix.add(mutableListOf(0f, 0f, -1f, 0f))

            return matrix

        }
    }

}