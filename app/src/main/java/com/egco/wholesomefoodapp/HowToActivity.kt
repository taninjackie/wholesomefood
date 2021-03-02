package com.egco.wholesomefoodapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HowToActivity:AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.how_to_activity)

    }
}