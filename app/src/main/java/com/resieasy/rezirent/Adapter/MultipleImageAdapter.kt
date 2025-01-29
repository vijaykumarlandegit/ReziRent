package com.resieasy.rezirent.Adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.resieasy.rezirent.R
import com.squareup.picasso.Picasso

class MultipleImageAdapter(var uriArraylist: ArrayList<Uri?>) :
    RecyclerView.Adapter<MultipleImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multiimage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //holder.imageView1.setImageURI(uriArraylist.get(position));
        Picasso.get().load(uriArraylist[position]).into(holder.imageView1)

        holder.imageView12.setOnClickListener {
            uriArraylist.remove(uriArraylist[position])
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return uriArraylist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView1: ImageView =
            itemView.findViewById(R.id.image)
        var imageView12: ImageView =
            itemView.findViewById(R.id.deleteimagebtn)
    }
}
