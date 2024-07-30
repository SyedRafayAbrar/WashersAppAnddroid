package com.koraspond.washershub.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.koraspond.washershub.R
import com.koraspond.washershub.databinding.ActivityMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var currentLocation: LatLng? = null
    private var marker: Marker? = null
    var autocompleteSupportFragment1: AutocompleteSupportFragment? = null
    var mapView: SupportMapFragment? = null
    var selLatitude = 0.0
    var selLongitude = 0.0
    private var isMapPressed = false
    var pbLoadingDialog: ProgressDialog? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        var staticLatitude: Double = 0.0
        var staticLongitude: Double = 0.0
        var staticNickName: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initClicks()
    }

    fun init() {
        pbLoadingDialog = ProgressDialog(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }
        autocompleteSupportFragment1 =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment1) as AutocompleteSupportFragment?

        mapView = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView!!.getMapAsync(this)
        openDialog()
    }

    fun initClicks() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSaveLocation.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("latitude", selLatitude)
            resultIntent.putExtra("longitude", selLongitude)
            resultIntent.putExtra("address", binding.txtLocationAddress.text.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.currentLocation.setOnClickListener {
            if (checkPermissions()) {
                requestLocationPermission()
            } else {
                showCurrentLocation()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (checkPermissions()) {
            requestLocationPermission()
        } else {
            showCurrentLocation()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission", "StringFormatInvalid")
    private fun showCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)

                        mMap.clear()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.latitude, it.longitude
                                ), 16f
                            )
                        )

                        if (marker == null) {
                            marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(currentLocation!!)
                                    .title("Current Location")
                                    .icon(getMarkerIconFromDrawable(R.drawable.marker))
                                    .draggable(true)
                            )
                        } else {
                            marker!!.position = currentLocation!!
                        }

                        GlobalScope.launch {
                            fetchLocation(it)
                        }
                    }
                }.addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        this,
                        getString(R.string.failed_to_get_location, e.localizedMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                mMap.setOnMapClickListener {
                    isMapPressed = true
                }

                mMap.setOnCameraIdleListener {
                    if (!isMapPressed) {
                        val target = mMap.cameraPosition.target
                        val latitude = target.latitude
                        val longitude = target.longitude
                        val location = Location("").apply {
                            this.latitude = latitude
                            this.longitude = longitude
                        }

                        GlobalScope.launch {
                            fetchLocation(location)
                        }
                    }
                    isMapPressed = false
                }

                mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDragStart(marker: Marker) {
                        // Do something when the drag starts
                    }

                    override fun onMarkerDrag(marker: Marker) {
                        // Do something when the marker is being dragged
                    }

                    override fun onMarkerDragEnd(marker: Marker) {
                        val position = marker.position
                        selLatitude = position.latitude
                        selLongitude = position.longitude

                        val location = Location("").apply {
                            latitude = position.latitude
                            longitude = position.longitude
                        }

                        GlobalScope.launch {
                            fetchLocation(location)
                        }
                    }
                })
            } else {
                Toast.makeText(this, getString(R.string.please_turn_on_location), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMarkerIconFromDrawable(drawableResId: Int): BitmapDescriptor {
        val drawable: Drawable? = ContextCompat.getDrawable(this, drawableResId)
        drawable?.let {
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
            canvas.setBitmap(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
        throw IllegalArgumentException("Invalid drawable resource id: $drawableResId")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation()
            } else {
                Toast.makeText(
                    this, getString(R.string.location_permission_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun openDialog() {
        autocompleteSupportFragment1!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL,
            )
        )

        autocompleteSupportFragment1!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val name = place.name
                val address = place.address
                val latlng = place.latLng
                val latitude = latlng?.latitude
                val longitude = latlng?.longitude

                val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
                val list: MutableList<Address>? =
                    geocoder.getFromLocation(latitude!!, longitude!!, 1)
                selLatitude = list!![0].latitude
                selLongitude = list!![0].longitude
                binding.txtCityName!!.text = name
                binding.txtLocationAddress!!.text = address

                if (marker == null) {
                    marker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(latitude, longitude))
                            .title(getString(R.string.current_location))
                            .icon(getMarkerIconFromDrawable(R.drawable.marker))
                            .draggable(true)
                    )
                } else {
                    marker!!.position = LatLng(latitude, longitude)
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16f))
            }

            override fun onError(status: Status) {
                Toast.makeText(
                    applicationContext, "Some error occurred.. " + status, Toast.LENGTH_SHORT
                ).show()

                binding.txtCityName.text = status.statusMessage
            }
        })
    }

    @SuppressLint("StringFormatInvalid")
    suspend fun fetchLocation(location: Location) {
        val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
        try {
            val addressList = withContext(Dispatchers.IO) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val addressText = address.getAddressLine(0)
                selLatitude = address.latitude
                selLongitude = address.longitude

                withContext(Dispatchers.Main) {
                    binding.txtCityName.text = address.locality
                    binding.txtLocationAddress.text = addressText
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@MapActivity, getString(R.string.failed_to_get_location, e.localizedMessage), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
