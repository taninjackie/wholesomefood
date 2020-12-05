package com.egco.wholesomefoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.create_user.*
import kotlinx.android.synthetic.main.create_user2.*
import kotlinx.android.synthetic.main.row.*
import java.util.ArrayList



class CreateUser : AppCompatActivity() {
    private var usernameCreate :String = String()
    private var passwordCreate :String = String()
    private var name           :String = String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user)

        nextBt.setOnClickListener {
            usernameCreate = usrNameP.text.toString()
            passwordCreate = passwordP.text.toString()
            name           = namesurP.text.toString()
            if(usrNameP.text.toString() == "")
            {
                usrNameP.error = "Please fill your username"
            }
            if(passwordP.text.toString() == "")
            {
                passwordP.error = "Please fill your password"
            }
            if(namesurP.text.toString() == "")
            {
                namesurP.error = "Please fill your name"
            }
            else if(usrNameP.text.toString() != "" && passwordP.text.toString() != "" && namesurP.text.toString() != ""){
                val intent = Intent(this, CreateUser2::class.java)
                intent.putExtra("username",usernameCreate)
                intent.putExtra("password",passwordCreate)
                intent.putExtra("name",name)
                startActivity(intent)
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
        passwordP.setText(passwordCreate)
        namesurP.setText(name)
    }
}




