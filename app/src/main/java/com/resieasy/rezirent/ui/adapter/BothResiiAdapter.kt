package com.resieasy.rezirent.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.data.remote.firebase.BothResiClass
import com.resieasy.rezirent.data.remote.firebase.LikeClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.data.remote.firebase.UnifiedResidencyClass
import com.resieasy.rezirent.databinding.BothhostelsapleBinding
import com.resieasy.rezirent.databinding.BothresisampleBinding
import com.resieasy.rezirent.databinding.BothsellsampleBinding
import com.resieasy.rezirent.ui.activity.ShowHostelDataActivity
import com.resieasy.rezirent.ui.activity.ShowResidencyDataActivity
import com.resieasy.rezirent.ui.activity.ShowSellDataActivity
import com.resieasy.rezirent.ui.viewmodel.remote.FacilityViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowHostelViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowResiViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowSellViewModel
import com.squareup.picasso.Picasso
import java.util.Date

class BothResiiAdapter(
    var list: ArrayList<UnifiedResidencyClass?>,
    var context: AppCompatActivity,
    private val facilityViewModel: FacilityViewModel
)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var RESI_VIEW_TYPE = 4
    private var Sell_VIEW_TYPE= 2
    private var HOSTEL_VIEW_TYPE  = 3

    fun updateList(newList : ArrayList<UnifiedResidencyClass?>){
        list=newList
        notifyDataSetChanged()
    }

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
        return when(list[position]?.rtype){
            "Rent"->RESI_VIEW_TYPE
            "Sell"->Sell_VIEW_TYPE
            "Hostel"->HOSTEL_VIEW_TYPE
            else ->  RESI_VIEW_TYPE
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val data = list[position]
      //  val id = data?.id
        if (data != null) {
            val id = data.id

            if (!id.isNullOrEmpty()) {


                if (viewHolder is ResiViewHolder) {

                    FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllImage").document(id).get()
                        .addOnSuccessListener { snapshot ->
                            val firstimage = snapshot.getString("image0")
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                                .into(viewHolder.binding.bothsampleimage)
                        }

//                    showResiViewModel.getResiData(id)
//                    showResiData(showResiViewModel, context, viewHolder)
                    val name = data.name
                    val resitype = data.subtype
                    val area = data.area
                    val address = data.address
                    val rent = data.rent
                    val agree = data.period.toInt()
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
                     facilityViewModel.getFacility(id)
                    showFacilityData(facilityViewModel, context, viewHolder)

                    viewHolder.binding.resicart.setOnClickListener {
                        val intent = Intent(context, ShowResidencyDataActivity::class.java)
                        intent.putExtra("id", id)
                        context.startActivity(intent)
                    }


                    FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                        .document(id).get()
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
                            .document(FirebaseAuth.getInstance().uid!!).set(hashMap1)
                            .addOnSuccessListener {
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
                else if (viewHolder is SellViewHolder) {
                    FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllImage").document(id!!).get()
                        .addOnSuccessListener { snapshot ->
                            val firstimage = snapshot.getString("image0")
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                                .into(viewHolder.binding.bothsampleimage)
                        }

//                    showSellViewModel.getSellData(id)
//                    showSellData(showSellViewModel, context, viewHolder)

                    val name = data.name
                    val resitype = data.subtype
                    val address = data.address
                    val area = data.area
                    val prize = data.prize

                    viewHolder.binding.bothsamplename.text = name
                    viewHolder.binding.bothsampleaddress.text = address
                    viewHolder.binding.bothsamplearea.text = area
                    viewHolder.binding.bothsamplesubtype.text = resitype
                    if (prize != null) {
                        if (prize.isEmpty()) {
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
                        .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                        .document(id).get()
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
                            .document(FirebaseAuth.getInstance().uid!!).set(hashMap1)
                            .addOnSuccessListener {
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
                else if (viewHolder is HostelViewHolder) {
                    FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllImage").document(id!!).get()
                        .addOnSuccessListener { snapshot ->
                            val firstimage = snapshot.getString("image0")
                            Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                                .into(viewHolder.binding.bothsampleimage)
                        }
//                    showHostelViewModel.getHostelData(id)
//                    showHostelData(showHostelViewModel, context, viewHolder)

                    val name =data.name
                    val resitype = data.subtype
                    val agree = data.period
                    val address = data.address
                    val area = data.area
                    val rent = data.rent
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

                    facilityViewModel.getFacility(id)
                    showHostelFacility(facilityViewModel, context, viewHolder)

                    viewHolder.binding.hostelcart.setOnClickListener {
                        val intent = Intent(context, ShowHostelDataActivity::class.java)
                        intent.putExtra("id", id)
                        context.startActivity(intent)
                    }

                    FirebaseFirestore.getInstance().collection("Like")
                        .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                        .document(id).get()
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
                            .document(FirebaseAuth.getInstance().uid!!).set(hashMap1)
                            .addOnSuccessListener {
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
        }
    }

    private fun showHostelFacility(facilityViewModel: FacilityViewModel,   context: AppCompatActivity, viewHolder: HostelViewHolder) {
      context.lifecycleScope.launchWhenStarted {
          facilityViewModel.data.observe(context){
              it?.let {documentSnapshot->
                  val clean = documentSnapshot.clean
                  val ac = documentSnapshot.ac
                  val rowater = documentSnapshot.rowater
                  val water = documentSnapshot.water
                  val wifi = documentSnapshot.wifi
                  val cctv = documentSnapshot.cctv
                  val bed = documentSnapshot.bed
                  val hotwater = documentSnapshot.hotwater
                  val table = documentSnapshot.table
                  val locker = documentSnapshot.locker
                  val fan = documentSnapshot.fan
                  val powerbackup = documentSnapshot.powerbackup
                  val washing = documentSnapshot.washing
                  val security = documentSnapshot.security
                  val inout = documentSnapshot.inout
                  val attach = documentSnapshot.attach
                  val shower = documentSnapshot.shower
                  val parking = documentSnapshot.parking
                  val mess = documentSnapshot.mess
                  val tv = documentSnapshot.tv
                  val gas = documentSnapshot.gas
                  val dining = documentSnapshot.dining
                  val refrigerator = documentSnapshot.refrigerator
                  val sofa = documentSnapshot.sofa
                  val elevator = documentSnapshot.elevator
                  val play = documentSnapshot.play
                  val gym = documentSnapshot.gym
                  val studyroom = documentSnapshot.studyroom
                  val kitchen = documentSnapshot.kitchen
                  val balcony = documentSnapshot.balcony
                  val indian = documentSnapshot.indian
                  val western = documentSnapshot.western
                  val terrace = documentSnapshot.terrace
                  val furnished = documentSnapshot.furnished
                  val more = documentSnapshot.more
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
              }
          }
      }
    }

    private fun showHostelData(showHostelViewModel: ShowHostelViewModel,  context: AppCompatActivity, viewHolder: HostelViewHolder) {
          showHostelViewModel.data.observe(context) {
              it?.let {documentSnapshot->
                  val name =documentSnapshot.name
                  val resitype = documentSnapshot.subtype
                  val agree = documentSnapshot.period
                  val address = documentSnapshot.address
                  val area = documentSnapshot.area
                  val rent = documentSnapshot.rent
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
          }


    }

    private fun showSellData(showSellViewModel: ShowSellViewModel,   context: AppCompatActivity, viewHolder: SellViewHolder) {

             showSellViewModel.data.observe(context) { result ->
                result?.let { documentSnapshot ->
                    val name = documentSnapshot.name
                    val resitype = documentSnapshot.subtype
                    val address = documentSnapshot.address
                    val area = documentSnapshot.area
                    val prize = documentSnapshot.prize

                    viewHolder.binding.bothsamplename.text = name
                    viewHolder.binding.bothsampleaddress.text = address
                    viewHolder.binding.bothsamplearea.text = area
                    viewHolder.binding.bothsamplesubtype.text = resitype
                    if (prize.isEmpty()) {
                        viewHolder.binding.prizeview.visibility = View.GONE
                    } else {
                        viewHolder.binding.bothsampleprize.text = prize + "₹"
                    }
                }
            }

    }

    private fun showFacilityData(facilityViewModel: FacilityViewModel,   context1: AppCompatActivity, viewHolder: ResiViewHolder) {
             facilityViewModel.data.observe(context1) {
                it?.let { documentSnapshot ->
                    val clean = documentSnapshot.clean
                    val ac = documentSnapshot.ac
                    val rowater = documentSnapshot.rowater
                    val water = documentSnapshot.water
                    val wifi = documentSnapshot.wifi
                    val cctv = documentSnapshot.cctv
                    val bed = documentSnapshot.bed
                    val hotwater = documentSnapshot.hotwater
                    val table = documentSnapshot.table
                    val locker = documentSnapshot.locker
                    val fan = documentSnapshot.fan
                    val powerbackup = documentSnapshot.powerbackup
                    val washing = documentSnapshot.washing
                    val security = documentSnapshot.security
                    val inout = documentSnapshot.inout
                    val attach = documentSnapshot.attach
                    val shower = documentSnapshot.shower
                    val parking = documentSnapshot.parking
                    val mess = documentSnapshot.mess
                    val tv = documentSnapshot.tv
                    val gas = documentSnapshot.gas
                    val dining = documentSnapshot.dining
                    val refrigerator = documentSnapshot.refrigerator
                    val sofa = documentSnapshot.sofa
                    val elevator = documentSnapshot.elevator
                    val play = documentSnapshot.play
                    val gym = documentSnapshot.gym
                    val studyroom = documentSnapshot.studyroom
                    val kitchen = documentSnapshot.kitchen
                    val balcony = documentSnapshot.balcony
                    val indian = documentSnapshot.indian
                    val western = documentSnapshot.western
                    val terrace = documentSnapshot.terrace
                    val furnished = documentSnapshot.furnished
                    val more = documentSnapshot.more
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
                }
            }



    }

    private fun showResiData(showResiViewModel: ShowResiViewModel,   context: AppCompatActivity, viewHolder: ResiViewHolder) {
            showResiViewModel.data.observe(context) {
               it?.let { documentSnapshot ->

                   val name = documentSnapshot.name

                   val resitype = documentSnapshot.subtype
                   val area = documentSnapshot.area
                   val address = documentSnapshot.address

                   val rent = documentSnapshot.rent

                   val agree = documentSnapshot.period.toInt()
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
           }


    }






    companion object {
        private const val AD_VIEW = 1
        private const val ITEM_FEED_COUNT = 4
    }
}


