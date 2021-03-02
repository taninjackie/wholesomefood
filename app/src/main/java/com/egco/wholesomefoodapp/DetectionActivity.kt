package com.egco.wholesomefoodapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detectionhome.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class DetectionActivity:AppCompatActivity() {
    private var bitmapForDetect: Bitmap? = null
    private var b64DetectingObject: Image64? = null
    private var imageUri: Uri? = null
    private fun checkImage(username: String) {
        if (b64DetectingObject != null) {
            val user_name: String = username
            val intent = Intent(this, ResultImageDetection::class.java)
            intent.putExtra("bitmapDetect", fixbug64(b64DetectingObject!!.b64))
            intent.putStringArrayListExtra("imageclass", b64DetectingObject!!.imageclass)
            intent.putExtra("score", b64DetectingObject!!.score)
            intent.putExtra("username", user_name)
            detectionPreImage.setImageDrawable(null)
            startActivity(intent)
        } else {
            checkImage(username)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detectionhome)
        val username:String = intent.getStringExtra("username").toString()
        Log.d("username2", username)
        camBt.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    MY_CAMERA_PERMISSION_CODE
                )
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }
        uploadBt.setOnClickListener {
            openGalleryForImage()
        }
        detecBT.setOnClickListener {
            if (bitmapForDetect != null) {
                sendimage(encodeImage(bitmapForDetect!!).toString(), username)
            } else {
                Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ProgressBar.setProgress(0, true)
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pickImage && resultCode == Activity.RESULT_OK && data!=null && data.data !=null){
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                detectionPreImage.setImageBitmap(bitmap);
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
                bitmapForDetect = getResizedBitmap(bitmap,500)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val photo = data!!.extras!!["data"] as Bitmap?
            detectionPreImage.setImageBitmap(photo);
            bitmapForDetect = photo
        }
        if (requestCode == 2) {
            Log.d("username", intent.getStringExtra("username").toString())
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fixbug64(b64: String):String{
        val new  = b64.substring(1).substring(1).dropLast(1)
        Log.d("Dedsecq", new)
        return new
    }

    private fun openGalleryForImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a Photo"), pickImage)
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val b64 = Base64.getEncoder().encodeToString(b)
        Log.d("testtt", b64)
        return b64
    }
    private fun sendimage(base64: String, username: String) {
        val usernametosent = username
        val client = OkHttpClient()
        ProgressBar.setProgress(80, true)
        val formbody: RequestBody = FormBody.Builder()
            .add("img", base64)
            .build();
        val request = Request.Builder()
            .url("https://24d637bae400.ngrok.io/test")
            .post(formbody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { e.message }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val resStr = response.body!!.string()
                runOnUiThread {
                    b64DetectingObject = Gson().fromJson(resStr, Image64::class.java)
                    ProgressBar.setProgress(100, false)
                    checkImage(usernametosent)
                }
            }

        })
    }

    companion object {
        private const val CAMERA_REQUEST = 1888
        private const val MY_CAMERA_PERMISSION_CODE = 100
        private const val pickImage = 100
    }



}


