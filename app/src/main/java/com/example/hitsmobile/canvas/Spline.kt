package com.example.hitsmobile.canvas

import kotlin.math.pow

class Spline(val _points: MutableList<Pair<Float, Float>>) {
    private var points = _points

    private var coefficientsA = mutableListOf<Float>()
    private var coefficientsB = mutableListOf<Float>()
    private var coefficientsC = mutableListOf<Float>()
    private var coefficientsD = mutableListOf<Float>()


    fun initializingFirstFunction(){
        if(points.size > 1){
            val coefficients = arrayOf(
                floatArrayOf(1.0f, points[0].first, points[0].first.pow(2), points[0].first.pow(3)),
                floatArrayOf(1.0f, points[1].first, points[1].first.pow(2), points[1].first.pow(3)),
                floatArrayOf(0.0f, 1.0f, 2.0f * points[1].first, 3.0f * points[1].first.pow(2)),
                floatArrayOf(0.0f, 1.0f, 2.0f * points[0].first, 3.0f * points[0].first.pow(2))
            )

            val constants = floatArrayOf(points[0].second, points[1].second,
                    (points[0].second - points[1].second) / (points[0].first - points[1].first), 0.0f)

            val det = determinant(coefficients)

            val solutions = FloatArray(4)

            for (i in coefficients.indices) {
                val tempMatrix = coefficients.map { it.copyOf() }.toTypedArray()

                for (j in coefficients.indices) {
                    tempMatrix[j][i] = constants[j]
                }

                solutions[i] = determinant(tempMatrix) / det
            }

            coefficientsA.add(solutions[0])
            coefficientsB.add(solutions[1])
            coefficientsC.add(solutions[2])
            coefficientsD.add(solutions[3])
        }
    }

    fun initializingAllFunctions(){
        for(i in 2..< points.size) {

            val valueFirstDerivative = coefficientsB[i - 2] + 2.0f * coefficientsC[i - 2] *
                    points[i - 1].first + 3.0f * coefficientsD[i - 2] * points[i - 1].first.pow(2)

            val coefficients = arrayOf(
                floatArrayOf(1.0f, points[i - 1].first, points[i - 1].first.pow(2), points[i - 1].first.pow(3)),
                floatArrayOf(1.0f, points[i].first, points[i].first.pow(2), points[i].first.pow(3)),
                floatArrayOf(0.0f, 1.0f, 2.0f * points[i - 1].first, 3.0f * points[i - 1].first.pow(2)),
                floatArrayOf(0.0f, 1.0f, 2.0f * points[i].first, 3.0f * points[i].first.pow(2))
            )

            val constants = floatArrayOf(points[i - 1].second, points[i].second, valueFirstDerivative,
                    (points[i - 1].second - points[i].second) / (points[i - 1].first - points[i].first))

            val det = determinant(coefficients)

            val solutions = FloatArray(4)

            for (i in coefficients.indices) {
                val tempMatrix = coefficients.map { it.copyOf() }.toTypedArray()

                for (j in coefficients.indices) {
                    tempMatrix[j][i] = constants[j]
                }

                solutions[i] = determinant(tempMatrix) / det
            }

            coefficientsA.add(solutions[0])
            coefficientsB.add(solutions[1])
            coefficientsC.add(solutions[2])
            coefficientsD.add(solutions[3])
        }
    }

    fun determinant(matrix: Array<FloatArray>): Float {
        if (matrix.size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        }

        var det = 0.0f
        for (i in matrix.indices) {
            val sign = if (i % 2 == 0) 1 else -1
            det += sign * matrix[0][i] * determinant(matrix.minor(0, i))
        }

        return det
    }

    fun Array<FloatArray>.minor(rowToRemove: Int, colToRemove: Int): Array<FloatArray> {
        return this.filterIndexed { index, _ -> index != rowToRemove }
            .map { it.filterIndexed { index, _ -> index != colToRemove }.toFloatArray() }
            .toTypedArray()
    }

    fun findValue(value : Float, index : Int) : Float{
        return coefficientsA[index] + coefficientsB[index] * value +
                coefficientsC[index] * value.pow(2) +
                coefficientsD[index] * value.pow(3)
    }
}