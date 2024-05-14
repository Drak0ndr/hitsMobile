package com.example.hitsmobile.filters


import android.annotation.SuppressLint
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
import com.example.hitsmobile.activity.PhotoActivity
import java.io.IOException


class FilterViewAdapter(private val filterListener: PhotoActivity) :
    RecyclerView.Adapter<FilterViewAdapter.ViewHolder>() {
    private val pairsList: MutableList<Pair<String, PhotoFilter>> = ArrayList()
    private var newPairsList: MutableList<kotlin.Pair<Bitmap, PhotoFilter>> = ArrayList()

    interface FilterListener {
        fun onFilterSelected(photoFilter: PhotoFilter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_filters, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(newPairsList.size == 0){
            val filterPair = pairsList[position]
            val fromAsset = getBitmapFromAsset(holder.itemView.context, filterPair.first)
            holder.imageFilterView.setImageBitmap(fromAsset)
            holder.txtFilterName.text = filterPair.second.name
        }
        else{
            val filterPair = newPairsList[position]
            holder.imageFilterView.setImageBitmap(filterPair.first)
            holder.txtFilterName.text = filterPair.second.name
        }
    }

    override fun getItemCount(): Int {
        return pairsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(pairsList2: MutableList<kotlin.Pair<Bitmap, PhotoFilter>> = ArrayList()){
        newPairsList = pairsList2
        notifyDataSetChanged()
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
        pairsList.add(Pair("filters/cat_none.jpg", PhotoFilter.NONE))
        pairsList.add(Pair("filters/green_cat.jpg", PhotoFilter.GREEN))
        pairsList.add(Pair("filters/cat_blue.jpg", PhotoFilter.BLUE))
        pairsList.add(Pair("filters/red_cat.jpg", PhotoFilter.RED))
        pairsList.add(Pair("filters/yellow_cat.jpg", PhotoFilter.YELLOW))
        pairsList.add(Pair("filters/gray_cat.jpg", PhotoFilter.GRAYSCALE))
        pairsList.add(Pair("filters/negative_cat.jpg", PhotoFilter.NEGATIVE))
        pairsList.add(Pair("filters/gauss_cat.jpg", PhotoFilter.BLUR))
        pairsList.add(Pair("filters/contrast_cat.jpg", PhotoFilter.CONTRAST))
        pairsList.add(Pair("filters/erosion_cat.jpg", PhotoFilter.EROSION))
    }

    init {
        setupFilters()
    }
}