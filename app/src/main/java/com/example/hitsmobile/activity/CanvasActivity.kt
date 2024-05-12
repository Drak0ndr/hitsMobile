package com.example.hitsmobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hitsmobile.R
import com.example.hitsmobile.canvas.Colors
import com.example.hitsmobile.canvas.ColorsAdapter
import com.example.hitsmobile.canvas.DrawView
import com.google.android.material.slider.RangeSlider


class CanvasActivity : AppCompatActivity(), ColorsAdapter.OnItemSelected {
    /*Скролл для выбора цвета*/
    private lateinit var rvColors: RecyclerView
    private val colorsAdapter = ColorsAdapter(this)

    /*Блок для рисования*/
    private lateinit var paint: DrawView

    /*Кнопка домой*/
    private lateinit var homeBtn: ImageView

    /*Кнопка отправить*/
    private lateinit var shareBtn: ImageView

    /*Кнопка сохранения изображения*/
    private lateinit var saveBtn: ImageView

    /*Кнопка выбора цвета*/
    private lateinit var colorBtn: ImageView

    /*Кнопка выбора размера кисти*/
    private lateinit var strokeBtn: ImageView

    /*Кнопка для отмены*/
    private lateinit var backBtn: ImageView

    /*Ползунок для выбора размера кисти*/
    private lateinit var rangeSlider: RangeSlider

    /*Блок для выбора цвета*/
    private lateinit var linearColors: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

        /*Адаптер для выбора цвета*/
        rvColors = findViewById(R.id.recyclerViewColors)
        rvColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColors.adapter = colorsAdapter

        /*Блок для рисования*/
        paint = findViewById(R.id.draw_view)

        /*Кнопка домой*/
        homeBtn = findViewById(R.id.imgHome)
        homeBtn.setOnClickListener{
            val intent = Intent(this@CanvasActivity, HomePageActivity::class.java)
            startActivity(intent)
        }

        /*Кнопка для отмены*/
        backBtn = findViewById(R.id.imgBack)
        backBtn.setOnClickListener {
            paint.back()
        }

        /*Сохранение фотографии в галерею*/
        saveBtn = findViewById(R.id.imgSave)
        saveBtn.setOnClickListener{
            val bitmap = paint!!.save()
            val title = "image_" + System.currentTimeMillis() + ".jpg"
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "")
            Toast.makeText(this,"Изображение сохранено", Toast.LENGTH_LONG).show()
        }

        /*Отправка фото*/
        shareBtn = findViewById(R.id.imgShare)
        shareBtn.setOnClickListener(){
            val wl = paint!!.save()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND

            val path = MediaStore.Images.Media.insertImage(contentResolver, wl, "newImg", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, "Share"))
        }

        /*Кнопка для выбора размера кисти*/
        strokeBtn = findViewById(R.id.imgStroke)
        strokeBtn.setOnClickListener {
            if (rangeSlider.visibility == View.VISIBLE)
                rangeSlider.visibility = View.GONE else rangeSlider.visibility = View.VISIBLE
        }

        /*Ползунок для выбора размера кисти*/
        rangeSlider = findViewById(R.id.rangebar)
        rangeSlider.valueFrom = 0.0f
        rangeSlider.setValues(20.0f)
        rangeSlider.valueTo = 100.0f
        rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            paint.setStrokeWidth(
                value.toInt()
            )
        })

        /*Кнопка и блок для выбора цвета*/
        linearColors = findViewById(R.id.linearColors)
        colorBtn = findViewById(R.id.imgColor)
        colorBtn.setOnClickListener {
            if (linearColors.visibility == View.VISIBLE)
                linearColors.visibility = View.GONE else linearColors.visibility = View.VISIBLE
        }

        /*Устанавливаем размер холста*/
        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight
                paint.init(height, width)
            }
        })
    }

    override fun onColorSelected(colors: Colors) {
        when (colors) {
            Colors.BLACK -> {paint.setColor(Color.BLACK);}

            Colors.BLUE -> {paint.setColor(Color.parseColor("#0000FF"));}

            Colors.RED -> {paint.setColor(Color.parseColor("#FF0000"));}

            Colors.GRAY -> {paint.setColor(Color.parseColor("#808080"));}

            Colors.GREEN -> {paint.setColor(Color.parseColor("#00FF00"));}

            Colors.ORANGE -> {paint.setColor(Color.parseColor("#FFA500"));}

            Colors.PINK -> {paint.setColor(Color.parseColor("#e45eb0"));}

            Colors.YELLOW -> {paint.setColor(Color.parseColor("#FFFF00"));}

            Colors.BROWN -> {paint.setColor(Color.parseColor("#4E3524"));}

            Colors.PURPLE -> {paint.setColor(Color.parseColor("#8031A7"));}

            Colors.AZURE-> {paint.setColor(Color.parseColor("#008AD8"));}

            Colors.DARKGREEN -> {paint.setColor(Color.parseColor("#19543E"));}
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }
}