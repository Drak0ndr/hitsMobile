package com.example.hitsmobile.Engine3D

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hitsmobile.R
import com.example.hitsmobile.activity.HomePageActivity


class CubeActivity : AppCompatActivity() {
    /*Кнопка домой*/
    private lateinit var homeBtn : ImageView

    /*Ползунок для поворота X*/
    private lateinit var seekBarX: SeekBar

    /*Ползунок для поворота Y*/
    private lateinit var seekBarY: SeekBar

    /*Ползунок для поворота Z*/
    private lateinit var seekBarZ: SeekBar

    /*Ползунок для изменения дистанции*/
    private lateinit var seekBarDist: SeekBar

    private var currX : Int = 0
    private var currY : Int = 0
    private var currZ : Int = 0
    private  var currDistance: Int = 300

    var cubeVertices = mutableListOf<Vector>()
    var cubeEdges = mutableListOf<MutableList<Int>>()
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cube)

        var img = findViewById<ImageView>(R.id.draw_view)

        cubeVertices.add(Vector(-0.5f,1f,0.5f))
        cubeVertices.add(Vector(-0.5f,1f,-0.5f))
        cubeVertices.add(Vector(0.5f,1f,-0.5f))
        cubeVertices.add(Vector(0.5f,1f,0.5f))
        cubeVertices.add(Vector(-1f,-1f,1f))
        cubeVertices.add(Vector(-1f,-1f,-1f))
        cubeVertices.add(Vector(1f,-1f,-1f))
        cubeVertices.add(Vector(1f,-1f,1f))

        cubeEdges.add(mutableListOf(0,1))
        cubeEdges.add(mutableListOf(1,2))
        cubeEdges.add(mutableListOf(2,3))
        cubeEdges.add(mutableListOf(3,0))
        cubeEdges.add(mutableListOf(0,4))
        cubeEdges.add(mutableListOf(1,5))
        cubeEdges.add(mutableListOf(2,6))
        cubeEdges.add(mutableListOf(3,7))
        cubeEdges.add(mutableListOf(4,5))
        cubeEdges.add(mutableListOf(5,6))
        cubeEdges.add(mutableListOf(6,7))
        cubeEdges.add(mutableListOf(7,4))

        render()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cube_bg)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Кнопка домой*/
        homeBtn = findViewById(R.id.imgHome)
        homeBtn.setOnClickListener{
            val intent = Intent(this@CubeActivity, HomePageActivity::class.java)
            startActivity(intent)
        }

        /*Ползунок для поворота X*/
        seekBarX = findViewById(R.id.rotationX)
        seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currX = seekBarX.progress
                render()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Ползунок для поворота Y*/
        seekBarY = findViewById(R.id.rotationY)
        seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currY = seekBarY.progress
                render()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Ползунок для поворота Z*/
        seekBarZ = findViewById(R.id.rotationZ)
        seekBarZ.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currZ = seekBarZ.progress
                render()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Ползунок для изменения дистанции*/
        seekBarDist = findViewById(R.id.distance)
        seekBarDist.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currDistance = seekBarDist.progress
                render()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    fun genMatrix(angleX:Float, angleY:Float, angleZ:Float, camZ:Float): MutableList<MutableList<Float>> {
        var matrix = Matrix.getRotationX(angleX)
        matrix = Matrix.multiply(Matrix.getRotationY(angleY), matrix)
        matrix = Matrix.multiply(Matrix.getRotationZ(angleZ), matrix)
        matrix = Matrix.multiply(Matrix.getScale(120f,120f,120f), matrix)
        matrix = Matrix.multiply(Matrix.getTranslation(0f,0f,0f), matrix)
        matrix = Matrix.multiply(
            Matrix.getLookAt(
                Vector(0f,0f,camZ),
                Vector(0f,0f,-1f),
                Vector(0f,1f,0f)
            ), matrix
        )
        matrix = Matrix.multiply(Matrix.getPerspectiveProjection(
            90f, (800f/600f).toFloat(), -1f, -1000f), matrix
        )

        return matrix
    }

    fun render() {
        var img = findViewById<ImageView>(R.id.draw_view)

        var sceneVertices = mutableListOf<Vector>()
        var matrix = genMatrix(currX.toFloat(),currY.toFloat(),currZ.toFloat(), currDistance.toFloat())
        var i = 0
        while (i < cubeVertices.size) {
            var vertex = Matrix.multiplyVector(matrix, cubeVertices[i])
            vertex.x = vertex.x / vertex.w * 400
            vertex.y = vertex.y / vertex.w * 300
            sceneVertices.add(vertex)
            i++
        }

        var drawer = Drawer(800,600)

        i = 0
        while (i < cubeEdges.size) {
            var e = cubeEdges[i]

            drawer.drawLine(sceneVertices[e[0]].x,
                sceneVertices[e[0]].y,
                sceneVertices[e[1]].x,
                sceneVertices[e[1]].y,
                0f,0f,1f
            )
            i++
        }

        img.setImageBitmap(drawer.bitmap)
    }
}