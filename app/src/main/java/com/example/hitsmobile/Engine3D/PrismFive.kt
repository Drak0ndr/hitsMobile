package com.example.hitsmobile.Engine3D

class PrismFive {
    companion object {
        fun getVertices(): MutableList<Vector> {
            var vertices = mutableListOf<Vector>()
            vertices.add(Vector(0f,1f,-1f))
            vertices.add(Vector(-0.9f,1f,-0.3f))
            vertices.add(Vector(0.9f,1f,-0.3f))
            vertices.add(Vector(-0.4f,1f,0.8f))
            vertices.add(Vector(0.4f,1f,0.8f))

            vertices.add(Vector(0f,-1f,-1f))
            vertices.add(Vector(-0.9f,-1f,-0.3f))
            vertices.add(Vector(0.9f,-1f,-0.3f))
            vertices.add(Vector(-0.4f,-1f,0.8f))
            vertices.add(Vector(0.4f,-1f,0.8f))

            return vertices
        }

        fun getPolygons(): MutableList<MutableList<Float>> {

            var polygons = mutableListOf<MutableList<Float>>()
            polygons.add(mutableListOf(1f,0f,2f, 1f, 0f, 0f))
            polygons.add(mutableListOf(1f,2f,3f, 1f, 0f, 0f))
            polygons.add(mutableListOf(3f,2f,4f, 1f, 0f, 0f))

            polygons.add(mutableListOf(8f,3f,4f, 0f, 1f, 0f))
            polygons.add(mutableListOf(8f,4f,9f, 0f, 1f, 0f))

            polygons.add(mutableListOf(6f,1f,3f, 0f, 0f, 1f))
            polygons.add(mutableListOf(6f,3f,8f, 0f, 0f, 1f))

            polygons.add(mutableListOf(4f,2f,9f, 0f, 1f, 1f))
            polygons.add(mutableListOf(9f,2f,7f, 0f, 1f, 1f))

            polygons.add(mutableListOf(0f,1f,6f, 1f, 1f, 0f))
            polygons.add(mutableListOf(0f,6f,5f, 1f, 1f, 0f))

            polygons.add(mutableListOf(0f,5f,2f, 1f, 0f, 1f))
            polygons.add(mutableListOf(2f,5f,7f, 1f, 0f, 1f))

            polygons.add(mutableListOf(5f,6f,7f, 0.5f, 0.5f, 0.5f))
            polygons.add(mutableListOf(7f,6f,8f, 0.5f, 0.5f, 0.5f))
            polygons.add(mutableListOf(7f,8f,9f, 0.5f, 0.5f, 0.5f))

            return polygons
        }
    }

}