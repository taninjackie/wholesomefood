package com.egco.wholesomefoodapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.create_user.*
import kotlinx.android.synthetic.main.create_user_result.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CreateUserResult:AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    fun save(file:String,text:String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(file, MODE_APPEND)
            fos.write(text.toByteArray())
            Toast.makeText(this, "Create User Success",Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val list = arrayOf("ChickenBreast" , "Shrimp", "Salmon", "Dolly_fish", "Boiled eggs", "Mackerel", "Tomato", "Pumpkin", "Cucumber", "Broccoli", "Carrot", "Rambutan", "Banana", "Guava")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        val name     = intent.getStringExtra("name")
        val foodallergy = intent.getIntegerArrayListExtra("foodallergy")
        val foodallergyOfUser =ArrayList<String>()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user_result)

        usrR.text = usrR.text.toString() +" "+ username
        nsR.text  = nsR.text.toString()  +" "+ name
        for(i in 0..14)
        {
            if(foodallergy!![i] == 1)
            {
                foodallergyText.text = foodallergyText.text.toString() + list[i] + "," + "   "
                foodallergyOfUser.add(list[i])
            }
        }

        nextBt3.setOnClickListener {
            val user = UserModel(username!!,password!!,name!!,foodallergyOfUser)
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val json: String = gsonPretty.toJson(user)
            save("$username.json", json)
            save("users.txt", "$username,")
            startActivity(Intent(this,MainActivity::class.java))
        }

        backBt3.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))

        }


    }
}