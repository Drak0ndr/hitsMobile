package com.example.hitsmobile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.hitsmobile.tools.ToolsAdapter
import com.example.hitsmobile.tools.ToolsAdapter.OnItemSelected
import com.example.hitsmobile.tools.ToolsType
import java.io.IOException
import java.util.concurrent.Executors
import com.example.hitsmobile.filters.FilterListener
import com.example.hitsmobile.filters.FilterViewAdapter
import com.example.hitsmobile.filters.PhotoFilter


/*class PhotoActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var textArr: ArrayList<String>

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerPermission()
        checkPermission()

        setContentView(R.layout.activity_photo)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CustomRecyclerAdapter(fillList())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        photoView = findViewById(R.id.photo_container)
        cameraOpenId = findViewById(R.id.it_camera)

        imgGallery = findViewById(R.id.it_gallery)

        cameraOpenId.setOnClickListener(){
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, pic_id)
        }

        fun onOptionsItemSelected(item: MenuItem?): Boolean {
            when (item!!.itemId) {
                R.id.it_gallery -> pickImageFromGallery()
            }

            return true
        }

        /*imgGallery.setOnMenuItemClickListener(){
            pickImageFromGallery()
        }*/
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED->{
            }
            else ->{
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun registerPermission(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                Toast.makeText(this,"Доступ к камере разрешен",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Доступ к камере запрещен",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pic_id){
            val photo = data!!.extras!!["data"] as Bitmap
            photoView.setImageBitmap(photo)
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                photoView.setImageURI(data?.data)
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    companion object{
        private const val pic_id = 123
        private const val PICK_IMAGE_REQUEST = 1
    }
}*/

class PhotoActivity: AppCompatActivity(), OnItemSelected, FilterListener {
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

    /*Отслеживаем выдвижные блоки*/
    private lateinit var closeButton: ImageView

    /*Блок для поворота*/
    private lateinit var rlRotate: RelativeLayout

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

        /*Блок для поворота*/
        rlRotate = findViewById(R.id.rotateBlock)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*Работа с камерой*/
        cameraButton = findViewById(R.id.imgCamera)
        cameraButton.setOnClickListener(){
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        /*Работа с галереей*/
        galleryButton = findViewById(R.id.imgGallery)
        galleryButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.setType("image/*")
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        /*Отслеживаем выдвижные блоки*/
        closeButton = findViewById(R.id.imgClose)
        closeButton.setOnClickListener{
            onBackPressed()
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
        }

        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            try {
                if (data != null) {
                    img.setImageBitmap(data.extras?.get("data") as Bitmap)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onFilterSelected(photoFilter: PhotoFilter) {

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