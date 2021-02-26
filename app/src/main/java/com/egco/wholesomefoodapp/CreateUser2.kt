package com.egco.wholesomefoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.create_user2.*


class CreateUser2:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val username:String = intent.getStringExtra("username")!!
        val name:String = intent.getStringExtra("name")!!
        val foodallergy = ArrayList<Int>(8)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user2)
        Toast.makeText(this, "Choose Your Food Allergy", Toast.LENGTH_SHORT).show()
        for(i in 0..8)
            foodallergy.add(0)
        fun check() {
            if(checkBox.isChecked){
                foodallergy[0] = 1
            }
            if(checkBox2.isChecked){
                foodallergy[1] = 1
            }
            if(checkBox3.isChecked){
                foodallergy[2] = 1
            }
            if(checkBox4.isChecked){
                foodallergy[3] = 1
            }
            if(checkBox5.isChecked){
                foodallergy[4] = 1
            }
            if(checkBox6.isChecked){
                foodallergy[5] = 1
            }
            if(checkBox7.isChecked){
                foodallergy[6] = 1
            }
            if(checkBox8.isChecked){
                foodallergy[7] = 1
            }

        }
        nextBt2.setOnClickListener {
            check()
            //Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
            val intent = Intent(this ,CreateUserResult::class.java )
            intent.putExtra("username",username)
            intent.putExtra("name",name)
            intent.putIntegerArrayListExtra("foodallergy",foodallergy)
            startActivity(intent)
        }

        backBt2.setOnClickListener {
            check()
            val intent = Intent(this, CreateUser::class.java)
            intent.putIntegerArrayListExtra("arrayInt", foodallergy)
            startActivity(intent)
        }



    }
}