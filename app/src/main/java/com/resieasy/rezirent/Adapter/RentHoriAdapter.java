package com.resieasy.rezirent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.resieasy.rezirent.Class.AddFlatClass;
import com.resieasy.rezirent.Class.LikeClass;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.Activity.ShowResidencyDataActivity;
import com.resieasy.rezirent.databinding.RenthorizontalsampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RentHoriAdapter extends RecyclerView.Adapter<RentHoriAdapter.ViewHolder> {


    Context context;
    ArrayList<AddFlatClass> list;


    public void updateList(ArrayList<AddFlatClass> newList){
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }
    public RentHoriAdapter(Context context, ArrayList<AddFlatClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RentHoriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.renthorizontalsample, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddFlatClass data = list.get(position);

        String subtype = data.getSubtype();
        String area = data.getArea();
        String rent = data.getRent();
        String id = data.getId();
        int agree = data.getPeriod();


        holder.binding.samplesubtype.setText(subtype);
        holder.binding.samplearea.setText(area);
        holder.binding.sellsampleprize.setText(rent+"₹/month");





        FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String firstimage = snapshot.getString("image0");
                        Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr).into(holder.binding.sellsampleimage);

                    }
                });

        if(agree==708){
            holder.binding.noagreeview.setVisibility(View.VISIBLE);
            holder.binding.yesagreeview.setVisibility(View.GONE);

        }else {
            holder.binding.noagreeview.setVisibility(View.GONE);
            holder.binding.yesagreeview.setVisibility(View.VISIBLE);

        }
        holder.binding.cartmainrenthiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ShowResidencyDataActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);

            }
        });

        FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().getUid()).collection("Nanded").document(id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document=task.getResult();
                                    if (document.exists()){
                                        holder.binding.unlike.setVisibility(View.GONE);
                                        holder.binding.like.setVisibility(View.VISIBLE);

                                    }else {
                                        holder.binding.unlike.setVisibility(View.VISIBLE);
                                        holder.binding.like.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });

        holder.binding.unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.unlike.setVisibility(View.GONE);
                holder.binding.like.setVisibility(View.VISIBLE);
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("userid",FirebaseAuth.getInstance().getUid());
                Date date=new Date();
                LikeClass likeClass=new LikeClass(id,"Nanded","","",7028,date.getTime());
                FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseFirestore.getInstance().collection("Like")
                                        .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                                        .document(id).set(likeClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(context, "Added to your like list", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });


            }
        });
        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.unlike.setVisibility(View.VISIBLE);
                holder.binding.like.setVisibility(View.GONE);
                FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().getUid()).collection("Nanded")
                        .document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(context, "Remove from like list", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RenthorizontalsampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RenthorizontalsampleBinding.bind(itemView);
        }
    }
}
