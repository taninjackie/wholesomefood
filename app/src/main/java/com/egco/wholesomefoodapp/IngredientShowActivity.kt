package com.egco.wholesomefoodapp

import android.R.layout
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.history_activity.*
import kotlinx.android.synthetic.main.ingredient_activity.*
import java.io.File
import java.io.FileInputStream


class IngredientShowActivity :AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredient_activity)


        val from = arrayOf("Apple", "Broccoli", "Cucumber", "Pumpkin","Rambutan","Salmon","Shrimp","Tomato")
        val listoficon = arrayOf(R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8)

        RecyclerViewCanDetect.layoutManager = LinearLayoutManager(this)
        RecyclerViewCanDetect.adapter = IngredientShowActivity.CustomAdapter(from, this,listoficon)


    }


    class CustomAdapter(private val array: Array<String>, context: Context,val iconArray : Array<Int>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private val MyContext = context
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView
            val nameText: TextView

            init {
                icon = view.findViewById(R.id.candetectIcon)
                nameText = view.findViewById(R.id.candetectText)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_appcandetectlist, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.nameText.text = array[position]
            viewHolder.icon.setImageResource(iconArray[position])

        }

        override fun getItemCount() = array.size

    }




}