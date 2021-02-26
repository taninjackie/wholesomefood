package com.egco.wholesomefoodapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.detectionhome.*
import kotlinx.android.synthetic.main.resultdetection.*
import java.io.ByteArrayOutputStream
import java.util.*


class ResultImageDetection:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultdetection)
        val arrayNameOfClass = arrayOf<String>("Apple","Broccoli","Cucumber","Pumpkin","Rambutan","Salmon","Shrimp","Tomato")
        val bitmapDetect = intent.getStringExtra("bitmapDetect")
        Log.d("d123", bitmapDetect!!)
        decodeImage(bitmapDetect)
        Log.d("imageclass", intent.getStringExtra("imageclass").toString())
        for(i in 0 until (arrayNameOfClass.size-1))
        {
            if((intent.getStringExtra("imageclass")!!.toInt())-1 == i)
                classname.text = arrayNameOfClass[i]
        }

        score.text = (intent.getStringExtra("score")!!.toFloat()*100).toString()

    }
    private fun decodeImage(b64: String){
        val decodedBytes = Base64.getDecoder().decode(b64)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        detectImage.setImageBitmap(bitmap)
    }

    override fun onStop() {
        super.onStop()

    }
}