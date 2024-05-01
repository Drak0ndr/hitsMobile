package com.example.hitsmobile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hitsmobile.tools.ToolsAdapter
import com.example.hitsmobile.tools.ToolsAdapter.OnItemSelected
import com.example.hitsmobile.tools.ToolsType


/*class PhotoActivity : AppCompatActivity() {

    private lateinit var cameraOpenId:FloatingActionButton
    private lateinit var photoView:ImageView
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var imgGallery: MenuItem

    private lateinit var bottomNavigationView:BottomNavigationView


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

    private fun fillList(): List<String> {
        val data = mutableListOf<String>()
        (0..30).forEach { i -> data.add("$i element") }
        return data
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

class PhotoActivity: AppCompatActivity(), OnItemSelected {

    private lateinit var rvTools: RecyclerView
    private val toolsAdapter = ToolsAdapter(this)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_photo)


        initViews()

        rvTools.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTools.adapter = toolsAdapter



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }


    private fun initViews() {
        rvTools = findViewById(R.id.recyclerViewTools)

    }

    override fun onToolSelected(toolType: ToolsType) {
        when (toolType) {
            ToolsType.ROTATE -> {}

            ToolsType.RESIZE -> {}

            ToolsType.FILTER -> {}

            ToolsType.RETOUCH -> {}

            ToolsType.MASKING-> {}
        }
    }
}


