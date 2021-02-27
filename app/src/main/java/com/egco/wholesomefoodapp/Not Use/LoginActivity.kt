/*package com.egco.wholesomefoodapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.login.*
import java.io.IOException

class LoginActivity:AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val username = intent.getStringExtra("username")
        usrLoginText.text = username
        val getuserinfo = getJson(this, "$username.json")
        val gson = Gson()
        val token = object : TypeToken<UserModel>() {}.type
        var userModel: UserModel = gson.fromJson(getuserinfo, token)



        loginBt.setOnClickListener {
            if(passwordLoginText.text.toString() == userModel.password){
                val intent = Intent(this,HomeActivity::class.java)
                intent.putExtra("username",username)
                intent.putExtra("name",userModel.name)
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else
            {
                passwordLoginText.error = "Wrong Password"
            }

        }
        backBt4.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
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
}*/