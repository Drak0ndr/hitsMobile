package com.example.hitsmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter(private val names: List<String>):
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>()  {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val largeTextView: ImageView = itemView.findViewById(R.id.imgToolIcon)
        val smallTextView: TextView = itemView.findViewById(R.id.txtTool)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.smallTextView.text = "кот"
    }

    override fun getItemCount(): Int {
        return names.size
    }

}