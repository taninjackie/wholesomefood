package com.egco.wholesomefoodapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.item_category.view.*
import java.io.IOException

class HomeActivity:AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val username:String = intent.getStringExtra("username")!!.toString()
        val name:String = intent.getStringExtra("name")!!

        nameHome.text = "Name : $name"

        settingicon.setOnClickListener {
            val intent = Intent(applicationContext, SettingActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        val categoryList = arrayListOf(
            Category("Status", R.drawable.catagory1),
            Category("History", R.drawable.catagory2),
            Category("Ingredient", R.drawable.catagory3),
            Category("Detection", R.drawable.catagory4),
        )

        val adapter = CustomAdapter(this, categoryList,username)
        GridView.adapter = adapter

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
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val username:String = intent.getStringExtra("username")!!
        val getuserinfo = getJson(this, "$username.json")
        val token = object : TypeToken<UserModel>() {}.type
        val userModel: UserModel = Gson().fromJson(getuserinfo, token)
        nameHome.text = "Name : ${userModel.name}"
    }



    private class ViewHolder(val categoryNameText: TextView, val iconCategoryView: ImageView)

    class CustomAdapter(private val context: Context, category: ArrayList<Category>,val username:String) : BaseAdapter() {
        private val categoryList:ArrayList<Category> = category
        override fun getCount(): Int {
            return categoryList.size
        }

        override fun getItem(position: Int): Any {
            return categoryList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowMain: View

            if (convertView == null) {
                val layoutInflater = LayoutInflater.from(parent!!.context)
                rowMain = layoutInflater.inflate(R.layout.item_category, parent, false)
                val viewHolder = ViewHolder(rowMain.categoryNameText, rowMain.iconCategoryView)
                rowMain.tag = viewHolder
            } else {
                rowMain = convertView
            }

            val viewHolder = rowMain.tag as ViewHolder
            viewHolder.categoryNameText.text = categoryList[position].name
            viewHolder.iconCategoryView.setImageResource(categoryList[position].icon)

            rowMain.setOnClickListener {
                if(rowMain.categoryNameText.text == "Detection")
                {
                    val intent = Intent(context, DetectionActivity::class.java)
                    intent.putExtra("Category", categoryList[position].name)
                    intent.putExtra("username",username)
                    context.startActivity(intent)
                }
                if(rowMain.categoryNameText.text == "History")
                {
                    Intent(context, HistoryActivity::class.java).putExtra("username",username)
                    context.startActivity(Intent(context, HistoryActivity::class.java))
                }
            }

            return rowMain
        }
    }
}