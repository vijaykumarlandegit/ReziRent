package com.resieasy.rezirent.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.resieasy.rezirent.databinding.ActivityUploadFromHareBinding

class UploadFromHareActivity : AppCompatActivity() {

    lateinit var binding: ActivityUploadFromHareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadFromHareBinding.inflate(layoutInflater)
        setContentView(binding.root)

      binding.resntview.setOnClickListener {
            val intent = Intent(this@UploadFromHareActivity, AddResidencyActivity::class.java)
            startActivity(intent)
        }
        binding.sellview.setOnClickListener {
            val intent = Intent(this@UploadFromHareActivity, AddSellResiActivity::class.java)
            startActivity(intent)
        }

        binding.cotbaseview.setOnClickListener {
            val intent = Intent(this@UploadFromHareActivity, AddHostelActivity::class.java)
            startActivity(intent)
        }
        binding.back.setOnClickListener { finish() }


    }

}