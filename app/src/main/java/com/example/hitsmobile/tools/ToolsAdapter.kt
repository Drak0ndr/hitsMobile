package com.example.hitsmobile.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hitsmobile.activity.PhotoActivity
import com.example.hitsmobile.R
import java.util.ArrayList

class ToolsAdapter(private val mOnItemSelected: PhotoActivity) :
    RecyclerView.Adapter<ToolsAdapter.ViewHolder>() {
    private val mToolList: MutableList<ToolModel> = ArrayList()

    interface OnItemSelected {
        fun onToolSelected(toolType: ToolsType)
    }

    internal inner class ToolModel(
        val mToolName: String,
        val mToolIcon: Int,
        val mToolType: ToolsType
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mToolList[position]
        holder.txtTool.text = item.mToolName
        holder.imgToolIcon.setImageResource(item.mToolIcon)
    }

    override fun getItemCount(): Int {
        return mToolList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgToolIcon: ImageView = itemView.findViewById(R.id.imgToolIcon)
        val txtTool: TextView = itemView.findViewById(R.id.txtTool)

        init {
            itemView.setOnClickListener { _: View? ->
                mOnItemSelected.onToolSelected(
                    mToolList[layoutPosition].mToolType
                )
            }
        }
    }
    init {
        mToolList.add(ToolModel("Rotate", R.drawable.rotate, ToolsType.ROTATE))
        mToolList.add(ToolModel("Resize", R.drawable.resize, ToolsType.RESIZE))
        mToolList.add(ToolModel("Filter", R.drawable.filter, ToolsType.FILTER))
        mToolList.add(ToolModel("Retouch", R.drawable.retouch, ToolsType.RETOUCH))
        mToolList.add(ToolModel("Masking", R.drawable.masking, ToolsType.MASKING))
        mToolList.add(ToolModel("Transform", R.drawable.affine_transformations, ToolsType.AFFINE))
    }
}