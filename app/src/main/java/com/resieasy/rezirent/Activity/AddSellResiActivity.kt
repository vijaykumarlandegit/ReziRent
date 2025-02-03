package com.resieasy.rezirent.Activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.resieasy.rezirent.Adapter.MultipleImageAdapter
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.Class.SingleIDClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityAddSellResiBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddSellResiActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
   lateinit var binding: ActivityAddSellResiBinding 
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    lateinit var rentaltype: Array<String>
    lateinit var areatype: Array<String>

    
    var `in`: Int = 0
    var hashMap: HashMap<String, String>? = null


    var multipleImageAdapter: MultipleImageAdapter? = null
    var dialog: ProgressDialog? = null
    var dialog1: ProgressDialog? = null


    private var upload_count = 0

    var newlist: ArrayList<Uri?> = ArrayList()
    var newuri: Uri? = null
    var mainum: Int = 0

    val newStrings = ArrayList<String>()

    
    var type: String? = null
    var subtype: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSellResiBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)


        dialog = ProgressDialog(this)
        dialog!!.setTitle("Creating Account")
        dialog!!.setMessage("Please wait, we are creating your property account")
        dialog!!.setCancelable(false)

        hashMap = HashMap()

        dialog1 = ProgressDialog(this)
        dialog1!!.setMessage("Fetching current location....")
        dialog1!!.setCancelable(false)





        binding.justrehds.setOnClickListener { }


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Sell,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selltype.adapter = adapter
        binding.selltype.onItemSelectedListener =
            this@AddSellResiActivity

        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Area,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areatype.adapter = adapter1
        binding.areatype.onItemSelectedListener =
            this@AddSellResiActivity


        multipleImageAdapter = MultipleImageAdapter(newlist)
        binding.multiimagerec.layoutManager =
            GridLayoutManager(this@AddSellResiActivity, 3)
        binding.multiimagerec.adapter = multipleImageAdapter

        binding.selltype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                if (item.toString() == "Flat") {
                    binding.flatview1.visibility = View.VISIBLE
                    binding.roomview1.visibility = View.GONE
                } else if (item.toString() == "Room") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.VISIBLE
                } else {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.opengallerybtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE)
            binding.currentimageview.visibility = View.VISIBLE
        }
        binding.capturelocation2.setOnClickListener {
            checkpermission()
            dialog1!!.show()
        }
        binding.back.setOnClickListener { finish() }
        binding.mapview.setOnClickListener {
            val intent = Intent(this@AddSellResiActivity, MapsActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", "Your residency name will fetch here")
            startActivity(intent)
        }
        binding.submitresibtn.setOnClickListener {
            dialog!!.show()
            val type = binding.selltype.selectedItem.toString()



            if (type == "Flat") {
                binding.flatview1.visibility = View.VISIBLE
                val ID01 = binding.flatview1.checkedRadioButtonId
                val radioButton01 = findViewById<RadioButton>(ID01)

                if (radioButton01.text == "1RK") {
                    subtype = "1RK"
                }
                if (radioButton01.text == "1BHK") {
                    subtype = "1BHK"
                }
                if (radioButton01.text == "2BHK") {
                    subtype = "2BHK"
                }
                if (radioButton01.text == "3BHK") {
                    subtype = "3BHK"
                }
                if (radioButton01.text == "4BHK") {
                    subtype = "4BHK"
                }
                if (radioButton01.text == "5BHK") {
                    subtype = "5BHK"
                }
            } else if (type == "Room") {
                binding.roomview1.visibility = View.VISIBLE
                val ID02 = binding.roomview1.checkedRadioButtonId
                val radioButton02 = findViewById<RadioButton>(ID02)

                if (radioButton02.text == "Single Room") {
                    subtype = "Single Room"
                }
                if (radioButton02.text == "Double Room") {
                    subtype = "Double Room"
                }
                if (radioButton02.text == "Triple Room") {
                    subtype = "Triple Room"
                }
            } else if (type == "Stutter") {
                subtype = "Shutter"
            } else if (type == "House") {
                subtype = "House"
            } else if (type == "Building") {
                subtype = "Building"
            } else if (type == "Land") {
                subtype = "Land"
            }


            val nametext = binding.resiname.text.toString()
            val addresstext = binding.resiaddress.text.toString()
             val onametext = binding.oname.text.toString()
            val contacttext = binding.contact.text.toString()
            val whatsapptext = binding.whatsapp.text.toString()
            if (nametext.isNotEmpty() && addresstext.isNotEmpty() && onametext.isNotEmpty() && contacttext.isNotEmpty() && whatsapptext.isNotEmpty()) {
                if (newlist.isEmpty()) {
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please select images",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (newlist.isNotEmpty()) {
                    if (newlist.size < 11) {
                        mainum = newlist.size
                        binding.numbertext.text =
                            "If Loading Takes to long press button again"
                        val ImageFolder = FirebaseStorage.getInstance().reference.child("Nanded")
                            .child(FirebaseAuth.getInstance().uid!!)
                            .child("SellImage")

                        upload_count = 0
                        while (upload_count < newlist.size) {
                            val IndividualImage = newlist[upload_count]
                            val ImageName =
                                ImageFolder.child("Images" + IndividualImage!!.lastPathSegment)


                            var bmp: Bitmap? = null
                            try {
                                bmp = MediaStore.Images.Media.getBitmap(
                                    contentResolver,
                                    IndividualImage
                                )
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val baos = ByteArrayOutputStream()
                            bmp!!.compress(Bitmap.CompressFormat.JPEG, 40, baos)
                            val data = baos.toByteArray()
                            val uploadTask2 = ImageName.putBytes(data)

                            uploadTask2.addOnSuccessListener {
                                ImageName.downloadUrl.addOnSuccessListener { uri ->
                                    newStrings.add(uri.toString())
                                    if (newStrings.size == newlist.size) {
                                        storeLink(newStrings, mainum, type, subtype)
                                    }
                                }
                            }


                            upload_count++
                        }
                    } else {
                        Toast.makeText(
                            this@AddSellResiActivity,
                            "Please don't select more than 10 images",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog!!.dismiss()
                    }
                }
            } else {
                if (nametext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.resiname.error = "Please enter residency name"
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please enter residency name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (addresstext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.resiaddress.error = "Please enter residency address"
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please enter residency address",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (onametext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.oname.error = "Please enter residency operator name"
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please enter residency operator name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (contacttext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.contact.error = "Please enter contact number"
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please enter contact number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (whatsapptext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.whatsapp.error = "Please enter whatsapp number"
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Please enter whatsapp number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun storeLink(
        newStrings: ArrayList<String>,
        mainum: Int,
        typex: String,
        subtypex: String?
    ) {
        val hashMap = HashMap<String, String>()

        var i = 0
        while (i < newlist.size) {
            hashMap["image$i"] = newStrings[i]

            i++
        }

        val name = binding.resiname.text.toString()
        val address = binding.resiaddress.text.toString()
        val area = binding.areatype.selectedItem.toString()
        val oname = binding.oname.text.toString()
        val contact = binding.contact.text.toString()
        val whatsapp = binding.whatsapp.text.toString()
        val mail = binding.email.text.toString()
        val rent = binding.rentamount.text.toString()
        val erent = binding.explainrent.text.toString()
        val more = binding.moredetails.text.toString()
        val size = binding.propertysize.text.toString()
        val userid1 = FirebaseAuth.getInstance().uid


        val toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage")
        val toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData")
        val toolsCollectionRef3 = FirebaseFirestore.getInstance().collection("OwnResi")
            .document(userid1!!).collection("Nanded")
        val newDocID = toolsCollectionRef.document().id

        toolsCollectionRef.document(newDocID).set(hashMap).addOnSuccessListener {
            val date = Date()
            val data = SellResiClass(
                "Active",
                "Sell",
                typex,
                subtypex,
                name,
                name.lowercase(Locale.getDefault()),
                address,
                area,
                oname,
                contact,
                whatsapp,
                mail,
                rent,
                erent,
                more,
                FirebaseAuth.getInstance().uid,
                newDocID,
                size,
                "",
                "",
                "",
                7028,
                mainum,
                19.114591,
                77.291456,
                date.time
            )
            toolsCollectionRef2.document(newDocID).set(data).addOnSuccessListener {
                val myresi = SingleIDClass(
                    newDocID,
                    "Sell",
                    "Nanded",
                    "",
                    "",
                    7028,
                    Date().time
                )
                toolsCollectionRef3.document(newDocID).set(myresi).addOnSuccessListener {
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Data Uploaded Successfully, please refresh",
                        Toast.LENGTH_LONG
                    ).show()
                    dialog!!.dismiss()
                    finish()
                }.addOnFailureListener { e ->
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@AddSellResiActivity,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
                dialog!!.dismiss()
            }
        }.addOnFailureListener { e ->
            dialog!!.dismiss()
            Toast.makeText(
                this@AddSellResiActivity,
                "" + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.numbertext.text = "Uploaded Successfully"

        newlist.clear()
    }

    private fun checkpermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            userLocation
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }

    private val userLocation: Unit
        get() {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

                return
            }
            val task =
                fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    binding.locationview.visibility = View.VISIBLE
                    dialog1!!.dismiss()
                    Toast.makeText(
                        this@AddSellResiActivity,
                        "Current location fetch successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    latitude = location.latitude
                    longitude = location.longitude
                    binding.showlocationtext.text = "Latitude: $latitude & Longitude: $longitude"


                    //LatLng usercl = new LatLng(latitude, longitude);
                } else {
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Accepted", Toast.LENGTH_SHORT).show()
                userLocation
            } else {
                Toast.makeText(this, "Permission Rejected", Toast.LENGTH_SHORT).show()
                dialog1!!.dismiss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.clipData != null) {
                        val x = data.clipData!!.itemCount

                        for (i in 0 until x) {
                            newuri = data.clipData!!.getItemAt(i).uri
                            newlist.add(newuri)
                        }
                        multipleImageAdapter!!.notifyDataSetChanged()
                        binding.numbertext.text = "You have select " + newlist.size + " images"
                    }
                }
            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val rentaltype = parent.getItemAtPosition(position).toString()
        val areatype = parent.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    companion object {
        //new
        private const val PICK_IMAGE = 1
    }   private fun toast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}

