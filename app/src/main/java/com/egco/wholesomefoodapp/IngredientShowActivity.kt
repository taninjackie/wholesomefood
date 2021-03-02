package com.egco.wholesomefoodapp

import android.R.layout
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class IngredientShowActivity :AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredient_activity)
        val listView: ListView

        val from = arrayOf("Apple", "Broccoli", "Cucumber", "Pumpkin","Rambutan","Salmon","Shrimp","Tomato")
        listView = findViewById<View>(R.id.listViewIngredient) as ListView
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            layout.simple_list_item_1, android.R.id.text1, from)
        listView.adapter = adapter



    }




}