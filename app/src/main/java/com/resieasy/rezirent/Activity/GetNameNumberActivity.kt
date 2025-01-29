package com.resieasy.rezirent.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.databinding.ActivityGetNameNumberBinding

class GetNameNumberActivity : AppCompatActivity() {
    var binding: ActivityGetNameNumberBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetNameNumberBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading . . . .")
        progressDialog.setCancelable(false)


        binding!!.skipbutton.setOnClickListener {
            val intent = Intent(this@GetNameNumberActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        binding!!.userupdatebtn.setOnClickListener {
            progressDialog.show()
            val name = binding!!.username.text.toString()
            val number = binding!!.usernumber.text.toString()
            FirebaseFirestore.getInstance().collection("AllUser")
                .document(FirebaseAuth.getInstance().uid!!)
                .update("name", name, "number", number).addOnSuccessListener {
                    Toast.makeText(
                        this@GetNameNumberActivity,
                        "Submited Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(
                        this@GetNameNumberActivity,
                        MainActivity::class.java
                    )
                    progressDialog.show()
                    startActivity(intent)
                    finishAffinity()
                }
        }
    }
}