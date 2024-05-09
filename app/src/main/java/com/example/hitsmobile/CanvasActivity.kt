package com.example.hitsmobile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hitsmobile.canvas.DrawView
import com.google.android.material.slider.RangeSlider
import java.io.OutputStream


class CanvasActivity : AppCompatActivity() {
    /*Блок для рисования*/
    private lateinit var paint: DrawView

    /*Кнопка домой*/
    private lateinit var homeBtn: ImageView

    /*Кнопка отправить*/
    private lateinit var shareBtn: ImageView

    /*Кнопка для сплайнов*/
    private lateinit var splineBtn: ImageView

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

    /*Блок для сплайнов*/
    private lateinit var linearSpline: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)

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
        rangeSlider.valueTo = 100.0f
        rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            paint.setStrokeWidth(
                value.toInt()
            )
        })

        /*Кнопка и блок для сплайнов*/
        linearSpline = findViewById(R.id.linearSpline)
        splineBtn = findViewById(R.id.imgSpline)
        splineBtn.setOnClickListener{
            if (linearSpline.visibility == View.VISIBLE)
                linearSpline.visibility = View.GONE else linearSpline.visibility = View.VISIBLE
        }

        colorBtn = findViewById(R.id.imgColor)
        colorBtn.setOnClickListener {

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
}