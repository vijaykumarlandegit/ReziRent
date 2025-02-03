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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.resieasy.rezirent.Adapter.MultioldImageAdapter
import com.resieasy.rezirent.Adapter.MultipleImageAdapter
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.ViewModel.ShowResiViewModel
import com.resieasy.rezirent.ViewModel.ShowSellViewModel
import com.resieasy.rezirent.databinding.ActivityEditSellDataBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditSellDataActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: ActivityEditSellDataBinding
    private val oldlist = ArrayList<Uri?>()
    private val newlist = ArrayList<Uri?>()
    private val newStrings = ArrayList<String>()
    private val oldStrings = ArrayList<String>()

    private lateinit var rentaltype: Array<String>
    private lateinit var areatype: Array<String>
    private lateinit var multipleImageAdapter: MultipleImageAdapter
    private lateinit var multioldImageAdapter: MultioldImageAdapter

    private var PICK_IMAGE = 123
    private var policy: String = ""
    private var period = 0
    private var upload_count = 0
    private var inumber = 0
    private var mainum = 0

    private var lat = 0.0
    private var lan = 0.0
    private var latitude = 0.0
    private var longitude = 0.0
    var oldlatitude :Double? = null
    var oldlongitude :Double? = null
    var newuri: Uri? = null
    var olduri: Uri? = null
     private var type: String? = null
    private var subtype: String? = null
    private var getsubtype: String? = null
    private var name: String = ""
    private var status: String? = null
    var item: Any? = null
    var item2: Any? = null
    private val showSellViewModel: ShowSellViewModel by viewModels()

    private var dialog: ProgressDialog? = null
    private var dialog1: ProgressDialog? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSellDataBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)


      val  id = intent.getStringExtra("id") ?: ""
     val currentUserId=FirebaseAuth.getInstance().uid



        dialog1 = ProgressDialog(this)
        dialog1!!.setMessage("Fetching current location....")
        dialog1!!.setCancelable(false)

        dialog = ProgressDialog(this@EditSellDataActivity)
        dialog!!.setMessage("Uploading Images please Wait.........!!!!!!")
        dialog!!.setCancelable(false)


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Sell1,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.rentaltype.adapter = adapter
        binding.rentaltype.onItemSelectedListener =
            this@EditSellDataActivity

        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Area1,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areatype.adapter = adapter1
        binding.areatype.onItemSelectedListener =
            this@EditSellDataActivity


        multipleImageAdapter = MultipleImageAdapter(newlist)
        binding.multiimagerec.layoutManager =
            GridLayoutManager(this@EditSellDataActivity, 3)
        binding.multiimagerec.adapter = multipleImageAdapter

        multioldImageAdapter = MultioldImageAdapter(oldlist)
        binding.multiimagerec2.layoutManager =
            GridLayoutManager(this@EditSellDataActivity, 3)
        binding.multiimagerec2.adapter = multioldImageAdapter






        showSellViewModel.getSellData(id)
        lifecycleScope.launchWhenStarted {
            showSellViewModel.data.collect {
                it?.let { documentSnapshot->
                    inumber = documentSnapshot.input
                    oldlatitude = documentSnapshot.latitude
                    oldlongitude = documentSnapshot.longitude

                    val type = documentSnapshot.type

                    val idd = documentSnapshot.id
                    subtype = documentSnapshot.subtype
                    val area = documentSnapshot.area
                    name = documentSnapshot.name
                    status = documentSnapshot.status
                    val address = documentSnapshot.address
                    val oname = documentSnapshot.oname
                    val number = documentSnapshot.number
                    val whatsapp = documentSnapshot.whatsapp
                    val mail = documentSnapshot.mail
                    val prize = documentSnapshot.prize
                    val eprize = documentSnapshot.eprize
                    val more = documentSnapshot.more
                    val size = documentSnapshot.size



                    binding.viewresitypetext.text = type
                    binding.vieareatext.text = area
                    binding.resiaddress.setText(address)
                    binding.resiname.setText(name)
                    binding.oname.setText(oname)
                    binding.contact.setText(number)
                    binding.whatsapp.setText(whatsapp)
                    binding.email.setText(mail)
                    binding.prizeamount.setText(prize)
                    binding.explainprize.setText(eprize)
                    binding.propertysize.setText(size)
                    binding.moredetails.setText(more)

                    binding.olshowlocationtext.text = "Latitude $oldlatitude And $oldlongitude"

                    FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllImage").document(idd!!).get()
                        .addOnSuccessListener { snapshot ->
                            for (i in 0 until inumber) {
                                val immm = snapshot.getString("image$i")

                                olduri = Uri.parse(snapshot.getString("image$i"))
                                oldlist.add(olduri)
                                if (immm != null) {
                                    oldStrings.add(immm)
                                }
                            }
                            multioldImageAdapter!!.notifyDataSetChanged()
                            binding.numbertext.text = "You have select " + oldlist.size + " images"
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@EditSellDataActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    if (type == "Flat") {
                        binding.flatview1.visibility = View.VISIBLE
                        binding.roomview1.visibility = View.GONE
                        if (getsubtype == "1RK") {
                            binding.rk1.isChecked = true
                        }
                        if (getsubtype == "1BHK") {
                            binding.bhk1.isChecked = true
                        }
                        if (getsubtype == "2BHK") {
                            binding.bhk2.isChecked = true
                        }
                        if (getsubtype == "3BHK") {
                            binding.bhk3.isChecked = true
                        }
                    } else if (type == "Room") {
                        binding.flatview1.visibility = View.GONE
                        binding.roomview1.visibility = View.VISIBLE
                        if (getsubtype == "Single Room") {
                            binding.singleroom1.isChecked = true
                        }
                        if (getsubtype == "Double Room") {
                            binding.doubleroom1.isChecked = true
                        }
                        if (getsubtype == "Triple Room") {
                            binding.tripleroom1.isChecked = true
                        }
                    }
                }
            }
        }


        binding.rentaltype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                item = parent.getItemAtPosition(position)
                if (item.toString() == "Flat") {
                    binding.flatview1.visibility = View.VISIBLE
                    binding.roomview1.visibility = View.GONE
                    binding.viewresitypetext.visibility = View.GONE

                    binding.viewresitypetext.text = "Flat"
                } else if (item.toString() == "Room") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.VISIBLE
                    binding.viewresitypetext.visibility = View.GONE
                    binding.viewresitypetext.text = "Room"
                } else if (item.toString() == "Stutter") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.GONE
                    binding.viewresitypetext.visibility = View.GONE
                    binding.viewresitypetext.text = "Shutter"
                } else if (item.toString() == "House") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.GONE
                    binding.viewresitypetext.visibility = View.GONE
                    binding.viewresitypetext.text = "House"
                } else if (item.toString() == "Building") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.GONE
                    binding.viewresitypetext.visibility = View.GONE
                    binding.viewresitypetext.text = "Building"
                } else if (item.toString() == "Land") {
                    binding.flatview1.visibility = View.GONE
                    binding.roomview1.visibility = View.GONE
                    binding.viewresitypetext.visibility = View.GONE
                    binding.viewresitypetext.text = "Land"
                } else if (item.toString() == "") {
                    binding.viewresitypetext.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.areatype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                item2 = parent.getItemAtPosition(position)
                if (item2.toString() == "Anand Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Anand Nagar"
                } else if (item2.toString() == "Asarjan") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Asarjan"
                } else if (item2.toString() == "Ashok Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Ashok Nagar"
                } else if (item2.toString() == "Baba Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Baba Nagar"
                } else if (item2.toString() == "Bafna") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Bafna"
                } else if (item2.toString() == "Balirampur") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Balirampur"
                } else if (item2.toString() == "Bhagya Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Bhagya Nagar"
                } else if (item2.toString() == "Chaitanya Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Chaitanya Nagar"
                } else if (item2.toString() == "Chaufula") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Chaufula"
                } else if (item2.toString() == "CIDCO") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "CIDCO"
                } else if (item2.toString() == "Dhanegaon") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Dhanegaon"
                } else if (item2.toString() == "Farande Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Farande Nagar"
                } else if (item2.toString() == "Ganesh Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Ganesh Nagar"
                } else if (item2.toString() == "Gopalchiwadi") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Gopalchiwadi"
                } else if (item2.toString() == "Hanuman Gad") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Hanuman Gad"
                } else if (item2.toString() == "Harsh Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Harsh Nagar"
                } else if (item2.toString() == "Hingoli Gate") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Hingoli Gate"
                } else if (item2.toString() == "HUDCO") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "HUDCO"
                } else if (item2.toString() == "Hyder Bagh") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Hyder Bagh"
                } else if (item2.toString() == "Itwara") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Itwara"
                } else if (item2.toString() == "Kabra Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Kabra Nagar"
                } else if (item2.toString() == "Kala Mandir") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Kala Mandir"
                } else if (item2.toString() == "Kamtha Village") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Kamtha Village"
                } else if (item2.toString() == "Kautha") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Kautha"
                } else if (item2.toString() == "Kautha(New)") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Kautha(New)"
                } else if (item2.toString() == "Khadkpura") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Khadkpura"
                } else if (item2.toString() == "Labour Colony") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Labour Colony"
                } else if (item2.toString() == "Lokmitra Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Lokmitra Nagar"
                } else if (item2.toString() == "MIDC") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "MIDC"
                } else if (item2.toString() == "Mondha(New)") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Mondha(New)"
                } else if (item2.toString() == "Mondha(Old)") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Mondha(Old)"
                } else if (item2.toString() == "Mujampeth") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Mujampeth"
                } else if (item2.toString() == "Peer Burhan Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Peer Burhan Nagar"
                } else if (item2.toString() == "Ravi Nagar(Kautha)") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Ravi Nagar(Kautha)"
                } else if (item2.toString() == "Sarafa") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Sarafa"
                } else if (item2.toString() == "Shahu Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Shahu Nagar"
                } else if (item2.toString() == "Shivaji Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Shivaji Nagar"
                } else if (item2.toString() == "Shrawasti Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Shrawasti Nagar"
                } else if (item2.toString() == "Shri Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Shri Nagar"
                } else if (item2.toString() == "Shyam Nagar") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Shyam Nagar"
                } else if (item2.toString() == "Taroda bk") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Taroda bk"
                } else if (item2.toString() == "Taroda kh") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Taroda kh"
                } else if (item2.toString() == "Vadibudruk") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Vadibudruk"
                } else if (item2.toString() == "Vajirabad") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Vajirabad"
                } else if (item2.toString() == "Vasarani") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Vasarani"
                } else if (item2.toString() == "Vishnupuri") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Vishnupuri"
                } else if (item2.toString() == "Wajegaon") {
                    binding.vieareatext.visibility = View.GONE
                    binding.vieareatext.text = "Wajegaon"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        binding.submitresibtn.setOnClickListener {
            dialog!!.show()
            val type = binding.rentaltype.selectedItem.toString()


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
            } else if (type == "") {
                val ttypee = binding.viewresitypetext.text.toString()
                if (ttypee == "Flat") {
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
                } else if (ttypee == "Room") {
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
                } else if (ttypee == "Stutter") {
                    subtype = "Shutter"
                } else if (ttypee == "House") {
                    subtype = "House"
                } else if (ttypee == "Building") {
                    subtype = "Building"
                } else if (ttypee == "Land") {
                    subtype = "Land"
                }
            } else {
                subtype = getsubtype
            }


            val nametext = binding.resiname.text.toString()
            val addresstext = binding.resiaddress.text.toString()
            val onametext = binding.oname.text.toString()
            val contacttext = binding.contact.text.toString()
            val whatsapptext = binding.whatsapp.text.toString()
            if (!nametext.isEmpty() && !addresstext.isEmpty() && !onametext.isEmpty() && !contacttext.isEmpty() && !whatsapptext.isEmpty()) {
                if (newlist.isEmpty() && oldlist.isEmpty()) {
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Please select images",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!newlist.isEmpty() && oldlist.isEmpty()) {
                    if (newlist.size < 11) {
                        mainum = newlist.size
                        val ImageFolder = FirebaseStorage.getInstance().reference
                            .child("Nanded")
                            .child(currentUserId!!)
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
                                ImageName.downloadUrl.addOnSuccessListener { uri -> //String image=uri.toString();
                                    newStrings.add(uri.toString())
                                    if (newStrings.size == newlist.size) {
                                        storeLink(id, newStrings, mainum, type, subtype)
                                    }
                                }
                            }

                            upload_count++
                        }
                    } else {
                        Toast.makeText(
                            this@EditSellDataActivity,
                            "Please don't select more than 10 images",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog!!.dismiss()
                    }
                } else if (newlist.isEmpty() && !oldlist.isEmpty()) {
                    if (oldlist.size < 11) {
                        mainum = oldlist.size
                        storeLink(id, oldStrings, mainum, type, subtype)
                    } else {
                        Toast.makeText(
                            this@EditSellDataActivity,
                            "Please don't select more than 10 images",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog!!.dismiss()
                    }
                } else if (!newlist.isEmpty() && !oldlist.isEmpty()) {
                    val combine = newlist.size + oldlist.size

                    if (combine < 11) {
                        mainum = combine
                        val ImageFolder = FirebaseStorage.getInstance().reference
                            .child("Nanded")
                            .child(currentUserId!!)
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
                                ImageName.downloadUrl.addOnSuccessListener { uri -> //String image=uri.toString();
                                    newStrings.add(uri.toString())
                                    if (newStrings.size == newlist.size) {
                                        newStrings.addAll(oldStrings)

                                        storeLink(id, newStrings, mainum, type, subtype)
                                    }
                                }
                            }

                            upload_count++
                        }
                    } else {
                        Toast.makeText(
                            this@EditSellDataActivity,
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
                        this@EditSellDataActivity,
                        "Please enter residency name",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (addresstext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.resiaddress.error = "Please enter residency address"
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Please enter residency address",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (onametext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.oname.error = "Please enter residency operator name"
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Please enter residency operator name",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (contacttext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.contact.error = "Please enter contact number"
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Please enter contact number",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if (whatsapptext.isEmpty()) {
                    dialog!!.dismiss()

                    binding.whatsapp.error = "Please enter whatsapp number"
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Please enter whatsapp number",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }





        binding.opengallerybtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE)
            binding.cureentimageview.visibility = View.VISIBLE
        }
        binding.capturelocation2.setOnClickListener {
            checkpermission()
            dialog1!!.show()
        }
        binding.back.setOnClickListener { finish() }
        binding.mapview.setOnClickListener {
            val intent = Intent(this@EditSellDataActivity, MapsActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", "Your residency name will fetch here")
            startActivity(intent)
        }
        binding.olmapview.setOnClickListener {
            val intent = Intent(this@EditSellDataActivity, MapsActivity::class.java)
            intent.putExtra("latitude", oldlatitude)
            intent.putExtra("longitude", oldlongitude)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }


    private fun storeLink(
        iddd: String?,
        newStrings: ArrayList<String>,
        mainumx: Int,
        type: String,
        subtypex: String?
    ) {
        val hashMap = HashMap<String, String>()


        var i = 0
        while (i < mainumx) {
            hashMap["image$i"] = newStrings[i]

            i++
        }

        val name = binding.resiname.text.toString()
        val address = binding.resiaddress.text.toString()
        val oname = binding.oname.text.toString()
        val contact = binding.contact.text.toString()
        val whatsapp = binding.whatsapp.text.toString()
        val mail = binding.email.text.toString()
        val prize = binding.prizeamount.text.toString()
        val eprize = binding.explainprize.text.toString()
        val size = binding.propertysize.text.toString()
        val more = binding.moredetails.text.toString()
        val ltype = binding.viewresitypetext.text.toString()
        val larea = binding.vieareatext.text.toString()



        if (binding.showlocationtext.text.toString().isEmpty()) {
            lat = oldlatitude!!
            lan = oldlongitude!!
        } else {
            lat = latitude
            lan = longitude
        }


        val toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage")
        val toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData")

        toolsCollectionRef.document(iddd!!).set(hashMap).addOnSuccessListener {
            val date = Date()
            val data = SellResiClass(
                status,
                "Sell",
                ltype,
                subtypex,
                name,
                name.lowercase(Locale.getDefault()),
                address,
                larea,
                oname,
                contact,
                whatsapp,
                mail,
                prize,
                eprize,
                more,
                FirebaseAuth.getInstance().uid,
                iddd,
                size,
                "",
                "",
                "",
                7028,
                mainumx,
                lat,
                lan,
                date.time
            )
            toolsCollectionRef2.document(iddd).set(data)
                .addOnSuccessListener {
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "Data Edited Successfully, please refresh",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@EditSellDataActivity,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                    finish()
                }
        }.addOnFailureListener { e ->
            dialog!!.dismiss()
            Toast.makeText(
                this@EditSellDataActivity,
                "" + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog!!.dismiss()
        binding.numbertext.text = "Uploaded Successfully"

        newlist.clear()
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
                        this@EditSellDataActivity,
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
    private fun toast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val rentaltype = parent.getItemAtPosition(position).toString()
        val areatype = parent.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
    }
}