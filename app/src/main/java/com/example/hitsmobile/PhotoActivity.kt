package com.example.hitsmobile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import com.example.hitsmobile.filters.ColorFilters
import com.example.hitsmobile.filters.FilterViewAdapter
import com.example.hitsmobile.filters.PhotoFilter
import com.example.hitsmobile.tools.ToolsAdapter
import com.example.hitsmobile.tools.ToolsAdapter.OnItemSelected
import com.example.hitsmobile.tools.ToolsType
import java.io.IOException
import java.util.concurrent.Executors


open class PhotoActivity: AppCompatActivity(), OnItemSelected, FilterViewAdapter.FilterListener {
    /*Храним изображение, которое обрабатываем*/
    private lateinit var currImg: Bitmap

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

    /*Контейнер для изменения размера*/
    private lateinit var rlResize: RelativeLayout

    /*Ползунок для поворота*/
    private lateinit var seekBarRotate: SeekBar
    private lateinit var seekBarProgressRotate: TextView

    /*Ползунок для изменения размера*/
    private lateinit var seekBarResize: SeekBar
    private lateinit var seekBarProgressResize: TextView

    /*Кнопка домой*/
    private lateinit var homeBtn : ImageView

    /*Сохранение фотографии в галерею*/
    private lateinit var saveBtn : ImageView

    /*Отправка фото*/
    private lateinit var shareBtn : ImageView

    /*Отслеживаем переключения для изменения размера фото*/
    private lateinit var radio : RadioGroup

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_photo)

        /*Динамическая загрузка изображения*/
        val imageView = findViewById<ImageView>(R.id.photoEditorView)
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
                    currImg = (image as Bitmap?)!!
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
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

        /*Контейнер для поворота*/
        rlRotate = findViewById(R.id.rotateBlock)

        /*Контейнер для изменения размера*/
        rlResize = findViewById(R.id.resizeBlock)

        /*Ползунок для поворота*/
        seekBarRotate = findViewById(R.id.seekBarRotate)
        seekBarProgressRotate = findViewById(R.id.progressBarCurr)

        /*Ползунок для изменения размера*/
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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)

            } else {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.setType("image/*")
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
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

        /*Отслеживаем переключения для изменения размера фото*/
        radio = findViewById(R.id.radio_group)
        radio.setOnCheckedChangeListener{ _, checkedId ->
            seekBarResize.setProgress(1)
        }

        /*Отслеживаем изменения ползунка для поворота*/
        seekBarRotate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarProgressRotate.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*Отслеживаем изменения ползунка для изменения размера*/
        seekBarResize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val change: Double
                change = progress.toDouble() / 100
                seekBarProgressResize.text = "$change"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
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
            }
        } else {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    Toast.makeText(this,"Доступ к камере запрещен",Toast.LENGTH_LONG).show()
                }
                GALLERY_REQUEST -> {
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

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            val selectedImage = data?.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            img.setImageBitmap(bitmap)

            if (bitmap != null) {
                currImg = bitmap
            }
        }

        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            try {
                if (data != null) {
                    img.setImageBitmap(data.extras?.get("data") as Bitmap)
                    currImg = data.extras?.get("data") as Bitmap
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /*Применяем фильтры*/
    override fun onFilterSelected(photoFilter: PhotoFilter) {
        var newImg = findViewById<ImageView>(R.id.photoEditorView)
        var filter = ColorFilters()

        when (photoFilter) {
            PhotoFilter.NONE -> {
                newImg.setImageBitmap(currImg)
            }
            PhotoFilter.GREEN -> {
                newImg.setImageBitmap(filter.toGreen(currImg))
            }
            PhotoFilter.BLUE -> {
                newImg.setImageBitmap(filter.toBlue(currImg))
            }
            PhotoFilter.RED -> {
                newImg.setImageBitmap(filter.toRed(currImg))
            }
            PhotoFilter.YELLOW -> {
                newImg.setImageBitmap(filter.toYellow(currImg))
            }
            PhotoFilter.GRAYSCALE-> {
                newImg.setImageBitmap(filter.toGray(currImg))
            }
            PhotoFilter.NEGATIVE -> {
                newImg.setImageBitmap(filter.toNegative(currImg))
            }
        }
    }

    /*Отслеживаем выдвижные блоки*/
    override fun onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false)

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
            }

            ToolsType.MASKING-> {
                currNumberBlock = 5
            }
        }
    }

    companion object{
        private const val GALLERY_REQUEST = 1
        private const val CAMERA_REQUEST = 2
    }
}