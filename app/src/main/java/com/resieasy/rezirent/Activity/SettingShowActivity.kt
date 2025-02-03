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
    val binding by lazy{ ActivitySettingShowBinding.inflate(layoutInflater)}
    lateinit var type : String
    lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      
        setContentView(binding.root)
        if (::name.isInitialized) {
            println(name) // Safe to use
        } else {
            println("Name is not initialized yet")
        }

          type = intent.getStringExtra("type").toString()

        if (type == "contactus") {
            binding.contacusview.visibility = View.VISIBLE
        }
        if (type == "aboutus") {
            binding.aboutusview.visibility = View.VISIBLE
        }
        if (type == "pp") {
            binding.privacypolicyview.visibility = View.VISIBLE
        }
        if (type == "tc") {
            binding.termconditionview.visibility = View.VISIBLE
        }

        binding.contactuscontact.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:7028297606"))
            startActivity(intent)
        }

        binding.contactuswhatsapp.setOnClickListener {
            val wn = "https://wa.me/+917028297606?text= Hi is anyone available?"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(wn))
            startActivity(intent)
        }

        binding.contactusemail.setOnClickListener {
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("type", type)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
       // type = savedInstanceState.getString("type").toString()-> null to "null"
        
        type = savedInstanceState.getString("type")?: ""
    }

}