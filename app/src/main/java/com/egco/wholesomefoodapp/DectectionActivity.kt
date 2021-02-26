package com.egco.wholesomefoodapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.detectionhome.*
import okhttp3.*
import java.io.IOException
import java.util.*
import java.io.ByteArrayOutputStream
import kotlin.collections.ArrayList

class DectectionActivity:AppCompatActivity() {
    private val pickImage = 100
    private var bitmapForDetect : Bitmap? = null
    private var b64DetectingObject : Image64? = null
    private var imageUri: Uri? = null


    private fun checkImage(){
        if(b64DetectingObject!=null){
            val intent = Intent(this, ResultImageDetection::class.java)
            intent.putExtra("bitmapDetect", fixbug64(b64DetectingObject!!.b64))
            intent.putExtra("imageclass",b64DetectingObject!!.imageclass)
            intent.putExtra("score",b64DetectingObject!!.score)
            detectionPreImage.setImageDrawable(null)
            startActivity(intent)
        }
        else{
            checkImage()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detectionhome)

        camBt.setOnClickListener {
            startActivity(Intent(this,CameraActivity::class.java))
        }
        uploadBt.setOnClickListener {
            openGalleryForImage()
        }
        detecBT.setOnClickListener {
            if (bitmapForDetect != null) {
                sendimage(encodeImage(bitmapForDetect!!).toString())
            } else {
                Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show()
            }
        }
        /*ResultBT.setOnClickListener {
            val intent = Intent(this, ResultImageDetection::class.java)
            intent.putExtra("bitmapDetect", fixbug64(b64DetectingObject!!.b64))
            intent.putExtra("imageclass",b64DetectingObject!!.imageclass)
            intent.putExtra("score",b64DetectingObject!!.score)
            detectionPreImage.setImageDrawable(null)
            startActivity(intent)
        }*/


    }

    override fun onResume() {
        super.onResume()
        //successText.text =""
        ResultBT.visibility = View.INVISIBLE

        if(intent.getParcelableExtra<Bitmap>("photo") != null)
        {
            val photo:Bitmap = intent.getParcelableExtra<Bitmap>("photo")!!
            detectionPreImage.setImageBitmap(photo);
            bitmapForDetect = photo
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
        startActivityForResult(Intent.createChooser(intent,"Select a Photo"),pickImage)
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val b64 = Base64.getEncoder().encodeToString(b)
        Log.d("testtt", b64)
        return b64
    }
    private fun sendimage(base64: String) {
        val client = OkHttpClient()
        val formbody: RequestBody = FormBody.Builder()
            .add("img", base64)
            .build();
        val request = Request.Builder()
            .url("https://06e979a5792b.ngrok.io/test")
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
                    Log.d("test", resStr)
                    b64DetectingObject = Gson().fromJson(resStr, Image64::class.java)
                    ResultBT.visibility = View.VISIBLE
                    //successText.text = "Detect Success"
                    Log.d("test2", b64DetectingObject!!.b64)
                    //Log.d("base64com", b64DetectingObject!!.b64)
                    //decodeImage(b64DetectingObject!!.b64)
                    checkImage()
                }
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pickImage && resultCode == Activity.RESULT_OK && data!=null && data.data !=null){
            imageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
                //detectionPreImage.setImageBitmap(bitmap)
                detectionPreImage.setImageBitmap(bitmap);
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                bitmapForDetect = bitmap
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }




}


