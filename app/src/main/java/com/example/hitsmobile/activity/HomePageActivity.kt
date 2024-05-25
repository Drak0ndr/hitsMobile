package com.example.hitsmobile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hitsmobile.Engine3D.CubeActivity
import com.example.hitsmobile.R

class HomePageActivity : AppCompatActivity() {
    var backPressedTime: Long = 0

    /*Переход на страницу с фото*/
    lateinit var algorithmsBtn : AppCompatButton

    /*Переход на страницу с рисованием*/
    lateinit var drawingBtn : AppCompatButton

    /*Переход на страницу со сплайнами*/
    lateinit var splineBtn : AppCompatButton

    /*Переход на страницу с кубом*/
    lateinit var btn3D : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Переход на страницу со сплайнами*/
        splineBtn = findViewById(R.id.splineBtn)
        splineBtn.setOnClickListener(){
            val intent = Intent(this@HomePageActivity, SplineActivity::class.java)
            startActivity(intent)
        }

        /*Переход на страницу с фото*/
        algorithmsBtn = findViewById(R.id.algorithmsBtn)
        algorithmsBtn.setOnClickListener(){
            val intent = Intent(this@HomePageActivity, PhotoActivity::class.java)
            startActivity(intent)
        }

        /*Переход на страницу с рисованием*/
        drawingBtn = findViewById(R.id.drawingBtn)
        drawingBtn.setOnClickListener(){
            val intent = Intent(this@HomePageActivity, CanvasActivity::class.java)
            startActivity(intent)
        }

        /*Переход на страницу с кубом*/
        btn3D = findViewById(R.id.btn3D)
        btn3D.setOnClickListener(){
            val intent = Intent(this@HomePageActivity, CubeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            moveTaskToBack(true)
        } else {
            Toast.makeText(this, "Нажмите на кнопку назад 2 раза, чтобы выйти из приложения", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}