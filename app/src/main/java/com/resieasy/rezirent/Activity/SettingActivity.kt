package com.resieasy.rezirent.Activity

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivitySettingBinding
import com.resieasy.rezirent.databinding.ActivitySettingShowBinding

class SettingActivity : AppCompatActivity() {
    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpAd()




        binding.contactus.setOnClickListener {
            val intent = Intent(this@SettingActivity, SettingShowActivity::class.java)
            intent.putExtra("type", "contactus")
            startActivity(intent)
        }
        binding.back.setOnClickListener { finish() }
        binding.moreapp.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=ResiEasy-+Buy/Rent/Sell+Residency")
                )
            )
        }
        binding.aboutus.setOnClickListener {
            val intent = Intent(this@SettingActivity, SettingShowActivity::class.java)
            intent.putExtra("type", "aboutus")
            startActivity(intent)
        }
        binding.privacypolicy.setOnClickListener {
            val intent = Intent(this@SettingActivity, SettingShowActivity::class.java)
            intent.putExtra("type", "pp")
            startActivity(intent)
        }
        binding.termandcondition.setOnClickListener {
            val intent = Intent(this@SettingActivity, SettingShowActivity::class.java)
            intent.putExtra("type", "tc")
            startActivity(intent)
        }
        binding.shareapp.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT, "ResiEasy")
                val applink =
                    "https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                intent.putExtra(Intent.EXTRA_TEXT, applink)
                startActivity(Intent.createChooser(intent, "Share ResiEasy Application"))

                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this@SettingActivity,
                    "Something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.rateus.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingActivity,
                    "Something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



        binding.logout.setOnClickListener {
            val builder = AlertDialog.Builder(this@SettingActivity)
            builder.setIcon(R.drawable.warna)
            builder.setTitle("LOGOUT")
            builder.setMessage("you are sure, you want to logout your account.")
            builder.setPositiveButton(
                "Yes"
            ) { dialog, which ->

                googleSignInClient.signOut().addOnSuccessListener {
                    val hashMap = HashMap<String, Any>()
                    hashMap["token"] = ""
                    FirebaseFirestore.getInstance().collection("AllUser").document(
                        FirebaseAuth.getInstance().uid!!
                    )
                        .update(hashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                auth.signOut()
                                val intent = Intent(
                                    this@SettingActivity,
                                    SignInActivity::class.java
                                )
                                startActivity(intent)
                                finishAffinity()
                                Toast.makeText(
                                    this@SettingActivity,
                                    "Logout Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }.setNegativeButton(
                "No"
            ) { dialog, which -> dialog.dismiss() }.setNeutralButton(
                "Help"
            ) { dialog, which ->
                Toast.makeText(
                    this@SettingActivity,
                    "for logout, press yes",
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
    }

    private fun setUpAd() {

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError)
                binding.adView.loadAd(adRequest)
            }
        }
    }
}