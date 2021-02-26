package com.egco.wholesomefoodapp
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_user.*
import kotlinx.android.synthetic.main.create_user2.*
import kotlinx.android.synthetic.main.create_user_result.*
import kotlinx.android.synthetic.main.row.*
import java.io.IOException
import java.util.ArrayList

class CreateUser : AppCompatActivity() {
    private var usernameCreate :String = String()
    private var name           :String = String()

    private fun getUserTxt(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)
        var check :Int =2
        nextBt.setOnClickListener {
            usernameCreate = usrNameP.text.toString()
            name           = namesurP.text.toString()

            if(usrNameP.text.toString() == "")
            {
                usrNameP.error = "Please fill your username"
            }

            if(namesurP.text.toString() == "")
            {
                namesurP.error = "Please fill your name"
            }


            else if(usrNameP.text.toString() != ""  && namesurP.text.toString() != ""){
                if(getUserTxt(applicationContext,"users.txt") != null)
                {
                    val useroftext = getUserTxt(applicationContext,"users.txt")
                    val list:List<String> = useroftext?.split(",")!!

                    for (i in list.indices) {
                        Log.d("test$i", list[i])
                        if(usrNameP.text.toString() == list[i]){
                            usrNameP.error = "username already have"
                            usrNameP.text.clear()
                            check = 0
                        }
                        else{
                            check = 1
                        }
                    }
                    if(check == 1)
                    {
                        val intent = Intent(this, CreateUser2::class.java)
                        intent.putExtra("username", usernameCreate)
                        intent.putExtra("name", name)
                        startActivity(intent)
                    }
                }

            }
        }
        backBt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        usrNameP.setText(usernameCreate)
        namesurP.setText(name)
    }


}




