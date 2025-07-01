package com.resieasy.rezirent.ui.activity

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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.ui.adapter.BothResiiAdapter
import com.resieasy.rezirent.data.remote.firebase.BothResiClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.data.remote.firebase.UnifiedResidencyClass

import com.resieasy.rezirent.databinding.ActivityBothResiiBinding
import com.resieasy.rezirent.ui.viewmodel.remote.BothActivityViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.FacilityViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowHostelViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowResiViewModel
import com.resieasy.rezirent.ui.viewmodel.remote.ShowSellViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BothResiiActivity : AppCompatActivity() {
    lateinit var binding: ActivityBothResiiBinding

    var list: ArrayList<UnifiedResidencyClass?> = ArrayList()
    lateinit var adapter12: BothResiiAdapter

    private val showResiViewModel: ShowResiViewModel by viewModels()
    private val showSellViewModel: ShowSellViewModel by viewModels()
    private val showHostelViewModel: ShowHostelViewModel by viewModels()
    private val facilityViewModel: FacilityViewModel by viewModels()
    private  val bothActivityViewModel: BothActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBothResiiBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // adapter12 = BothResiiAdapter(list, this@BothResiiActivity,showResiViewModel,showSellViewModel,showHostelViewModel,facilityViewModel)
        adapter12 = BothResiiAdapter(list, this@BothResiiActivity,facilityViewModel)
        binding.bothrec.adapter = adapter12
        val manager = LinearLayoutManager(this@BothResiiActivity)
        binding.bothrec.layoutManager = manager


        val querytype = intent.getStringExtra("topquery")

        binding.bothrec.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager=recyclerView.layoutManager as LinearLayoutManager
                val lastPosition=layoutManager.findLastVisibleItemPosition()
                val totalItems=layoutManager.itemCount
                if(lastPosition>=totalItems-5){
                    bothActivityViewModel.fetchNextPage()
                }
            }
        })



        //All,Rent,Sell,Hostel
        if (querytype == "All") {
            binding.bothshimmer.visibility = View.VISIBLE
            binding.bothshimmer.startShimmer()
            Toast.makeText(this, "All Residency", Toast.LENGTH_SHORT).show()

            // 1. Observe first
            bothActivityViewModel.allData.observe(this) { data ->
                if (!data.isNullOrEmpty()) {
                    adapter12.updateList(ArrayList(data))
                    binding.bothshimmer.stopShimmer()
                    binding.bothshimmer.visibility = View.GONE
                }
            }

            // 2. Trigger fetch
            bothActivityViewModel.fetchNextPage()


        }

        if (querytype == "Rent" || querytype == "Sell" || querytype == "Hostel") {
            binding.bothshimmer.startShimmer()
            binding.bothshimmer.visibility = View.VISIBLE
            Toast.makeText(this, "$querytype Residency", Toast.LENGTH_SHORT).show()

            bothActivityViewModel.filteredData.observe(this@BothResiiActivity, Observer { data ->
                if(data!=null && data.isNotEmpty()){
                    adapter12.updateList(ArrayList(data))
                    binding.bothshimmer.visibility = View.GONE
                    binding.bothshimmer.stopShimmer()
                }
            })
            bothActivityViewModel.filterDataByType(querytype)

        }

        binding.showrentswip.setOnRefreshListener {
            Toast.makeText(this, "$querytype Refreshing", Toast.LENGTH_SHORT).show()

            if (querytype == "All") {
                // Step 1: Observe once, at setup
                bothActivityViewModel.allData.observe(this) { data ->
                    adapter12.updateList(ArrayList(data))
                }

               // Step 2: Trigger logic as needed
                bothActivityViewModel.resetAll()
                bothActivityViewModel.fetchNextPage()


            }

            if (querytype == "Rent" || querytype == "Sell" || querytype == "Hostel") {
                bothActivityViewModel.filteredData.observe(this) { data ->
                    if (!data.isNullOrEmpty()) {
                        adapter12.updateList(ArrayList(data))
                    }
                }
                bothActivityViewModel.filterDataByType(querytype)
            }

            binding.showrentswip.isRefreshing = false
            Toast.makeText(this, "Data Refreshed", Toast.LENGTH_SHORT).show()
        }



        binding.searchview1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
                        val data1 = data.toObject(UnifiedResidencyClass::class.java)
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

        binding.homebottom.setOnClickListener {
            val intent = Intent(this@BothResiiActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.sortbtn.setOnClickListener {
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
                binding.bothshimmer.startShimmer()
                binding.bothshimmer.visibility = View.VISIBLE

                bothActivityViewModel.filteredData.observe(this, Observer { data ->
                    if(data!=null && data.isNotEmpty()){
                        adapter12.updateList(ArrayList(data))
                        binding.bothshimmer.visibility = View.GONE
                        binding.bothshimmer.stopShimmer()
                    }
                })
                bothActivityViewModel.filterDataByType("Rent")

            }
            dsell.setOnClickListener {
                alertDialog1.dismiss()
                binding.bothshimmer.startShimmer()
                binding.bothshimmer.visibility = View.VISIBLE
                bothActivityViewModel.filteredData.observe(this, Observer { data ->
                    if(data!=null && data.isNotEmpty()){
                        adapter12.updateList(ArrayList(data))
                        binding.bothshimmer.visibility = View.GONE
                        binding.bothshimmer.stopShimmer()
                    }
                })
                bothActivityViewModel.filterDataByType("Sell")

            }
            dhostel.setOnClickListener {
                alertDialog1.dismiss()
                binding.bothshimmer.startShimmer()
                binding.bothshimmer.visibility = View.VISIBLE
                bothActivityViewModel.filteredData.observe(this, Observer { data ->
                    if(data!=null && data.isNotEmpty()){
                        adapter12.updateList(ArrayList(data))
                        binding.bothshimmer.visibility = View.GONE
                        binding.bothshimmer.stopShimmer()
                    }
                })
                bothActivityViewModel.filterDataByType("Hostel")

            }
            dall.setOnClickListener {
                alertDialog1.dismiss()
                binding.bothshimmer.startShimmer()
                binding.bothshimmer.visibility = View.VISIBLE
                bothActivityViewModel.allData.observe(this, Observer { data ->
                    if(data!=null && data.isNotEmpty()){
                        adapter12.updateList(ArrayList(data))
                        binding.bothshimmer.visibility = View.GONE
                        binding.bothshimmer.stopShimmer()
                    }
                })
               // bothActivityViewModel.fetchAllData()

            }
            alertDialog1.show()
        }
    }


}