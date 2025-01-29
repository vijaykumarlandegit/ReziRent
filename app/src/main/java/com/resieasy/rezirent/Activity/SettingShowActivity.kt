package com.resieasy.rezirent.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.resieasy.rezirent.databinding.ActivitySettingShowBinding

class SettingShowActivity : AppCompatActivity() {
    var binding: ActivitySettingShowBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingShowBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)


        val type = intent.getStringExtra("type")

        if (type == "contactus") {
            binding!!.contacusview.visibility = View.VISIBLE
        }
        if (type == "aboutus") {
            binding!!.aboutusview.visibility = View.VISIBLE
        }
        if (type == "pp") {
            binding!!.privacypolicyview.visibility = View.VISIBLE
        }
        if (type == "tc") {
            binding!!.termconditionview.visibility = View.VISIBLE
        }

        binding!!.contactuscontact.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:7028297606"))
            startActivity(intent)
        }

        binding!!.contactuswhatsapp.setOnClickListener {
            val wn = "https://wa.me/+917028297606?text= Hi is anyone available?"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(wn))
            startActivity(intent)
        }

        binding!!.contactusemail.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("mailto:" + "help@resieasy.com")
                )
                intent.putExtra(Intent.EXTRA_SUBJECT, "I have problem in ResiEasy Application")
                intent.putExtra(Intent.EXTRA_TEXT, "Write your problem")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingShowActivity,
                    "Something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}