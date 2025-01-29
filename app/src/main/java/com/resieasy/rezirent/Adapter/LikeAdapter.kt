package com.resieasy.rezirent.Adapter

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
import com.resieasy.rezirent.Activity.ShowResidencyDataActivity
import com.resieasy.rezirent.Activity.ShowSellDataActivity
import com.resieasy.rezirent.Class.LikeClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.LikeownsampleBinding
import com.squareup.picasso.Picasso
import java.util.Date

class LikeAdapter(var context: Context?, var list: ArrayList<LikeClass?>) :
    RecyclerView.Adapter<LikeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.likeownsample, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val id = data?.id


        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage").document(id!!).get()
            .addOnSuccessListener { snapshot ->
                val firstimage = snapshot.getString("image0")
                Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                    .into(holder.binding.bothsampleimage)
            }


        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData").document(id).get()
            .addOnSuccessListener { snapshot ->
                val rtype = snapshot.getString("rtype")
                if (rtype == "Rent") {
                    holder.binding.toptext.text = "Rent"

                    val name = snapshot.getString("name")
                    val type = snapshot.getString("type")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val rent = snapshot.getString("rent")
                    val agree = snapshot.getLong("period")!!.toInt()

                    holder.binding.bothsamplename.text = name
                    holder.binding.bothsampleaddress.text = address
                    holder.binding.bothsamplearea.text = area
                    holder.binding.bothsampletype.text = type
                    holder.binding.bothsampleprize.text = "Monthly Rent : " + rent + "₹"


                    if (agree == 708) {
                        holder.binding.noagreeview.visibility = View.VISIBLE
                        holder.binding.yesagreeview.visibility = View.GONE
                    } else {
                        holder.binding.noagreeview.visibility = View.GONE
                        holder.binding.yesagreeview.visibility = View.VISIBLE
                    }
                    holder.binding.resicart.setOnClickListener {
                        val intent = Intent(context, ShowResidencyDataActivity::class.java)
                        intent.putExtra("id", id)
                        context?.startActivity(intent)
                    }
                } else if (rtype == "Sell") {
                    holder.binding.toptext.text = "Sell"

                    val name = snapshot.getString("name")
                    val type = snapshot.getString("type")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val rent = snapshot.getString("prize")
                    holder.binding.bothsamplename.text = name
                    holder.binding.bothsampleaddress.text = address
                    holder.binding.bothsamplearea.text = area
                    holder.binding.bothsampletype.text = type
                    if (rent!!.isEmpty()) {
                        holder.binding.rentview.visibility = View.GONE
                    } else {
                        holder.binding.bothsampleprize.text = "Selling Prize : " + rent + "₹"
                    }

                    holder.binding.resicart.setOnClickListener {
                        val intent = Intent(context, ShowSellDataActivity::class.java)
                        intent.putExtra("id", id)
                        context!!.startActivity(intent)
                    }
                } else if (rtype == "Hostel") {
                    holder.binding.toptext.text = "Cot-Base"

                    val name = snapshot.getString("name")
                    val type = snapshot.getString("type")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val rent = snapshot.getString("rent")
                    val agree = snapshot.getLong("period")!!.toInt()


                    holder.binding.bothsamplename.text = name
                    holder.binding.bothsampleaddress.text = address
                    holder.binding.bothsamplearea.text = area
                    holder.binding.bothsampletype.text = type
                    holder.binding.bothsampleprize.text = "Per Person Monthly Rent : " + rent + "₹"


                    if (agree == 708) {
                        holder.binding.noagreeview.visibility = View.VISIBLE
                        holder.binding.yesagreeview.visibility = View.GONE
                    } else {
                        holder.binding.noagreeview.visibility = View.GONE
                        holder.binding.yesagreeview.visibility = View.VISIBLE
                    }
                    holder.binding.resicart.setOnClickListener {
                        val intent = Intent(context, ShowHostelDataActivity::class.java)
                        intent.putExtra("id", id)
                        context!!.startActivity(intent)
                    }
                }
            }


        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllFacility").document(id).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val clean = documentSnapshot.getString("clean")
                    val ac = documentSnapshot.getString("ac")
                    val rowater = documentSnapshot.getString("rowater")
                    val water = documentSnapshot.getString("water")
                    val wifi = documentSnapshot.getString("wifi")
                    val cctv = documentSnapshot.getString("cctv")
                    val bed = documentSnapshot.getString("bed")
                    val hotwater = documentSnapshot.getString("hotwater")
                    val table = documentSnapshot.getString("table")
                    val locker = documentSnapshot.getString("locker")
                    val fan = documentSnapshot.getString("fan")
                    val powerbackup = documentSnapshot.getString("powerbackup")
                    val washing = documentSnapshot.getString("washing")
                    val security = documentSnapshot.getString("security")
                    val inout = documentSnapshot.getString("inout")
                    val attach = documentSnapshot.getString("attach")
                    val shower = documentSnapshot.getString("shower")
                    val parking = documentSnapshot.getString("parking")
                    val mess = documentSnapshot.getString("mess")
                    val tv = documentSnapshot.getString("tv")
                    val gas = documentSnapshot.getString("gas")
                    val dining = documentSnapshot.getString("dining")
                    val refrigerator = documentSnapshot.getString("refrigerator")
                    val sofa = documentSnapshot.getString("sofa")
                    val elevator = documentSnapshot.getString("elevator")
                    val play = documentSnapshot.getString("play")
                    val gym = documentSnapshot.getString("gym")
                    val studyroom = documentSnapshot.getString("studyroom")
                    val kitchen = documentSnapshot.getString("kitchen")
                    val balcony = documentSnapshot.getString("balcony")
                    val indian = documentSnapshot.getString("indian")
                    val western = documentSnapshot.getString("western")
                    val terrace = documentSnapshot.getString("terrace")
                    val furnished = documentSnapshot.getString("furnished")
                    if (clean == "Yes") {
                        holder.binding.cleanbotcart.visibility = View.VISIBLE
                    }
                    if (ac == "Yes") {
                        holder.binding.acbotcart.visibility = View.VISIBLE
                    }
                    if (rowater == "Yes") {
                        holder.binding.rowaterbotcart.visibility = View.VISIBLE
                    }
                    if (water == "Yes") {
                        holder.binding.waterbotcart.visibility = View.VISIBLE
                    }
                    if (wifi == "Yes") {
                        holder.binding.wifibotcart.visibility = View.VISIBLE
                    }
                    if (cctv == "Yes") {
                        holder.binding.cctvbotcart.visibility = View.VISIBLE
                    }
                    if (bed == "Yes") {
                        holder.binding.bedbotcart.visibility = View.VISIBLE
                    }
                    if (hotwater == "Yes") {
                        holder.binding.hotwaterbotcart.visibility = View.VISIBLE
                    }
                    if (table == "Yes") {
                        holder.binding.tablebotcart.visibility = View.VISIBLE
                    }
                    if (locker == "Yes") {
                        holder.binding.lockerbotcart.visibility = View.VISIBLE
                    }
                    if (fan == "Yes") {
                        holder.binding.fanbotcart.visibility = View.VISIBLE
                    }
                    if (powerbackup == "Yes") {
                        holder.binding.backupbotcart.visibility = View.VISIBLE
                    }
                    if (washing == "Yes") {
                        holder.binding.washingbotcart.visibility = View.VISIBLE
                    }
                    if (security == "Yes") {
                        holder.binding.securitybotcart.visibility = View.VISIBLE
                    }
                    if (inout == "Yes") {
                        holder.binding.inoutbotcart.visibility = View.VISIBLE
                    }
                    if (attach == "Yes") {
                        holder.binding.attachedbotcart.visibility = View.VISIBLE
                    }
                    if (shower == "Yes") {
                        holder.binding.showerbotcart.visibility = View.VISIBLE
                    }
                    if (parking == "Yes") {
                        holder.binding.parkingbotcart.visibility = View.VISIBLE
                    }
                    if (mess == "Yes") {
                        holder.binding.messbotcart.visibility = View.VISIBLE
                    }
                    if (tv == "Yes") {
                        holder.binding.tvbotcart.visibility = View.VISIBLE
                    }
                    if (gas == "Yes") {
                        holder.binding.gasbotcart.visibility = View.VISIBLE
                    }
                    if (dining == "Yes") {
                        holder.binding.dianingbotcart.visibility = View.VISIBLE
                    }
                    if (refrigerator == "Yes") {
                        holder.binding.refribotcart.visibility = View.VISIBLE
                    }
                    if (sofa == "Yes") {
                        holder.binding.sofabotcart.visibility = View.VISIBLE
                    }
                    if (elevator == "Yes") {
                        holder.binding.elevatorbotcart.visibility = View.VISIBLE
                    }
                    if (play == "Yes") {
                        holder.binding.playbotcart.visibility = View.VISIBLE
                    }
                    if (gym == "Yes") {
                        holder.binding.gymbotcart.visibility = View.VISIBLE
                    }
                    if (studyroom == "Yes") {
                        holder.binding.studeybotcart.visibility = View.VISIBLE
                    }
                    if (kitchen == "Yes") {
                        holder.binding.kitchenbotcart.visibility = View.VISIBLE
                    }
                    if (balcony == "Yes") {
                        holder.binding.balconybotcart.visibility = View.VISIBLE
                    }
                    if (indian == "Yes") {
                        holder.binding.indianbotcart.visibility = View.VISIBLE
                    }
                    if (western == "Yes") {
                        holder.binding.westernbotcart.visibility = View.VISIBLE
                    }
                    if (terrace == "Yes") {
                        holder.binding.terracebotcart.visibility = View.VISIBLE
                    }
                    if (furnished == "Yes") {
                        holder.binding.furnishedbotcart.visibility = View.VISIBLE
                    }
                }
            }.addOnFailureListener { }
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: LikeownsampleBinding =
            LikeownsampleBinding.bind(itemView)
    }
}
