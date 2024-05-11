package com.example.hitsmobile.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.hitsmobile.R
import com.example.hitsmobile.canvas.CustomView

class SplineActivity : AppCompatActivity() {
    /*Блок для рисования*/
    private lateinit var paint2: CustomView

    /*Кнопка домой*/
    private lateinit var homeBtn2: ImageView

    /*Кнопка отправить*/
    private lateinit var shareBtn2: ImageView

    /*Кнопка сохранения изображения*/
    private lateinit var saveBtn2: ImageView

    /*Кнопка для отмены*/
    private lateinit var backBtn2: ImageView

    /*Кнопка для алгоритма сплайна*/
    public lateinit var startBtn2: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spline)

        /*Блок для рисования*/
        paint2 = findViewById(R.id.draw_view2)

        /*Кнопка домой*/
        homeBtn2 = findViewById(R.id.imgHome2)
        homeBtn2.setOnClickListener{
            val intent = Intent(this@SplineActivity, HomePageActivity::class.java)
            startActivity(intent)
        }

        /*Кнопка для отмены*/
        backBtn2 = findViewById(R.id.imgBack2)
        backBtn2.setOnClickListener {
            paint2.back()
        }

        /*Сохранение фотографии в галерею*/
        saveBtn2 = findViewById(R.id.imgSave2)
        saveBtn2.setOnClickListener{
            val bitmap = paint2!!.save()
            val title = "image_" + System.currentTimeMillis() + ".jpg"
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "")
            Toast.makeText(this,"Изображение сохранено", Toast.LENGTH_LONG).show()
        }

        /*Отправка фото*/
        shareBtn2 = findViewById(R.id.imgShare2)
        shareBtn2.setOnClickListener(){
            val wl = paint2!!.save()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND

            val path = MediaStore.Images.Media.insertImage(contentResolver, wl, "newImg", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, "Share"))
        }

        /*Устанавливаем размер холста*/
        val vto = paint2.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint2.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint2.measuredWidth
                val height = paint2.measuredHeight
                paint2.init(height, width)
            }
        })
    }
}