package com.resieasy.rezirent.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.resieasy.rezirent.Activity.AddSellResiActivity
import com.resieasy.rezirent.Adapter.ShowSellOwnAdapter
import com.resieasy.rezirent.Class.SingleIDClass
import com.resieasy.rezirent.databinding.FragmentShowSellBinding

class ShowSellFragment : Fragment() {
    var binding: FragmentShowSellBinding? = null


    var list123: ArrayList<SingleIDClass?> = ArrayList()
    var adapter123: ShowSellOwnAdapter? = null

    var dialog: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShowSellBinding.inflate(inflater, container, false)

        dialog = ProgressDialog(context)
        dialog!!.setMessage("Fetching Data . . . .")
        dialog!!.setCancelable(false)

        binding!!.progressBar.visibility = View.VISIBLE



        adapter123 = ShowSellOwnAdapter(context, list123)
        binding!!.showsellrec.adapter = adapter123
        val manager = LinearLayoutManager(context)
        binding!!.showsellrec.layoutManager = manager

        val auth1 = FirebaseAuth.getInstance()
        val userid11 = auth1.uid

        val query = FirebaseFirestore.getInstance().collection("OwnResi").document(
            userid11!!
        )
            .collection("Nanded")
            .whereEqualTo("type", "Sell")
            .orderBy("time", Query.Direction.DESCENDING)

        query.get().addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                binding!!.showsellswip.visibility = View.VISIBLE
                binding!!.adddataframe.visibility = View.GONE
                binding!!.progressBar.visibility = View.GONE
                list123.clear()
                val snapshotList = queryDocumentSnapshots.documents
                for (snapshot in snapshotList) {
                    val list01 = snapshot.toObject(SingleIDClass::class.java)
                    list123.add(list01)
                }
                adapter123!!.notifyDataSetChanged()
            } else {
                binding!!.progressBar.visibility = View.GONE
                binding!!.adddataframe.visibility = View.VISIBLE
                Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }

        binding!!.showsellswip.setOnRefreshListener {
            val query = FirebaseFirestore.getInstance().collection("OwnResi").document(
                userid11
            )
                .collection("Nanded")
                .whereEqualTo("type", "Sell")
            //.orderBy("time", Query.Direction.DESCENDING);
            query.get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    binding!!.showsellswip.visibility = View.VISIBLE
                    binding!!.adddataframe.visibility = View.GONE
                    binding!!.progressBar.visibility = View.GONE

                    list123.clear()
                    val snapshotList = queryDocumentSnapshots.documents
                    for (snapshot in snapshotList) {
                        val list01 = snapshot.toObject(
                            SingleIDClass::class.java
                        )
                        list123.add(list01)
                    }
                    adapter123!!.notifyDataSetChanged()
                    Toast.makeText(context, "Refresh Data", Toast.LENGTH_SHORT).show()
                } else {
                    binding!!.adddataframe.visibility = View.VISIBLE
                    binding!!.progressBar.visibility = View.GONE

                    Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
                }
            }
            binding!!.showsellswip.isRefreshing = false
        }
        binding!!.adddataimage.setOnClickListener {
            val intent = Intent(activity, AddSellResiActivity::class.java)
            startActivity(intent)
        }

        return binding!!.root
    }
}