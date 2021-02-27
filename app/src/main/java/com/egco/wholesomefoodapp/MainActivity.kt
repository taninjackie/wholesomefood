package com.egco.wholesomefoodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.*
import kotlin.math.log


class MainActivity : AppCompatActivity() ,CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("filepath", filesDir.toString())

        fun save(file: String, text: String) {
            var fos: FileOutputStream? = null
            try {
                fos = openFileOutput(file, MODE_APPEND)
                fos.write(text.toByteArray())
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

        if(getUserTxt(applicationContext, "users.txt") != null)
        {
            val useroftext = getUserTxt(applicationContext, "users.txt")
            val list:List<String> = useroftext?.split(",")!!
            val myAdapter = MyAdapter(list, applicationContext, this)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = myAdapter
        }
        else
        {
            save("users.txt", "")
        }

        //val arrayList = ArrayList<UserModel>()
        //arrayList.add(UserModel("taninjackie","12345"))
        //arrayList.add(UserModel("boat001"    ,"12345"))


        addUser.setOnClickListener {
            intent = Intent(this, CreateUser::class.java)
            startActivity(intent)
        }


    }
    override fun onCellClickListener(username: String) {
        val intent = Intent(this, HomeActivity::class.java)
        val gson = Gson()
        val getuserinfo = getJson(this, "$username.json")
        val token = object : TypeToken<UserModel>() {}.type
        var userModel: UserModel = gson.fromJson(getuserinfo, token)
        intent.putExtra("username", username)
        intent.putExtra("name", userModel.name)
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun getJson(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }



}


