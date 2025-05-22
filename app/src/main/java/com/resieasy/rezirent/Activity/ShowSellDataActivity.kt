package com.resieasy.rezirent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import com.resieasy.rezirent.ViewModel.MainActivityViewModel
import com.resieasy.rezirent.ViewModel.ShowSellViewModel
import com.resieasy.rezirent.databinding.ActivityShowSellDataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ShowSellDataActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowSellDataBinding


    var latitude: Double? = null
    var longitude: Double? = null
    var name: String = ""
    var number: String? = null
    var whatsapp: String? = null
    var userid: String? = null
    var currentUserID: String? = null

    var `in`: Int = 0
    var mInterstitialAdcall: InterstitialAd? = null
    var mInterstitialAdwhats: InterstitialAd? = null
    var ad_dialog: ProgressDialog? = null
    private val showSellViewModel: ShowSellViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowSellDataBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        ad_dialog = ProgressDialog(this)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)



        currentUserID=FirebaseAuth.getInstance().uid
        loadAd(adRequest)


        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                binding.adView.loadAd(adRequest)
            }
        }

        val id = intent.getStringExtra("id")



        if (id != null) {
            showSellViewModel.getSellData(id)
            showSellData(showSellViewModel,id)
        }else{
            toast("Something is wrong")
        }

        binding.mapview.setOnClickListener {
            if(latitude==null||longitude==null){
                toast("Something is wrong")

            }else{
                 val intent = Intent(this@ShowSellDataActivity, MapsActivity::class.java)
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                intent.putExtra("name", name)
                startActivity(intent)
            }

        }
        binding.cmscontact.setOnClickListener {
            if (number.isNullOrBlank()){
                toast("Something is wrong")
            }else{
                if (mInterstitialAdcall != null) {
                    ad_dialog!!.show()
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        ad_dialog!!.dismiss()
                        mInterstitialAdcall!!.show(this@ShowSellDataActivity)
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

        }

        binding.cmswhatsapp.setOnClickListener {
            if (whatsapp.isNullOrBlank()){
                toast("Something is wrong")
            }else{

                if (mInterstitialAdwhats != null) {
                    ad_dialog!!.show()
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        ad_dialog!!.dismiss()
                        mInterstitialAdwhats!!.show(this@ShowSellDataActivity)
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

        }
    }

    private fun showSellData(showSellViewModel: ShowSellViewModel,id:String) {
        val remotimage: MutableList<SlideModel> = ArrayList()
        lifecycleScope.launchWhenStarted {
            showSellViewModel.data.observe(this@ShowSellDataActivity) { result ->
                result?.let { documentSnapshot ->
                    `in` = documentSnapshot.input
                    latitude = documentSnapshot.latitude
                    longitude = documentSnapshot.longitude
                    name = documentSnapshot.name

                    val address = documentSnapshot.address
                    val type = documentSnapshot.type
                    val subtype = documentSnapshot.subtype
                    val area = documentSnapshot.area

                    val oname = documentSnapshot.oname
                    val idd = documentSnapshot.id
                    number = documentSnapshot.number
                    whatsapp = documentSnapshot.whatsapp
                    val mail = documentSnapshot.mail

                    val prize = documentSnapshot.prize
                    val eprize = documentSnapshot.eprize
                    val more = documentSnapshot.more
                    val size = documentSnapshot.size
                    userid = documentSnapshot.userid

                    if (userid != FirebaseAuth.getInstance().uid) {
                        sendnotandlead(userid!!, id, subtype)
                    }

                    binding.name.text = name
                    binding.address.text = address
                    binding.samplesubtype.text = subtype
                    binding.samplearea.text = area
                    binding.oname.text = oname
                    binding.contact.text = number
                    binding.whatsapp.text = whatsapp
                    if (mail!!.isEmpty()) {
                        binding.emailview.visibility = View.GONE
                    } else {
                        binding.mail.text = mail
                    }
                    if (more.isEmpty()) {
                        binding.moreview.visibility = View.GONE
                    } else {
                        binding.moree.text = more
                    }
                    if (prize.isEmpty()) {
                        binding.prizeview.visibility = View.GONE
                    } else {
                        binding.prize.text = prize + "₹"
                        binding.eprize.text = eprize
                    }
                    if (size.isEmpty()) {
                        binding.sizeview.visibility = View.GONE
                    } else {
                        binding.sizee.text = size
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
                                binding.imageSlider.setImageList(remotimage, ScaleTypes.FIT)
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@ShowSellDataActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

    private fun toast(s: String) {
           Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }


    private fun sendnotandlead(userid1: String, id11: String?, subtype1: String?) {
        FirebaseFirestore.getInstance().collection("Lead").document(userid1).collection("Nanded")
            .document(FirebaseAuth.getInstance().uid + id11).get().addOnCompleteListener { task ->
                val snapshot1 = task.result
                if (snapshot1.exists()) {
                    val d = task.result.toObject(LeadClass::class.java)
                    if (d!=null){
                        val date = Date()
                        val oldtt = d.time?:0L
                        val newtt = date.time
                        val diffrence = newtt - oldtt
                        val myValue =
                            convertSecondsToHMmSs(diffrence).toLong()
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
                                                        this@ShowSellDataActivity
                                                    )
                                                    notificationsSender.SendNotifications()
                                                }
                                        }
                                }
                        }
                    }

                } else {
                    val date = Date()
                    val leadClass = LeadClass(
                        FirebaseAuth.getInstance().uid ?: "default_uid",
                        id11 ?: "default_id",
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
                                                this@ShowSellDataActivity
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

    private fun loadAd(adRequest: AdRequest) {
        InterstitialAd.load(
            this, R.string.Showselldatacallintertitial_id.toString(), adRequest,
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
            this, R.string.Showselldatawhatsintertitial_id.toString(), adRequest,
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

    }
}