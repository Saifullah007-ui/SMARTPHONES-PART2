package com.nurdev.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurdev.weatherapp.R

class SearchAdapter(
    private val dataSet: List<String>,
    private val listener: RecyclerViewEvent,
    private var fav_arr: List<String>
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = itemView.findViewById(R.id.tv)
        val imageView: ImageView = itemView.findViewById(R.id.iv_favorite)

        init {
            imageView.setOnClickListener {
                onClick(imageView)
            }
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, textView, imageView)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_city, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position]
        if (fav_arr.contains(dataSet[position])) {
            viewHolder.imageView.setImageResource(R.drawable.favorite)
        } else {
            viewHolder.imageView.setImageResource(R.drawable.favorite_border)
        }
    }

    override fun getItemCount() = dataSet.size

    interface RecyclerViewEvent {
        fun onItemClick(position: Int, textView: TextView, imageView: ImageView)
    }
}