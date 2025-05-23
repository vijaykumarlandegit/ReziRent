package com.resieasy.rezirent.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Activity.ShowHostelDataActivity
import com.resieasy.rezirent.data.remote.firebase.AddHostelClass
import com.resieasy.rezirent.data.remote.firebase.LikeClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.HostelhorisampleBinding
import com.squareup.picasso.Picasso
import java.util.Date

class HostelHoriAdapter(var context: Context, var list: ArrayList<AddHostelClass>) :
    RecyclerView.Adapter<HostelHoriAdapter.ViewHolder>() {
    fun updateList(newList: ArrayList<AddHostelClass>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = HostelhorisampleBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.hostelhorisample, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val subtype = data.subtype
        val area = data.area
        val rent = data.rent
        val id = data.id
        val agree = data.period


        holder.binding.samplesubtype.text = subtype
        holder.binding.samplearea.text = area
        holder.binding.sellsampleprize.text = rent + "₹/month"





        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage").document(id!!).get()
            .addOnSuccessListener { snapshot ->
                val firstimage = snapshot.getString("image0")
                Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                    .into(holder.binding.sellsampleimage)
            }

        if (agree == 708) {
            holder.binding.noagreeview.visibility = View.VISIBLE
            holder.binding.yesagreeview.visibility = View.GONE
        } else {
            holder.binding.noagreeview.visibility = View.GONE
            holder.binding.yesagreeview.visibility = View.VISIBLE
        }
        holder.binding.cartmainrenthiri.setOnClickListener {
            val intent = Intent(context, ShowHostelDataActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
        FirebaseFirestore.getInstance().collection("Like")
            .document(FirebaseAuth.getInstance().uid!!).collection("Nanded").document(id).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        holder.binding.unlike.visibility = View.GONE
                        holder.binding.like.visibility = View.VISIBLE
                    } else {
                        holder.binding.unlike.visibility = View.VISIBLE
                        holder.binding.like.visibility = View.GONE
                    }
                }
            }

        holder.binding.unlike.setOnClickListener {
            holder.binding.unlike.visibility = View.GONE
            holder.binding.like.visibility = View.VISIBLE
            val hashMap1 = HashMap<String, Any?>()
            hashMap1["userid"] = FirebaseAuth.getInstance().uid
            val date = Date()
            val likeClass = LikeClass(id, "Nanded", "", "", 7028, date.time)
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).set(hashMap1).addOnSuccessListener {
                    FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                        .document(id).set(likeClass).addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Added to your like list",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }
        holder.binding.like.setOnClickListener {
            holder.binding.unlike.visibility = View.VISIBLE
            holder.binding.like.visibility = View.GONE
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                .document(id).delete().addOnCompleteListener {
                    Toast.makeText(
                        context,
                        "Remove from like list",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}
