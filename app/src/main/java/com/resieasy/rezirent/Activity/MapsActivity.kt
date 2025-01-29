package com.resieasy.rezirent.Activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.resieasy.rezirent.R
import com.resieasy.rezirent.databinding.ActivityMapsBinding

class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var binding: ActivityMapsBinding? = null
    private var mMap: GoogleMap? = null

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        latitude = intent.getDoubleExtra("latitude", 12.0)
        longitude = intent.getDoubleExtra("longitude", 12.0)
        name = intent.getStringExtra("name")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isMyLocationButtonEnabled = true

        // Add a marker in Sydney and move the camera
        //19.1715791,75.8780116
        val sydney = LatLng(latitude, longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title(name))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(12f))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
    }
}