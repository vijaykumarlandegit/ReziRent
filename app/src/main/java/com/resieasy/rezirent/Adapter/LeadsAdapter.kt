package com.resieasy.rezirent.Adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.LeadClass
import com.resieasy.rezirent.Class.UsersClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.LeadshowsampleBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LeadsAdapter(var context: Context, var list: ArrayList<LeadClass?>) :
    RecyclerView.Adapter<LeadsAdapter.ViewHolder>() {
    var ad_dialog: ProgressDialog? = null

    var mInterstitialAdcall: InterstitialAd? = null
    var mInterstitialAdwhats: InterstitialAd? = null

    private fun getTime(time: String, timestamp: Long?): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = time.toLong()
        val timee = SimpleDateFormat("dd-MM-yy hh:mm aa").format(timestamp)
        return timee
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(context).inflate(R.layout.leadshowsample, parent, false)
        return ViewHolder(view1)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ad_dialog = ProgressDialog(context)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)

        val adRequestcall = AdRequest.Builder().build()
        val adRequestwhats = AdRequest.Builder().build()


        InterstitialAd.load(
            context, R.string.Leadcallbtnintertitial_id.toString(), adRequestcall,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAdcall = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdcall = null
                }
            })
        InterstitialAd.load(
            context, R.string.Leadwhatsappbtninterstitial_id.toString(), adRequestwhats,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAdwhats = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdwhats = null
                }
            })


        val data = list[position]

        val userid = data!!.userid
        val resiid = data.resiid
        val timestamp = data.time
        val tt = timestamp.toString()

        holder.binding.datetext.text = getTime(tt, timestamp)

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllData").document(resiid!!).get()
            .addOnSuccessListener { snapshot ->
                val rtype = snapshot.getString("rtype")
                if (rtype == "Rent") {
                    val name = snapshot.getString("name")
                    holder.binding.resiname.text = "Residency Name : $name"
                } else if (rtype == "Sell") {
                    val name = snapshot.getString("name")
                    holder.binding.resiname.text = "Property Name : $name"
                } else if (rtype == "Hostel") {
                    val name = snapshot.getString("name")
                    holder.binding.resiname.text = "Hostel/PG Name : $name"
                }
            }


        FirebaseFirestore.getInstance().collection("AllUser").document(userid!!).get()
            .addOnSuccessListener { snapshot ->
                val data1 = snapshot.toObject(UsersClass::class.java)
                val name = data1!!.name
                val number = data1.number
                holder.binding.profilename.text = name
                holder.binding.profilenumber.text = number


                holder.binding.callbtn.setOnClickListener {
                    if (mInterstitialAdcall != null) {
                        ad_dialog!!.show()
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            ad_dialog!!.dismiss()
                            mInterstitialAdcall!!.show((context as Activity))
                            mInterstitialAdcall!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        mInterstitialAdcall = null
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:$number"))
                                        context.startActivity(intent)
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        super.onAdFailedToShowFullScreenContent(adError)
                                        mInterstitialAdcall = null
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.setData(Uri.parse("tel:$number"))
                                        context.startActivity(intent)
                                    }
                                }
                        }, 1000)
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.setData(Uri.parse("tel:$number"))
                        context.startActivity(intent)
                    }
                }
                holder.binding.whatsappbtn.setOnClickListener {
                    if (mInterstitialAdwhats != null) {
                        ad_dialog!!.show()
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed({
                            ad_dialog!!.dismiss()
                            mInterstitialAdwhats!!.show((context as Activity))
                            mInterstitialAdwhats!!.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        mInterstitialAdwhats = null
                                        val wn =
                                            "https://wa.me/+917028297606?text= Hi is anyone available?"
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.setData(Uri.parse("https://wa.me/+91$number?text=  Hey hi, have you visited my property on ResiEasy ?"))
                                        context.startActivity(intent)
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                        super.onAdFailedToShowFullScreenContent(adError)
                                        mInterstitialAdwhats = null
                                        val wn =
                                            "https://wa.me/+917028297606?text= Hi is anyone available?"
                                        val intent = Intent(Intent.ACTION_VIEW)
                                        intent.setData(Uri.parse("https://wa.me/+91$number?text=  Hey hi, have you visited my property on ResiEasy ?"))
                                        context.startActivity(intent)
                                    }
                                }
                        }, 1000)
                    } else {
                        val wn = "https://wa.me/+917028297606?text= Hi is anyone available?"
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse("https://wa.me/+91$number?text=  Hey hi, have you visited my property on ResiEasy ?"))
                        context.startActivity(intent)
                    }
                }
            }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: LeadshowsampleBinding =
            LeadshowsampleBinding.bind(itemView)
    }
}
