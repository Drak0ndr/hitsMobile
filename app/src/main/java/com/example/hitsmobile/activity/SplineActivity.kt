package com.example.hitsmobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hitsmobile.R
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView0
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView1
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView2
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView3
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView4
import com.example.hitsmobile.activity.SplineActivity.MyFun.imageView5
import com.example.hitsmobile.activity.SplineActivity.MyFun.paint
import com.example.hitsmobile.algorithms.ChoosingShapesFragment
import com.example.hitsmobile.canvas.CustomView
import com.example.hitsmobile.canvas.HexagonView
import com.example.hitsmobile.canvas.OctagonView
import com.example.hitsmobile.canvas.PoligonView
import com.example.hitsmobile.canvas.SquareView
import com.example.hitsmobile.canvas.TriangleView

class SplineActivity : AppCompatActivity() {
    /*Кнопка домой*/
    private lateinit var homeBtn: ImageView

    /*Кнопка отправить*/
    private lateinit var shareBtn: ImageView

    /*Кнопка сохранения изображения*/
    private lateinit var saveBtn: ImageView

    /*Кнопка для диалога*/
    private lateinit var shapeBtn: ImageView

    /*Кнопка для отмены*/
    private lateinit var backBtn: ImageView

    /*Кнопка для алгоритма сплайна*/
    private lateinit var startBtn: ImageView
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spline)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_spline)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView4 = findViewById(R.id.draw_view)
        imageView0 = findViewById(R.id.draw_view2)
        imageView1 = findViewById(R.id.draw_view3)
        imageView2 = findViewById(R.id.draw_view4)
        imageView3 = findViewById(R.id.draw_view5)
        imageView5 = findViewById(R.id.draw_view6)

        /*Кнопка для диалога*/
        shapeBtn = findViewById(R.id.imgShapes)
        shapeBtn.setOnClickListener {
            val choosingShapesFragment = ChoosingShapesFragment()
            val manager = supportFragmentManager
            choosingShapesFragment.show(manager, "myDialog")
        }

        /*Кнопка для алгоритма сплайна*/
        startBtn = findViewById(R.id.imgSpline)
        startBtn.setOnClickListener(){
            if(MyFun.currShape == 6){
                imageView5.start()
            }
            else if(MyFun.currShape == 4 || MyFun.currShape == 5){
                paint.start()
            }
        }

        /*Блок для рисования*/
        paint = findViewById(R.id.draw_view)

        /*Кнопка домой*/
        homeBtn = findViewById(R.id.imgHome)
        homeBtn.setOnClickListener{
            val intent = Intent(this@SplineActivity, HomePageActivity::class.java)
            startActivity(intent)
        }

        /*Кнопка для отмены*/
        backBtn = findViewById(R.id.imgBack)
        backBtn.setOnClickListener {
            when (MyFun.currShape) {
                0 -> {
                    imageView0.reset()
                }
                1 -> {
                    imageView1.reset()
                }
                2 -> {
                    imageView2.reset()
                }
                3 -> {
                    imageView3.reset()
                }
                6 -> {
                    imageView5.reset()
                }
                else -> {
                    paint.back()
                }
            }
        }

        /*Сохранение фотографии в галерею*/
        saveBtn = findViewById(R.id.imgSave)
        saveBtn.setOnClickListener{
            val bitmap = paint.save()
            val title = "image_" + System.currentTimeMillis() + ".jpg"
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "")
            Toast.makeText(this,"Изображение сохранено", Toast.LENGTH_LONG).show()
        }

        /*Отправка фото*/
        shareBtn = findViewById(R.id.imgShare)
        shareBtn.setOnClickListener(){
            val wl = paint.save()
            val intent = Intent()
            intent.action = Intent.ACTION_SEND

            val path = MediaStore.Images.Media.insertImage(contentResolver, wl, "newImg", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, "Share"))
        }

        /*Устанавливаем размер холста*/
        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight

                paint.init(height, width)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    object MyFun {
        /*Блок для рисования*/
        lateinit var paint: CustomView

        var currShape : Int = 4
        lateinit var imageView4 : CustomView
        lateinit var imageView0 : TriangleView
        lateinit var imageView1 : SquareView
        lateinit var imageView2 : HexagonView
        lateinit var imageView3 : OctagonView
        lateinit var imageView5 : PoligonView

        fun checkView(numberView : Int){
            when(numberView){
                0 -> {
                    currShape = 0

                    imageView4.visibility = View.GONE
                    imageView0.visibility = View.VISIBLE
                    imageView1.visibility = View.GONE
                    imageView2.visibility = View.GONE
                    imageView3.visibility = View.GONE
                    imageView5.visibility = View.GONE
                }
                1 -> {
                    currShape = 1

                    imageView4.visibility = View.GONE
                    imageView0.visibility = View.GONE
                    imageView1.visibility = View.VISIBLE
                    imageView2.visibility = View.GONE
                    imageView3.visibility = View.GONE
                    imageView5.visibility = View.GONE
                }
                2 -> {
                    currShape = 2

                    imageView4.visibility = View.GONE
                    imageView0.visibility = View.GONE
                    imageView1.visibility = View.GONE
                    imageView2.visibility = View.VISIBLE
                    imageView3.visibility = View.GONE
                    imageView5.visibility = View.GONE
                }
                3 -> {
                    currShape = 3

                    imageView4.visibility = View.GONE
                    imageView0.visibility = View.GONE
                    imageView1.visibility = View.GONE
                    imageView2.visibility = View.GONE
                    imageView3.visibility = View.VISIBLE
                    imageView5.visibility = View.GONE
                }
                4 -> {
                    if(currShape != 4){
                        paint.reset()
                        currShape = 4

                        imageView4.visibility = View.VISIBLE
                        imageView0.visibility = View.GONE
                        imageView1.visibility = View.GONE
                        imageView2.visibility = View.GONE
                        imageView3.visibility = View.GONE
                        imageView5.visibility = View.GONE
                    }
                }
                5 -> {
                    if(currShape != 5){
                        paint.reset()
                        currShape = 5

                        imageView4.visibility = View.VISIBLE
                        imageView0.visibility = View.GONE
                        imageView1.visibility = View.GONE
                        imageView2.visibility = View.GONE
                        imageView3.visibility = View.GONE
                        imageView5.visibility = View.GONE
                    }
                }
                6 ->{
                    currShape = 6

                    imageView4.visibility = View.GONE
                    imageView0.visibility = View.GONE
                    imageView1.visibility = View.GONE
                    imageView2.visibility = View.GONE
                    imageView3.visibility = View.GONE
                    imageView5.visibility = View.VISIBLE
                }
            }
        }
    }
}