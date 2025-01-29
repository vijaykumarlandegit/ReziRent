package com.resieasy.rezirent.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Adapter.BothResiiAdapter
import com.resieasy.rezirent.Class.BothResiClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityBothResiiBinding

class BothResiiActivity : AppCompatActivity() {
    var binding: ActivityBothResiiBinding? = null

    var list: ArrayList<BothResiClass?> = ArrayList()
    var adapter12: BothResiiAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBothResiiBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        adapter12 = BothResiiAdapter(list, this@BothResiiActivity)
        binding!!.bothrec.adapter = adapter12
        val manager = LinearLayoutManager(this@BothResiiActivity)
        binding!!.bothrec.layoutManager = manager


        val querytype = intent.getStringExtra("topquery")


        //All,Rent,Sell,Hostel
        if (querytype == "All") {
            binding!!.bothshimmer.visibility = View.VISIBLE
            binding!!.bothshimmer.startShimmer()
            Toast.makeText(this, "All Residency", Toast.LENGTH_SHORT).show()
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active")
                .get().addOnSuccessListener { queryDocumentSnapshots ->
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(BothResiClass::class.java)
                        list.add(data1)
                    }
                    adapter12!!.notifyDataSetChanged()
                    // dialog.dismiss();
                    binding!!.bothshimmer.visibility = View.GONE
                    binding!!.bothshimmer.stopShimmer()
                }
        }
        if (querytype == "Rent") {
            binding!!.bothshimmer.startShimmer()
            binding!!.bothshimmer.visibility = View.VISIBLE

            Toast.makeText(this, "Rental Residency", Toast.LENGTH_SHORT).show()
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("rtype", "Rent")
                .whereEqualTo("status", "Active")
                .get().addOnSuccessListener { queryDocumentSnapshots ->
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(BothResiClass::class.java)
                        list.add(data1)
                    }
                    adapter12!!.notifyDataSetChanged()

                    binding!!.bothshimmer.visibility = View.GONE
                    binding!!.bothshimmer.stopShimmer()
                }
        }
        if (querytype == "Sell") {
            binding!!.bothshimmer.startShimmer()
            binding!!.bothshimmer.visibility = View.VISIBLE

            Toast.makeText(this, "Property On Sell", Toast.LENGTH_SHORT).show()
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("rtype", "Sell")
                .whereEqualTo("status", "Active").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(BothResiClass::class.java)
                        list.add(data1)
                    }
                    adapter12!!.notifyDataSetChanged()
                    binding!!.bothshimmer.visibility = View.GONE
                    binding!!.bothshimmer.stopShimmer()
                }
        }
        if (querytype == "Hostel") {
            binding!!.bothshimmer.startShimmer()
            binding!!.bothshimmer.visibility = View.VISIBLE

            Toast.makeText(this, "Cot-Base Residency", Toast.LENGTH_SHORT).show()
            FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("rtype", "Hostel")
                .whereEqualTo("status", "Active").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(BothResiClass::class.java)
                        list.add(data1)
                    }
                    adapter12!!.notifyDataSetChanged()
                    binding!!.bothshimmer.visibility = View.GONE
                    binding!!.bothshimmer.stopShimmer()
                }
        }

        binding!!.showrentswip.setOnRefreshListener {
            if (querytype == "All") {
                Toast.makeText(
                    this@BothResiiActivity,
                    "All Residency",
                    Toast.LENGTH_SHORT
                ).show()
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("status", "Active")
                    .get().addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                    }
            }
            if (querytype == "Rent") {
                Toast.makeText(this@BothResiiActivity, "Rent", Toast.LENGTH_SHORT)
                    .show()
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Rent")
                    .whereEqualTo("status", "Active")
                    .get().addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                    }
            }
            if (querytype == "Sell") {
                Toast.makeText(this@BothResiiActivity, "Sell", Toast.LENGTH_SHORT)
                    .show()
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Sell")
                    .whereEqualTo("status", "Active").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                    }
            }
            if (querytype == "Hostel") {
                Toast.makeText(this@BothResiiActivity, "Hostel", Toast.LENGTH_SHORT)
                    .show()
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Hostel")
                    .whereEqualTo("status", "Active").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                    }
            }

            binding!!.showrentswip.isRefreshing = false
            Toast.makeText(this@BothResiiActivity, "Data Refresh", Toast.LENGTH_SHORT)
                .show()
        }


        binding!!.searchview1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                val querySearch =
                    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                        .collection("AllData") //.whereEqualTo("status", "Active")
                        .orderBy("lowercase").startAt(text)

                querySearch.get().addOnSuccessListener { queryDocumentSnapshots ->
                    list.clear()
                    for (data in queryDocumentSnapshots.documents) {
                        val data1 = data.toObject(BothResiClass::class.java)
                        val status = data1!!.status
                        if (status == "Active") {
                            list.add(data1)
                        }
                    }
                    adapter12!!.notifyDataSetChanged()
                }
                return false
            }
        })

        binding!!.homebottom.setOnClickListener {
            val intent = Intent(this@BothResiiActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding!!.sortbtn.setOnClickListener {
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val drent: LinearLayout
            val dsell: LinearLayout
            val dhostel: LinearLayout
            val dall: LinearLayout


            val builder =
                AlertDialog.Builder(this@BothResiiActivity)
            val view = LayoutInflater.from(this@BothResiiActivity)
                .inflate(R.layout.bothsortdialog, viewGroup, false)
            builder.setCancelable(true)
            builder.setView(view)

            drent = view.findViewById(R.id.rentsdialog)
            dsell = view.findViewById(R.id.sellsdialog)
            dhostel = view.findViewById(R.id.hoostelsdialog)
            dall = view.findViewById(R.id.allsdialog)


            val alertDialog1 = builder.create()
            alertDialog1.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            drent.setOnClickListener {
                alertDialog1.dismiss()
                binding!!.bothshimmer.startShimmer()
                binding!!.bothshimmer.visibility = View.VISIBLE
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Rent")
                    .whereEqualTo("status", "Active").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                        binding!!.bothshimmer.visibility = View.GONE
                        binding!!.bothshimmer.stopShimmer()
                    }
            }
            dsell.setOnClickListener {
                alertDialog1.dismiss()
                binding!!.bothshimmer.startShimmer()
                binding!!.bothshimmer.visibility = View.VISIBLE
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Sell")
                    .whereEqualTo("status", "Active").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                        binding!!.bothshimmer.visibility = View.GONE
                        binding!!.bothshimmer.stopShimmer()
                    }
            }
            dhostel.setOnClickListener {
                alertDialog1.dismiss()
                binding!!.bothshimmer.startShimmer()
                binding!!.bothshimmer.visibility = View.VISIBLE
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("rtype", "Hostel")
                    .whereEqualTo("status", "Active").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                        binding!!.bothshimmer.visibility = View.GONE
                        binding!!.bothshimmer.stopShimmer()
                    }
            }
            dall.setOnClickListener {
                alertDialog1.dismiss()
                binding!!.bothshimmer.startShimmer()
                binding!!.bothshimmer.visibility = View.VISIBLE
                FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                    .collection("AllData").whereEqualTo("status", "Active")
                    .get().addOnSuccessListener { queryDocumentSnapshots ->
                        list.clear()
                        for (data in queryDocumentSnapshots.documents) {
                            val data1 = data.toObject(
                                BothResiClass::class.java
                            )
                            list.add(data1)
                        }
                        adapter12!!.notifyDataSetChanged()
                        binding!!.bothshimmer.visibility = View.GONE
                        binding!!.bothshimmer.stopShimmer()
                    }
            }
            alertDialog1.show()
        }
    }
}