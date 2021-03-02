package com.egco.wholesomefoodapp

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.history_activity.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row_history.view.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.RuntimeException
import java.lang.reflect.InvocationTargetException
import kotlin.math.log


class HistoryActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)
        val pathFileList:ArrayList<String> = ArrayList()
        val username = intent.getStringExtra("username").toString()

        File(this.filesDir.absolutePath + File.separator + username).walkTopDown().forEach {
            pathFileList.add(it.toString())
        }

        Log.d("arraytest", pathFileList.toString())
        val arrayListofList :ArrayList<PathFile> = ArrayList()
        for (i in pathFileList.indices)
        {
            arrayListofList.add(PathFile(pathFileList[i]))
        }
        val arrayNameOfFile:ArrayList<HistoryFileName> = ArrayList()

        try {
            for(i in 0 until arrayListofList.size)
            {
                val nameoffile = arrayListofList[i+1].path.split("/")
                arrayNameOfFile.add(HistoryFileName(nameoffile[7]))
            }
        }catch (e : RuntimeException)
        {
            e.printStackTrace()
        }

        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = CustomAdapter(arrayNameOfFile, this,username)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2){ Log.d("back", "Back")}
    }



    class CustomAdapter(private val arrayNameOfFile: ArrayList<HistoryFileName>, context: Context,username:String) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private val MyContext = context
        private val username2 = username
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val dateHis: TextView
            val namefood : TextView

            init {
                dateHis = view.findViewById(R.id.dateHis)
                namefood = view.findViewById(R.id.nameOfFoodHis)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.row_history, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val outputStreamString = FileInputStream(File(MyContext.filesDir.absolutePath + File.separator + username2,arrayNameOfFile[position].name))
                .readBytes().toString(Charsets.UTF_8)
            val saveData = Gson().fromJson(outputStreamString, SaveDetection::class.java)

            viewHolder.dateHis.text  = saveData.date
            viewHolder.namefood.text = saveData.name

            Log.d("po231", saveData.name)


            viewHolder.itemView.setOnClickListener {
                val intent = Intent(MyContext,HistoryDetailActivity::class.java)
                intent.putExtra("savedata",outputStreamString)
                MyContext.startActivity(intent)
                //Toast.makeText(MyContext, position.toString(), Toast.LENGTH_SHORT).show()
            }


        }

        override fun getItemCount() = arrayNameOfFile.size

    }


}
