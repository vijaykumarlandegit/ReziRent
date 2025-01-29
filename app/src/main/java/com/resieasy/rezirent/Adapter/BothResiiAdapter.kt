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
import com.resieasy.rezirent.Class.BothResiClass
import com.resieasy.rezirent.Class.LikeClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.BothhostelsapleBinding
import com.resieasy.rezirent.databinding.BothresisampleBinding
import com.resieasy.rezirent.databinding.BothsellsampleBinding
import com.squareup.picasso.Picasso
import java.util.Date

class BothResiiAdapter(var list: ArrayList<BothResiClass?>, var context: Context)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var RESI_VIEW_TYPE = 4
    private var Sell_VIEW_TYPE= 2
    private var HOSTEL_VIEW_TYPE  = 3

    inner class ResiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: BothresisampleBinding =
            BothresisampleBinding.bind(itemView)
    }

    inner class SellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: BothsellsampleBinding =
            BothsellsampleBinding.bind(itemView)
    }

    inner class HostelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: BothhostelsapleBinding =
            BothhostelsapleBinding.bind(itemView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == RESI_VIEW_TYPE) {
            val view1 = LayoutInflater.from(context).inflate(R.layout.bothresisample, parent, false)
            return ResiViewHolder(view1)
        } else if (viewType == Sell_VIEW_TYPE) {
            val view2 = LayoutInflater.from(context).inflate(R.layout.bothsellsample, parent, false)
            return SellViewHolder(view2)
        } else {
            val view3 =
                LayoutInflater.from(context).inflate(R.layout.bothhostelsaple, parent, false)
            return HostelViewHolder(view3)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position]?.rtype == "Rent") {
            RESI_VIEW_TYPE
        } else if (list[position]?.rtype == "Sell") {
            Sell_VIEW_TYPE
        } else {
            HOSTEL_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
        val id = data?.id

        if (viewHolder is ResiViewHolder) {

            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id!!).get()
                .addOnSuccessListener { snapshot ->
                    val firstimage = snapshot.getString("image0")
                    Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                        .into(viewHolder.binding.bothsampleimage)
                }
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val resitype = snapshot.getString("subtype")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val rent = snapshot.getString("rent")
                    val agree = snapshot.getLong("period")!!.toInt()

                    viewHolder.binding.bothsamplename.text = name
                    viewHolder.binding.bothsampleaddress.text = address
                    viewHolder.binding.bothsamplearea.text = area
                    viewHolder.binding.bothsamplesubtype.text = resitype
                    viewHolder.binding.bothresirenttext.text = rent + "₹/month"
                    if (agree == 708) {
                        viewHolder.binding.noagreeview.visibility = View.VISIBLE
                        viewHolder.binding.yesagreeview.visibility = View.GONE
                    } else {
                        viewHolder.binding.noagreeview.visibility = View.GONE
                        viewHolder.binding.yesagreeview.visibility = View.VISIBLE
                    }
                }
            viewHolder.binding.resicart.setOnClickListener {
                val intent = Intent(context, ShowResidencyDataActivity::class.java)
                intent.putExtra("id", id)
                context.startActivity(intent)
            }
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(id).get()
                .addOnSuccessListener { documentSnapshot ->
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
                        viewHolder.binding.cleanbotcart.visibility = View.VISIBLE
                    }
                    if (ac == "Yes") {
                        viewHolder.binding.acbotcart.visibility = View.VISIBLE
                    }
                    if (rowater == "Yes") {
                        viewHolder.binding.rowaterbotcart.visibility = View.VISIBLE
                    }
                    if (water == "Yes") {
                        viewHolder.binding.waterbotcart.visibility = View.VISIBLE
                    }
                    if (wifi == "Yes") {
                        viewHolder.binding.wifibotcart.visibility = View.VISIBLE
                    }
                    if (cctv == "Yes") {
                        viewHolder.binding.cctvbotcart.visibility = View.VISIBLE
                    }
                    if (bed == "Yes") {
                        viewHolder.binding.bedbotcart.visibility = View.VISIBLE
                    }
                    if (hotwater == "Yes") {
                        viewHolder.binding.hotwaterbotcart.visibility = View.VISIBLE
                    }
                    if (table == "Yes") {
                        viewHolder.binding.tablebotcart.visibility = View.VISIBLE
                    }
                    if (locker == "Yes") {
                        viewHolder.binding.lockerbotcart.visibility = View.VISIBLE
                    }
                    if (fan == "Yes") {
                        viewHolder.binding.fanbotcart.visibility = View.VISIBLE
                    }
                    if (powerbackup == "Yes") {
                        viewHolder.binding.backupbotcart.visibility = View.VISIBLE
                    }
                    if (washing == "Yes") {
                        viewHolder.binding.washingbotcart.visibility = View.VISIBLE
                    }
                    if (security == "Yes") {
                        viewHolder.binding.securitybotcart.visibility = View.VISIBLE
                    }
                    if (inout == "Yes") {
                        viewHolder.binding.inoutbotcart.visibility = View.VISIBLE
                    }
                    if (attach == "Yes") {
                        viewHolder.binding.attachedbotcart.visibility = View.VISIBLE
                    }
                    if (shower == "Yes") {
                        viewHolder.binding.showerbotcart.visibility = View.VISIBLE
                    }
                    if (parking == "Yes") {
                        viewHolder.binding.parkingbotcart.visibility = View.VISIBLE
                    }
                    if (mess == "Yes") {
                        viewHolder.binding.messbotcart.visibility = View.VISIBLE
                    }
                    if (tv == "Yes") {
                        viewHolder.binding.tvbotcart.visibility = View.VISIBLE
                    }
                    if (gas == "Yes") {
                        viewHolder.binding.gasbotcart.visibility = View.VISIBLE
                    }
                    if (dining == "Yes") {
                        viewHolder.binding.dianingbotcart.visibility = View.VISIBLE
                    }
                    if (refrigerator == "Yes") {
                        viewHolder.binding.refribotcart.visibility = View.VISIBLE
                    }
                    if (sofa == "Yes") {
                        viewHolder.binding.sofabotcart.visibility = View.VISIBLE
                    }
                    if (elevator == "Yes") {
                        viewHolder.binding.elevatorbotcart.visibility = View.VISIBLE
                    }
                    if (play == "Yes") {
                        viewHolder.binding.playbotcart.visibility = View.VISIBLE
                    }
                    if (gym == "Yes") {
                        viewHolder.binding.gymbotcart.visibility = View.VISIBLE
                    }
                    if (studyroom == "Yes") {
                        viewHolder.binding.studeybotcart.visibility = View.VISIBLE
                    }
                    if (kitchen == "Yes") {
                        viewHolder.binding.kitchenbotcart.visibility = View.VISIBLE
                    }
                    if (balcony == "Yes") {
                        viewHolder.binding.balconybotcart.visibility = View.VISIBLE
                    }
                    if (indian == "Yes") {
                        viewHolder.binding.indianbotcart.visibility = View.VISIBLE
                    }
                    if (western == "Yes") {
                        viewHolder.binding.westernbotcart.visibility = View.VISIBLE
                    }
                    if (terrace == "Yes") {
                        viewHolder.binding.terracebotcart.visibility = View.VISIBLE
                    }
                    if (furnished == "Yes") {
                        viewHolder.binding.furnishedbotcart.visibility = View.VISIBLE
                    }
                }.addOnFailureListener { }
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).collection("Nanded").document(id).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            viewHolder.binding.unlike.visibility = View.GONE
                            viewHolder.binding.like.visibility = View.VISIBLE
                        } else {
                            viewHolder.binding.unlike.visibility = View.VISIBLE
                            viewHolder.binding.like.visibility = View.GONE
                        }
                    }
                }

            viewHolder.binding.unlike.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.GONE
                viewHolder.binding.like.visibility = View.VISIBLE
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
            viewHolder.binding.like.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.VISIBLE
                viewHolder.binding.like.visibility = View.GONE
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
        } else if (viewHolder is SellViewHolder) {
             FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id!!).get()
                .addOnSuccessListener { snapshot ->
                    val firstimage = snapshot.getString("image0")
                    Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                        .into(viewHolder.binding.bothsampleimage)
                }
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val resitype = snapshot.getString("subtype")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val prize = snapshot.getString("prize")
                    viewHolder.binding.bothsamplename.text = name
                    viewHolder.binding.bothsampleaddress.text = address
                    viewHolder.binding.bothsamplearea.text = area
                    viewHolder.binding.bothsamplesubtype.text = resitype
                    if (prize!!.isEmpty()) {
                        viewHolder.binding.prizeview.visibility = View.GONE
                    } else {
                        viewHolder.binding.bothsampleprize.text = prize + "₹"
                    }
                }
            viewHolder.binding.sellcart.setOnClickListener {
                val intent = Intent(context, ShowSellDataActivity::class.java)
                intent.putExtra("id", id)
                context.startActivity(intent)
            }
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).collection("Nanded").document(id).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            viewHolder.binding.unlike.visibility = View.GONE
                            viewHolder.binding.like.visibility = View.VISIBLE
                        } else {
                            viewHolder.binding.unlike.visibility = View.VISIBLE
                            viewHolder.binding.like.visibility = View.GONE
                        }
                    }
                }
            viewHolder.binding.unlike.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.GONE
                viewHolder.binding.like.visibility = View.VISIBLE
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
            viewHolder.binding.like.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.VISIBLE
                viewHolder.binding.like.visibility = View.GONE
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
        } else if (viewHolder is HostelViewHolder) {
             FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllImage").document(id!!).get()
                .addOnSuccessListener { snapshot ->
                    val firstimage = snapshot.getString("image0")
                    Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                        .into(viewHolder.binding.bothsampleimage)
                }
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val resitype = snapshot.getString("subtype")
                    val area = snapshot.getString("area")
                    val address = snapshot.getString("address")
                    val rent = snapshot.getString("rent")
                    val agree = snapshot.getLong("period")!!.toInt()

                    viewHolder.binding.bothsamplename.text = name
                    viewHolder.binding.bothsampleaddress.text = address
                    viewHolder.binding.bothsamplearea.text = area
                    viewHolder.binding.bothsamplesubtype.text = resitype
                    viewHolder.binding.bothsampleprize.text = rent + "₹/month"
                    if (agree == 708) {
                        viewHolder.binding.noagreeview.visibility = View.VISIBLE
                        viewHolder.binding.yesagreeview.visibility = View.GONE
                    } else {
                        viewHolder.binding.noagreeview.visibility = View.GONE
                        viewHolder.binding.yesagreeview.visibility = View.VISIBLE
                    }
                }
            viewHolder.binding.hostelcart.setOnClickListener {
                val intent = Intent(context, ShowHostelDataActivity::class.java)
                intent.putExtra("id", id)
                context.startActivity(intent)
            }
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllFacility").document(id).get()
                .addOnSuccessListener { documentSnapshot ->
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
                        viewHolder.binding.cleanbotcart.visibility = View.VISIBLE
                    }
                    if (ac == "Yes") {
                        viewHolder.binding.acbotcart.visibility = View.VISIBLE
                    }
                    if (rowater == "Yes") {
                        viewHolder.binding.rowaterbotcart.visibility = View.VISIBLE
                    }
                    if (water == "Yes") {
                        viewHolder.binding.waterbotcart.visibility = View.VISIBLE
                    }
                    if (wifi == "Yes") {
                        viewHolder.binding.wifibotcart.visibility = View.VISIBLE
                    }
                    if (cctv == "Yes") {
                        viewHolder.binding.cctvbotcart.visibility = View.VISIBLE
                    }
                    if (bed == "Yes") {
                        viewHolder.binding.bedbotcart.visibility = View.VISIBLE
                    }
                    if (hotwater == "Yes") {
                        viewHolder.binding.hotwaterbotcart.visibility = View.VISIBLE
                    }
                    if (table == "Yes") {
                        viewHolder.binding.tablebotcart.visibility = View.VISIBLE
                    }
                    if (locker == "Yes") {
                        viewHolder.binding.lockerbotcart.visibility = View.VISIBLE
                    }
                    if (fan == "Yes") {
                        viewHolder.binding.fanbotcart.visibility = View.VISIBLE
                    }
                    if (powerbackup == "Yes") {
                        viewHolder.binding.backupbotcart.visibility = View.VISIBLE
                    }
                    if (washing == "Yes") {
                        viewHolder.binding.washingbotcart.visibility = View.VISIBLE
                    }
                    if (security == "Yes") {
                        viewHolder.binding.securitybotcart.visibility = View.VISIBLE
                    }
                    if (inout == "Yes") {
                        viewHolder.binding.inoutbotcart.visibility = View.VISIBLE
                    }
                    if (attach == "Yes") {
                        viewHolder.binding.attachedbotcart.visibility = View.VISIBLE
                    }
                    if (shower == "Yes") {
                        viewHolder.binding.showerbotcart.visibility = View.VISIBLE
                    }
                    if (parking == "Yes") {
                        viewHolder.binding.parkingbotcart.visibility = View.VISIBLE
                    }
                    if (mess == "Yes") {
                        viewHolder.binding.messbotcart.visibility = View.VISIBLE
                    }
                    if (tv == "Yes") {
                        viewHolder.binding.tvbotcart.visibility = View.VISIBLE
                    }
                    if (gas == "Yes") {
                        viewHolder.binding.gasbotcart.visibility = View.VISIBLE
                    }
                    if (dining == "Yes") {
                        viewHolder.binding.dianingbotcart.visibility = View.VISIBLE
                    }
                    if (refrigerator == "Yes") {
                        viewHolder.binding.refribotcart.visibility = View.VISIBLE
                    }
                    if (sofa == "Yes") {
                        viewHolder.binding.sofabotcart.visibility = View.VISIBLE
                    }
                    if (elevator == "Yes") {
                        viewHolder.binding.elevatorbotcart.visibility = View.VISIBLE
                    }
                    if (play == "Yes") {
                        viewHolder.binding.playbotcart.visibility = View.VISIBLE
                    }
                    if (gym == "Yes") {
                        viewHolder.binding.gymbotcart.visibility = View.VISIBLE
                    }
                    if (studyroom == "Yes") {
                        viewHolder.binding.studeybotcart.visibility = View.VISIBLE
                    }
                    if (kitchen == "Yes") {
                        viewHolder.binding.kitchenbotcart.visibility = View.VISIBLE
                    }
                    if (balcony == "Yes") {
                        viewHolder.binding.balconybotcart.visibility = View.VISIBLE
                    }
                    if (indian == "Yes") {
                        viewHolder.binding.indianbotcart.visibility = View.VISIBLE
                    }
                    if (western == "Yes") {
                        viewHolder.binding.westernbotcart.visibility = View.VISIBLE
                    }
                    if (terrace == "Yes") {
                        viewHolder.binding.terracebotcart.visibility = View.VISIBLE
                    }
                    if (furnished == "Yes") {
                        viewHolder.binding.furnishedbotcart.visibility = View.VISIBLE
                    }
                }.addOnFailureListener { }
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).collection("Nanded").document(id).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            viewHolder.binding.unlike.visibility = View.GONE
                            viewHolder.binding.like.visibility = View.VISIBLE
                        } else {
                            viewHolder.binding.unlike.visibility = View.VISIBLE
                            viewHolder.binding.like.visibility = View.GONE
                        }
                    }
                }
            viewHolder.binding.unlike.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.GONE
                viewHolder.binding.like.visibility = View.VISIBLE
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

            viewHolder.binding.like.setOnClickListener {
                viewHolder.binding.unlike.visibility = View.VISIBLE
                viewHolder.binding.like.visibility = View.GONE
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
    }



    override fun getItemCount(): Int {
        return list.size
    }



    companion object {
        private const val AD_VIEW = 1
        private const val ITEM_FEED_COUNT = 4
    }
}


