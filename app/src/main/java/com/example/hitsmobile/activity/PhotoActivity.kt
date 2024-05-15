package com.example.hitsmobile.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.hitsmobile.R
import com.example.hitsmobile.algorithms.Mask
import com.example.hitsmobile.algorithms.MyDialogFragment
import com.example.hitsmobile.algorithms.Resize
import com.example.hitsmobile.algorithms.Retouch
import com.example.hitsmobile.algorithms.Rotate
import com.example.hitsmobile.filters.ColorFilters
import com.example.hitsmobile.filters.FilterViewAdapter
import com.example.hitsmobile.filters.PhotoFilter
import com.example.hitsmobile.tools.ToolsAdapter
import com.example.hitsmobile.tools.ToolsAdapter.OnItemSelected
import com.example.hitsmobile.tools.ToolsType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.IOException
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.math.floor


open class PhotoActivity: AppCompatActivity(), OnItemSelected, FilterViewAdapter.FilterListener {
    /*Выпадающий блок*/
    private lateinit var spinner : Spinner

    /*Контейнер для аффинных преобразований*/
    private lateinit var transformBlock :RelativeLayout

    /*Кнопки для поворота*/
    private lateinit var leftBtn: ImageView
    private lateinit var rightBtn:ImageView

    /*Скролл для алгоритмов*/
    private lateinit var rvTools: RecyclerView
    private val toolsAdapter = ToolsAdapter(this)

    /*Работа с камерой и галереей*/
    private lateinit var galleryButton: ImageView
    private lateinit var cameraButton :ImageView
    private lateinit var img: ImageView

    /*Скролл для фильтров*/
    private lateinit var rvFilters: RecyclerView
    private val filterAdapter = FilterViewAdapter(this)
    private lateinit var mainView: ConstraintLayout
    private val mConstraintSet = ConstraintSet()
    private var mIsFilterVisible = false

    /*Отслеживаем текущие выдвижные блоки*/
    private var currNumberBlock: Int = 0
    private var currBlock: Int = 0
    private lateinit var closeButton: ImageView

    /*Контейнер для поворота*/
    private lateinit var rlRotate: RelativeLayout

    /*Контейнер для изменения размера фото*/
    private lateinit var rlResize: RelativeLayout

    /*Контейнер для изменения ретуши*/
    private lateinit var rlRetouch: RelativeLayout

    /*Контейнер для маскирования*/
    private lateinit var maskingBlock: RelativeLayout

    /*Ползунок для поворота*/
    private lateinit var seekBarRotate: SeekBar
    private lateinit var seekBarProgressRotate: TextView

    /*Ползунок для изменения размера фото*/
    private lateinit var seekBarResize: SeekBar
    private lateinit var seekBarProgressResize: TextView

    /*Ползунок для резкости ретуши*/
    private lateinit var seekBarRetouchSharpness: SeekBar
    private lateinit var currSharpness: TextView

    /*Ползунок для радиуса ретуши*/
    private lateinit var seekBarRetouchRadius: SeekBar
    private lateinit var currRadius: TextView

    /*Ползунок для интенсивности маскирования*/
    private lateinit var seekBarMasking: SeekBar
    private lateinit var progressBarCurr2: TextView

    /*Кнопка домой*/
    private lateinit var homeBtn : ImageView

    /*Кнопка для алгоритма маскирования*/
    private lateinit var maskingStartBtn : AppCompatButton

    /*Сохранение фотографии в галерею*/
    private lateinit var saveBtn : ImageView

    /*Отправка фото*/
    private lateinit var shareBtn : ImageView

    /*Отслеживаем переключения для изменения размера фото*/
    private lateinit var radio : RadioGroup
    private var currRadio : Int = 1

    /*Кнопка для диалога*/
    private lateinit var helpImg : ImageView

    var retouch = Retouch()
    lateinit var retouchImg : Bitmap
    private var pX = 0f
    private var pY = 0f

