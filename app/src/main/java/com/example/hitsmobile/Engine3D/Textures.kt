package com.example.hitsmobile.Engine3D

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Textures {
    companion object {
        val netherBrickBase64 = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAwUExURSQTFzAZHTwfJEYjKVIjLFosNWM1PQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHJD8KEAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABrSURBVBjTPczBDYMwEETRQTbcIxYXAKnA3x1gqAB3ENx/CVmJKDv6t6fV/9q99LulplwilBGVPOctz0jD68lFXdpxadxh8mSwGu+snxhU0yfVVE1EJi9qM8zfmINnOqv17lA4KHtArAEIfAENHRIxaeEPYwAAAABJRU5ErkJggg=="

        @OptIn(ExperimentalEncodingApi::class)
        fun getBitmap(string:String): Bitmap {
            var imageBytes = Base64.decode(string, 0)
            var image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            return image
        }
    }
}