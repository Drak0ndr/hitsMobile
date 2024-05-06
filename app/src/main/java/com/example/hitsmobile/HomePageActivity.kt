package com.example.hitsmobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePageActivity : AppCompatActivity() {
    /*Переход на страницу с фото*/
    public lateinit var algorithmsBtn : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Переход на страницу с фото*/
        algorithmsBtn = findViewById(R.id.algorithmsBtn)
        algorithmsBtn.setOnClickListener(){
            val intent = Intent(this@HomePageActivity, PhotoActivity::class.java)
            startActivity(intent)
        }
    }
}