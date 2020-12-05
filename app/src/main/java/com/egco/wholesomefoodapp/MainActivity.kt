package com.egco.wholesomefoodapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.egco.wholesomefoodapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.nio.charset.Charset
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }



        //val arrayList = ArrayList<UserModel>()
        if(getJsonDataFromAsset(applicationContext,"users.txt") != null)
        {
            val useroftext = getJsonDataFromAsset(applicationContext,"users.txt")
            val list:List<String> = useroftext?.split(",")!!
            val myAdapter = MyAdapter(list,this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = myAdapter
        }


        //arrayList.add(UserModel("taninjackie","12345"))
        //arrayList.add(UserModel("boat001"    ,"12345"))


        addUser.setOnClickListener {
            intent = Intent(this, CreateUser::class.java)
            startActivity(intent)
        }

    }
}