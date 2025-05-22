package com.resieasy.rezirent.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.Continue.with
import com.onesignal.ContinueResult
import com.onesignal.OneSignal
import com.onesignal.OneSignal.initWithContext
 import com.onesignal.debug.LogLevel

import com.resieasy.rezirent.Activity.AddHostelActivity
import com.resieasy.rezirent.Activity.AddResidencyActivity
import com.resieasy.rezirent.Activity.AddSellResiActivity
import com.resieasy.rezirent.Activity.RoomDB.DAO.HostelRoomDBClass
import com.resieasy.rezirent.Activity.RoomDB.DAO.ResiRoomViewmodel
import com.resieasy.rezirent.Adapter.BothResiiAdapter
import com.resieasy.rezirent.Adapter.HostelHoriAdapter
import com.resieasy.rezirent.Adapter.RentHoriAdapter
import com.resieasy.rezirent.Adapter.SellHoriAdapter
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Class.BothResiClass
import com.resieasy.rezirent.Class.SellResiClass
import com.resieasy.rezirent.Factory.MainActivityViewModelFactory
import com.resieasy.rezirent.R
import com.resieasy.rezirent.Repository.MainActivityRepository
import com.resieasy.rezirent.ViewModel.MainActivityViewModel
import com.resieasy.rezirent.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.function.Consumer
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
   lateinit var binding: ActivityMainBinding 
    var auth: FirebaseAuth? = null

     private var list1: ArrayList<SellResiClass>? = null
    private var list2: ArrayList<AddFlatClass>? = null
    private var list3: ArrayList<AddHostelClass>? = null

    var adapter: BothResiiAdapter? = null

   private var adapter1: SellHoriAdapter? = null
   private var adapter2: RentHoriAdapter? = null
   private var adapter3: HostelHoriAdapter? = null
   private var UPDATE_CODE: Int = 8888
    private var appUpdateManager: AppUpdateManager? = null

    private val resiRoomViewmodel: ResiRoomViewmodel by viewModels()
     private val mainActivityViewModel: MainActivityViewModel by viewModels()

   //  private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(
            this
        ) { }

        //val viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        //val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                binding.adView.loadAd(adRequest)
            }
        }


        inAppUpdate()
        //onesignal()


        list1 = ArrayList()
        adapter1 = SellHoriAdapter(this@MainActivity, list1!!)
        binding.sellhorihomerec.adapter = adapter1

        val layoutManager1 =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.sellhorihomerec.layoutManager = layoutManager1

        list2 = ArrayList()
        adapter2 = RentHoriAdapter(this@MainActivity, list2!!)
        binding.renthorihomerec.adapter = adapter2

        val layoutManager2 =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.renthorihomerec.layoutManager = layoutManager2

        list3 = ArrayList()
        adapter3 = HostelHoriAdapter(this@MainActivity, list3!!)
        binding.hostelhorihomerec.adapter = adapter3

        val layoutManager3 =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.hostelhorihomerec.layoutManager = layoutManager3

        binding.hostelshimmer.visibility = View.VISIBLE
        binding.hostelshimmer.startShimmer()
        binding.rentshimmer.visibility = View.VISIBLE
        binding.rentshimmer.startShimmer()
        binding.sellshimmer.visibility = View.VISIBLE
        binding.sellshimmer.startShimmer()


      /*  val firebaseFirestore = FirebaseFirestore.getInstance()
        val repository = MainActivityRepository(firebaseFirestore)
        mainActivityViewModel = ViewModelProvider(this, MainActivityViewModelFactory(repository)).get(MainActivityViewModel::class.java)
*/


       if (isInternetAvailable(this)) {
           Toast.makeText(this, "Internet Available", Toast.LENGTH_SHORT).show()
           //online
           mainActivityViewModel.hostePG.observe(this) { users: List<AddHostelClass>? ->
               if (users != null) {
                   binding.hostelshimmer.visibility = View.GONE
                   binding.hostelshimmer.stopShimmer()
                   adapter3!!.updateList(ArrayList(users))

                   val convertedList = users.map { it.toHostelRoomDBClass() }
                   resiRoomViewmodel.saveToLocalRoom(convertedList)
               } else {
                   Log.d("MainActivity2", "No users observed.")
               }
           }
           mainActivityViewModel.loadHostelPG()
       } else {
           //offline
           Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show()
           resiRoomViewmodel.offlineResidencies.observe(this) { list ->
               if (!list.isNullOrEmpty()) {
                   binding.hostelshimmer.visibility = View.GONE
                   binding.hostelshimmer.stopShimmer()
                   val convertedList = list.map { it.toAddHostelClass() }
                   adapter3!!.updateList(ArrayList(convertedList))

                }
           }
           resiRoomViewmodel.loadFromRoom()
       }







        mainActivityViewModel.rent.observe(
            this
        ) { users: List<AddFlatClass> ->
            binding.rentshimmer.visibility = View.GONE
            binding.rentshimmer.stopShimmer()

            Log.d("MainActivity2", "Observed users: $users")
            adapter2!!.updateList(ArrayList(users))
        }
        mainActivityViewModel.loadRent()


        mainActivityViewModel.sell.observe(
            this
        ) { users: List<SellResiClass> ->
            binding.sellshimmer.visibility = View.GONE
            binding.sellshimmer.stopShimmer()


            Log.d("MainActivity2", "Observed users: $users")
            adapter1!!.updateList(ArrayList(users))
        }
        mainActivityViewModel.loadSell()



        binding.allresibottom.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "All")
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.search12.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "All")
            startActivity(intent)
        }


        binding.toprent.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "Rent")
            startActivity(intent)
        }
        binding.topsell.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "Sell")
            startActivity(intent)
        }
        binding.tophostelpg.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "Hostel")
            startActivity(intent)
        }
        binding.topallproperty.setOnClickListener {
            val intent = Intent(this@MainActivity, BothResiiActivity::class.java)
            intent.putExtra("topquery", "All")
            startActivity(intent)
        }
        binding.rentcart.setOnClickListener {
            val intent = Intent(this@MainActivity, AddResidencyActivity::class.java)
            startActivity(intent)
        }
        binding.imageView.setOnClickListener { }
        binding.sellcart.setOnClickListener {
            val intent = Intent(this@MainActivity, AddSellResiActivity::class.java)
            startActivity(intent)
        }
        binding.hostelcart.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHostelActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isInternetAvailable(context: MainActivity): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /*private fun onesignal() {
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(
            true,
            with<Boolean>(Consumer<ContinueResult<Boolean>> { r: ContinueResult<Boolean> ->
                if (r.isSuccess) {
                    if (r.data!!) {
                        // `requestPermission` completed successfully and the user has accepted permission
                    } else {
                        // `requestPermission` completed successfully but the user has rejected permission
                    }
                } else {
                    // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                }
            })
        )
    }*/


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Exit")
            .setMessage("Are you sure you want to close application ?")
            .setIcon(R.drawable.warna)
            .setCancelable(true)
            .setPositiveButton(
                "Yes"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                finishAffinity()
            }.setNegativeButton(
                "No"
            ) { dialogInterface, i -> dialogInterface.dismiss() }.create().show()
    }


    //In All Update Code
    private fun inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)

        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@MainActivity,
                        UPDATE_CODE
                    )
                } catch (e: SendIntentException) {
                    throw RuntimeException(e)
                }
            }
        }
        appUpdateManager!!.registerListener(listener)
    }

    var listener: InstallStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            popupSnackbarForCompleteUpdate()
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar =
            Snackbar.make(
                findViewById(android.R.id.content),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            )
        snackbar.setAction(
            "RESTART"
        ) { appUpdateManager!!.completeUpdate() }

        snackbar.setActionTextColor(
            resources.getColor(R.color.black)
        )
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
            }
        }
    }

    companion object {
        private const val ONESIGNAL_APP_ID = "105b8d9e-51ac-45d4-bed8-c23bbe105b32"
    }
    fun HostelRoomDBClass.toAddHostelClass(): AddHostelClass {
        return AddHostelClass(
            mail = mail,
            status = status,
            rtype = rtype,
            type = type,
            subtype = subtype,
            name = name,
            lowercase = lowercase,
            address = address,
            area = area,
            oname = oname,
            number = number,
            whatsapp = whatsapp,
            rent = rent,
            erent = erent,
            deposit = deposit,
            extra = extra,
            more = more,
            policy = policy,
            gopen = gopen,
            gclose = gclose,
            userid = userid,
            id = id,
            f1 = f1,
            f2 = f2,
            f3 = f3,
            i1 = i1,
            input = input,
            period = period,
            latitude = latitude,
            longitude = longitude,
            time = time
        )
    } fun  AddHostelClass.toHostelRoomDBClass(): HostelRoomDBClass {
        return HostelRoomDBClass(
            mail = mail.toString(),
            status = status,
            rtype = rtype,
            type = type,
            subtype = subtype,
            name = name,
            lowercase = lowercase,
            address = address,
            area = area,
            oname = oname,
            number = number,
            whatsapp = whatsapp,
            rent = rent,
            erent = erent,
            deposit = deposit,
            extra = extra,
            more = more,
            policy = policy,
            gopen = gopen,
            gclose = gclose,
            userid = userid,
            id = id,
            f1 = f1,
            f2 = f2,
            f3 = f3,
            i1 = i1,
            input = input,
            period = period,
            latitude = latitude,
            longitude = longitude,
            time = time
        )
    }

}