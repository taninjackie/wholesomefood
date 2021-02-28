package com.egco.wholesomefoodapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.resultdetection.*
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ResultImageDetection:AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, DetectionActivity::class.java).putExtra("username", getUsername())
        Log.d("username2222", getUsername())
        startActivity(Intent(this, DetectionActivity::class.java))
    }
    private fun withCustomStyle(nutrient: List<String>) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
        var dataForShow  = ""
        for(i in nutrient.indices)
        {
            dataForShow = dataForShow + nutrient[i] + "\n"
        }
        with(builder)
        {
            setTitle("Nutrient")
            setMessage(dataForShow)
            setPositiveButton("OK", null)
            show()
        }
    }
    private fun decodeImage(b64: String){
        val decodedBytes = Base64.getDecoder().decode(b64)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        detectImage.setImageBitmap(bitmap)
    }

    private fun getUsername():String{
        val username: String = intent.getStringExtra("username").toString()
        return username
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultdetection)
        val username: String = intent.getStringExtra("username").toString()
        val arrayNameOfClass = arrayOf(
            "Apple",
            "Broccoli",
            "Cucumber",
            "Pumpkin",
            "Rambutan",
            "Salmon",
            "Shrimp",
            "Tomato"
        )
        val bitmapDetect = intent.getStringExtra("bitmapDetect")
        var nutrient: List<String>? = null
        Log.d("d123", bitmapDetect!!)
        decodeImage(bitmapDetect)
        Log.d("imageclass", intent.getStringExtra("imageclass").toString())
        for (i in 0 until (arrayNameOfClass.size )) {
            if ((intent.getStringExtra("imageclass")!!.toInt()) - 1 == i)
                classname.text = arrayNameOfClass[i]
        }

        score.text = (intent.getStringExtra("score")!!.toFloat() * 100).toString() + " %"

        try {
            val inputStream: InputStream = assets.open("ingredient.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val string = String(buffer)
            val ingredientModel = Gson().fromJson(string, Ingredient::class.java)

            //Log.d("test", ingredientModel.apple.nutrient[0])
            when (classname.text.toString()) {
                "Apple" -> nutrient = ingredientModel.apple.nutrient
                "Broccoli" -> nutrient = ingredientModel.broccoli.nutrient
                "Cucumber" -> nutrient = ingredientModel.cucumber.nutrient
                "Pumpkin" -> nutrient = ingredientModel.pumpkin.nutrient
                "Rambutan" -> nutrient = ingredientModel.rambutan.nutrient
                "Shrimp" -> nutrient = ingredientModel.shrimp.nutrient
                "Tomato" -> nutrient = ingredientModel.tomato.nutrient
                "Salmon" -> nutrient = ingredientModel.salmon.nutrient
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        saveDetectBT.setOnClickListener {
                if (nameOfFood.text.toString() != "") {
                    val dateTime:String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toString()
                    val saveData = SaveDetection(
                        dateTime, nameOfFood.text.toString(), classname.text.toString(), nutrient!!
                    )
                    //val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                    //val json: String = gsonPretty.toJson(saveData)
                    var hisdata :String = saveData.date+","+saveData.ingredient+","+saveData.name+","
                    for(i in saveData.nutrient.indices)
                    {
                        hisdata += saveData.nutrient[i] + ","
                    }
                    Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show()

                    val subFolder = File(this.filesDir, "/$username")
                    if (!subFolder.exists()) {
                        subFolder.mkdirs();
                    }
                    val subFolderPath = this.filesDir.absolutePath + File.separator + username
                    val outputStream = FileOutputStream(File(subFolderPath, "$username+$dateTime.txt"))
                    outputStream.write(hisdata.toByteArray())
                    outputStream.close()

                    Intent(this, DetectionActivity::class.java).putExtra("username", username)
                    startActivity(Intent(this, DetectionActivity::class.java))

                }
                else {
                    nameOfFood.error = "Please Enter Name"
                }
            }
                shownutrientBT.setOnClickListener {
                    withCustomStyle(nutrient!!)
                }
                cancelBT.setOnClickListener {
                    Intent(this, DetectionActivity::class.java).putExtra("username", username)
                    startActivity(Intent(this, DetectionActivity::class.java))
                }
    }
}





