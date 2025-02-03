package com.resieasy.rezirent.Activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.resieasy.rezirent.Adapter.LeadsAdapter
import com.resieasy.rezirent.Class.LeadClass
import com.resieasy.rezirent.databinding.ActivityLeadShowBinding

class LeadShowActivity : AppCompatActivity() {
   
    private lateinit var binding: ActivityLeadShowBinding
    private lateinit var leadsAdapter: LeadsAdapter
    private val list = ArrayList<LeadClass>()
    private var count: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeadShowBinding.inflate(layoutInflater)



        setContentView(binding.root)
         leadsAdapter = LeadsAdapter(this, list)
        binding.leadsrec.adapter = leadsAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.leadsrec.layoutManager = layoutManager

        binding.leadshimmer.visibility = View.VISIBLE
        binding.leadshimmer.startShimmer()

        FirebaseFirestore.getInstance().collection("Lead")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("Nanded").orderBy("time", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    binding.leadshimmer.visibility = View.GONE
                    binding.leadshimmer.stopShimmer()
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(LeadClass::class.java)
                        if (data1 != null) {
                            list.add(data1)
                        }
                    }

                    leadsAdapter.notifyDataSetChanged()
                    count = queryDocumentSnapshots.size().toString()

                    binding.leadcount.text = count
                } else {
                    binding.leadshimmer.visibility = View.GONE
                    binding.leadshimmer.stopShimmer()
                    Toast.makeText(this@LeadShowActivity, "Leads not available", Toast.LENGTH_SHORT).show()
                }
            }

        binding.back.setOnClickListener { finish() }
    }
}