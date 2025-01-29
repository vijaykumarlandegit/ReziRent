package com.resieasy.rezirent.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
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
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.resieasy.rezirent.Adapter.MultipleImageAdapter
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.SingleIDClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityAddResidencyBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale

class AddResidencyActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var binding: ActivityAddResidencyBinding? = null
    lateinit var rentaltype: Array<String>
    lateinit var areatype: Array<String>

    var PICK_IMG: Int = 123
    var `in`: Int = 0
    private val mMap: GoogleMap? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var hashMap: HashMap<String, String>? = null


    var multipleImageAdapter: MultipleImageAdapter? = null
    var dialog: ProgressDialog? = null
    var dialog1: ProgressDialog? = null

    private var upload_count = 0

    var newlist: ArrayList<Uri?> = ArrayList()
    var newuri: Uri? = null
    var mainum: Int = 0

    val newStrings = ArrayList<String>()


    var subtype: String? = null


    private val picker1: MaterialTimePicker? = null
    private val picker2: MaterialTimePicker? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddResidencyBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Uploading...")
        dialog!!.setMessage("Please wait, we are creating your residency account")
        dialog!!.setCancelable(false)
        dialog1 = ProgressDialog(this)
        dialog1!!.setMessage("Fetching current location....")
        dialog1!!.setCancelable(false)

        hashMap = HashMap()


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Rental,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.rentaltype.adapter = adapter
        binding!!.rentaltype.onItemSelectedListener =
            this@AddResidencyActivity

        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Area,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.areatype.adapter = adapter1
        binding!!.areatype.onItemSelectedListener =
            this@AddResidencyActivity


        multipleImageAdapter = MultipleImageAdapter(newlist)
        binding!!.multiimagerec.layoutManager =
            GridLayoutManager(this@AddResidencyActivity, 3)
        binding!!.multiimagerec.adapter = multipleImageAdapter


        binding!!.rentaltype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(position)
                if (item.toString() == "Flat") {
                    binding!!.flatview1.visibility = View.VISIBLE
                    binding!!.roomview1.visibility = View.GONE
                } else if (item.toString() == "Room") {
                    binding!!.flatview1.visibility = View.GONE
                    binding!!.roomview1.visibility = View.VISIBLE
                } else {
                    binding!!.flatview1.visibility = View.GONE
                    binding!!.roomview1.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding!!.yesdeposit.setOnClickListener {
            binding!!.deposit.visibility = View.VISIBLE
            binding!!.nodepositblue.visibility = View.GONE
        }
        binding!!.nodeposit.setOnClickListener {
            binding!!.deposit.visibility = View.GONE
            binding!!.nodepositblue.visibility = View.VISIBLE
        }
        binding!!.yescharge.setOnClickListener {
            binding!!.extracharges.visibility = View.VISIBLE
            binding!!.noextrablue.visibility = View.GONE
        }
        binding!!.nocharge.setOnClickListener {
            binding!!.extracharges.visibility = View.GONE
            binding!!.noextrablue.visibility = View.VISIBLE
        }

        binding!!.yesargee.setOnClickListener {
            binding!!.yesagreeview.visibility = View.VISIBLE
            binding!!.noagreementtext.visibility = View.GONE
        }
        binding!!.noagree.setOnClickListener {
            binding!!.yesagreeview.visibility = View.GONE
            binding!!.noagreementtext.visibility = View.VISIBLE
        }
        binding!!.back.setOnClickListener { finish() }


        binding!!.opengallerybtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE)
            binding!!.currentimageview.visibility = View.VISIBLE
        }

        binding!!.submitresibtn.setOnClickListener {
            dialog!!.show()
            val type = binding!!.rentaltype.selectedItem.toString()



            if (type == "Flat") {
                binding!!.flatview1.visibility = View.VISIBLE
                val ID01 = binding!!.flatview1.checkedRadioButtonId
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
                binding!!.roomview1.visibility = View.VISIBLE
                val ID02 = binding!!.roomview1.checkedRadioButtonId
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
            }

            val ID1 = binding!!.mainradiodeposit.checkedRadioButtonId
            val radioButton11 = findViewById<RadioButton>(ID1)

            val ID2 = binding!!.mainradioextra.checkedRadioButtonId
            val radioButton22 = findViewById<RadioButton>(ID2)

            val ID4 = binding!!.mainrediopolicy.checkedRadioButtonId
            val radioButton33 = findViewById<RadioButton>(ID4)
            if (radioButton11.text == "Yes") {
                //deposit = binding.deposit.getText().toString();
                if (!binding!!.deposit.text.toString().isEmpty()) {
                    val deposit = binding!!.deposit.text.toString()

                    if (radioButton22.text == "Yes") {
                        if (!binding!!.extracharges.text.toString().isEmpty()) {
                            val extra = binding!!.extracharges.text.toString()

                            if (radioButton33.text == "Agreement will be done") {
                                if (!binding!!.periodtime.text.toString().isEmpty()) {
                                    val period = binding!!.periodtime.text.toString().toInt()
                                    //  int[] array = { period };
                                    val policy = binding!!.agreementtext.text.toString()
                                    checktext(type, subtype, deposit, extra, period, policy)
                                } else {
                                    binding!!.periodtime.error =
                                        "Please enter how many months agreement will be done"
                                    dialog!!.dismiss()
                                    Toast.makeText(
                                        this@AddResidencyActivity,
                                        "Please enter how many months agreement will be done",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else if (radioButton33.text == "No Agreement, we have own rules") {
                                val period = 708
                                //   int[] array = { period };
                                val policy = binding!!.noagreementtext.text.toString()
                                checktext(type, subtype, deposit, extra, period, policy)
                            }
                        } else {
                            binding!!.extracharges.error = "Please enter extra charges details"
                            dialog!!.dismiss()
                            Toast.makeText(
                                this@AddResidencyActivity,
                                "Please enter extra charges details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (radioButton22.text == "No") {
                        val extra = "No extra charges will taken"

                        if (radioButton33.text == "Agreement will be done") {
                            if (!binding!!.periodtime.text.toString().isEmpty()) {
                                val period = binding!!.periodtime.text.toString().toInt()

                                //int[] array = { period };
                                val policy = binding!!.agreementtext.text.toString()

                                checktext(type, subtype, deposit, extra, period, policy)
                            } else {
                                binding!!.periodtime.error =
                                    "Please enter how many months agreement will be done"
                                dialog!!.dismiss()
                                Toast.makeText(
                                    this@AddResidencyActivity,
                                    "Please enter how many months agreement will be done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (radioButton33.text == "No Agreement, we have own rules") {
                            val period = 708
                            //    int[] array = { period };
                            val policy = binding!!.noagreementtext.text.toString()
                            checktext(type, subtype, deposit, extra, period, policy)
                        }
                    }
                } else {
                    binding!!.deposit.error = "Please enter deposit details"
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "Please enter deposit details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (radioButton11.text == "No") {
                val deposit = "No deposit will taken"

                if (radioButton22.text == "Yes") {
                    if (!binding!!.extracharges.text.toString().isEmpty()) {
                        val extra = binding!!.extracharges.text.toString()

                        if (radioButton33.text == "Agreement will be done") {
                            if (!binding!!.periodtime.text.toString().isEmpty()) {
                                val period = binding!!.periodtime.text.toString().toInt()

                                //  int[] array = { period };
                                val policy = binding!!.agreementtext.text.toString()
                                checktext(type, subtype, deposit, extra, period, policy)
                            } else {
                                binding!!.periodtime.error =
                                    "Please enter how many months agreement will be done"
                                dialog!!.dismiss()
                                Toast.makeText(
                                    this@AddResidencyActivity,
                                    "Please enter how many months agreement will be done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (radioButton33.text == "No Agreement, we have own rules") {
                            val period = 708

                            //  int[] array = { period };
                            val policy = binding!!.noagreementtext.text.toString()
                            checktext(type, subtype, deposit, extra, period, policy)
                        }
                    } else {
                        binding!!.extracharges.error = "Please enter extra charges details"
                        dialog!!.dismiss()
                        Toast.makeText(
                            this@AddResidencyActivity,
                            "Please enter extra charges details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (radioButton22.text == "No") {
                    val extra = "No extra charges will taken"

                    if (radioButton33.text == "Agreement will be done") {
                        if (!binding!!.periodtime.text.toString().isEmpty()) {
                            val period = binding!!.periodtime.text.toString().toInt()

                            //    int[] array = { period };
                            val policy = binding!!.agreementtext.text.toString()
                            checktext(type, subtype, deposit, extra, period, policy)
                        } else {
                            binding!!.periodtime.error =
                                "Please enter how many months agreement will be done"
                            dialog!!.dismiss()
                            Toast.makeText(
                                this@AddResidencyActivity,
                                "Please enter how many months agreement will be done",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (radioButton33.text == "No Agreement, we have own rules") {
                        val period = 708
                        //  int[] array = { period };
                        val policy = binding!!.noagreementtext.text.toString()
                        checktext(type, subtype, deposit, extra, period, policy)
                    }
                }
            }
        }
        binding!!.capturelocation2.setOnClickListener {
            checkpermission()
            dialog1!!.show()
        }
        binding!!.mapview.setOnClickListener {
            val intent = Intent(this@AddResidencyActivity, MapsActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", "Your residency name will fetch here")
            startActivity(intent)
        }
    }


    private fun checktext(
        typez: String,
        subtypez: String?,
        depositz: String,
        extraz: String,
        periodz: Int,
        policyz: String
    ) {
        val name = binding!!.resiname.text.toString()
        val address = binding!!.resiaddress.text.toString()
        // String locationtext = binding.showlocationtext.getText().toString();
        val oname = binding!!.oname.text.toString()
        val contact = binding!!.contact.text.toString()
        val whatsapp = binding!!.whatsapp.text.toString()
        val rent = binding!!.rentamount.text.toString()






        if (!name.isEmpty() && !address.isEmpty() && !oname.isEmpty() && !contact.isEmpty() && !whatsapp.isEmpty() && !rent.isEmpty()) {
            if (newlist.isEmpty()) {
                dialog!!.dismiss()
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please select images",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!newlist.isEmpty()) {
                if (newlist.size < 11) {
                    mainum = newlist.size
                    uploadimages(typez, subtypez, depositz, extraz, periodz, policyz)
                } else {
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "Please don't select more than 10 images",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
            }
        } else {
            if (name.isEmpty()) {
                dialog!!.dismiss()

                binding!!.resiname.error = "Please enter residency name"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter residency name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (address.isEmpty()) {
                dialog!!.dismiss()

                binding!!.resiaddress.error = "Please enter residency address"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter residency address",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (oname.isEmpty()) {
                dialog!!.dismiss()

                binding!!.oname.error = "Please enter residency operator name"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter residency operator name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (contact.isEmpty()) {
                dialog!!.dismiss()

                binding!!.contact.error = "Please enter contact number"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter contact number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (whatsapp.isEmpty()) {
                dialog!!.dismiss()

                binding!!.whatsapp.error = "Please enter whatsapp number"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter whatsapp number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (rent.isEmpty()) {
                dialog!!.dismiss()

                binding!!.rentamount.error = "Please enter monthly rent"
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Please enter monthly rent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uploadimages(
        typex: String,
        subtypex: String?,
        depositx: String,
        extrax: String,
        periodx: Int,
        policyx: String
    ) {
        binding!!.numbertext.text = "If Loading Takes to long press button again"
        val ImageFolder = FirebaseStorage.getInstance().reference.child("Nanded")
            .child(FirebaseAuth.getInstance().uid!!)
            .child("RentImage")


        upload_count = 0
        while (upload_count < newlist.size) {
            val IndividualImage = newlist[upload_count]
            val ImageName = ImageFolder.child("Images" + IndividualImage!!.lastPathSegment)


            var bmp: Bitmap? = null
            try {
                bmp = MediaStore.Images.Media.getBitmap(contentResolver, IndividualImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val baos = ByteArrayOutputStream()
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 40, baos)
            val data = baos.toByteArray()
            val uploadTask2 = ImageName.putBytes(data)

            uploadTask2.addOnSuccessListener {
                ImageName.downloadUrl.addOnSuccessListener { uri ->
                    newStrings.add(uri.toString()  )
                    if (newStrings.size == newlist.size) {
                        storeLink(
                            newStrings,
                            newlist.size,
                            typex,
                            subtypex,
                            depositx,
                            extrax,
                            periodx,
                            policyx
                        )
                    }
                }
            }



            upload_count++
        }
    }


    private fun storeLink(
        urlStrings: ArrayList<String>,
        mainum: Int,
        typec: String,
        subtypec: String?,
        depositc: String,
        extrac: String,
        periodc: Int,
        policyc: String
    ) {
        val hashMap = HashMap<String, String>()

        var i = 0
        while (i < newlist.size) {
            hashMap["image$i"] = urlStrings[i]
            i++
        }


        val name = binding!!.resiname.text.toString()
        val address = binding!!.resiaddress.text.toString()
        val locationtext = binding!!.showlocationtext.text.toString()
        val oname = binding!!.oname.text.toString()
        val contact = binding!!.contact.text.toString()
        val whatsapp = binding!!.whatsapp.text.toString()
        val mail = binding!!.email.text.toString()
        val rent = binding!!.rentamount.text.toString()
        val erent = binding!!.explainrent.text.toString()
        val more = binding!!.moredetails.text.toString()
        val userid = FirebaseAuth.getInstance().uid
        val area = binding!!.areatype.selectedItem.toString()

        val toolsCollectionRef = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllImage")
        val toolsCollectionRef2 = FirebaseFirestore.getInstance().collection("Nanded")
            .document("NandedCity").collection("AllData")
        val toolsCollectionRef3 = FirebaseFirestore.getInstance().collection("OwnResi")
            .document(userid!!).collection("Nanded")
        val newDocID = toolsCollectionRef.document().id


        toolsCollectionRef.document(newDocID).set(hashMap).addOnSuccessListener {
            val date = Date()
            val data = AddFlatClass(
                "Active",
                "Rent",
                typec,
                subtypec,
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
                depositc,
                extrac,
                more,
                policyc,
                FirebaseAuth.getInstance().uid,
                newDocID,
                "",
                "",
                "",
                7028,
                mainum,
                periodc,
                19.114591,
                77.291456,
                date.time
            )
            toolsCollectionRef2.document(newDocID).set(data).addOnSuccessListener {
                val myresi = SingleIDClass(
                    newDocID,
                    "Rent",
                    "Nanded",
                    "",
                    "",
                    7028,
                    Date().time
                )
                toolsCollectionRef3.document(newDocID).set(myresi).addOnSuccessListener {
                    addfacility(newDocID)
                    addrules(newDocID)
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "Data Uploaded Successfully, please refresh",
                        Toast.LENGTH_LONG
                    ).show()
                    dialog!!.dismiss()
                    finish()
                }.addOnFailureListener { e ->
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@AddResidencyActivity,
                    "" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
                dialog!!.dismiss()
            }
        }.addOnFailureListener { e ->
            dialog!!.dismiss()
            Toast.makeText(
                this@AddResidencyActivity,
                "" + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog!!.dismiss()
        binding!!.numbertext.text = "Uploaded Successfully"

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
                        binding!!.numbertext.text = "You have select " + newlist.size + " images"
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
                    binding!!.locationview.visibility = View.VISIBLE
                    dialog1!!.dismiss()
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "Current location fetch successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    latitude = location.latitude
                    longitude = location.longitude
                    binding!!.showlocationtext.text = "Latitude: $latitude & Longitude: $longitude"


                    //LatLng usercl = new LatLng(latitude, longitude);
                } else {
                    Toast.makeText(
                        this@AddResidencyActivity,
                        "Something is wrong, location is not fetched",
                        Toast.LENGTH_LONG
                    ).show()
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


    private fun addfacility(newDocID: String) {
        val a1 = if (binding!!.checkCleanontime.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a2 = if (binding!!.checkac.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a3 = if (binding!!.checkrowateer.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a4 = if (binding!!.checkwateerr.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a5 = if (binding!!.checkwifi.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a6 = if (binding!!.checkcamera.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a7 = if (binding!!.checkbed.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a8 = if (binding!!.checkhotwater.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a9 = if (binding!!.checktable.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a10 = if (binding!!.checklocker.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a11 = if (binding!!.checkcooler.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a12 = if (binding!!.checkpower.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a13 = if (binding!!.checkwashing.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a14 = if (binding!!.checksecurity.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a15 = if (binding!!.checkinout.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a16 = if (binding!!.checkattached.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a17 = if (binding!!.checkshower.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a18 = if (binding!!.checkparking.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a19 = if (binding!!.checkmess.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a20 = if (binding!!.checktv.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a21 = if (binding!!.checkgas.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a22 = if (binding!!.checkdining.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a23 = if (binding!!.checkrefre.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a24 = if (binding!!.checksofa.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a25 = if (binding!!.checkelvator.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a26 = if (binding!!.checkground.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a27 = if (binding!!.checkgym.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a28 = if (binding!!.checkstudyroom.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a29 = if (binding!!.checkkitchenn.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a30 = if (binding!!.checkbalcony.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a31 = if (binding!!.checkindiant.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a32 = if (binding!!.checkwesterntt.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a33 = if (binding!!.checkterrace.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a34 = if (binding!!.checkfullf.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val hashMap1 = HashMap<String, Any>()
        hashMap1["clean"] = a1
        hashMap1["ac"] = a2
        hashMap1["rowater"] = a3
        hashMap1["water"] = a4
        hashMap1["wifi"] = a5
        hashMap1["cctv"] = a6
        hashMap1["bed"] = a7
        hashMap1["hotwater"] = a8
        hashMap1["table"] = a9
        hashMap1["locker"] = a10
        hashMap1["fan"] = a11
        hashMap1["powerbackup"] = a12
        hashMap1["washing"] = a13
        hashMap1["security"] = a14
        hashMap1["inout"] = a15
        hashMap1["attach"] = a16
        hashMap1["shower"] = a17
        hashMap1["parking"] = a18
        hashMap1["mess"] = a19
        hashMap1["tv"] = a20
        hashMap1["gas"] = a21
        hashMap1["dining"] = a22
        hashMap1["refrigerator"] = a23
        hashMap1["sofa"] = a24
        hashMap1["elevator"] = a25
        hashMap1["play"] = a26
        hashMap1["gym"] = a27
        hashMap1["studyroom"] = a28
        hashMap1["kitchen"] = a29
        hashMap1["balcony"] = a30
        hashMap1["indian"] = a31
        hashMap1["western"] = a32
        hashMap1["terrace"] = a33
        hashMap1["furnished"] = a34
        val more = binding!!.facility.text.toString()
        hashMap1["more"] = more

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllFacility").document(newDocID).set(hashMap1)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@AddResidencyActivity,
                    "facility fail",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addrules(newDocID: String) {
        val a01 = if (binding!!.clinerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a02 = if (binding!!.nottrublerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a03 = if (binding!!.licencerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a04 = if (binding!!.entryrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a05 = if (binding!!.alcoholrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a06 = if (binding!!.damagerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a07 = if (binding!!.outsiderrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a08 = if (binding!!.prentperule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val hashMap11 = HashMap<String, Any>()
        hashMap11["clean"] = a01
        hashMap11["trouble"] = a02
        hashMap11["licence"] = a03
        hashMap11["gateenry"] = a04
        hashMap11["alcohol"] = a05
        hashMap11["damage"] = a06
        hashMap11["ousiders"] = a07
        hashMap11["permission"] = a08
        val more1 = binding!!.rules.text.toString()
        hashMap11["more"] = more1

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllRule").document(newDocID).set(hashMap11)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@AddResidencyActivity,
                    "Rules fail",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val rentaltype = parent.getItemAtPosition(position).toString()
        val areatype = parent.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                this@AddResidencyActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this@AddResidencyActivity as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    companion object {
        private const val PICK_IMAGE = 1


        private const val READ_STORAGE_PERMISSION_REQUEST_CODE = 41
    }
}