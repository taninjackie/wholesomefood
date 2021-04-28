package com.egco.wholesomefoodapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row.view.*

class MyAdapter(private val List:List<String>, private val context:Context, private val CellClick:CellClickListener):
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(model : String)
        {
            itemView.candetectName.text = model
        }
        fun getName():String
        {
            return itemView.candetectName.text.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return  ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(List[position])

        holder.itemView.setOnClickListener{
            CellClick.onCellClickListener(holder.getName())
        }

    }

    override fun getItemCount(): Int {
        return  List.size-1
    }
}