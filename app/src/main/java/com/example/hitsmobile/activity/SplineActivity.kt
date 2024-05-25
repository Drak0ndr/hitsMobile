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
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.drawView
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.hexagonView
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.octagonView
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.paint
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.polygonView
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.squareView
import com.example.hitsmobile.activity.SplineActivity.MyVarSpline.triangleView
import com.example.hitsmobile.algorithms.ChoosingShapesFragment
import com.example.hitsmobile.canvas.CustomView
import com.example.hitsmobile.canvas.HexagonView
import com.example.hitsmobile.canvas.OctagonView
import com.example.hitsmobile.canvas.PolygonView
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

        drawView = findViewById(R.id.draw_view)
        triangleView = findViewById(R.id.triangle_view)
        squareView = findViewById(R.id.square_view)
        hexagonView = findViewById(R.id.hexagon_view)
        octagonView = findViewById(R.id.octagon_view)
        polygonView = findViewById(R.id.polygon_view)

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
            if(MyVarSpline.currShape == 6){
                polygonView.start()
            }
            else if(MyVarSpline.currShape == 4 || MyVarSpline.currShape == 5){
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
            when (MyVarSpline.currShape) {
                0 -> {
                    triangleView.reset()
                }
                1 -> {
                    squareView.reset()
                }
                2 -> {
                    hexagonView.reset()
                }
                3 -> {
                    octagonView.reset()
                }
                6 -> {
                    polygonView.reset()
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

    object MyVarSpline {
        /*Блок для рисования*/
        lateinit var paint: CustomView

        var currShape : Int = 4
        lateinit var drawView : CustomView
        lateinit var triangleView : TriangleView
        lateinit var squareView : SquareView
        lateinit var hexagonView : HexagonView
        lateinit var octagonView : OctagonView
        lateinit var polygonView : PolygonView

        fun checkView(numberView : Int){
            when(numberView){
                0 -> {
                    currShape = 0

                    drawView.visibility = View.GONE
                    triangleView.visibility = View.VISIBLE
                    squareView.visibility = View.GONE
                    hexagonView.visibility = View.GONE
                    octagonView.visibility = View.GONE
                    polygonView.visibility = View.GONE
                }
                1 -> {
                    currShape = 1

                    drawView.visibility = View.GONE
                    triangleView.visibility = View.GONE
                    squareView.visibility = View.VISIBLE
                    hexagonView.visibility = View.GONE
                    octagonView.visibility = View.GONE
                    polygonView.visibility = View.GONE
                }
                2 -> {
                    currShape = 2

                    drawView.visibility = View.GONE
                    triangleView.visibility = View.GONE
                    squareView.visibility = View.GONE
                    hexagonView.visibility = View.VISIBLE
                    octagonView.visibility = View.GONE
                    polygonView.visibility = View.GONE
                }
                3 -> {
                    currShape = 3

                    drawView.visibility = View.GONE
                    triangleView.visibility = View.GONE
                    squareView.visibility = View.GONE
                    hexagonView.visibility = View.GONE
                    octagonView.visibility = View.VISIBLE
                    polygonView.visibility = View.GONE
                }
                4 -> {
                    if(currShape != 4){
                        paint.reset()
                        currShape = 4

                        drawView.visibility = View.VISIBLE
                        triangleView.visibility = View.GONE
                        squareView.visibility = View.GONE
                        hexagonView.visibility = View.GONE
                        octagonView.visibility = View.GONE
                        polygonView.visibility = View.GONE
                    }
                }
                5 -> {
                    if(currShape != 5){
                        paint.reset()
                        currShape = 5

                        drawView.visibility = View.VISIBLE
                        triangleView.visibility = View.GONE
                        squareView.visibility = View.GONE
                        hexagonView.visibility = View.GONE
                        octagonView.visibility = View.GONE
                        polygonView.visibility = View.GONE
                    }
                }
                6 ->{
                    currShape = 6

                    drawView.visibility = View.GONE
                    triangleView.visibility = View.GONE
                    squareView.visibility = View.GONE
                    hexagonView.visibility = View.GONE
                    octagonView.visibility = View.GONE
                    polygonView.visibility = View.VISIBLE
                }
            }
        }
    }
}