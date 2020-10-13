package com.focusonme.tflitesample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.focusonme.tflitesample.R
import com.focusonme.tflitesample.extension.glide.GlideHelper
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ItemHolder>() {

    private val items = listOf(
        R.drawable.case1,
        R.drawable.case2,
        R.drawable.case3,
        R.drawable.case4,
        R.drawable.case5,
        R.drawable.case6
    )

    private var listener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolder(parent)

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        items[position].let { item ->
            with(holder.itemView) {
                GlideHelper.loadImage(context, item, itemIvInput)
                setOnClickListener { listener?.onItemClick(position) }
            }
        }
    }

    override fun getItemCount() = items.size

    class ItemHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
    )

    interface ItemClickListener {

        fun onItemClick(position: Int)
    }
}