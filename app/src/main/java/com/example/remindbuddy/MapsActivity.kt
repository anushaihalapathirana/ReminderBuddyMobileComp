package com.example.remindbuddy

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CircleOptions
import com.google.firebase.ktx.Firebase
import java.util.*

const val LOCATION_REQUEST_CODE = 123
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345
const val CAMERA_ZOOM_LEVEL = 13f
const val GEOFENCE_RADIUS = 500
const val GEOFENCE_ID = "REMINDER_GEOFENCE_ID"
const val GEOFENCE_EXPIRATION = 10*24*60*60*1000 //10 days
const val GEOFENCE_DWELL_DELAY = 10*1000 //to sec

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    lateinit var extras: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)

        extras = intent.extras!!

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        if (!isLocationPermmissionGranted()) {
            val permissions = mutableListOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                LOCATION_REQUEST_CODE
            )
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            mMap.isMyLocationEnabled = true
            // get last location
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    with(mMap) {
                        val latlang =
                            com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude)
                        moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, CAMERA_ZOOM_LEVEL))

                    }
                } else {
                    with(mMap) {
                        moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(6.74112,79.967110),
                                CAMERA_ZOOM_LEVEL
                            )
                        )
                    }
                }
            }
            setLongClick(mMap)
            setOnPOI(mMap)
        }

    }

    private fun setLongClick(mMap: GoogleMap) {
        mMap.setOnMapLongClickListener { latlang ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latlang.latitude, latlang.longitude
            )

            mMap.addMarker(
                MarkerOptions().position(latlang)
                    .title("Add")
                    .snippet(snippet)
            )

            //show circle
            mMap.addCircle(

                CircleOptions().center(latlang)
                    .strokeColor(Color.argb(50,70,70,70))
                    .fillColor(Color.argb(70,150,150,150))
                    .radius(GEOFENCE_RADIUS.toDouble())
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, CAMERA_ZOOM_LEVEL))

//            createGeofence(latlang, key!!, geofencingClient)

            startActivity(Intent(
                applicationContext,
                AddTaskActivity::class.java)
                .putExtra("source", "map")
                .putExtra("lat",latlang.latitude.toString())
                .putExtra("lng", latlang.longitude.toString())
                .putExtra("title", extras.getString("title") )
                .putExtra("message", extras.getString("message"))
                .putExtra("remindertime", extras.getString("remindertime"))
                .putExtra("reminderdate",extras.getString("reminderdate"))
                .putExtra("latlang", latlang)


            )
        }
    }

    private fun isLocationPermmissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setOnPOI(googleMap: GoogleMap) {
        googleMap.setOnPoiClickListener { poi ->
            val poiMarker = googleMap.addMarker(MarkerOptions().position(poi.latLng).title(poi.name))
        }
    }

}