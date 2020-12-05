package com.egco.wholesomefoodapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egco.wholesomefoodapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*
import java.io.IOException
import java.nio.charset.Charset
import kotlin.math.log


class MainActivity : AppCompatActivity() ,CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun getUserTxt(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }

        if(getUserTxt(applicationContext,"users.txt") != null)
        {
            val useroftext = getUserTxt(applicationContext,"users.txt")
            val list:List<String> = useroftext?.split(",")!!
            val myAdapter = MyAdapter(list,applicationContext,this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = myAdapter
        }

        //val arrayList = ArrayList<UserModel>()
        //arrayList.add(UserModel("taninjackie","12345"))
        //arrayList.add(UserModel("boat001"    ,"12345"))


        addUser.setOnClickListener {
            intent = Intent(this, CreateUser::class.java)
            startActivity(intent)
        }


    }
    override fun onCellClickListener(username:String) {
        val intent = Intent(applicationContext,LoginActivity::class.java)
        intent.putExtra("username" , username)
        startActivity(intent)
    }



}

