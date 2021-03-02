package com.egco.wholesomefoodapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
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
import kotlin.collections.ArrayList


class ResultImageDetection:AppCompatActivity() {
    override fun onBackPressed() {
        val username = intent.getStringExtra("username").toString()
        setResult(2,intent);
        Intent(this, DetectionActivity::class.java).putExtra("username", username)
        finish();
    }
    private fun withCustomStyle(classnameList:List<String> , nutrient: ArrayList<List<String>>) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
        var dataForShow  = ""
        for(i in nutrient.indices)
        {
            dataForShow += classnameList[i] + " :  \n"
            for(j in nutrient[i].indices)
            {
                dataForShow = dataForShow + "    " +nutrient[i][j] + "\n"
            }
            dataForShow += "\n"
        }
        with(builder)
        {
            setTitle("Nutrient")
            setMessage(dataForShow)
            setPositiveButton("OK", null)
            show()
        }
    }
    private fun warningAlert(string: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustomWarning))
        with(builder)
        {
            setTitle("Warning this is your foodallery")
            setMessage(string)
            setPositiveButton("OK", null)
            show()
        }
    }
    private fun decodeImage(b64: String){
        val decodedBytes = Base64.getDecoder().decode(b64)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        detectImage.setImageBitmap(bitmap)
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
        val nutrient: ArrayList<List<String>> = ArrayList()
        val imgClassArrayList = intent.getStringArrayListExtra("imageclass")
        //Log.d("d123", bitmapDetect!!)
        decodeImage(bitmapDetect!!)
        var classresult  = ""
        for (i in arrayNameOfClass.indices) {
            for(j in imgClassArrayList!!.indices)
            {
                if(i == imgClassArrayList[j].toInt()-1)
                {
                    classresult += arrayNameOfClass[i]+","
                }
            }
        }
        classname.text = classresult.dropLast(1)
        val classnameList:List<String> = classname.text.toString().split(',')
        Log.d("classnametext", classname.text.toString())
        score.text = (intent.getStringExtra("score")!!.toFloat() * 100).toString() + " %"

        try {
            val inputStream: InputStream = assets.open("ingredient.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val string = String(buffer)
            val ingredientModel = Gson().fromJson(string, Ingredient::class.java)
            for(i in classnameList.indices)
            {
                when (classnameList[i]) {
                    "Apple"     -> nutrient.add(ingredientModel.apple.nutrient)
                    "Broccoli"  -> nutrient.add(ingredientModel.broccoli.nutrient)
                    "Cucumber"  -> nutrient.add(ingredientModel.cucumber.nutrient)
                    "Pumpkin"   -> nutrient.add(ingredientModel.pumpkin.nutrient)
                    "Rambutan"  -> nutrient.add(ingredientModel.rambutan.nutrient)
                    "Shrimp"    -> nutrient.add(ingredientModel.shrimp.nutrient)
                    "Tomato"    -> nutrient.add(ingredientModel.tomato.nutrient)
                    "Salmon"    -> nutrient.add(ingredientModel.salmon.nutrient)
                }
            }
            } catch (e: IOException) { e.printStackTrace() }

            val outputStreamString = FileInputStream(File(filesDir.absolutePath + File.separator + "$username.json"))
            .readBytes().toString(Charsets.UTF_8)
            val userData     = Gson().fromJson(outputStreamString, UserModel::class.java)
            var warnings:String = ""
            var warningAlert = 0
            for(i in classnameList.indices)
            {
                for(j in userData.foodallergy.indices)
                {
                    if(classnameList[i] == userData.foodallergy[j])
                    {
                        warnings    += classnameList[i]+"\n"
                        warningAlert = 1
                    }
                }
            }
            if(warningAlert == 1)
            {
                warningAlert(warnings)
            }
            Log.d("sssssssssssssss", warningAlert.toString())




            saveDetectBT.setOnClickListener {
                if (nameOfFood.text.toString() != "") {
                    val dateTime:String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toString()
                    val dateTimeForSave :String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()

                    val saveData = SaveDetection(
                        dateTimeForSave, nameOfFood.text.toString(), classnameList, nutrient
                    )

                    Log.d("savedata", saveData.ingredient.toString())

                    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                    val json: String = gsonPretty.toJson(saveData)
                    Toast.makeText(this, "Save Success", Toast.LENGTH_SHORT).show()
                    val subFolder = File(this.filesDir, "/$username")
                    if (!subFolder.exists()) {
                        subFolder.mkdirs()
                    }
                    val subFolderPath = this.filesDir.absolutePath + File.separator + username
                    val outputStream = FileOutputStream(File(subFolderPath, "$username+$dateTime.json"))
                    outputStream.write(json.toByteArray())
                    outputStream.close()

                    setResult(2,intent)
                    Intent(this, DetectionActivity::class.java).putExtra("username", username)
                    finish()
                }
                else {
                    nameOfFood.error = "Please Enter Name"
                }
            }
            shownutrientBT.setOnClickListener {
                withCustomStyle(classnameList,nutrient)
            }
            cancelBT.setOnClickListener {
                setResult(2,intent);
                Intent(this, DetectionActivity::class.java).putExtra("username", username)
                finish();
            }

    }
}





