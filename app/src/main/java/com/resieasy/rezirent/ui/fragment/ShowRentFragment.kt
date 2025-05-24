package com.resieasy.rezirent.ui.fragment

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
import com.resieasy.rezirent.ui.adapter.ResiShowOwnerAdapter
import com.resieasy.rezirent.data.remote.firebase.SingleIDClass
import com.resieasy.rezirent.databinding.FragmentShowRentBinding
import com.resieasy.rezirent.ui.activity.AddResidencyActivity

class ShowRentFragment : Fragment() {
    var binding: FragmentShowRentBinding? = null


    var dialog: ProgressDialog? = null


    var list: ArrayList<SingleIDClass?> = ArrayList()
    var adapter12: ResiShowOwnerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowRentBinding.inflate(inflater, container, false)


        adapter12 = ResiShowOwnerAdapter(list, context)
        binding!!.showrentrec.adapter = adapter12
        val manager = LinearLayoutManager(context)
        binding!!.showrentrec.layoutManager = manager



        dialog = ProgressDialog(context)
        dialog!!.setMessage("Fetching Data . . . .")
        dialog!!.setCancelable(false)

        binding!!.progressBar.visibility = View.VISIBLE

        val query = FirebaseFirestore.getInstance().collection("OwnResi").document(
            FirebaseAuth.getInstance().uid!!
        )
            .collection("Nanded")
            .whereEqualTo("type", "Rent")
            .orderBy("time", Query.Direction.DESCENDING)


        query.get().addOnSuccessListener { queryDocumentSnapshots ->
            if (!queryDocumentSnapshots.isEmpty) {
                binding!!.showrentswip.visibility = View.VISIBLE
                binding!!.adddataframe.visibility = View.GONE
                binding!!.progressBar.visibility = View.GONE

                list.clear()
                for (data in queryDocumentSnapshots.documents) {
                    val data1 = data.toObject(SingleIDClass::class.java)
                    list.add(data1)
                }
                adapter12!!.notifyDataSetChanged()
                dialog!!.dismiss()
            } else {
                binding!!.adddataframe.visibility = View.VISIBLE
                binding!!.progressBar.visibility = View.GONE

                Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }








        binding!!.showrentswip.setOnRefreshListener {
            val query = FirebaseFirestore.getInstance().collection("OwnResi").document(
                FirebaseAuth.getInstance().uid!!
            )
                .collection("Nanded")
                .whereEqualTo("type", "Rent")
                .orderBy("time", Query.Direction.DESCENDING)
            query.get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    binding!!.showrentswip.visibility = View.VISIBLE
                    binding!!.adddataframe.visibility = View.GONE
                    binding!!.progressBar.visibility = View.GONE

                    list.clear()
                    val snapshotList = queryDocumentSnapshots.documents
                    for (snapshot in snapshotList) {
                        val list01 = snapshot.toObject(
                            SingleIDClass::class.java
                        )
                        list.add(list01)
                    }
                    adapter12!!.notifyDataSetChanged()
                    Toast.makeText(context, "Refresh Data", Toast.LENGTH_SHORT).show()
                } else {
                    binding!!.adddataframe.visibility = View.VISIBLE
                    binding!!.progressBar.visibility = View.GONE

                    Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
                }
            }
            binding!!.showrentswip.isRefreshing = false
        }


        binding!!.adddataimage.setOnClickListener {
            val intent = Intent(activity, AddResidencyActivity::class.java)
            startActivity(intent)
        }


        return binding!!.root
    }
}