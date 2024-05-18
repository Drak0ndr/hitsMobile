package com.example.hitsmobile.Engine3D

class Cube {
    companion object {
        fun getVertices(): MutableList<Vector> {
            var vertices = mutableListOf<Vector>()
            vertices.add(Vector(-1f,1f,1f))
            vertices.add(Vector(-1f,1f,-1f))
            vertices.add(Vector(1f,1f,-1f))
            vertices.add(Vector(1f,1f,1f))
            vertices.add(Vector(-1f,-1f,1f))
            vertices.add(Vector(-1f,-1f,-1f))
            vertices.add(Vector(1f,-1f,-1f))
            vertices.add(Vector(1f,-1f,1f))

            return vertices
        }

        fun getPolygons(): MutableList<MutableList<Float>> {
            var polygons = mutableListOf<MutableList<Float>>()
            polygons.add(mutableListOf(0f,1f,2f, 1f, 0f, 0f))
            polygons.add(mutableListOf(0f,2f,3f, 1f, 0f, 0f))
            polygons.add(mutableListOf(4f,6f,5f, 0f, 1f, 0f))
            polygons.add(mutableListOf(4f,7f,6f, 0f, 1f, 0f))
            polygons.add(mutableListOf(0f,5f,1f, 0f, 0f, 1f))
            polygons.add(mutableListOf(0f,4f,5f, 0f, 0f, 1f))
            polygons.add(mutableListOf(1f,5f,2f, 0f, 1f, 1f))
            polygons.add(mutableListOf(6f,2f,5f, 0f, 1f, 1f))
            polygons.add(mutableListOf(3f,2f,6f, 1f, 1f, 0f))
            polygons.add(mutableListOf(3f,6f,7f, 1f, 1f, 0f))
            polygons.add(mutableListOf(3f,4f,0f, 1f, 0f, 1f))
            polygons.add(mutableListOf(4f,3f,7f, 1f, 0f, 1f))

            return polygons
        }
    }

}