package com.resieasy.rezirent.Activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
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
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Class.FacilityClass
import com.resieasy.rezirent.Class.RulesClass
import com.resieasy.rezirent.Class.SingleIDClass
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityAddHostelBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.HashMap
import java.util.Date
import java.util.ArrayList
import java.util.Locale

class AddHostelActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
   lateinit var binding: ActivityAddHostelBinding 
    lateinit var rentaltype: Array<String>
    lateinit var areatype: Array<String>


    var latitude = 0.0
    var longitude = 0.0
    var hashMap: HashMap<String, String>? = null

    var multipleImageAdapter: MultipleImageAdapter? = null
    var dialog: ProgressDialog? = null
    var dialog1: ProgressDialog? = null
    private var upload_count = 0

    var newlist = ArrayList<Uri?>()
    var newuri: Uri? = null
    var mainum = 0
    val newStrings = ArrayList<String>()

    var policy = ""
    var subtype = ""
    var period = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHostelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Updating...")
        dialog!!.setMessage("Please wait, we are creating your residency account")
        dialog!!.setCancelable(false)
        dialog1 = ProgressDialog(this)
        dialog1!!.setMessage("Fetching current location....")
        dialog1!!.setCancelable(false)

        hashMap = HashMap()


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
        binding.texdsddftVedhiedw4.setOnClickListener { }


        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Area,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.areatype.adapter = adapter1
        binding.areatype.onItemSelectedListener =
            this@AddHostelActivity


        multipleImageAdapter = MultipleImageAdapter(newlist)
        binding.multiimagerec.layoutManager =
            GridLayoutManager(this@AddHostelActivity, 3)
        binding.multiimagerec.adapter = multipleImageAdapter


        binding.opengallerybtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE)
            binding.currentimageview.visibility = View.VISIBLE
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
                //deposit = binding.deposit.getText().toString();
                if (binding.deposit.text.toString().isNotEmpty()) {
                    val deposit = binding.deposit.text.toString()

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
                                                    this@AddHostelActivity,
                                                    "Please pick gate open time",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                dialog!!.dismiss()
                                            }
                                            if (binding.closepicker.text.toString().isEmpty()) {
                                                binding.openpicker.error =
                                                    "Please pick gate close time"
                                                Toast.makeText(
                                                    this@AddHostelActivity,
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
                                        this@AddHostelActivity,
                                        "Please enter how many months agreement will be done",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else if (radioButton33.text == "No Agreement, we have own rules") {
                                val period = 708
                                //   int[] array = { period };
                                val policy = binding.noagreetext.text.toString()
                                if (radioButton44.text == "Yes") {
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
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
                                                this@AddHostelActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@AddHostelActivity,
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
                                this@AddHostelActivity,
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
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
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
                                                this@AddHostelActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@AddHostelActivity,
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
                                    this@AddHostelActivity,
                                    "Please enter how many months agreement will be done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else if (radioButton33.text == "No Agreement, we have own rules") {
                            val period = 708
                            //    int[] array = { period };
                            val policy = binding.noagreetext.text.toString()
                            if (radioButton44.text == "Yes") {
                                if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()) {
                                    val opengate = binding.openpicker.text.toString()
                                    val closegate = binding.closepicker.text.toString()

                                    checktext(
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
                                            this@AddHostelActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@AddHostelActivity,
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
                        this@AddHostelActivity,
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
                                    if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()
                                    ) {
                                        val opengate = binding.openpicker.text.toString()
                                        val closegate = binding.closepicker.text.toString()

                                        checktext(
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
                                                this@AddHostelActivity,
                                                "Please pick gate open time",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog!!.dismiss()
                                        }
                                        if (binding.closepicker.text.toString().isEmpty()) {
                                            binding.openpicker.error =
                                                "Please pick gate close time"
                                            Toast.makeText(
                                                this@AddHostelActivity,
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
                                    this@AddHostelActivity,
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
                                            this@AddHostelActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@AddHostelActivity,
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
                            this@AddHostelActivity,
                            "Please enter extra charges details",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (radioButton22.text == "No") {
                    val extra = "No extra charges will taken"

                    if (radioButton33.text == "Agreement will be done") {
                        if (!binding.periodtime.text.toString().isEmpty()) {
                            val period = binding.periodtime.text.toString().toInt()

                            //    int[] array = { period };
                            val policy = binding.yesagreetext.text.toString()
                            if (radioButton44.text == "Yes") {
                                if (binding.openpicker.text.toString().isNotEmpty() && binding.closepicker.text.toString().isNotEmpty()) {
                                    val opengate = binding.openpicker.text.toString()
                                    val closegate = binding.closepicker.text.toString()

                                    checktext(
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
                                            this@AddHostelActivity,
                                            "Please pick gate open time",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog!!.dismiss()
                                    }
                                    if (binding.closepicker.text.toString().isEmpty()) {
                                        binding.openpicker.error =
                                            "Please pick gate close time"
                                        Toast.makeText(
                                            this@AddHostelActivity,
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
                                this@AddHostelActivity,
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
                                    .isNotEmpty()) {
                                val opengate = binding.openpicker.text.toString()
                                val closegate = binding.closepicker.text.toString()

                                checktext(
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
                                        this@AddHostelActivity,
                                        "Please pick gate open time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog!!.dismiss()
                                }
                                if (binding.closepicker.text.toString().isEmpty()) {
                                    binding.openpicker.error = "Please pick gate close time"
                                    Toast.makeText(
                                        this@AddHostelActivity,
                                        "Please pick gate close time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog!!.dismiss()
                                }
                            }
                        } else if (radioButton44.text == "No") {
                            val opengate = "No"
                            val closegate = "No"

                            checktext(subtype, deposit, extra, period, policy, opengate, closegate)
                        }
                    }
                }
            }
        }
        binding.capturelocation2.setOnClickListener {
            checkpermission()
            dialog1!!.show()
        }
        binding.mapview.setOnClickListener {
            val intent = Intent(this@AddHostelActivity, MapsActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("name", "Your residency name will fetch here")
            startActivity(intent)
        }
        binding.openpicker.setOnClickListener {
            val timePicker = TimePickerDialog(
                this@AddHostelActivity,
                timePickerDialogListener1,
                12,
                10,
                false
            )
            timePicker.show()
        }
        binding.closepicker.setOnClickListener {
            val timePicker = TimePickerDialog(
                this@AddHostelActivity,
                timePickerDialogListener,
                12,
                10,
                false
            )
            timePicker.show()
        }
    }


    private fun checktext(
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
            if (newlist.isEmpty()) {
                dialog!!.dismiss()
                Toast.makeText(this@AddHostelActivity, "Please select images", Toast.LENGTH_SHORT)
                    .show()
            } else if (newlist.isNotEmpty()) {
                if (newlist.size < 11) {
                    mainum = newlist.size
                    uploadimages(
                        subtypez,
                        depositz,
                        extraz,
                        periodz,
                        policyz,
                        opengatez,
                        closegatez
                    )
                } else {
                    Toast.makeText(
                        this@AddHostelActivity,
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
                    this@AddHostelActivity,
                    "Please enter residency name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (address.isEmpty()) {
                dialog!!.dismiss()

                binding.resiaddress.error = "Please enter residency address"
                Toast.makeText(
                    this@AddHostelActivity,
                    "Please enter residency address",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (oname.isEmpty()) {
                dialog!!.dismiss()

                binding.oname.error = "Please enter residency operator name"
                Toast.makeText(
                    this@AddHostelActivity,
                    "Please enter residency operator name",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (contact.isEmpty()) {
                dialog!!.dismiss()

                binding.contact.error = "Please enter contact number"
                Toast.makeText(
                    this@AddHostelActivity,
                    "Please enter contact number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (whatsapp.isEmpty()) {
                dialog!!.dismiss()

                binding.whatsapp.error = "Please enter whatsapp number"
                Toast.makeText(
                    this@AddHostelActivity,
                    "Please enter whatsapp number",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (rent.isEmpty()) {
                dialog!!.dismiss()

                binding.rentamount.error = "Please enter monthly rent"
                Toast.makeText(
                    this@AddHostelActivity,
                    "Please enter monthly rent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun uploadimages(
        subtypex: String,
        depositx: String,
        extrax: String,
        periodx: Int,
        policyx: String,
        opengatex: String,
        closegatex: String
    ) {
        mainum = newlist.size
        binding.numbertext.text = "If Loading Takes to long press button again"
        val ImageFolder = FirebaseStorage.getInstance().reference.child("Nanded")
            .child(FirebaseAuth.getInstance().uid!!)
            .child("HostelImage")


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
                    newStrings.add(uri.toString() )
                    if (newStrings.size == newlist.size) {
                        storeLink(
                            newStrings,
                            mainum,
                            subtypex,
                            depositx,
                            extrax,
                            periodx,
                            policyx,
                            opengatex,
                            closegatex
                        )
                    }
                }
            }

            upload_count++
        }
    }


    private fun storeLink(
        newStrings: MutableList<String>,
        mainum: Int,
        subtypex: String,
        depositx: String,
        extrax: String,
        periodx: Int,
        policyx: String,
        opengatex: String,
        closegatex: String
    ) {
        val hashMap = HashMap<String, String>()

        var i = 0
        while (i < newlist.size) {
            hashMap["image$i"] = newStrings[i].toString()
            i++
        }


        val type = "Hostel"
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
            val data = FirebaseAuth.getInstance().uid?.let { it1 ->
                AddHostelClass(
                    "Active",
                    "Hostel",
                    "Hostel",
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
                    depositx,
                    extrax,
                    more,
                    policyx,
                    opengatex,
                    closegatex,
                    it1,
                    newDocID,
                    "",
                    "",
                    "",
                    7028,
                    mainum,
                    periodx,
                    19.114591,
                    77.291456,
                    date.time
                )
            }
            if (data != null) {
                toolsCollectionRef2.document(newDocID).set(data).addOnSuccessListener {
                    val myresi = SingleIDClass(
                        newDocID,
                        "Hostel",
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
                            this@AddHostelActivity,
                            "Data Uploaded Successfully, please refresh",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog!!.dismiss()
                        finish()
                    }.addOnFailureListener { e ->
                        dialog!!.dismiss()
                        Toast.makeText(
                            this@AddHostelActivity,
                            "" + e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@AddHostelActivity,
                        "" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog!!.dismiss()
                }
            }
        }.addOnFailureListener { e ->
            dialog!!.dismiss()
            Toast.makeText(this@AddHostelActivity, "" + e.message, Toast.LENGTH_SHORT)
                .show()
        }

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
                        this@AddHostelActivity,
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
        val facility = FacilityClass(
            clean = a1,
            ac = a2,
            rowater = a3,
            water = a4,
            wifi = a5,
            cctv = a6,
            bed = a7,
            hotwater = a8,
            table = a9,
            locker = a10,
            fan = a11,
            powerbackup = a12,
            washing = a13,
            security = a14,
            inout = a15,
            attach = a16,
            shower = a17,
            parking = a18,
            mess = a19,
            tv = a20,
            gas = a21,
            dining = a22,
            refrigerator = a23,
            sofa = a24,
            elevator = a25,
            play = a26,
            gym = a27,
            studyroom = a28,
            kitchen = a29,
            balcony = a30,
            indian = a31,
            western = a32,
            terrace = a33,
            furnished = a34,
            more = binding.facility.text.toString()
        )


        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllFacility").document(newDocID).set(facility)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@AddHostelActivity,
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

        val rules = RulesClass(
            clean = a01,
            trouble = a02,
            licence = a03,
            gateentry = a04,
            alcohol = a05,
            damage = a06,
            outsiders = a07,
            permission = a08,
            more = binding.rules.text.toString()
        )

        FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
            .collection("AllRule").document(newDocID).set(rules)
            .addOnSuccessListener { }.addOnFailureListener {
                Toast.makeText(
                    this@AddHostelActivity,
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
                this@AddHostelActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this@AddHostelActivity as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    companion object {
        //new
        private const val PICK_IMAGE = 1


        private const val READ_STORAGE_PERMISSION_REQUEST_CODE = 41
    }   private fun toast(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}