package com.resieasy.rezirent.ui.adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Activity.EditResidencyDataActivity
import com.resieasy.rezirent.Activity.ProfileActivity
import com.resieasy.rezirent.Activity.ShowResidencyDataActivity
import com.resieasy.rezirent.data.remote.firebase.LeadClass
import com.resieasy.rezirent.data.remote.firebase.SingleIDClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ShowresidencysampleBinding
import com.squareup.picasso.Picasso

class ResiShowOwnerAdapter(var list: ArrayList<SingleIDClass?>, var context: Context?) :
    RecyclerView.Adapter<ResiShowOwnerAdapter.ViewHolder>() {
    var mInterstitialAdedit: InterstitialAd? = null
    var mInterstitialAdshow: InterstitialAd? = null
    var ad_dialog: ProgressDialog? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.showresidencysample, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val id = data!!.id

        ad_dialog = ProgressDialog(context)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)

        val adRequestedit = AdRequest.Builder().build()
        val adRequestshow = AdRequest.Builder().build()

        InterstitialAd.load(
            context!!, R.string.Ownresieditinterstitial_id.toString(), adRequestedit,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAdedit = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdedit = null
                }
            })
        InterstitialAd.load(
            context!!, R.string.Ownresishowinterstitial_id.toString(), adRequestshow,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAdshow = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdshow = null
                }
            })


        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage").document(id!!).get()
            .addOnSuccessListener { snapshot ->
                val firstimage = snapshot.getString("image0")
                Picasso.get().load(firstimage).placeholder(R.drawable.iplaceholdr)
                    .into(holder.binding.srimage)
            }

        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData").document(id).get()
            .addOnSuccessListener { snapshot ->
                val name = snapshot.getString("name")
                val subtype = snapshot.getString("subtype")
                val area = snapshot.getString("area")
                val address = snapshot.getString("address")
                val rent = snapshot.getString("rent")
                val status = snapshot.getString("status")

                val agree = snapshot.getLong("period")!!.toInt()
                val `in` = snapshot.getLong("in")!!.toInt()

                holder.binding.imagenumtext.text = "$`in` images"

                holder.binding.srname.text = name
                holder.binding.sraddress.text = address
                holder.binding.samplearea.text = area
                holder.binding.samplesubtype.text = subtype
                holder.binding.rent.text = rent + "₹/month"

                if (agree == 708) {
                    holder.binding.noagreeview.visibility = View.VISIBLE
                    holder.binding.yesagreeview.visibility = View.GONE
                } else {
                    holder.binding.noagreeview.visibility = View.GONE
                    holder.binding.yesagreeview.visibility = View.VISIBLE
                }
                if (status == "Active") {
                    holder.binding.activestatsview.visibility = View.VISIBLE
                    holder.binding.inactivestatsview.visibility = View.GONE
                } else {
                    holder.binding.activestatsview.visibility = View.GONE
                    holder.binding.inactivestatsview.visibility = View.VISIBLE
                }
            }

        val ddialog = ProgressDialog(context)
        ddialog.setCancelable(false)
        ddialog.setMessage("Please wait....")
        holder.binding.deletview.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val builder = AlertDialog.Builder(
                        context
                    )
                    builder.setIcon(R.drawable.warna)
                    builder.setTitle("DELETE --> $name")
                    builder.setMessage("Are you sure, you want to delete your residency account.")
                    builder.setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        ddialog.show()
                        val userid = FirebaseAuth.getInstance().uid
                        FirebaseFirestore.getInstance().collection("OwnResi")
                            .document(userid!!).collection("Nanded").document(id).delete()
                            .addOnSuccessListener {
                                FirebaseFirestore.getInstance().collection("Nanded")
                                    .document("NandedCity").collection("AllImage")
                                    .document(id).delete()
                                    .addOnSuccessListener {
                                        FirebaseFirestore.getInstance().collection("Nanded")
                                            .document("NandedCity").collection("AllData")
                                            .document(
                                                id
                                            )
                                            .delete().addOnSuccessListener {
                                                FirebaseFirestore.getInstance()
                                                    .collection("Nanded")
                                                    .document("NandedCity")
                                                    .collection("AllFacility")
                                                    .document(id).delete()
                                                    .addOnSuccessListener {
                                                        FirebaseFirestore.getInstance()
                                                            .collection("Nanded")
                                                            .document("NandedCity")
                                                            .collection("AllRule")
                                                            .document(id).delete()
                                                            .addOnSuccessListener {
                                                                ddialog.dismiss()
                                                                dialog.dismiss()
                                                                val intent = Intent(
                                                                    context,
                                                                    ProfileActivity::class.java
                                                                )
                                                                context!!.startActivity(intent)
                                                                Toast.makeText(
                                                                    context,
                                                                    "Your residency account deleted successfully",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                                deletwlike(id)
                                                                deletelead(id)
                                                            }
                                                    }
                                            }
                                    }
                            }
                    }.setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(context, "Thanks !!!", Toast.LENGTH_SHORT).show()
                    }.setNeutralButton(
                        "Help"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            context,
                            "for delete residency account, press yes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()
                }
        }


        holder.binding.editview.setOnClickListener {
            if (mInterstitialAdedit != null) {
                ad_dialog!!.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    ad_dialog!!.dismiss()
                    mInterstitialAdedit!!.show((context as Activity))
                    mInterstitialAdedit!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAdedit = null
                                val intent =
                                    Intent(context, EditResidencyDataActivity::class.java)
                                intent.putExtra("id", id)

                                context!!.startActivity(intent)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAdedit = null
                                val intent =
                                    Intent(context, EditResidencyDataActivity::class.java)
                                intent.putExtra("id", id)

                                context!!.startActivity(intent)
                            }
                        }
                }, 1000)
            } else {
                val intent = Intent(context, EditResidencyDataActivity::class.java)
                intent.putExtra("id", id)

                context!!.startActivity(intent)
            }
        }
        holder.binding.viewview.setOnClickListener {
            if (mInterstitialAdshow != null) {
                ad_dialog!!.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    ad_dialog!!.dismiss()
                    mInterstitialAdshow!!.show((context as Activity))
                    mInterstitialAdshow!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAdshow = null
                                val intent =
                                    Intent(context, ShowResidencyDataActivity::class.java)
                                intent.putExtra("id", id)

                                context!!.startActivity(intent)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAdshow = null
                                val intent =
                                    Intent(context, ShowResidencyDataActivity::class.java)
                                intent.putExtra("id", id)

                                context!!.startActivity(intent)
                            }
                        }
                }, 1000)
            } else {
                val intent = Intent(context, ShowResidencyDataActivity::class.java)
                intent.putExtra("id", id)

                context!!.startActivity(intent)
            }
        }
        holder.binding.activestatsview.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val builder = AlertDialog.Builder(
                        context
                    )
                    builder.setIcon(R.drawable.warna)
                    builder.setTitle("Change Status --> $name")
                    builder.setMessage("Are you sure, you want to change residency status.")
                    builder.setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        ddialog.show()
                        val hashMap = HashMap<String, Any>()
                        hashMap["status"] = "Inactive"
                        FirebaseFirestore.getInstance().collection("Nanded")
                            .document("NandedCity").collection("AllData").document(id)
                            .update(hashMap).addOnSuccessListener {
                                ddialog.dismiss()
                                dialog.dismiss()
                                holder.binding.activestatsview.visibility =
                                    View.GONE
                                holder.binding.inactivestatsview.visibility =
                                    View.VISIBLE
                                Toast.makeText(
                                    context,
                                    "Status changed successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }.setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(context, "Thanks !!!", Toast.LENGTH_SHORT).show()
                    }.setNeutralButton(
                        "Help"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            context,
                            "for change residency status, press yes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()
                }
        }
        holder.binding.inactivestatsview.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get()
                .addOnSuccessListener { snapshot ->
                    val name = snapshot.getString("name")
                    val builder = AlertDialog.Builder(
                        context
                    )
                    builder.setIcon(R.drawable.warna)
                    builder.setTitle("Change Status --> $name")
                    builder.setMessage("Are you sure, you want to change residency status.")
                    builder.setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        ddialog.show()
                        val hashMap = HashMap<String, Any>()
                        hashMap["status"] = "Active"
                        FirebaseFirestore.getInstance().collection("Nanded")
                            .document("NandedCity").collection("AllData").document(id)
                            .update(hashMap).addOnSuccessListener {
                                ddialog.dismiss()
                                dialog.dismiss()
                                holder.binding.activestatsview.visibility =
                                    View.VISIBLE
                                holder.binding.inactivestatsview.visibility =
                                    View.GONE
                                Toast.makeText(
                                    context,
                                    "Status changed successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }.setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                    }.setNeutralButton(
                        "Help"
                    ) { dialog, which ->
                        ddialog.dismiss()
                        dialog.dismiss()
                        Toast.makeText(
                            context,
                            "for change residency status, press yes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()
                }
        }
    }


    private fun deletelead(iddd: String?) {
        FirebaseFirestore.getInstance().collection("Lead")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("Nanded").get().addOnSuccessListener { queryDocumentSnapshots ->
                for (data in queryDocumentSnapshots.documents) {
                    val data1 = data.toObject(LeadClass::class.java)

                    val resiid = data1!!.resiid
                    val uid = data1.userid



                    if (resiid == iddd) {
                        FirebaseFirestore.getInstance().collection("Lead")
                            .document(FirebaseAuth.getInstance().uid!!)
                            .collection("Nanded").document(uid + resiid).delete()
                            .addOnSuccessListener { }
                    }
                }
            }
    }

    private fun deletwlike(idddd: String) {
        FirebaseFirestore.getInstance().collection("Like").get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (data in queryDocumentSnapshots.documents) {
                    val useridd = data.getString("userid")

                    FirebaseFirestore.getInstance().collection("Like").document(useridd!!)
                        .collection("Nanded").document(idddd).delete().addOnSuccessListener { }
                }
            }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ShowresidencysampleBinding =
            ShowresidencysampleBinding.bind(itemView)
    }
}
