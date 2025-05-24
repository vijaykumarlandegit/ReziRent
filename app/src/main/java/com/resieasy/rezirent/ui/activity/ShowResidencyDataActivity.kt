package com.resieasy.rezirent.ui.activity

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
import com.resieasy.rezirent.data.remote.firebase.LeadClass
import com.resieasy.rezirent.notification.FcmNotificationsSender
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityShowResidencyDataBinding
import com.resieasy.rezirent.ui.viewmodel.remote.FacilityViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.RulesViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowResiViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
@AndroidEntryPoint
class ShowResidencyDataActivity : AppCompatActivity() {
     private val binding by lazy { ActivityShowResidencyDataBinding.inflate(layoutInflater) }
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

    private val showResiViewModel: ShowResiViewModel by viewModels()
    private val facilityViewModel: FacilityViewModel by viewModels()
    private val rulesViewModel: RulesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val id = intent.getStringExtra("id")

        ad_dialog = ProgressDialog(this)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)

        currentUserID=FirebaseAuth.getInstance().uid
        //admob
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


       callAd(adRequest)


        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                binding.adView.loadAd(adRequest)
            }
        }







        if (id != null) {
            showResiViewModel.getResiData(id)
            showResiData(showResiViewModel,id)
        }else{
            toast("Something is wrong")
        }


        binding.mapview.setOnClickListener {
            val intent = Intent(
                this@ShowResidencyDataActivity,
                MapsActivity::class.java
            )
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", name)
            startActivity(intent)
        }
        binding.cmscontact.setOnClickListener {
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

        binding.cmswhatsapp.setOnClickListener {
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

        if (id==null){
            toast("Something is wrong")
        }else{
            facilityViewModel.getFacility(id)
            showFacilityData(facilityViewModel,id)
        }



        if (id==null){
            toast("Something is wrong")
        }else{
            rulesViewModel.getRules(id)
            showRulesData(rulesViewModel,id)
        }



    }

    private fun showRulesData(rulesViewModel: RulesViewModel, id: String) {
        lifecycleScope.launchWhenStarted {
            rulesViewModel.data.observe(this@ShowResidencyDataActivity) {
                it?.let { documentSnapshot ->
                    val clean = documentSnapshot.clean
                    val trouble = documentSnapshot.trouble
                    val licence = documentSnapshot.licence
                    val gateenry = documentSnapshot.gateentry
                    val alcohol = documentSnapshot.alcohol
                    val damage = documentSnapshot.damage
                    val ousiders = documentSnapshot.outsiders
                    val permission = documentSnapshot.permission
                    val morerule = documentSnapshot.more


                    if (clean == "Yes") {
                        binding.clinerule.visibility = View.VISIBLE
                    }
                    if (trouble == "Yes") {
                        binding.nottrublerule.visibility = View.VISIBLE
                    }
                    if (licence == "Yes") {
                        binding.licencerule.visibility = View.VISIBLE
                    }
                    if (gateenry == "Yes") {
                        binding.entryrule.visibility = View.VISIBLE
                    }
                    if (alcohol == "Yes") {
                        binding.alcoholrule.visibility = View.VISIBLE
                    }
                    if (damage == "Yes") {
                        binding.damagerule.visibility = View.VISIBLE
                    }
                    if (ousiders == "Yes") {
                        binding.outsiderrule.visibility = View.VISIBLE
                    }
                    if (permission == "Yes") {
                        binding.prentperule.visibility = View.VISIBLE
                    }
                    if (!morerule!!.isEmpty()) {
                        binding.morerules.visibility = View.VISIBLE
                        binding.moreruletext.text = morerule
                    }
                }
            }
        }
    }

    private fun showFacilityData(facilityViewModel: FacilityViewModel, id: String) {
        lifecycleScope.launchWhenStarted {
            facilityViewModel.data.observe(this@ShowResidencyDataActivity) {
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
                        binding.clean.visibility = View.VISIBLE
                    }
                    if (ac == "Yes") {
                        binding.ac.visibility = View.VISIBLE
                    }
                    if (rowater == "Yes") {
                        binding.rowater.visibility = View.VISIBLE
                    }
                    if (water == "Yes") {
                        binding.water.visibility = View.VISIBLE
                    }
                    if (wifi == "Yes") {
                        binding.wifi.visibility = View.VISIBLE
                    }
                    if (cctv == "Yes") {
                        binding.cctv.visibility = View.VISIBLE
                    }
                    if (bed == "Yes") {
                        binding.bedandmat.visibility = View.VISIBLE
                    }
                    if (hotwater == "Yes") {
                        binding.hotwater.visibility = View.VISIBLE
                    }
                    if (table == "Yes") {
                        binding.table.visibility = View.VISIBLE
                    }
                    if (locker == "Yes") {
                        binding.locker.visibility = View.VISIBLE
                    }
                    if (fan == "Yes") {
                        binding.cooler.visibility = View.VISIBLE
                    }
                    if (powerbackup == "Yes") {
                        binding.backup.visibility = View.VISIBLE
                    }
                    if (washing == "Yes") {
                        binding.washing.visibility = View.VISIBLE
                    }
                    if (security == "Yes") {
                        binding.security.visibility = View.VISIBLE
                    }
                    if (inout == "Yes") {
                        binding.inout.visibility = View.VISIBLE
                    }
                    if (attach == "Yes") {
                        binding.attached.visibility = View.VISIBLE
                    }
                    if (shower == "Yes") {
                        binding.shower.visibility = View.VISIBLE
                    }
                    if (parking == "Yes") {
                        binding.parking.visibility = View.VISIBLE
                    }
                    if (mess == "Yes") {
                        binding.mess.visibility = View.VISIBLE
                    }
                    if (tv == "Yes") {
                        binding.tv.visibility = View.VISIBLE
                    }
                    if (gas == "Yes") {
                        binding.gas.visibility = View.VISIBLE
                    }
                    if (dining == "Yes") {
                        binding.dinning.visibility = View.VISIBLE
                    }
                    if (refrigerator == "Yes") {
                        binding.refrigi.visibility = View.VISIBLE
                    }
                    if (sofa == "Yes") {
                        binding.sofa.visibility = View.VISIBLE
                    }
                    if (elevator == "Yes") {
                        binding.elevator.visibility = View.VISIBLE
                    }
                    if (play == "Yes") {
                        binding.play.visibility = View.VISIBLE
                    }
                    if (gym == "Yes") {
                        binding.gym.visibility = View.VISIBLE
                    }
                    if (studyroom == "Yes") {
                        binding.stuyroom.visibility = View.VISIBLE
                    }
                    if (kitchen == "Yes") {
                        binding.kitchen.visibility = View.VISIBLE
                    }
                    if (balcony == "Yes") {
                        binding.balcony.visibility = View.VISIBLE
                    }
                    if (indian == "Yes") {
                        binding.indian.visibility = View.VISIBLE
                    }
                    if (western == "Yes") {
                        binding.western.visibility = View.VISIBLE
                    }
                    if (terrace == "Yes") {
                        binding.terrace.visibility = View.VISIBLE
                    }
                    if (furnished == "Yes") {
                        binding.furnised.visibility = View.VISIBLE
                    }
                    if (!more.isEmpty()) {
                        binding.morefaci.visibility = View.VISIBLE
                        binding.morefacilitytext.text = more
                    }
                }
            }
        }
    }

    private fun showResiData(showResiViewModel: ShowResiViewModel, id: String) {
        lifecycleScope.launchWhenStarted {
            showResiViewModel.data.observe(this@ShowResidencyDataActivity) {
                it?.let { documentSnapshot->
                    `in` = documentSnapshot.input
                    latitude = documentSnapshot.latitude
                    longitude = documentSnapshot.longitude


                    name = documentSnapshot.name
                    val idd = documentSnapshot.id
                    val type = documentSnapshot.type
                    val subtype = documentSnapshot.subtype
                    val area = documentSnapshot.area
                    val address = documentSnapshot.address
                    val oname = documentSnapshot.oname
                    number = documentSnapshot.number
                    whatsapp = documentSnapshot.whatsapp
                    val mail = documentSnapshot.mail
                    val rent = documentSnapshot.rent
                    val erent = documentSnapshot.erent
                    val deposit = documentSnapshot.deposit
                    val extra = documentSnapshot.extra
                    val more = documentSnapshot.more
                    val policy = documentSnapshot.policy
                    val period = documentSnapshot.period.toInt()
                    val numperiod = java.lang.String.valueOf(
                        documentSnapshot.period.toInt()
                    )


                    binding.name.text = name
                    binding.address.text = address
                    binding.samplesubtype.text = subtype
                    binding.samplearea.text = area
                    binding.oname.text = oname
                    binding.contact.text = number
                    binding.whatsapp.text = whatsapp
                    binding.rent.text = rent + "₹/month"
                    binding.erent.text = erent

                    binding.expagreeorpolicy.text = policy

                    userid = documentSnapshot.userid

                    if (userid != FirebaseAuth.getInstance().uid) {
                        sendnotandlead(userid!!, id, subtype)
                    }



                    if (mail!!.isEmpty()) {
                        binding.emailview.visibility = View.GONE
                    } else {
                        binding.mail.text = mail
                    }
                    if (more.isEmpty()) {
                        binding.moreview.visibility = View.GONE
                    } else {
                        binding.more.text = more
                    }


                    if (period == 708) {
                        binding.noagreeview.visibility = View.VISIBLE
                        binding.yesagreeview.visibility = View.GONE
                    } else {
                        binding.noagreeview.visibility = View.GONE
                        binding.yesagreeview.visibility = View.VISIBLE
                        binding.agreperiodn.text = numperiod
                    }

                    if (deposit == "No deposit will taken") {
                        binding.nodepositblue.visibility = View.VISIBLE
                        binding.depositview.visibility = View.GONE
                    } else {
                        binding.nodepositblue.visibility = View.GONE
                        binding.depositview.visibility = View.VISIBLE
                        binding.deposit.text = deposit
                    }
                    if (extra == "No extra charges will taken") {
                        binding.noextrablue.visibility = View.VISIBLE
                        binding.extraview.visibility = View.GONE
                    } else {
                        binding.noextrablue.visibility = View.GONE
                        binding.extraview.visibility = View.VISIBLE
                        binding.extra.text = extra
                    }
                    val remotimage: MutableList<SlideModel> = ArrayList()
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
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@ShowResidencyDataActivity,
                                "Image is not load, something is wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }
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
                                                    val notificationsSender =
                                                        FcmNotificationsSender(
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
                                            val notificationsSender =
                                                FcmNotificationsSender(
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
    private fun toast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
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
    private fun callAd(adRequest: AdRequest) {

        InterstitialAd.load(
            this, R.string.Showresidatacallintertitial_id.toString(), adRequest,
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
            this, R.string.Showresidatawhatsintertitial_id.toString(), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAdwhats = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAdwhats = null
                }
            })
    }

}