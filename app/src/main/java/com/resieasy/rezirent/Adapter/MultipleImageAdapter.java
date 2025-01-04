package com.resieasy.rezirent.Adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.resieasy.rezirent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.ViewHolder> {

    ArrayList<Uri>uriArraylist;

    public MultipleImageAdapter(ArrayList<Uri> uriArraylist) {
        this.uriArraylist = uriArraylist;
    }

    @NonNull
    @Override
    public MultipleImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.multiimage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultipleImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.imageView1.setImageURI(uriArraylist.get(position));
        Picasso.get().load(uriArraylist.get(position)).into(holder.imageView1);

        holder.imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArraylist.remove(uriArraylist.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });

    }

    @Override
    public int getItemCount() {
        return uriArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        ImageView imageView12;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1= itemView.findViewById(R.id.image);
            imageView12= itemView.findViewById(R.id.deleteimagebtn);
        }
    }
}
