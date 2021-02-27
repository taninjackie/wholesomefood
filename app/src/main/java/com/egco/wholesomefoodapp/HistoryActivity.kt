package com.egco.wholesomefoodapp

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.history_activity.*
import kotlinx.android.synthetic.main.row_history.view.*

class HistoryActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)
        val arrayListTest:ArrayList<String> = ArrayList()
        arrayListTest.add("tettt")
        arrayListTest.add("tettt")
        arrayListTest.add("tettt")

        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = CustomAdapter(arrayListTest)

    }

    class CustomAdapter(private val dataSet: ArrayList<String>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val dateHis: TextView

            init {
                dateHis = view.findViewById(R.id.dateHis)
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
            viewHolder.dateHis.text = dataSet[position]
        }

        override fun getItemCount() = dataSet.size

    }


}
