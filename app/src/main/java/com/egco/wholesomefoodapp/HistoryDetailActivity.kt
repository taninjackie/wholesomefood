package com.egco.wholesomefoodapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.history_activity.*
import kotlinx.android.synthetic.main.historydetail.*
import kotlinx.android.synthetic.main.row_historydetail.view.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.math.log

class HistoryDetailActivity :AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historydetail)
        val outputStreamString = intent.getStringExtra("savedata").toString()
        val saveData = Gson().fromJson(outputStreamString, SaveDetection::class.java)
        val listOfIngredient = saveData.ingredient
        val listOfNutrient   = saveData.nutrient
        val arrayListofNutrient:ArrayList<String> = ArrayList()
        var tempString = ""
        for(i in listOfNutrient.indices)
        {
            for(j in listOfNutrient[i].indices)
            {
                tempString += listOfNutrient[i][j]+"\n"
            }
            arrayListofNutrient.add(tempString)
            tempString = ""
        }
        Log.d("arrayListofNutrient", arrayListofNutrient.toString())
        dateInHisDetail.text     = saveData.date
        nameFoodInHisDetail.text = saveData.name
        recycleViewHisDetail.layoutManager = LinearLayoutManager(this)
        recycleViewHisDetail.adapter = CustomAdapter(saveData.date,saveData.name,listOfIngredient,arrayListofNutrient,this)

    }


    class CustomAdapter(private val date: String,private val name:String,private val listOfIngredient: List<String>,private val listOfNutrient: ArrayList<String>, context: Context) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private val MyContext = context
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ingredientHisDetail: TextView
            val nutraHisDetail: TextView
            init {
                ingredientHisDetail = view.findViewById(R.id.ingredientHisDetail)
                nutraHisDetail = view.findViewById(R.id.nutraHisDetail)
            }
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_historydetail, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.ingredientHisDetail.text = listOfIngredient[position]
            viewHolder.nutraHisDetail.text      = listOfNutrient[position]
            viewHolder.itemView.setOnClickListener {
                //Toast.makeText(MyContext, position.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount() = listOfIngredient.size

    }



}