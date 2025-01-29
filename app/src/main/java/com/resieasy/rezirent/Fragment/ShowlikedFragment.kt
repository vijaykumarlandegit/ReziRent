package com.resieasy.rezirent.Fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.resieasy.rezirent.Adapter.LikeAdapter
import com.resieasy.rezirent.Class.LikeClass
import com.resieasy.rezirent.ViewModel.MainActivityViewModel
import com.resieasy.rezirent.databinding.FragmentShowlikedBinding

class ShowlikedFragment : Fragment() {
    var binding: FragmentShowlikedBinding? = null

    var list: ArrayList<LikeClass?> = ArrayList()
    var likeAdapter: LikeAdapter? = null
    var dialog: ProgressDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShowlikedBinding.inflate(inflater, container, false)

         likeAdapter = LikeAdapter(context, list)
        binding!!.likerec.adapter = likeAdapter
        val layoutManager = LinearLayoutManager(context)
        binding!!.likerec.layoutManager = layoutManager


        dialog = ProgressDialog(context)
        dialog!!.setMessage("Fetching Data . . . .")
        dialog!!.setCancelable(false)

        binding!!.progressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("Like")
            .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
            .orderBy("time", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    binding!!.likerec.visibility = View.VISIBLE
                    binding!!.adddataframe.visibility = View.GONE
                    binding!!.progressBar.visibility = View.GONE

                    list.clear()
                    val snapshotList = queryDocumentSnapshots.documents
                    for (data in snapshotList) {
                        val data1 = data.toObject(LikeClass::class.java)
                        list.add(data1)
                    }
                    likeAdapter!!.notifyDataSetChanged()
                } else {
                    binding!!.adddataframe.visibility = View.VISIBLE
                    binding!!.progressBar.visibility = View.GONE

                    Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Something is wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding!!.showrentswip.setOnRefreshListener {
            FirebaseFirestore.getInstance().collection("Like")
                .document(FirebaseAuth.getInstance().uid!!).collection("Nanded")
                .orderBy("time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        binding!!.likerec.visibility = View.VISIBLE
                        binding!!.adddataframe.visibility = View.GONE
                        binding!!.progressBar.visibility = View.GONE

                        list.clear()
                        val snapshotList = queryDocumentSnapshots.documents
                        for (data in snapshotList) {
                            val data1 = data.toObject(LikeClass::class.java)
                            list.add(data1)
                        }
                        likeAdapter!!.notifyDataSetChanged()
                        Toast.makeText(context, "Data Refresh", Toast.LENGTH_SHORT).show()
                    } else {
                        binding!!.adddataframe.visibility = View.VISIBLE
                        binding!!.progressBar.visibility = View.GONE

                        Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Something is wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            binding!!.showrentswip.isRefreshing = false
        }






        return binding!!.root
    }
}