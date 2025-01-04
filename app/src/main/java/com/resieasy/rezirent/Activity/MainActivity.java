package com.resieasy.rezirent.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.resieasy.rezirent.Adapter.BothResiiAdapter;
import com.resieasy.rezirent.Adapter.HostelHoriAdapter;
import com.resieasy.rezirent.Adapter.RentHoriAdapter;
import com.resieasy.rezirent.Adapter.SellHoriAdapter;
import com.resieasy.rezirent.Class.AddFlatClass;
import com.resieasy.rezirent.Class.AddHostelClass;
import com.resieasy.rezirent.Class.BothResiClass;
import com.resieasy.rezirent.Class.SellResiClass;
import com.resieasy.rezirent.Factory.MainActivityViewModelFactory;
import com.resieasy.rezirent.R;
import com.resieasy.rezirent.Repository.MainActivityRepository;
import com.resieasy.rezirent.ViewModel.MainActivityViewModel;
import com.resieasy.rezirent.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth  auth;
    ArrayList<Fragment> fragmentarrylist = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    ArrayList<BothResiClass> list;
    ArrayList<SellResiClass> list1;
    ArrayList<AddFlatClass> list2;
    ArrayList<AddHostelClass> list3;

    BothResiiAdapter adapter;

    SellHoriAdapter adapter1;
    RentHoriAdapter adapter2;
    HostelHoriAdapter adapter3;
    int UPDATE_CODE = 8888;
    AppUpdateManager appUpdateManager;
    MainActivityViewModel mainActivityViewModel;

    private static final String ONESIGNAL_APP_ID = "105b8d9e-51ac-45d4-bed8-c23bbe105b32";

    @SuppressLint("UseSupportActionBar")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                binding.adView.loadAd(adRequest);
            }
        });


        inAppUpdate();
        onesignal();


        list1 = new ArrayList<>();
        adapter1 = new SellHoriAdapter(MainActivity.this, list1);
        binding.sellhorihomerec.setAdapter(adapter1);

        LinearLayoutManager layoutManager1 =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.sellhorihomerec.setLayoutManager(layoutManager1);

        list2 = new ArrayList<>();
        adapter2 = new RentHoriAdapter(MainActivity.this, list2);
        binding.renthorihomerec.setAdapter(adapter2);

        LinearLayoutManager layoutManager2 =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.renthorihomerec.setLayoutManager(layoutManager2);

        list3 = new ArrayList<>();
        adapter3 = new HostelHoriAdapter(MainActivity.this, list3);
        binding.hostelhorihomerec.setAdapter(adapter3);

        LinearLayoutManager layoutManager3 =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        binding.hostelhorihomerec.setLayoutManager(layoutManager3);

        binding.hostelshimmer.setVisibility(View.VISIBLE);
        binding.hostelshimmer.startShimmer();
        binding.rentshimmer.setVisibility(View.VISIBLE);
        binding.rentshimmer.startShimmer();
        binding.sellshimmer.setVisibility(View.VISIBLE);
        binding.sellshimmer.startShimmer();


        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        MainActivityRepository repository= new MainActivityRepository(firebaseFirestore);
        mainActivityViewModel= new ViewModelProvider(this, new MainActivityViewModelFactory(repository)).get(MainActivityViewModel.class);



        mainActivityViewModel.getHostePG().observe(this, users -> {
            if (users != null) {
                // Stop shimmer and hide it
                binding.hostelshimmer.setVisibility(View.GONE);
                binding.hostelshimmer.stopShimmer();

                // Log observed data
                Log.d("MainActivity2", "Observed users: " + users);

                // Update the adapter's list
                adapter3.updateList(new ArrayList<>(users));
            } else {
                Log.d("MainActivity2", "No users observed.");
            }
        });

        mainActivityViewModel.loadHostelPG();

       /* FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Hostel")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            binding.hostelshimmer.setVisibility(View.GONE);
                            binding.hostelshimmer.stopShimmer();

                            list3.clear();
                            List<DocumentSnapshot> list111 = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d3 : list111) {
                                AddHostelClass data111 = d3.toObject(AddHostelClass.class);
                                list3.add(data111);

                            }
                            adapter3.notifyDataSetChanged();
                        }


                    }
                });*/

        mainActivityViewModel.getRent().observe(this, users -> {
            binding.rentshimmer.setVisibility(View.GONE);
            binding.rentshimmer.stopShimmer();

            Log.d("MainActivity2", "Observed users: " + users);
            adapter2.updateList(new ArrayList<>(users));
        });
      mainActivityViewModel.loadRent();

    /*    FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Rent")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            binding.rentshimmer.setVisibility(View.GONE);
                            binding.rentshimmer.stopShimmer();

                            list2.clear();
                            List<DocumentSnapshot> list22 = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d2 : list22) {
                                AddFlatClass data2 = d2.toObject(AddFlatClass.class);
                                list2.add(data2);

                            }
                            adapter2.notifyDataSetChanged();
                        }
                    }
                });*/

        mainActivityViewModel.getSell().observe(this, users -> {
            binding.sellshimmer.setVisibility(View.GONE);
            binding.sellshimmer.stopShimmer();


            Log.d("MainActivity2", "Observed users: " + users);
            adapter1.updateList(new ArrayList<>(users));
        });
        mainActivityViewModel.loadSell();


       /* FirebaseFirestore.getInstance().collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Sell")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            binding.sellshimmer.setVisibility(View.GONE);
                            binding.sellshimmer.stopShimmer();

                            list1.clear();
                            List<DocumentSnapshot> list11 = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d1 : list11) {
                                SellResiClass data1 = d1.toObject(SellResiClass.class);
                                list1.add(data1);

                            }
                            adapter1.notifyDataSetChanged();

                        }else {
                            binding.sellshimmer.setVisibility(View.GONE);
                            binding.sellshimmer.stopShimmer();
                        }

                    }
                });*/

        binding.allresibottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "All");
                startActivity(intent);


            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);


            }
        });
        binding.search12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "All");

                startActivity(intent);
            }
        });


        binding.toprent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "Rent");
                startActivity(intent);
            }
        });
        binding.topsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "Sell");

                startActivity(intent);
            }
        });
        binding.tophostelpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "Hostel");

                startActivity(intent);
            }
        });
        binding.topallproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BothResiiActivity.class);
                intent.putExtra("topquery", "All");

                startActivity(intent);
            }
        });
        binding.rentcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddResidencyActivity.class);
                startActivity(intent);
            }
        });
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        binding.sellcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSellResiActivity.class);
                startActivity(intent);
            }
        });
        binding.hostelcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddHostelActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onesignal() {
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
            if (r.isSuccess()) {
                if (r.getData()) {
                    // `requestPermission` completed successfully and the user has accepted permission
                }
                else {
                    // `requestPermission` completed successfully but the user has rejected permission
                }
            }
            else {
                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
            }
        }));


    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to close application ?")
                .setIcon(R.drawable.warna)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finishAffinity();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).create().show();
    }


    //In All Update Code
    private void inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        appUpdateManager.registerListener(listener);
    }

    InstallStateUpdatedListener listener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            popupSnackbarForCompleteUpdate();

        }
    };

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(
                getResources().getColor(R.color.black));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE) {
            if(resultCode != RESULT_OK){

            }


        }
    }
}