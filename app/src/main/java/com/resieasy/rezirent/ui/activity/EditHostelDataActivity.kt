package com.resieasy.rezirent.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.resieasy.rezirent.ui.adapter.MultioldImageAdapter
import com.resieasy.rezirent.ui.adapter.MultipleImageAdapter
import com.resieasy.rezirent.data.remote.firebase.AddHostelClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.ViewModel.FacilityViewModel
import com.resieasy.rezirent.ViewModel.RulesViewModel
import com.resieasy.rezirent.ViewModel.ShowHostelViewModel
import com.resieasy.rezirent.databinding.ActivityEditHostelDataBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale

class EditHostelDataActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
   lateinit var binding: ActivityEditHostelDataBinding 
    var PICK_IMAGE: Int = 123
    var policy: String? = null
    var period: Int = 0
    var upload_count: Int = 0
    var inumber: Int = 0
    lateinit var rentaltype: Array<String>
    lateinit var areatype: Array<String>

     private var dialog: ProgressDialog? = null
    private var dialog1: ProgressDialog? = null

     var oldlist: ArrayList<Uri?> = ArrayList()
    var newlist: ArrayList<Uri?> = ArrayList()
    var newuri: Uri? = null
    var olduri: Uri? = null
    var newStrings: ArrayList<String?> = ArrayList()
    var oldStrings: ArrayList<String?> = ArrayList()
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var oldlatitude: Double? = null
    var oldlongitude: Double? = null

    var lat: Double = 0.0
    var lan: Double = 0.0

    var multipleImageAdapter: MultipleImageAdapter? = null
    var multioldImageAdapter: MultioldImageAdapter? = null
     var type: String = ""
    var subtype: String = ""
    var name: String = ""
    var status: String = ""

    var mainum: Int = 0
    var item: Any? = null
    var item2: Any? = null

     private val showHostelViewModel: ShowHostelViewModel by viewModels()
    private val facilityViewModel: FacilityViewModel by viewModels()
    private val rulesViewModel: RulesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHostelDataBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

       val id = intent.getStringExtra("id")


        dialog1 = ProgressDialog(this)
        dialog1!!.setMessage("Fetching current location....")
        dialog1!!.setCancelable(false)

        dialog = ProgressDialog(this@EditHostelDataActivity)
        dialog!!.setMessage("Uploading Images please Wait.........!!!!!!")
        dialog!!.setCancelable(false)


        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Area1,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areatype.adapter = adapter1
        binding.areatype.onItemSelectedListener =
            this@EditHostelDataActivity


        multipleImageAdapter = MultipleImageAdapter(newlist)
        binding.multiimagerec.layoutManager =
            GridLayoutManager(this@EditHostelDataActivity, 3)
        binding.multiimagerec.adapter = multipleImageAdapter

        multioldImageAdapter = MultioldImageAdapter(oldlist)
        binding.multiimagerec2.layoutManager =
            GridLayoutManager(this@EditHostelDataActivity, 3)
        binding.multiimagerec2.adapter = multioldImageAdapter

        if (id != null) {
            showHostelViewModel.getHostelData(id)
        }else{
            toast("Something is wrong")
        }

        lifecycleScope.launchWhenStarted {
            showHostelViewModel.data.observe(this@EditHostelDataActivity) {
                it?.let { documentSnapshot ->
                    inumber = documentSnapshot.input
                    val period = documentSnapshot.period
                    oldlatitude = documentSnapshot.latitude
                    oldlongitude = documentSnapshot.longitude
                    val period1 =  documentSnapshot.period

                    val idd = documentSnapshot.id
                    val status = documentSnapshot.status
                    val getsubtype = documentSnapshot.subtype
                    val area = documentSnapshot.area
                    val name = documentSnapshot.name
                    val address = documentSnapshot.address
                    val oname = documentSnapshot.oname
                    val number = documentSnapshot.number
                    val whatsapp = documentSnapshot.whatsapp
                    val mail = documentSnapshot.mail
                    val rent = documentSnapshot.rent
                    val erent = documentSnapshot.erent
                    val deposit = documentSnapshot.deposit
                    val extra = documentSnapshot.extra
                    val more = documentSnapshot.more
                    val opengate = documentSnapshot.gopen
                    val closegate = documentSnapshot.gclose
                    val oppolicy = documentSnapshot.policy


                    binding.vieareatext.text = area
                    binding.resiaddress.setText(address)
                    binding.resiname.setText(name)
                    binding.oname.setText(oname)
                    binding.contact.setText(number)
                    binding.whatsapp.setText(whatsapp)
                    binding.email.setText(mail)
                    binding.rentamount.setText(rent)
                    binding.explainrent.setText(erent)
                    binding.moredetails.setText(more)


                    binding.olshowlocationtext.text = "Latitude $oldlatitude And $oldlongitude"


                    FirebaseFirestore.getInstance().collection("Nanded")
                        .document("NandedCity").collection("AllImage").document(idd!!).get()
                        .addOnSuccessListener { snapshot ->
                            for (i in 0 until inumber) {
                                val immm = snapshot.getString("image$i")

                                olduri = Uri.parse(snapshot.getString("image$i"))
                                oldlist.add(olduri)
                                oldStrings.add(immm)
                            }
                            multioldImageAdapter!!.notifyDataSetChanged()
                            binding.numbertext.text = "You have select " + oldlist.size + " images"
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@EditHostelDataActivity,
                                e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    if (getsubtype == "Boys Hostel") {
                        subtype = "Boys Hostel"
                        binding.boyshostel.isChecked = true
                    }
                    if (getsubtype == "Girls Hostel") {
                        subtype = "Girls Hostel"
                        binding.girlshostel.isChecked = true
                    }
                    if (getsubtype == "Boys PG") {
                        subtype = "Boys PG"
                        binding.boyspg.isChecked = true
                    }
                    if (getsubtype == "Girls PG") {
                        subtype = "Girls PG"
                        binding.girlspg.isChecked = true
                    }

                    if (opengate == "No" && closegate == "No") {
                        binding.gatepicker.visibility = View.GONE
                        binding.nogateblue.visibility = View.VISIBLE
                        binding.nogate.isChecked = true
                        binding.yesgate.isChecked = false
                    } else {
                        binding.gatepicker.visibility = View.VISIBLE
                        binding.nogateblue.visibility = View.GONE
                        binding.openpicker.text = opengate
                        binding.closepicker.text = closegate
                        binding.nogate.isChecked = false
                        binding.yesgate.isChecked = true
                    }
                    if (deposit == "No deposit will taken") {
                        binding.nodepositblue.visibility = View.VISIBLE
                        binding.deposit.visibility = View.GONE
                        binding.nodeposit.isChecked = true
                        binding.yesdeposit.isChecked = false
                    } else {
                        binding.nodepositblue.visibility = View.GONE
                        binding.deposit.visibility = View.VISIBLE
                        binding.deposit.setText(deposit)
                        binding.nodeposit.isChecked = false
                        binding.yesdeposit.isChecked = true
                    }
                    if (extra == "No extra charges will taken") {
                        binding.noextrablue.visibility = View.VISIBLE
                        binding.extracharges.visibility = View.GONE
                        binding.yescharge.isChecked = false
                        binding.nocharge.isChecked = true
                    } else {
                        binding.noextrablue.visibility = View.GONE
                        binding.extracharges.visibility = View.VISIBLE
                        binding.extracharges.setText(extra)
                        binding.nocharge.isChecked = false
                        binding.yescharge.isChecked = true
                    }
                    if (period == 708) {
                        binding.noagreetext.visibility = View.VISIBLE
                        binding.yesagreeview.visibility = View.GONE
                        binding.noagreetext.setText(oppolicy)
                        binding.yesargee.isChecked = false
                        binding.noagree.isChecked = true
                    } else {
                        binding.noagreetext.visibility = View.GONE
                        binding.yesagreeview.visibility = View.VISIBLE
                        binding.periodtime.setText(period1)
                        binding.yesagreetext.setText(oppolicy)
                        binding.noagree.isChecked = false
                        binding.yesargee.isChecked = true
                    }
                }
            }
        }


        if (id==null){
            toast("Something is wrong")
        }else{
            facilityViewModel.getFacility(id)
        }

        lifecycleScope.launchWhenStarted {
            facilityViewModel.data.observe(this@EditHostelDataActivity) {
                it?.let { documentSnapshot ->
                    val clean = documentSnapshot.clean
                    val ac = documentSnapshot.ac
                    val rowater = documentSnapshot.rowater
                    val water = documentSnapshot.water
                    val wifi = documentSnapshot.wifi
                    val cctv = documentSnapshot.cctv
                    val bed = documentSnapshot.bed
                    val hotwater = documentSnapshot.hotwater
                    val table = documentSnapshot.table
                    val locker = documentSnapshot.locker
                    val fan = documentSnapshot.fan
                    val powerbackup = documentSnapshot.powerbackup
                    val washing = documentSnapshot.washing
                    val security = documentSnapshot.security
                    val inout = documentSnapshot.inout
                    val attach = documentSnapshot.attach
                    val shower = documentSnapshot.shower
                    val parking = documentSnapshot.parking
                    val mess = documentSnapshot.mess
                    val tv = documentSnapshot.tv
                    val gas = documentSnapshot.gas
                    val dining = documentSnapshot.dining
                    val refrigerator = documentSnapshot.refrigerator
                    val sofa = documentSnapshot.sofa
                    val elevator = documentSnapshot.elevator
                    val play = documentSnapshot.play
                    val gym = documentSnapshot.gym
                    val studyroom = documentSnapshot.studyroom
                    val kitchen = documentSnapshot.kitchen
                    val balcony = documentSnapshot.balcony
                    val indian = documentSnapshot.indian
                    val western = documentSnapshot.western
                    val terrace = documentSnapshot.terrace
                    val furnished = documentSnapshot.furnished
                    val more = documentSnapshot.more

                    if (clean == "Yes") {
                        binding.checkCleanontime.isChecked = true
                    }
                    if (ac == "Yes") {
                        binding.checkac.isChecked = true
                    }
                    if (rowater == "Yes") {
                        binding.checkrowateer.isChecked = true
                    }
                    if (water == "Yes") {
                        binding.checkwateerr.isChecked = true
                    }
                    if (wifi == "Yes") {
                        binding.checkwifi.isChecked = true
                    }
                    if (cctv == "Yes") {
                        binding.checkcamera.isChecked = true
                    }
                    if (bed == "Yes") {
                        binding.checkbed.isChecked = true
                    }
                    if (hotwater == "Yes") {
                        binding.checkhotwater.isChecked = true
                    }
                    if (table == "Yes") {
                        binding.checktable.isChecked = true
                    }
                    if (locker == "Yes") {
                        binding.checklocker.isChecked = true
                    }
                    if (fan == "Yes") {
                        binding.checkcooler.isChecked = true
                    }
                    if (powerbackup == "Yes") {
                        binding.checkpower.isChecked = true
                    }
                    if (washing == "Yes") {
                        binding.checkwashing.isChecked = true
                    }
                    if (security == "Yes") {
                        binding.checksecurity.isChecked = true
                    }
                    if (inout == "Yes") {
                        binding.checkinout.isChecked = true
                    }
                    if (attach == "Yes") {
                        binding.checkattached.isChecked = true
                    }
                    if (shower == "Yes") {
                        binding.checkshower.isChecked = true
                    }
                    if (parking == "Yes") {
                        binding.checkparking.isChecked = true
                    }
                    if (mess == "Yes") {
                        binding.checkmess.isChecked = true
                    }
                    if (tv == "Yes") {
                        binding.checktv.isChecked = true
                    }
                    if (gas == "Yes") {
                        binding.checkgas.isChecked = true
                    }
                    if (dining == "Yes") {
                        binding.checkdining.isChecked = true
                    }
                    if (refrigerator == "Yes") {
                        binding.checkrefre.isChecked = true
                    }
                    if (sofa == "Yes") {
                        binding.checksofa.isChecked = true
                    }
                    if (elevator == "Yes") {
                        binding.checkelvator.isChecked = true
                    }
                    if (play == "Yes") {
                        binding.checkground.isChecked = true
                    }
                    if (gym == "Yes") {
                        binding.checkgym.isChecked = true
                    }
                    if (studyroom == "Yes") {
                        binding.checkstudyroom.isChecked = true
                    }
                    if (kitchen == "Yes") {
                        binding.checkkitchenn.isChecked = true
                    }
                    if (balcony == "Yes") {
                        binding.checkbalcony.isChecked = true
                    }
                    if (indian == "Yes") {
                        binding.checkindiant.isChecked = true
                    }
                    if (western == "Yes") {
                        binding.checkwesterntt.isChecked = true
                    }
                    if (terrace == "Yes") {
                        binding.checkterrace.isChecked = true
                    }
                    if (furnished == "Yes") {
                        binding.checkfullf.isChecked = true
                    }
                    binding.facility.setText(more)
                }
            }
        }
        if (id==null){
            toast("Something is wrong")
        }else{
            rulesViewModel.getRules(id)
        }

        lifecycleScope.launchWhenStarted {
            rulesViewModel.data.observe(this@EditHostelDataActivity) {
                it?.let { documentSnapshot ->
                    val clean = documentSnapshot.clean
                    val trouble = documentSnapshot.trouble
                    val licence = documentSnapshot.licence
                    val gateenry = documentSnapshot.gateentry
                    val alcohol = documentSnapshot.alcohol
                    val damage = documentSnapshot.damage
                    val ousiders = documentSnapshot.outsiders
                    val permission = documentSnapshot.permission
                    val morerule = documentSnapshot.more



                    if (clean == "Yes") {
                        binding.clinerule.isChecked = true
                    }
                    if (trouble == "Yes") {
                        binding.nottrublerule.isChecked = true
                    }
                    if (licence == "Yes") {
                        binding.licencerule.isChecked = true
                    }
                    if (gateenry == "Yes") {
                        binding.entryrule.isChecked = true
                    }
                    if (alcohol == "Yes") {
                        binding.alcoholrule.isChecked = true
                    }
                    if (damage == "Yes") {
                        binding.damagerule.isChecked = true
                    }
                    if (ousiders == "Yes") {
                        binding.outsiderrule.isChecked = true
                    }
                    if (permission == "Yes") {
                        binding.prentperule.isChecked = true
                    }
                    binding.rules.setText(morerule)
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


        binding.yesdeposit.setOnClickListener {
            binding.deposit.visibility = View.VISIBLE
            binding.nodepositblue.visibility = View.GONE
        }
        binding.nodeposit.setOnClickListener {
            binding.deposit.visibility = View.GONE
            binding.nodepositblue.visibility = View.VISIBLE
        }
        binding.yescharge.setOnClickListener {
            binding.extracharges.visibility = View.VISIBLE
            binding.noextrablue.visibility = View.GONE
        }
        binding.nocharge.setOnClickListener {
            binding.extracharges.visibility = View.GONE
            binding.noextrablue.visibility = View.VISIBLE
        }
        binding.yesgate.setOnClickListener {
            binding.gatepicker.visibility = View.VISIBLE
            binding.nogateblue.visibility = View.GONE
        }
        binding.nogate.setOnClickListener {
            binding.gatepicker.visibility = View.GONE
            binding.nogateblue.visibility = View.VISIBLE
        }
        binding.yesargee.setOnClickListener {
            binding.yesagreeview.visibility = View.VISIBLE
            binding.noagreetext.visibility = View.GONE
        }
        binding.noagree.setOnClickListener {
            binding.yesagreeview.visibility = View.GONE
            binding.noagreetext.visibility = View.VISIBLE
        }


        binding.justrehds.setOnClickListener { }
        binding.back.setOnClickListener { finish() }
        binding.capturelocation2.setOnClickListener {
            checkpermission()
            dialog1!!.show()
        }
        binding.mapview.setOnClickListener {
            val intent = Intent(this@EditHostelDataActivity, MapsActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", "Your residency name will fetch here")
            startActivity(intent)
        }
        binding.olmapview.setOnClickListener {
            val intent = Intent(this@EditHostelDataActivity, MapsActivity::class.java)
            intent.putExtra("latitude", oldlatitude)
            intent.putExtra("longitude", oldlongitude)
            intent.putExtra("name", name)
            startActivity(intent)
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
            val ID03 = binding.hostelpgview1.checkedRadioButtonId
            val radioButton03 = findViewById<RadioButton>(ID03)


            if (radioButton03.text == "Boys Hostel") {
                subtype = "Boys Hostel"
            }
            if (radioButton03.text == "Girls Hostel") {
                subtype = "Girls Hostel"
            }
            if (radioButton03.text == "Boys PG") {
                subtype = "Boys PG"
            }
            if (radioButton03.text == "Girls PG") {
                subtype = "Girls PG"
            }


            val ID1 = binding.mainradiodeposit.checkedRadioButtonId
            val radioButton11 = findViewById<RadioButton>(ID1)

            val ID2 = binding.mainradioextra.checkedRadioButtonId
            val radioButton22 = findViewById<RadioButton>(ID2)

            val ID4 = binding.mainrediopolicy.checkedRadioButtonId
            val radioButton33 = findViewById<RadioButton>(ID4)

            val ID5 = binding.mainrediogate.checkedRadioButtonId
            val radioButton44 = findViewById<RadioButton>(ID5)
            if (radioButton11.text == "Yes") {
                if (binding.deposit.text.toString().isNotEmpty()) {
                    val deposit = binding.deposit.text.toString()

                    if (radioButton22.text == "Yes") {
                        if (binding.extracharges.text.toString().isNotEmpty()) {
                            val extra = binding.extracharges.text.toString()

                            if (radioButton33.text == "Agreement will be done") {
                                if (binding.periodtime.text.toString().isNotEmpty()) {
                                    val period = binding.periodtime.text.toString().toInt()
                                    val policy = binding.yesagreetext.text.toString()

                                    if (radioButton44.text == "Yes") {
                                        if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString()
                                                .isNotEmpty()
                                        ) {
                                            val opengate = binding.openpicker.text.toString()
                                            val closegate = binding.closepicker.text.toString()

                                            checktext(
                                                id!!,
                                                subtype,
                                                deposit,
                                                extra,
                                                period,
                                                policy,
                                                opengate,
                                                closegate
                                            )
                                        } else {
                                            if (binding.openpicker.text.toString().isEmpty()) {
                                                binding.openpicker.error =
                                                    "Please pick gate open time"
                                                Toast.makeText(
                                                    this@EditHostelDataActivity,
                                                    "Please pick gate open time",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                dialog!!.dismiss()
                                            }
                                            if (binding.closepicker.text.toString().isEmpty()) {
                                                binding.closepicker.error =
                                                    "Please pick gate close time"
                                                Toast.makeText(
                                                    this@EditHostelDataActivity,
                                                    "Please pick gate close time",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                dialog!!.dismiss()
                                            }
                                        }
                                    } else if (radioButton44.text == "No") {
                                        val opengate = "No"
                                        val closegate = "No"

                                        checktext(
                                            id!!,
                                            subtype,
                                            deposit,
                                            extra,
                                            period,
                                            policy,
                                            opengate,
                                            closegate
                                        )
                                    }
                                } else {
                                    binding.periodtime.error =
                                        "Please enter how many months agreement will be done"
                                    dialog!!.dismiss()
                                    Toast.makeText(
                                        this@EditHostelDataActivity,
                                        "Please enter how many months agreement will be done",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else if (radioButton33.text == "No Agreement, we have own rules") {
                                val period = 708
                                //   int[] array = { period };
                                val policy = binding.noagreetext.text.toString()
                                if (radioButton44.text == "Yes") {
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString()
                                            .isNotEmpty()
                                    ) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
                                            id!!,
                                            subtype,
                                            deposit,
                                            extra,
                                            period,
                                            policy,
                                            opengate,
                                            closegate
                                        )
                                    } else {
                                        if (binding.openpicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate open time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.closepicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate close time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                    }
                                } else if (radioButton44.text == "No") {
                                    val opengate = "No"
                                    val closegate = "No"

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                }
                            }
                        } else {
                            binding.extracharges.error = "Please enter extra charges details"
                            dialog!!.dismiss()
                            Toast.makeText(
                                this@EditHostelDataActivity,
                                "Please enter extra charges details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (radioButton22.text == "No") {
                        val extra = "No extra charges will taken"

                        if (radioButton33.text == "Agreement will be done") {
                            if (binding.periodtime.text.toString().isNotEmpty()) {
                                val period = binding.periodtime.text.toString().toInt()

                                //int[] array = { period };
                                val policy = binding.yesagreetext.text.toString()

                                if (radioButton44.text == "Yes") {
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()
                                    ) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
                                            id!!,
                                            subtype,
                                            deposit,
                                            extra,
                                            period,
                                            policy,
                                            opengate,
                                            closegate
                                        )
                                    } else {
                                        if (binding.openpicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate open time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.closepicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate close time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                    }
                                } else if (radioButton44.text == "No") {
                                    val opengate = "No"
                                    val closegate = "No"

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                }
                            } else {
                                binding.periodtime.error =
                                    "Please enter how many months agreement will be done"
                                dialog!!.dismiss()
                                Toast.makeText(
                                    this@EditHostelDataActivity,
                                    "Please enter how many months agreement will be done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (radioButton33.text == "No Agreement, we have own rules") {
                            val period = 708
                            //    int[] array = { period };
                            val policy = binding.noagreetext.text.toString()
                            if (radioButton44.text == "Yes") {
                                if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()
                                ) {
                                    val opengate = binding.openpicker.text.toString()
                                    val closegate = binding.closepicker.text.toString()

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                } else {
                                    if (binding.openpicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate open time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.closepicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate close time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                }
                            } else if (radioButton44.text == "No") {
                                val opengate = "No"
                                val closegate = "No"

                                checktext(
                                    id!!,
                                    subtype,
                                    deposit,
                                    extra,
                                    period,
                                    policy,
                                    opengate,
                                    closegate
                                )
                            }
                        }
                    }
                } else {
                    binding.deposit.error = "Please enter deposit details"
                    dialog!!.dismiss()
                    Toast.makeText(
                        this@EditHostelDataActivity,
                        "Please enter deposit details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (radioButton11.text == "No") {
                val deposit = "No deposit will taken"

                if (radioButton22.text == "Yes") {
                    if (binding.extracharges.text.toString().isNotEmpty()) {
                        val extra = binding.extracharges.text.toString()

                        if (radioButton33.text == "Agreement will be done") {
                            if (binding.periodtime.text.toString().isNotEmpty()) {
                                val period = binding.periodtime.text.toString().toInt()

                                //  int[] array = { period };
                                val policy = binding.yesagreetext.text.toString()
                                if (radioButton44.text == "Yes") {
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString()
                                            .isNotEmpty()
                                    ) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
                                            id!!,
                                            subtype,
                                            deposit,
                                            extra,
                                            period,
                                            policy,
                                            opengate,
                                            closegate
                                        )
                                    } else {
                                        if (binding.openpicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate open time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.closepicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@EditHostelDataActivity,
                                                "Please pick gate close time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                    }
                                } else if (radioButton44.text == "No") {
                                    val opengate = "No"
                                    val closegate = "No"

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                }
                            } else {
                                binding.periodtime.error =
                                    "Please enter how many months agreement will be done"
                                dialog!!.dismiss()
                                Toast.makeText(
                                    this@EditHostelDataActivity,
                                    "Please enter how many months agreement will be done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (radioButton33.text == "No Agreement, we have own rules") {
                            val period = 708

                            //  int[] array = { period };
                            val policy = binding.noagreetext.text.toString()
                            if (radioButton44.text == "Yes") {
                                if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString()
                                        .isNotEmpty()
                                ) {
                                    val opengate = binding.openpicker.text.toString()
                                    val closegate = binding.closepicker.text.toString()

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                } else {
                                    if (binding.openpicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate open time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.closepicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate close time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                }
                            } else if (radioButton44.text == "No") {
                                val opengate = "No"
                                val closegate = "No"

                                checktext(
                                    id!!,
                                    subtype,
                                    deposit,
                                    extra,
                                    period,
                                    policy,
                                    opengate,
                                    closegate
                                )
                            }
                        }
                    } else {
                        binding.extracharges.error = "Please enter extra charges details"
                        dialog!!.dismiss()
                        Toast.makeText(
                            this@EditHostelDataActivity,
                            "Please enter extra charges details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (radioButton22.text == "No") {
                    val extra = "No extra charges will taken"

                    if (radioButton33.text == "Agreement will be done") {
                        if (binding.periodtime.text.toString().isNotEmpty()) {
                            val period = binding.periodtime.text.toString().toInt()

                            //    int[] array = { period };
                            val policy = binding.yesagreetext.text.toString()
                            if (radioButton44.text == "Yes") {
                                if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString()
                                        .isNotEmpty()
                                ) {
                                    val opengate = binding.openpicker.text.toString()
                                    val closegate = binding.closepicker.text.toString()

                                    checktext(
                                        id!!,
                                        subtype,
                                        deposit,
                                        extra,
                                        period,
                                        policy,
                                        opengate,
                                        closegate
                                    )
                                } else {
                                    if (binding.openpicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate open time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.closepicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@EditHostelDataActivity,
                                            "Please pick gate close time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                }
                            } else if (radioButton44.text == "No") {
                                val opengate = "No"
                                val closegate = "No"

                                checktext(
                                    id!!,
                                    subtype,
                                    deposit,
                                    extra,
                                    period,
                                    policy,
                                    opengate,
                                    closegate
                                )
                            }
                        } else {
                            binding.periodtime.error =
                                "Please enter how many months agreement will be done"
                            dialog!!.dismiss()
                            Toast.makeText(
                                this@EditHostelDataActivity,
                                "Please enter how many months agreement will be done",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (radioButton33.text == "No Agreement, we have own rules") {
                        val period = 708
                        //  int[] array = { period };
                        val policy = binding.noagreetext.text.toString()
                        if (radioButton44.text == "Yes") {
                            if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()
                            ) {
                                val opengate = binding.openpicker.text.toString()
                                val closegate = binding.closepicker.text.toString()

                                checktext(
                                    id!!,
                                    subtype,
                                    deposit,
                                    extra,
                                    period,
                                    policy,
                                    opengate,
                                    closegate
                                )
                            } else {
                                if (binding.openpicker.text.toString().isEmpty()) {
                                    binding.openpicker.error = "Please pick gate open time"
                                    Toast.makeText(
                                        this@EditHostelDataActivity,
                                        "Please pick gate open time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog!!.dismiss()
                                }
                                if (binding.closepicker.text.toString().isEmpty()) {
                                    binding.closepicker.error = "Please pick gate close time"
                                    Toast.makeText(
                                        this@EditHostelDataActivity,
                                        "Please pick gate close time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog!!.dismiss()
                                }
                            }
                        } else if (radioButton44.text == "No") {
                            val opengate = "No"
                            val closegate = "No"

                            checktext(
                                id!!,
                                subtype,
                                deposit,
                                extra,
                                period,
                                policy,
                                opengate,
                                closegate
                            )
                        }
                    }
                }
            }
        }
        binding.openpicker.setOnClickListener {
            val timePicker = TimePickerDialog(
                this@EditHostelDataActivity,
                timePickerDialogListener1,
                12,
                10,
                false
            )
            timePicker.show()
        }
        binding.closepicker.setOnClickListener {
            val timePicker = TimePickerDialog(
                this@EditHostelDataActivity,
                timePickerDialogListener,
                12,
                10,
                false
            )
            timePicker.show()
        }
    }

    private fun checktext(
        idd: String,
        subtypez: String,
        depositz: String,
        extraz: String,
        periodz: Int,
        policyz: String,
        opengatez: String,
        closegatez: String
    ) {
        val name = binding.resiname.text.toString()
        val address = binding.resiaddress.text.toString()
        val oname = binding.oname.text.toString()
        val contact = binding.contact.text.toString()
        val whatsapp = binding.whatsapp.text.toString()
        val rent = binding.rentamount.text.toString()


        if (name.isNotEmpty() && address.isNotEmpty() && oname.isNotEmpty() && contact.isNotEmpty() && whatsapp.isNotEmpty() && rent.isNotEmpty()) {
            if (newlist.isEmpty() && oldlist.isEmpty()) {
                dialog!!.dismiss()
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please select images",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (newlist.isNotEmpty() && oldlist.isEmpty()) {
                if (newlist.size < 11) {
                    mainum = newlist.size
                    val ImageFolder = FirebaseStorage.getInstance().reference
                        .child("Nanded")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .child("HostelImage")

                    upload_count = 0
                    while (upload_count < newlist.size) {
                        val IndividualImage = newlist[upload_count]
                        val ImageName =
                            ImageFolder.child("Images" + IndividualImage!!.lastPathSegment)


                        var bmp: Bitmap? = null
                        try {
                            bmp =
                                MediaStore.Images.Media.getBitmap(contentResolver, IndividualImage)
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
                                    storeLink(
                                        idd,
                                        subtypez,
                                        newStrings,
                                        mainum,
                                        depositz,
                                        extraz,
                                        periodz,
                                        policyz,
                                        opengatez,
                                        closegatez
                                    )
                                }
                            }
                        }
                        upload_count++
                    }
                } else {
                    Toast.makeText(
                        this@EditHostelDataActivity,
                        "Please don't select more than 10 images",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
            } else if (newlist.isEmpty() && !oldlist.isEmpty()) {
                if (oldlist.size < 11) {
                    mainum = oldlist.size
                    storeLink(
                        idd,
                        subtypez,
                        oldStrings,
                        mainum,
                        depositz,
                        extraz,
                        periodz,
                        policyz,
                        opengatez,
                        closegatez
                    )
                } else {
                    Toast.makeText(
                        this@EditHostelDataActivity,
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
                        .child(FirebaseAuth.getInstance().uid!!)
                        .child("HostelImage")

                    upload_count = 0
                    while (upload_count < newlist.size) {
                        val IndividualImage = newlist[upload_count]
                        val ImageName =
                            ImageFolder.child("Images" + IndividualImage!!.lastPathSegment)


                        var bmp: Bitmap? = null
                        try {
                            bmp =
                                MediaStore.Images.Media.getBitmap(contentResolver, IndividualImage)
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

                                    storeLink(
                                        idd,
                                        subtypez,
                                        newStrings,
                                        mainum,
                                        depositz,
                                        extraz,
                                        periodz,
                                        policyz,
                                        opengatez,
                                        closegatez
                                    )
                                }
                            }
                        }

                        upload_count++
                    }
                } else {
                    Toast.makeText(
                        this@EditHostelDataActivity,
                        "Please don't select more than 10 images",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
            }
        } else {
            if (name.isEmpty()) {
                dialog!!.dismiss()

                binding.resiname.error = "Please enter residency name"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter residency name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (address.isEmpty()) {
                dialog!!.dismiss()

                binding.resiaddress.error = "Please enter residency address"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter residency address",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (oname.isEmpty()) {
                dialog!!.dismiss()

                binding.oname.error = "Please enter residency operator name"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter residency operator name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (contact.isEmpty()) {
                dialog!!.dismiss()

                binding.contact.error = "Please enter contact number"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter contact number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (whatsapp.isEmpty()) {
                dialog!!.dismiss()

                binding.whatsapp.error = "Please enter whatsapp number"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter whatsapp number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (rent.isEmpty()) {
                dialog!!.dismiss()

                binding.rentamount.error = "Please enter monthly rent"
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Please enter monthly rent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun storeLink(
        iddd: String,
        subtypex: String,
        newStrings: ArrayList<String?>,
        mainumx: Int,
        depositx: String,
        extrax: String,
        periodx: Int,
        policyx: String,
        opengatex: String,
        closegatex: String
    ) {
        val hashMap = HashMap<String, String?>()


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
        val rent = binding.rentamount.text.toString()
        val erent = binding.explainrent.text.toString()
        val more = binding.moredetails.text.toString()

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

        toolsCollectionRef.document(iddd).set(hashMap)
            .addOnSuccessListener(OnSuccessListener<Void?> {
                val date = Date()
                val data = AddHostelClass(
                    status,
                    "Hostel",
                    "Hostel",
                    subtypex,
                    name,
                    name.lowercase(Locale.getDefault()),
                    address,
                    larea,
                    oname,
                    contact,
                    whatsapp,
                    mail,
                    rent,
                    erent,
                    depositx,
                    extrax,
                    more,
                    policyx,
                    opengatex,
                    closegatex,
                    FirebaseAuth.getInstance().uid!!,
                    iddd,
                    "",
                    "",
                    "",
                    7028,
                    mainumx,
                    periodx,
                    lat,
                    lan,
                    date.time
                )
                toolsCollectionRef2.document(iddd).set(data).addOnSuccessListener {
                    addfacility(iddd)
                    addrules(iddd)


                    dialog!!.dismiss()
                    Toast.makeText(
                        this@EditHostelDataActivity,
                        "Data Edited Successfully, please refresh",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@EditHostelDataActivity,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                    finish()
                }
            })

        dialog!!.dismiss()
        binding.numbertext.text = "Uploaded Successfully"

        newlist.clear()
    }

    private val timePickerDialogListener =
        OnTimeSetListener { view, hourOfDay, minute ->
            val formattedTime: String
            if (hourOfDay == 0) {
                formattedTime = if ((minute < 10)) "12:0$minute am" else "12:$minute am"
            } else if (hourOfDay > 12) {
                val hour = hourOfDay - 12
                formattedTime = if ((minute < 10)) "$hour:0$minute pm" else "$hour:$minute pm"
            } else if (hourOfDay == 12) {
                formattedTime = if ((minute < 10)) "12:0$minute pm" else "12:$minute pm"
            } else {
                formattedTime =
                    if ((minute < 10)) "$hourOfDay:0$minute am" else "$hourOfDay:$minute am"
            }
            binding.closepicker.text = formattedTime
        }
    private val timePickerDialogListener1 =
        OnTimeSetListener { view, hourOfDay, minute ->
            val formattedTime: String
            if (hourOfDay == 0) {
                formattedTime = if ((minute < 10)) "12:0$minute am" else "12:$minute am"
            } else if (hourOfDay > 12) {
                val hour = hourOfDay - 12
                formattedTime = if ((minute < 10)) "$hour:0$minute pm" else "$hour:$minute pm"
            } else if (hourOfDay == 12) {
                formattedTime = if ((minute < 10)) "12:0$minute pm" else "12:$minute pm"
            } else {
                formattedTime =
                    if ((minute < 10)) "$hourOfDay:0$minute am" else "$hourOfDay:$minute am"
            }
            binding.openpicker.text = formattedTime
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
                        this@EditHostelDataActivity,
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

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
    }

    private fun addfacility(newDocID: String) {
        val a1 = if (binding.checkCleanontime.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a2 = if (binding.checkac.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a3 = if (binding.checkrowateer.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a4 = if (binding.checkwateerr.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a5 = if (binding.checkwifi.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a6 = if (binding.checkcamera.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a7 = if (binding.checkbed.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a8 = if (binding.checkhotwater.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a9 = if (binding.checktable.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a10 = if (binding.checklocker.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a11 = if (binding.checkcooler.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a12 = if (binding.checkpower.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a13 = if (binding.checkwashing.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a14 = if (binding.checksecurity.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a15 = if (binding.checkinout.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a16 = if (binding.checkattached.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a17 = if (binding.checkshower.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a18 = if (binding.checkparking.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a19 = if (binding.checkmess.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a20 = if (binding.checktv.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a21 = if (binding.checkgas.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a22 = if (binding.checkdining.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a23 = if (binding.checkrefre.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a24 = if (binding.checksofa.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a25 = if (binding.checkelvator.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a26 = if (binding.checkground.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a27 = if (binding.checkgym.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a28 = if (binding.checkstudyroom.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a29 = if (binding.checkkitchenn.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a30 = if (binding.checkbalcony.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a31 = if (binding.checkindiant.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a32 = if (binding.checkwesterntt.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a33 = if (binding.checkterrace.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a34 = if (binding.checkfullf.isChecked) {
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
        val more = binding.facility.text.toString()
        hashMap1["more"] = more

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllFacility").document(newDocID).set(hashMap1)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "facility fail",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addrules(newDocID: String) {
        val a01 = if (binding.clinerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a02 = if (binding.nottrublerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a03 = if (binding.licencerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a04 = if (binding.entryrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a05 = if (binding.alcoholrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a06 = if (binding.damagerule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a07 = if (binding.outsiderrule.isChecked) {
            "Yes"
        } else {
            "No"
        }
        val a08 = if (binding.prentperule.isChecked) {
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
        val more1 = binding.rules.text.toString()
        hashMap11["more"] = more1

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllRule").document(newDocID).set(hashMap11)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@EditHostelDataActivity,
                    "Rules fail",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }   private fun toast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}


