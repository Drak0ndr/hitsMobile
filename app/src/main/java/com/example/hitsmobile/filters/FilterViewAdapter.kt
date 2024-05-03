package com.example.hitsmobile.filters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hitsmobile.R


import java.io.IOException
import java.util.ArrayList


class FilterViewAdapter(private val filterListener: FilterListener) :
    RecyclerView.Adapter<FilterViewAdapter.ViewHolder>() {
    private val pairsList: MutableList<Pair<String, PhotoFilter>> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_filters, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filterPair = pairsList[position]
        val fromAsset = getBitmapFromAsset(holder.itemView.context, filterPair.first)
        holder.imageFilterView.setImageBitmap(fromAsset)
        holder.txtFilterName.text = filterPair.second.name.replace("_", " ")
    }

    override fun getItemCount(): Int {
        return pairsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageFilterView: ImageView = itemView.findViewById(R.id.imgFilterView)
        val txtFilterName: TextView = itemView.findViewById(R.id.txtFilterName)

        init {
            itemView.setOnClickListener{
                filterListener.onFilterSelected(
                    pairsList[layoutPosition].second
                )
            }
        }
    }

    private fun getBitmapFromAsset(context: Context, strName: String): Bitmap? {
        val assetManager = context.assets
        return try {
            val istr = assetManager.open(strName)
            BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun setupFilters() {
        pairsList.add(Pair("filters/cat.jpg", PhotoFilter.NONE))
        pairsList.add(Pair("filters/cat.jpg", PhotoFilter.GREEN))
        pairsList.add(Pair("filters/cat.jpg", PhotoFilter.GRAYSCALE))
        pairsList.add(Pair("filters/cat.jpg", PhotoFilter.NEGATIVE))
    }

    init {
        setupFilters()
    }
}