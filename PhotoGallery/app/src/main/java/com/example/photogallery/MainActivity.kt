package com.example.photogallery

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.GridLayout
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var imageRecycler: RecyclerView?=null
    private var progressBar: ProgressBar?=null
    private var allPictures:ArrayList<Image>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageRecycler = findViewById(R.id.image_recycler)
        progressBar = findViewById(R.id.recycler_progress)

        //Storage Permissions
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            ) {
           ActivityCompat.requestPermissions(
               this@MainActivity,
               arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
           )
        }

        allPictures=ArrayList()

        if(allPictures!!.isEmpty())
        {
            progressBar?.visibility= View.VISIBLE
            //get ALL Images from Storage
            allPictures=getAllImages()
            //Set Adapter to recycler
            imageRecycler?.adapter=ImageAdapter(this,allPictures!!)
            progressBar?.visibility=View.GONE
        }


    }

    private fun getAllImages(): ArrayList<Image>? {
        val images=ArrayList<Image>()

        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection =
            arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)

        val cursor =
            this@MainActivity.contentResolver.query(allImageUri, projection,null, null, null)

        try {
            cursor!!.moveToFirst()
            do{
                val image=Image()
                image.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while(cursor.moveToNext())
            cursor.close()
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
        return images
    }
}