    /*Точки для аффинных преобразований*/
    private lateinit var redImg : ImageView
    private lateinit var blueImg : ImageView
    private lateinit var greenImg : ImageView

    /*Кнопки для аффинных преобразований*/
    private lateinit var firstPointsBtn : AppCompatButton
    private lateinit var secondPointsBtn : AppCompatButton
    private lateinit var startTransformBtn : AppCompatButton

    /*Сохраняем точки для аффинных преобразований*/
    var listFirstPoints = mutableListOf<Pair<Float, Float>>()
    var listSecondPoints = mutableListOf<Pair<Float, Float>>()

    private var pairsList2: MutableList<Pair<Bitmap, PhotoFilter>> = ArrayList()

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility", "ResourceType",
        "UseCompatLoadingForDrawables"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_photo)

        /*Точки для аффинных преобразований*/
        redImg = findViewById(R.id.redImg)
        blueImg = findViewById(R.id.blueImg)
        greenImg = findViewById(R.id.greenImg)

        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!")
        else
            Log.d("OpenCV", "OpenCV loaded Successfully!")

        /*Динамическая загрузка изображения*/
        val imageView = findViewById<ImageView>(R.id.photoEditorView)
        var PictureWidth = imageView.drawable.intrinsicWidth
        var PictureHeight = imageView.drawable.intrinsicHeight
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null

        executor.execute {
            val imageURL =
                "https://fydi.ru/wp-content/uploads/2021/08/koty-i-koshki-89.jpg"
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageView.setImageBitmap(image)
                    MyVariables.currImg = (image as Bitmap?)!!
                    MyVariables.rotateImg = MyVariables.currImg
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /*Кнопки для аффинных преобразований*/
        firstPointsBtn = findViewById(R.id.pointsBtn1)
        firstPointsBtn.setOnClickListener(){
            if(listFirstPoints.size < 3){
                MyVariables.isFirstPoints = true
                MyVariables.isSecondPoints = false
                firstPointsBtn = findViewById(R.id.pointsBtn1)
                firstPointsBtn.background = this.getResources().getDrawable(R.drawable.pressed_btn)
            }
        }

        secondPointsBtn = findViewById(R.id.pointsBtn2)
        secondPointsBtn.setOnClickListener(){
            if(listSecondPoints.size < 3) {
                if (MyVariables.isFirstPoints || listFirstPoints.size < 3) {
                    Toast.makeText(this, "Сначала расставьте начальные точки!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    MyVariables.isSecondPoints = true
                    MyVariables.isFirstPoints = false
                    secondPointsBtn.background =
                        this.getResources().getDrawable(R.drawable.pressed_btn)
                    redImg.visibility = View.GONE
                    blueImg.visibility = View.GONE
                    greenImg.visibility = View.GONE
                }
            }
        }

        startTransformBtn = findViewById(R.id.transformStartBtn)
        startTransformBtn.setOnClickListener(){
            if(listSecondPoints.size < 3 || listFirstPoints.size < 3){
                Toast.makeText(this,"Сначала расставьте все точки!",Toast.LENGTH_LONG).show()
            }
            else{
                MyVariables.isFirstPoints = false
                MyVariables.isSecondPoints = false
                redImg.visibility = View.GONE
                blueImg.visibility = View.GONE
                greenImg.visibility = View.GONE


                listSecondPoints.clear()
                listFirstPoints.clear()
            }
        }

        /*Выпадающий блок*/
        spinner = findViewById(R.id.mySpinner)
        val adapter = ArrayAdapter.createFromResource(this, R.array.choiceFilter, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        /*Адаптер для алгоритмов*/
        rvTools = findViewById(R.id.recyclerViewTools)
        rvTools.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTools.adapter = toolsAdapter

        mainView = findViewById(R.id.main)

        /*Адаптер для фильтров*/
        rvFilters = findViewById(R.id.recyclerViewFilter)
        rvFilters.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFilters.adapter = filterAdapter

        /*Контейнер для аффинных преобразований*/
        transformBlock = findViewById(R.id.transformBlock)

        /*Контейнер для поворота*/
        rlRotate = findViewById(R.id.rotateBlock)

        /*Контейнер для изменения размера*/
        rlResize = findViewById(R.id.resizeBlock)

        /*Контейнер для изменения ретуши*/
        rlRetouch = findViewById(R.id.retouchBlock)

        /*Контейнер для маскирования*/
        maskingBlock = findViewById(R.id.maskingBlock)

        /*Ползунок для поворота*/
        seekBarRotate = findViewById(R.id.seekBarRotate)
        seekBarProgressRotate = findViewById(R.id.progressBarCurr)

        /*Ползунок для резкости ретуши*/
        seekBarRetouchSharpness = findViewById(R.id.seekBarRetouchSharpness)
        currSharpness = findViewById(R.id.currSharpness)

        /*Ползунок для интенсивности маскирования*/
        seekBarMasking = findViewById(R.id.seekBarMasking)
        progressBarCurr2 = findViewById(R.id.progressBarCurr2)

        /*Ползунок для радиуса ретуши*/
        seekBarRetouchRadius = findViewById(R.id.seekBarRetouchRadius)
        currRadius = findViewById(R.id.currRadius)

        /*Ползунок для изменения размера фото*/
        seekBarResize = findViewById(R.id.seekBarResize)
        seekBarProgressResize = findViewById(R.id.progressBarCurrResize)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Работа с камерой*/
        cameraButton = findViewById(R.id.imgCamera)
        cameraButton.setOnClickListener(){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)

            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }

        /*Работа с галереей*/
        galleryButton = findViewById(R.id.imgGallery)
        galleryButton.setOnClickListener {
            if(Build.VERSION.SDK_INT < 33){
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)

                } else {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.setType("image/*")
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
                }
            }
            else{
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), GALLERY_REQUEST2)

                } else {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.setType("image/*")
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST2)
                }
            }
        }

        /*Сохранение фотографии в галерею*/
        saveBtn = findViewById(R.id.imgSave)
        saveBtn.setOnClickListener{
            val imageView = findViewById<ImageView>(R.id.photoEditorView)
            val drawable = imageView.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val title = "image_" + System.currentTimeMillis() + ".jpg"
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, title, "")
            Toast.makeText(this,"Изображение сохранено",Toast.LENGTH_LONG).show()
        }

        /*Кнопка домой*/
        homeBtn = findViewById(R.id.imgHome)
        homeBtn.setOnClickListener{
            val intent = Intent(this@PhotoActivity, HomePageActivity::class.java)
            startActivity(intent)
        }

        /*Кнопка для диалога*/
        helpImg = findViewById(R.id.helpImg)
        helpImg.setOnClickListener(){
            val myDialogFragment = MyDialogFragment()
            val manager = supportFragmentManager
            myDialogFragment.show(manager, "myDialog")
        }

        /*Отслеживаем выдвижные блоки*/
        closeButton = findViewById(R.id.imgClose)
        closeButton.setOnClickListener{
            onBackPressed()
        }

        /*Отправка фото*/
        shareBtn = findViewById(R.id.imgShare)
        shareBtn.setOnClickListener(){

            val wl = (imageView.getDrawable() as BitmapDrawable).bitmap
            val intent = Intent()
            intent.action = Intent.ACTION_SEND

            val path = MediaStore.Images.Media.insertImage(contentResolver, wl, "newImg", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.type = "image/*"
            startActivity(Intent.createChooser(intent, "Share"))
        }

        /*Поворот влево*/
        leftBtn = findViewById(R.id.imgLeftRotate)
        leftBtn.setOnClickListener(){
            var newImg = findViewById<ImageView>(R.id.photoEditorView)
            var rotate = Rotate()
            MyVariables.rotateImg = rotate.rotateLeft(MyVariables.rotateImg)
            newImg.setImageBitmap(MyVariables.rotateImg)
            MyVariables.currImg = rotate.rotateLeft(MyVariables.currImg)
        }

        /*Поворот вправо*/
        rightBtn = findViewById(R.id.imgRightRotate)
        rightBtn.setOnClickListener(){
            var newImg = findViewById<ImageView>(R.id.photoEditorView)
            var rotate = Rotate()
            MyVariables.rotateImg = rotate.rotateRight(MyVariables.rotateImg)
            newImg.setImageBitmap(MyVariables.rotateImg)
            MyVariables.currImg = rotate.rotateRight(MyVariables.currImg)
        }

        /*Кнопка для алгоритма маскирования*/
        maskingStartBtn = findViewById(R.id.maskingStartBtn)
        maskingStartBtn.setOnClickListener(){
            var mask = Mask()
            var newMask = mask.UnsharpMask(MyVariables.rotateImg, (seekBarMasking.progress).toFloat())
            imageView.setImageBitmap(newMask)
            MyVariables.currImg = newMask
            MyVariables.rotateImg = newMask
            seekBarMasking.progress = 5
        }

        /*Отслеживаем переключения для изменения размера фото*/
        radio = findViewById(R.id.radio_group)
        radio.setOnCheckedChangeListener{ _, checkedId ->
            if(currRadio == 1){
                currRadio = 2
            }
            else{
                currRadio = 1
            }

            seekBarResize.setProgress(1)
        }

        /*Отслеживаем изменения ползунка для радиуса ретуши*/
        seekBarRetouchRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currRadius.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Отслеживаем изменения ползунка для интенсивности маскирования*/
        seekBarMasking.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressBarCurr2.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Отслеживаем изменения ползунка для резкости ретуши*/
        seekBarRetouchSharpness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currSharpness.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        /*Отслеживаем изменения ползунка для поворота*/
        seekBarRotate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarProgressRotate.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                var newImg = findViewById<ImageView>(R.id.photoEditorView)
                var rotate = Rotate()
                MyVariables.rotateImg = rotate.rotateAny(MyVariables.rotateImg, seekBarRotate.progress)
                newImg.setImageBitmap(MyVariables.rotateImg)
                MyVariables.currImg = rotate.rotateAny(MyVariables.currImg, seekBarRotate.progress)
                seekBarRotate.progress = 0
            }
        })

        /*Отслеживаем изменения ползунка для изменения размера*/
        seekBarResize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val change: Double
                change = progress.toDouble() / 100
                seekBarProgressResize.text = "$change"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                var newImg = findViewById<ImageView>(R.id.photoEditorView)
                var resize = Resize()

                if(currRadio == 1){
                    MyVariables.rotateImg = resize.bilinearFilter(resize.upScale(MyVariables.rotateImg, (seekBarResize.progress).toFloat() / 100))
                    newImg.setImageBitmap(MyVariables.rotateImg)
                    MyVariables.currImg = resize.bilinearFilter(resize.upScale(MyVariables.currImg, (seekBarResize.progress).toFloat() / 100))
                    seekBarResize.progress = 0
                }
                else{
                    MyVariables.rotateImg = resize.trilinearFilter(resize.downScale(MyVariables.rotateImg, (seekBarResize.progress).toFloat() / 100),
                        resize.downScale(MyVariables.rotateImg, (seekBarResize.progress).toFloat() / 50))
                    newImg.setImageBitmap(MyVariables.rotateImg)
                    MyVariables.currImg = resize.trilinearFilter(resize.downScale(MyVariables.currImg, (seekBarResize.progress).toFloat() / 100),
                        resize.downScale(MyVariables.currImg, (seekBarResize.progress).toFloat() / 50))
                    seekBarResize.progress = 0
                }
            }
        })

        /*Алгоритм ретуши*/
        val otl: View.OnTouchListener = object : View.OnTouchListener {
            var inverse = Matrix()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val  x = event.x
                        val y = event.y

                        if(MyVariables.isFirstPoints && x > 0 && y > 0 && x < PictureWidth && y < PictureHeight){
                            if(listFirstPoints.size == 0){
                                val param = redImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30 ,y.toInt() - 30,0,0)
                                redImg.visibility = View.VISIBLE
                                listFirstPoints.add(x to y)
                            }
                            else if(listFirstPoints.size == 1){
                                val param = blueImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30 ,y.toInt() - 30,0,0)
                                blueImg.visibility = View.VISIBLE
                                listFirstPoints.add(x to y)
                            }
                            else if(listFirstPoints.size == 2){
                                val param = greenImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30,y.toInt() - 30,0,0)
                                greenImg.visibility = View.VISIBLE
                                listFirstPoints.add(x to y)
                                firstPointsBtn.background = this@PhotoActivity.getResources().getDrawable(R.drawable.btn_bg)
                                MyVariables.isFirstPoints = false
                            }
                        }
                        else if(MyVariables.isSecondPoints && x > 0 && y > 0 && x < PictureWidth && y < PictureHeight){
                            if(listSecondPoints.size == 0){
                                val param = redImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30 ,y.toInt() - 30,0,0)
                                redImg.visibility = View.VISIBLE
                                listSecondPoints.add(x to y)
                            }
                            else if(listSecondPoints.size == 1){
                                val param = blueImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30 ,y.toInt() - 30,0,0)
                                blueImg.visibility = View.VISIBLE
                                listSecondPoints.add(x to y)
                            }
                            else if(listSecondPoints.size == 2){
                                val param = greenImg.layoutParams as ViewGroup.MarginLayoutParams
                                param.setMargins(x.toInt() - 30,y.toInt() - 30,0,0)
                                greenImg.visibility = View.VISIBLE
                                listSecondPoints.add(x to y)
                                secondPointsBtn.background = this@PhotoActivity.getResources().getDrawable(R.drawable.btn_bg)
                                MyVariables.isSecondPoints = false
                            }
                        }
                    }

                    ACTION_MOVE -> {
                        if(MyVariables.isRetouch) {
                            imageView.imageMatrix.invert(inverse)

                            val pts = floatArrayOf(event.x, event.y)

                            inverse.mapPoints(pts)

                            if (pX != floor(pts[0]) && pY != floor(pts[1]) &&
                                floor(pts[0]) > 0 && floor(pts[1]) > 0 &&
                                floor(pts[0]) < PictureWidth && floor(pts[1]) < PictureHeight
                            ) {

                                Log.d(
                                    ContentValues.TAG,
                                    "onTouch x: " + floor(pts[0].toDouble()) + ", y: " + floor(pts[1].toDouble())
                                )

                                retouchImg = retouch.blur(
                                    MyVariables.rotateImg, (seekBarRetouchRadius.progress).toFloat(),
                                    (seekBarRetouchSharpness.progress).toFloat(), floor(pts[0]).toInt(), floor(pts[1]).toInt()
                                )

                                imageView.setImageBitmap(retouchImg)
                                MyVariables.currImg = retouchImg
                                MyVariables.rotateImg = retouchImg

                                pX = floor(pts[0])
                                pY = floor(pts[1])
                            }
                        }
                        return false
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
                }
                return true
            }
        }
        imageView.setOnTouchListener(otl)
    }

    /*Работа с разрешением для галереи и камеры*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }
                GALLERY_REQUEST -> {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.setType("image/*")
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
                }
                GALLERY_REQUEST2 -> {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.setType("image/*")
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST2)
                }
            }
        } else {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    Toast.makeText(this,"Доступ к камере запрещен",Toast.LENGTH_LONG).show()
                }
                GALLERY_REQUEST -> {
                    Toast.makeText(this,"Доступ к галерее запрещен",Toast.LENGTH_LONG).show()
                }
                GALLERY_REQUEST2 -> {
                    Toast.makeText(this,"Доступ к галерее запрещен",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /*Загружаем фото из галереи или камеры*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        img = findViewById(R.id.photoEditorView)

        if((requestCode == GALLERY_REQUEST || requestCode == GALLERY_REQUEST2)  && resultCode == RESULT_OK){
            val selectedImage = data?.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            img.setImageBitmap(bitmap)

            if (bitmap != null) {
                MyVariables.currImg = bitmap
                MyVariables.rotateImg = MyVariables.currImg

                var resize = Resize()
                var newImage = resize.downScale(bitmap, 10f)
                fillList(newImage)
                filterAdapter.updateAdapter(pairsList2)
            }
        }

        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            try {
                if (data != null) {
                    img.setImageBitmap(data.extras?.get("data") as Bitmap)
                    MyVariables.currImg = data.extras?.get("data") as Bitmap
                    MyVariables.rotateImg = MyVariables.currImg

                    var resize = Resize()
                    var newImage = resize.downScale(MyVariables.currImg, 4f)
                    fillList(newImage)
                    filterAdapter.updateAdapter(pairsList2)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun fillList(image: Bitmap){
        pairsList2.clear()
        var filter = ColorFilters()
        pairsList2.add(Pair(image, PhotoFilter.NONE))
        pairsList2.add(Pair(filter.toGreenBasic(image), PhotoFilter.GREEN))
        pairsList2.add(Pair(filter.toBlueBasic(image), PhotoFilter.BLUE))
        pairsList2.add(Pair(filter.toRedBasic(image), PhotoFilter.RED))
        pairsList2.add(Pair(filter.toYellowBasic(image), PhotoFilter.YELLOW))
        pairsList2.add(Pair(filter.toGrayBasic(image), PhotoFilter.GRAYSCALE))
        pairsList2.add(Pair(filter.toNegativeBasic(image), PhotoFilter.NEGATIVE))
        pairsList2.add(Pair(filter.gausBlurBasic(image, 5f), PhotoFilter.BLUR))
        pairsList2.add(Pair(filter.changeContrastBasic(image, 100f), PhotoFilter.CONTRAST))
        pairsList2.add(Pair(filter.erosionFilter(image), PhotoFilter.EROSION))
    }

    /*Применяем фильтры*/
    @SuppressLint("HalfFloat")
    override fun onFilterSelected(photoFilter: PhotoFilter) {
        var newImg = findViewById<ImageView>(R.id.photoEditorView)
        var filter = ColorFilters()
        var filterImg : Bitmap

        when (photoFilter) {
            PhotoFilter.NONE -> {
                newImg.setImageBitmap(MyVariables.currImg)
                MyVariables.rotateImg = MyVariables.currImg
            }

            PhotoFilter.GREEN -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.toGreen(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.BLUE -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.toBlue(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.RED -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.toRed(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.YELLOW -> {
                thread {
                    runBlocking{
                        launch(Dispatchers.IO) {
                            filterImg = filter.toYellow(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.GRAYSCALE-> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.toGray(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.NEGATIVE -> {
                thread {
                    runBlocking {
                        launch {
                            filterImg = filter.toNegative(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.BLUR -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.gausBlur(MyVariables.currImg, 5f)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.CONTRAST -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.changeContrast(MyVariables.currImg, 100f)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }

            PhotoFilter.EROSION -> {
                thread {
                    runBlocking {
                        launch(Dispatchers.IO) {
                            filterImg = filter.erosionFilter(MyVariables.currImg)
                            newImg.setImageBitmap(filterImg)
                            MyVariables.rotateImg = filterImg
                        }
                    }
                }
            }
        }
    }

    /*Отслеживаем выдвижные блоки*/
    override fun onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false)
            MyVariables.isRetouch = false
            seekBarRetouchRadius.progress = 5
            seekBarRetouchSharpness.progress = 5
            redImg.visibility = View.GONE
            blueImg.visibility = View.GONE
            greenImg.visibility = View.GONE
            listFirstPoints.clear()
            listSecondPoints.clear()

        } else if(!mIsFilterVisible){

        }else{
            super.onBackPressed()
        }
    }

    /*Отображаем/убираем выдвижной блок*/
    private fun showFilter(isVisible: Boolean) {
        mIsFilterVisible = isVisible
        mConstraintSet.clone(mainView)

        if(currNumberBlock == 1){
            currBlock = rlRotate.id
        }
        else if(currNumberBlock == 2){
            currBlock = rlResize.id
        }
        else if(currNumberBlock == 3){
            currBlock = rvFilters.id
        }
        else if(currNumberBlock == 4){
            currBlock = rlRetouch.id
        }
        else if(currNumberBlock == 5){
            currBlock = maskingBlock.id
        }
        else if(currNumberBlock == 6){
            currBlock = transformBlock.id
        }

        if (isVisible) {
            mConstraintSet.clear(currBlock, ConstraintSet.START)
            mConstraintSet.connect(
                currBlock, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )

            mConstraintSet.connect(
                currBlock, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )

        } else {
            mConstraintSet.connect(
                currBlock, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )

            val changeBounds = ChangeBounds()
            changeBounds.duration = 300 /*Анимация*/
            changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f) /*Анимация*/
            TransitionManager.beginDelayedTransition(mainView, changeBounds)

            mConstraintSet.clear(currBlock, ConstraintSet.END)
        }

        val changeBounds = ChangeBounds()
        changeBounds.duration = 300 /*Анимация*/
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f) /*Анимация*/
        TransitionManager.beginDelayedTransition(mainView, changeBounds)

        mConstraintSet.applyTo(mainView)
    }

    override fun onToolSelected(toolType: ToolsType) {
        when (toolType) {
            ToolsType.ROTATE -> {
                currNumberBlock = 1
                showFilter(true)
            }

            ToolsType.RESIZE -> {
                currNumberBlock = 2
                showFilter(true)
            }

            /*Скролл для фильтров*/
            ToolsType.FILTER -> {
                currNumberBlock = 3
                showFilter(true)
            }

            ToolsType.RETOUCH -> {
                currNumberBlock = 4
                showFilter(true)
                MyVariables.isRetouch = true
            }

            ToolsType.MASKING -> {
                currNumberBlock = 5
                showFilter(true)
            }

            ToolsType.AFFINE -> {
                currNumberBlock = 6
                showFilter(true)
            }

            ToolsType.FACE -> {
                var newImg = findViewById<ImageView>(R.id.photoEditorView)
                detectFaces(newImg)
            }
        }
    }
    fun detectFaces(imageView: ImageView) {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val image = Mat()
        Utils.bitmapToMat(bitmap, image)

        val faceCascade = CascadeClassifier()
        faceCascade.load("haarcascade_frontalface_default.xml")

        val grayImage = Mat()
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY)
        Imgproc.equalizeHist(grayImage, grayImage)

        val faces = MatOfRect()

        faceCascade.detectMultiScale(grayImage, faces)
        for(rect in faces.toArray()){
            Imgproc.rectangle(image, rect.tl(), rect.br(), Scalar(255.0, 0.0, 0.0), 2)
        }

        val outputBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.RGB_565)
        Utils.matToBitmap(image, outputBitmap)
        imageView.setImageBitmap(outputBitmap)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    companion object{
        private const val GALLERY_REQUEST = 1
        private const val CAMERA_REQUEST = 2
        private const val GALLERY_REQUEST2 = 3
    }

    object MyVariables {
        /*Храним изображение, которое обрабатываем*/
        lateinit var currImg: Bitmap
        lateinit var rotateImg: Bitmap

        var isRetouch : Boolean = false

        var isFirstPoints : Boolean = false
        var isSecondPoints : Boolean = false
    }
}