package com.resieasy.rezirent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.LeadClass
import com.resieasy.rezirent.FcmNotificationsSender
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityShowResidencyDataBinding
import java.util.Date

class ShowResidencyDataActivity : AppCompatActivity() {
    var binding: ActivityShowResidencyDataBinding? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var name: String? = null
    var number: String? = null
    var whatsapp: String? = null
    var userid: String? = null
    var `in`: Int = 0
    var mInterstitialAdcall: InterstitialAd? = null
    var mInterstitialAdwhats: InterstitialAd? = null
    var just: String = "123"
    var ad_dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowResidencyDataBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        val id = intent.getStringExtra("id")

        ad_dialog = ProgressDialog(this)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)


        //admob
        val adRequest = AdRequest.Builder().build()
        binding!!.adView.loadAd(adRequest)

        val adRequestcall = AdRequest.Builder().build()
        val adRequestwhats = AdRequest.Builder().build()


        InterstitialAd.load(
            this, R.string.Showresidatacallintertitial_id.toString(), adRequestcall,
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
            this, R.string.Showresidatawhatsintertitial_id.toString(), adRequestwhats,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAdwhats = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdwhats = null
                }
            })


        binding!!.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                binding!!.adView.loadAd(adRequest)
            }
        }


        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //final ArrayList<SlideModel> slidmodel=new ArrayList<>();
        val remotimage: MutableList<SlideModel> = ArrayList()


        //binding.imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);
        FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData").document(id!!).get()
            .addOnSuccessListener { documentSnapshot ->
                `in` = documentSnapshot.getLong("in")!!.toInt()
                latitude = documentSnapshot.getDouble("latitude")!!
                longitude = documentSnapshot.getDouble("longitude")!!


                name = documentSnapshot.getString("name")
                val idd = documentSnapshot.getString("id")
                val type = documentSnapshot.getString("type")
                val subtype = documentSnapshot.getString("subtype")
                val area = documentSnapshot.getString("area")
                val address = documentSnapshot.getString("address")
                val oname = documentSnapshot.getString("oname")
                number = documentSnapshot.getString("number")
                whatsapp = documentSnapshot.getString("whatsapp")
                val mail = documentSnapshot.getString("mail")
                val rent = documentSnapshot.getString("rent")
                val erent = documentSnapshot.getString("erent")
                val deposit = documentSnapshot.getString("deposit")
                val extra = documentSnapshot.getString("extra")
                val more = documentSnapshot.getString("more")
                val policy = documentSnapshot.getString("policy")
                val period = documentSnapshot.getLong("period")!!.toInt()
                val numperiod = java.lang.String.valueOf(
                    documentSnapshot.getLong("period")!!.toInt()
                )


                binding!!.name.text = name
                binding!!.address.text = address
                binding!!.samplesubtype.text = subtype
                binding!!.samplearea.text = area
                binding!!.oname.text = oname
                binding!!.contact.text = number
                binding!!.whatsapp.text = whatsapp
                binding!!.rent.text = rent + "₹/month"
                binding!!.erent.text = erent

                binding!!.expagreeorpolicy.text = policy

                userid = documentSnapshot.getString("userid")

                if (userid != FirebaseAuth.getInstance().uid) {
                    sendnotandlead(userid!!, id, subtype)
                }



                if (mail!!.isEmpty()) {
                    binding!!.emailview.visibility = View.GONE
                } else {
                    binding!!.mail.text = mail
                }
                if (more!!.isEmpty()) {
                    binding!!.moreview.visibility = View.GONE
                } else {
                    binding!!.more.text = more
                }


                if (period == 708) {
                    binding!!.noagreeview.visibility = View.VISIBLE
                    binding!!.yesagreeview.visibility = View.GONE
                } else {
                    binding!!.noagreeview.visibility = View.GONE
                    binding!!.yesagreeview.visibility = View.VISIBLE
                    binding!!.agreperiodn.text = numperiod
                }

                if (deposit == "No deposit will taken") {
                    binding!!.nodepositblue.visibility = View.VISIBLE
                    binding!!.depositview.visibility = View.GONE
                } else {
                    binding!!.nodepositblue.visibility = View.GONE
                    binding!!.depositview.visibility = View.VISIBLE
                    binding!!.deposit.text = deposit
                }
                if (extra == "No extra charges will taken") {
                    binding!!.noextrablue.visibility = View.VISIBLE
                    binding!!.extraview.visibility = View.GONE
                } else {
                    binding!!.noextrablue.visibility = View.GONE
                    binding!!.extraview.visibility = View.VISIBLE
                    binding!!.extra.text = extra
                }
                FirebaseFirestore.getInstance().collection("Nanded")
                    .document("NandedCity").collection("AllImage").document(idd!!).get()
                    .addOnSuccessListener { snapshot ->
                        for (i in 0 until `in`) {
                            val ima = snapshot.getString("image$i")
                            remotimage.add(
                                SlideModel(
                                    ima,
                                    (i + 1).toString() + "/" + `in`,
                                    ScaleTypes.FIT
                                )
                            )
                            binding!!.imageSlider.setImageList(remotimage, ScaleTypes.FIT)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@ShowResidencyDataActivity,
                            "Image is not load, something is wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@ShowResidencyDataActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }


        binding!!.mapview.setOnClickListener {
            val intent = Intent(
                this@ShowResidencyDataActivity,
                MapsActivity::class.java
            )
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", name)
            startActivity(intent)
        }
        binding!!.cmscontact.setOnClickListener {
            if (mInterstitialAdcall != null) {
                ad_dialog!!.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    ad_dialog!!.dismiss()
                    mInterstitialAdcall!!.show(this@ShowResidencyDataActivity)
                    mInterstitialAdcall!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAdcall = null
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.setData(Uri.parse("tel:$number"))
                                startActivity(intent)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAdcall = null
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.setData(Uri.parse("tel:$number"))
                                startActivity(intent)
                            }
                        }
                }, 1000)
            } else {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData(Uri.parse("tel:$number"))
                startActivity(intent)
            }
        }

        binding!!.cmswhatsapp.setOnClickListener {
            if (mInterstitialAdwhats != null) {
                ad_dialog!!.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    ad_dialog!!.dismiss()
                    mInterstitialAdwhats!!.show(this@ShowResidencyDataActivity)
                    mInterstitialAdwhats!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAdwhats = null
                                val wn =
                                    "https://wa.me/+917028297606?text= Hi is anyone available?"
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setData(Uri.parse("https://wa.me/+91$whatsapp?text= Hi is anyone available?"))
                                startActivity(intent)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAdwhats = null
                                val wn =
                                    "https://wa.me/+917028297606?text= Hi is anyone available?"
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setData(Uri.parse("https://wa.me/+91$whatsapp?text= Hi is anyone available?"))
                                startActivity(intent)
                            }
                        }
                }, 1000)
            } else {
                val wn = "https://wa.me/+917028297606?text= Hi is anyone available?"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse("https://wa.me/+91$whatsapp?text= Hi is anyone available?"))
                startActivity(intent)
            }
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
                val morefaci = documentSnapshot.getString("more")
                if (clean == "Yes") {
                    binding!!.clean.visibility = View.VISIBLE
                }
                if (ac == "Yes") {
                    binding!!.ac.visibility = View.VISIBLE
                }
                if (rowater == "Yes") {
                    binding!!.rowater.visibility = View.VISIBLE
                }
                if (water == "Yes") {
                    binding!!.water.visibility = View.VISIBLE
                }
                if (wifi == "Yes") {
                    binding!!.wifi.visibility = View.VISIBLE
                }
                if (cctv == "Yes") {
                    binding!!.cctv.visibility = View.VISIBLE
                }
                if (bed == "Yes") {
                    binding!!.bedandmat.visibility = View.VISIBLE
                }
                if (hotwater == "Yes") {
                    binding!!.hotwater.visibility = View.VISIBLE
                }
                if (table == "Yes") {
                    binding!!.table.visibility = View.VISIBLE
                }
                if (locker == "Yes") {
                    binding!!.locker.visibility = View.VISIBLE
                }
                if (fan == "Yes") {
                    binding!!.cooler.visibility = View.VISIBLE
                }
                if (powerbackup == "Yes") {
                    binding!!.backup.visibility = View.VISIBLE
                }
                if (washing == "Yes") {
                    binding!!.washing.visibility = View.VISIBLE
                }
                if (security == "Yes") {
                    binding!!.security.visibility = View.VISIBLE
                }
                if (inout == "Yes") {
                    binding!!.inout.visibility = View.VISIBLE
                }
                if (attach == "Yes") {
                    binding!!.attached.visibility = View.VISIBLE
                }
                if (shower == "Yes") {
                    binding!!.shower.visibility = View.VISIBLE
                }
                if (parking == "Yes") {
                    binding!!.parking.visibility = View.VISIBLE
                }
                if (mess == "Yes") {
                    binding!!.mess.visibility = View.VISIBLE
                }
                if (tv == "Yes") {
                    binding!!.tv.visibility = View.VISIBLE
                }
                if (gas == "Yes") {
                    binding!!.gas.visibility = View.VISIBLE
                }
                if (dining == "Yes") {
                    binding!!.dinning.visibility = View.VISIBLE
                }
                if (refrigerator == "Yes") {
                    binding!!.refrigi.visibility = View.VISIBLE
                }
                if (sofa == "Yes") {
                    binding!!.sofa.visibility = View.VISIBLE
                }
                if (elevator == "Yes") {
                    binding!!.elevator.visibility = View.VISIBLE
                }
                if (play == "Yes") {
                    binding!!.play.visibility = View.VISIBLE
                }
                if (gym == "Yes") {
                    binding!!.gym.visibility = View.VISIBLE
                }
                if (studyroom == "Yes") {
                    binding!!.stuyroom.visibility = View.VISIBLE
                }
                if (kitchen == "Yes") {
                    binding!!.kitchen.visibility = View.VISIBLE
                }
                if (balcony == "Yes") {
                    binding!!.balcony.visibility = View.VISIBLE
                }
                if (indian == "Yes") {
                    binding!!.indian.visibility = View.VISIBLE
                }
                if (western == "Yes") {
                    binding!!.western.visibility = View.VISIBLE
                }
                if (terrace == "Yes") {
                    binding!!.terrace.visibility = View.VISIBLE
                }
                if (furnished == "Yes") {
                    binding!!.furnised.visibility = View.VISIBLE
                }
                if (!morefaci!!.isEmpty()) {
                    binding!!.morefaci.visibility = View.VISIBLE
                    binding!!.morefacilitytext.text = morefaci
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@ShowResidencyDataActivity,
                    "Facility are not load, something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllRule").document(id).get().addOnSuccessListener { documentSnapshot ->
                val clean = documentSnapshot.getString("clean")
                val trouble = documentSnapshot.getString("trouble")
                val licence = documentSnapshot.getString("licence")
                val gateenry = documentSnapshot.getString("gateenry")
                val alcohol = documentSnapshot.getString("alcohol")
                val damage = documentSnapshot.getString("damage")
                val ousiders = documentSnapshot.getString("ousiders")
                val permission = documentSnapshot.getString("permission")
                val morerule = documentSnapshot.getString("more")


                if (clean == "Yes") {
                    binding!!.clinerule.visibility = View.VISIBLE
                }
                if (trouble == "Yes") {
                    binding!!.nottrublerule.visibility = View.VISIBLE
                }
                if (licence == "Yes") {
                    binding!!.licencerule.visibility = View.VISIBLE
                }
                if (gateenry == "Yes") {
                    binding!!.entryrule.visibility = View.VISIBLE
                }
                if (alcohol == "Yes") {
                    binding!!.alcoholrule.visibility = View.VISIBLE
                }
                if (damage == "Yes") {
                    binding!!.damagerule.visibility = View.VISIBLE
                }
                if (ousiders == "Yes") {
                    binding!!.outsiderrule.visibility = View.VISIBLE
                }
                if (permission == "Yes") {
                    binding!!.prentperule.visibility = View.VISIBLE
                }
                if (!morerule!!.isEmpty()) {
                    binding!!.morerules.visibility = View.VISIBLE
                    binding!!.moreruletext.text = morerule
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@ShowResidencyDataActivity,
                    "Rules are not load, something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendnotandlead(userid1: String, id11: String?, subtype1: String?) {
        FirebaseFirestore.getInstance().collection("Lead").document(userid1).collection("Nanded")
            .document(FirebaseAuth.getInstance().uid + id11).get().addOnCompleteListener { task ->
                val snapshot1 = task.result
                if (snapshot1.exists()) {
                    val d = task.result.toObject(LeadClass::class.java)
                    val date = Date()
                    val oldtt = d!!.time
                    val newtt = date.time
                    val diffrence = newtt - oldtt!!
                    val myValue =
                        convertSecondsToHMmSs(diffrence)
                            .toLong()
                    if (myValue < 1) {
                        val hashMap1 = HashMap<String, Any?>()
                        hashMap1["time"] = date.time
                        hashMap1["resiid"] = id11


                        FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                            .collection("Nanded").document(FirebaseAuth.getInstance().uid + id11)
                            .update(hashMap1)
                    } else {
                        val hashMap2 = HashMap<String, Any?>()
                        hashMap2["time"] = date.time
                        hashMap2["resiid"] = id11
                        FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                            .collection("Nanded").document(FirebaseAuth.getInstance().uid + id11)
                            .update(hashMap2)
                            .addOnSuccessListener {
                                FirebaseFirestore.getInstance().collection("AllUser").document(
                                    userid!!
                                ).get()
                                    .addOnSuccessListener { snapshot1 ->
                                        FirebaseFirestore.getInstance().collection("AllUser")
                                            .document(
                                                FirebaseAuth.getInstance().uid!!
                                            ).get()
                                            .addOnSuccessListener { snapshot2 ->
                                                val token = snapshot1.getString("token")
                                                val myname = snapshot2.getString("name")
                                                val notificationsSender = FcmNotificationsSender(
                                                    token,
                                                    "ResiEasy, Lead For $subtype1",
                                                    "$myname see your  $subtype1 details, please check.",
                                                    applicationContext,
                                                    this@ShowResidencyDataActivity
                                                )
                                                notificationsSender.SendNotifications()
                                            }
                                    }
                            }
                    }
                } else {
                    val date = Date()
                    val leadClass = LeadClass(
                        FirebaseAuth.getInstance().uid,
                        id11,
                        "Nanded",
                        "",
                        "",
                        7028.toString(),
                        date.time
                    )
                    FirebaseFirestore.getInstance().collection("Lead").document(userid1)
                        .collection("Nanded").document(FirebaseAuth.getInstance().uid + id11)
                        .set(leadClass)
                        .addOnSuccessListener {
                            FirebaseFirestore.getInstance().collection("AllUser").document(userid!!)
                                .get()
                                .addOnSuccessListener { snapshot1 ->
                                    FirebaseFirestore.getInstance().collection("AllUser").document(
                                        FirebaseAuth.getInstance().uid!!
                                    ).get()
                                        .addOnSuccessListener { snapshot2 ->
                                            val token = snapshot1.getString("token")
                                            val myname = snapshot2.getString("name")
                                            val notificationsSender = FcmNotificationsSender(
                                                token,
                                                "ResiEasy, Lead For $subtype1",
                                                "$myname see your  $subtype1 details, please check.",
                                                applicationContext,
                                                this@ShowResidencyDataActivity
                                            )
                                            notificationsSender.SendNotifications()
                                        }
                                }
                        }
                }
            }
    }

    companion object {
        fun convertSecondsToHMmSs(millis: Long): String {
            //long seconds = (millis / 1000) % 60;
            //long minutes = (millis / (1000 * 60)) % 60;
            val hours = millis / (1000 * 60 * 60)

            val b = StringBuilder()
            b.append(if (hours == 0L) "00" else if (hours < 10) ("0$hours").toString() else hours.toString())
            // b.append(":");
            //b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
            // b.append(":");
            // b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
            return b.toString()
        }
    }
}