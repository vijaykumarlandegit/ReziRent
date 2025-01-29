package com.resieasy.rezirent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Adapter.AdapterViewPager
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    var binding: ActivityProfileBinding? = null
    var image11: ImageView? = null

    var selectedImage: Uri? = null

    var personName: String? = null
    var personEmail: String? = null
    var personalNumber: String? = null
    var auth: FirebaseAuth? = null
    var name: String? = null
    var number: String? = null
    var gmail: String? = null
    var pic: String? = null
    var dialog: ProgressDialog? = null
    var mInterstitialAd: InterstitialAd? = null

    var ad_dialog: ProgressDialog? = null

    var courses: Array<String> = arrayOf("On Rent", "For Sell")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ad_dialog = ProgressDialog(this)
        ad_dialog!!.setMessage("Ad loading")
        ad_dialog!!.setCancelable(false)

        //admob
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this, R.string.Profileleadbuttoninterstitial_id.toString(), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    mInterstitialAd = null
                }
            })

        binding!!.leadbtn2.setOnClickListener {
            if (mInterstitialAd != null) {
                ad_dialog!!.show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    ad_dialog!!.dismiss()
                    mInterstitialAd!!.show(this@ProfileActivity)
                    mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                mInterstitialAd = null
                                val intent2 = Intent(
                                    this@ProfileActivity,
                                    LeadShowActivity::class.java
                                )
                                startActivity(intent2)
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                super.onAdFailedToShowFullScreenContent(adError)
                                mInterstitialAd = null
                                val intent2 = Intent(
                                    this@ProfileActivity,
                                    LeadShowActivity::class.java
                                )
                                startActivity(intent2)
                            }
                        }
                }, 1000)
            } else {
                val intent2 = Intent(
                    this@ProfileActivity,
                    LeadShowActivity::class.java
                )
                startActivity(intent2)
            }
        }


        auth = FirebaseAuth.getInstance()
        val userid = auth!!.uid
        dialog = ProgressDialog(this@ProfileActivity)
        dialog!!.setCancelable(false)
        dialog!!.setTitle("Data Uploading .....")


        val adapterViewPager = AdapterViewPager(this)
        binding!!.viewpagr22.adapter = adapterViewPager
        binding!!.tablyout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.viewpagr22.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        binding!!.viewpagr22.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0, 1, 2, 3 -> binding!!.tablyout.getTabAt(position)!!.select()
                }
                super.onPageSelected(position)
            }
        })


        FirebaseFirestore.getInstance().collection("OwnResi").document(userid!!)
            .collection("Nanded")
            .whereEqualTo("type", "Rent")
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val count = queryDocumentSnapshots.size().toString()
                    binding!!.rentcount.text = count
                }
            }

        FirebaseFirestore.getInstance().collection("OwnResi").document(userid).collection("Nanded")
            .whereEqualTo("type", "Sell")
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val count = queryDocumentSnapshots.size().toString()
                    binding!!.sellcount.text = count
                }
            }
        FirebaseFirestore.getInstance().collection("OwnResi").document(userid).collection("Nanded")
            .whereEqualTo("type", "Hostel")
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val count = queryDocumentSnapshots.size().toString()
                    binding!!.hostelcount.text = count
                }
            }
        FirebaseFirestore.getInstance().collection("Like").document(userid).collection("Nanded")
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val count = queryDocumentSnapshots.size().toString()
                    binding!!.likecount.text = count
                }
            }


        /*  ArrayAdapter adapter=new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_spinner_item,courses);
  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  binding.coursesspinner.setAdapter(adapter);
*/
        FirebaseFirestore.getInstance().collection("AllUser").document(userid)
            .get().addOnSuccessListener { snapshot ->
                name = snapshot.getString("name")
                number = snapshot.getString("number")
                gmail = snapshot.getString("mail")

                binding!!.profilename.text = name
                binding!!.profilenumber.text = number
                binding!!.profileemail.text = gmail
                binding!!.optionalname.text = name
            }

        binding!!.showprofile.setOnClickListener {
            binding!!.showprofile.visibility = View.GONE
            binding!!.allogoogleprofile.visibility = View.VISIBLE
        }
        binding!!.back.setOnClickListener { finish() }
        binding!!.hideprofile.setOnClickListener {
            binding!!.allogoogleprofile.visibility = View.GONE
            binding!!.showprofile.visibility = View.VISIBLE
        }
        binding!!.addresidencybtn2.setOnClickListener {
            val intent = Intent(
                this@ProfileActivity,
                UploadFromHareActivity::class.java
            )
            startActivity(intent)
        }
        binding!!.settingbtn.setOnClickListener {
            val intent = Intent(this@ProfileActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        binding!!.editbtninprofile.setOnClickListener {
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dname: TextView
            val dnumber: TextView
            val dgmail: TextView
            val dadd: Button

            val builder =
                AlertDialog.Builder(this@ProfileActivity)
            val view = LayoutInflater.from(this@ProfileActivity)
                .inflate(R.layout.updateuserdatadialog, viewGroup, false)
            builder.setCancelable(true)
            builder.setView(view)

            dname = view.findViewById(R.id.dialogeditusername)
            dnumber = view.findViewById(R.id.dialogeditusernumber)
            dgmail = view.findViewById(R.id.dialogeditusermail)
            dadd = view.findViewById(R.id.dialogedituserupdatebtn)


            val alertDialog = builder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dname.text = name
            dnumber.text = number
            dgmail.text = gmail


            dadd.setOnClickListener {
                dialog!!.show()
                val user = FirebaseAuth.getInstance().currentUser
                val userid = user!!.uid
                val firestore = FirebaseFirestore.getInstance()

                val username = dname.text.toString()
                val usernumber = dnumber.text.toString()
                val usermail = dgmail.text.toString()

                val hashMap = HashMap<String, Any>()
                hashMap["number"] = usernumber
                hashMap["name"] = username
                firestore.collection("AllUser").document(userid).update(hashMap)
                    .addOnSuccessListener {
                        dialog!!.dismiss()
                        alertDialog.dismiss()
                        Toast.makeText(
                            this@ProfileActivity,
                            "Data Uploaded Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            alertDialog.show()
        }
    }
}