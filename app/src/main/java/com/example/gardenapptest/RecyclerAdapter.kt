package com.example.gardenapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(
    private val context: Context,
    private val dataset: List<ProfileData>
) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.user_name_fl)
        val textView2: TextView = view.findViewById(R.id.description_text_fl)
        val imageView: ImageView = view.findViewById(R.id.profile_picture_fl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_layout, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        //holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.textView.text = (item.stringResourceId)
        holder.textView2.text = (item.stringResourceId2)
        holder.imageView.setImageResource(item.imageResourceId)
    }

    override fun getItemCount() = dataset.size
}