package com.example.hitsmobile.canvas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.hitsmobile.activity.CanvasActivity
import com.example.hitsmobile.R
import java.util.ArrayList

class ColorsAdapter(private val mOnItemSelected: CanvasActivity) :
    RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {
    private val mColorList: MutableList<ColorModel> = ArrayList()

    interface OnItemSelected {
        fun onColorSelected(colors: Colors)
    }

    internal inner class ColorModel(
        val mColorIcon: Int,
        val mColorType: Colors
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_colors, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mColorList[position]
        holder.imgColorIcon.setImageResource(item.mColorIcon)
    }

    override fun getItemCount(): Int {
        return mColorList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgColorIcon: ImageView = itemView.findViewById(R.id.imgColorIcon)

        init {
            itemView.setOnClickListener { _: View? ->
                mOnItemSelected.onColorSelected(
                    mColorList[layoutPosition].mColorType
                )
            }
        }
    }
    init {
        mColorList.add(ColorModel( R.drawable.orange_color, Colors.ORANGE))
        mColorList.add(ColorModel( R.drawable.black_color, Colors.BLACK))
        mColorList.add(ColorModel( R.drawable.blue_color, Colors.BLUE))
        mColorList.add(ColorModel( R.drawable.color_red, Colors.RED))
        mColorList.add(ColorModel( R.drawable.gray_color, Colors.GRAY))
        mColorList.add(ColorModel( R.drawable.green_color, Colors.GREEN))
        mColorList.add(ColorModel( R.drawable.pink_color, Colors.PINK))
        mColorList.add(ColorModel( R.drawable.yellow_color, Colors.YELLOW))
        mColorList.add(ColorModel( R.drawable.brown_color, Colors.BROWN))
        mColorList.add(ColorModel( R.drawable.purple_color, Colors.PURPLE))
        mColorList.add(ColorModel( R.drawable.azure_color, Colors.AZURE))
        mColorList.add(ColorModel( R.drawable.darkgreen_color, Colors.DARKGREEN))
    }
}