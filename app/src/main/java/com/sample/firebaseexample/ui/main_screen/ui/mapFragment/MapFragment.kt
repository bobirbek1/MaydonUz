package com.sample.firebaseexample.ui.main_screen.ui.mapFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import com.sample.firebaseexample.model.map.CallFirebaseForMap
import com.sample.firebaseexample.model.maydonlar.CallFirebaseForMaydonlar
import com.sample.firebaseexample.model.models.ModelMaydon
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.LIST_MAYDONLAR
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.item_info_window.view.*
import kotlinx.android.synthetic.main.map_fragment.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001
const val ERROR_DIALOG_REQUEST: Int = 1002
const val REQUEST_CODE_RESOLUTION_CODE = 1003

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener,
    View.OnClickListener {

    private val FINELOCATION= Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSELOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var rootView: View
    private lateinit var locationCallback: LocationCallback
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var callFirebaseForMap: CallFirebaseForMap

    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson


    private var location: Location? = null
    private var mMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private var mLocationPermissionGranted: Boolean? = false

    private lateinit var listMaydonlar: ArrayList<ModelMaydon>


    private fun init() {
        if (isServiceOk()) {
            Log.d("supportMapFragment", "$activity")
            Log.d(
                "supportMapFragment",
                "${requireActivity().supportFragmentManager.findFragmentById(R.id.map)}"
            )
//            val mapFragment =
//                activity?.supportFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment
            rootView.map.getMapAsync(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.map_fragment, container, false)
        rootView.map.onCreate(savedInstanceState)
//        activity!!.toolbar.setNavigationOnClickListener(this)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: ((Location) -> Unit) = { loc ->
            setLocationWithZoom(loc)
        }
        preferences = activity?.getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )!!
        gson = Gson()
        listMaydonlar = arrayListOf()
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        callFirebaseForMap = CallFirebaseForMap()
        var count = 0
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                Log.d("MainActivity", "onLocationResult $result")
                if (result == null) return
                Log.d("MainActivity:", "${result.locations}")
                location = result.locations[0]
                if (count == 0) {
                    callback.invoke(location!!)
                    runBlocking {
                        launch{
                            getListMaydonlar()
                        }
                    }
                }
                count++
            }
        }
        setLocation(location)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        rootView.map.onResume()
        super.onResume()
        setLocation(location)
        init()
        getLocationPermission()
    }

    private fun isServiceOk(): Boolean {
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        when {
            available == ConnectionResult.SUCCESS -> {
                return true
            }
            GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                Log.d("GoogleApiAvailability", "isServiceOk: an error occured but we can fix it")
                val dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(activity, available, ERROR_DIALOG_REQUEST)
                dialog.show()
            }
            else -> {
                Toast.makeText(context, "you can't make map requests", Toast.LENGTH_SHORT).show()
            }
        }
        return false

    }

    private fun setLocationWithZoom(loc: Location){
        mLastKnownLocation = loc
        if (mLastKnownLocation != null) {
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mLastKnownLocation!!.latitude,
                        mLastKnownLocation!!.longitude
                    ),11F
                )
            )
        }
    }

    private fun setLocation(loc: Location?) {
        mLastKnownLocation = loc
        if (mLastKnownLocation != null) {
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        mLastKnownLocation!!.latitude,
                        mLastKnownLocation!!.longitude
                    )
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                FINELOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    COARSELOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationPermissionGranted = true
                checkLocationAvailibility()
            } else {
                requireActivity().requestPermissions(
                    arrayOf(FINELOCATION, COARSELOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        } else {
            requireActivity().requestPermissions(
                arrayOf(FINELOCATION, COARSELOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE_RESOLUTION_CODE && data?.data != null) {
            requestLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationAvailibility() {
        mFusedLocationProviderClient.locationAvailability
            .addOnSuccessListener {
                if (it.isLocationAvailable) {
                    requestLocation()
                } else {
                    setUpLocationService()
                }
            }
            .addOnFailureListener { setUpLocationService() }

    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val req = createLocationRequest()
        mFusedLocationProviderClient.requestLocationUpdates(
            req,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun createLocationRequest() = LocationRequest()
        .apply {
            interval = 10_000
            fastestInterval = 5_000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    private fun setUpLocationService() {
        val request = createLocationRequest()
        val settingRequest = LocationSettingsRequest.Builder()
        settingRequest.addLocationRequest(request)
        LocationServices
            .getSettingsClient(requireActivity())
            .checkLocationSettings(settingRequest.build())
            .addOnSuccessListener { requestLocation() }
            .addOnFailureListener(this::resolveLocationException)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {

            (0..grantResults.size).forEach {
                if (grantResults[it] != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context, "something went wrong!", Toast.LENGTH_SHORT).show()

                    return
                } else {
                    mLocationPermissionGranted = true
                    checkLocationAvailibility()
                }
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(map: GoogleMap?) {
        if (!mLocationPermissionGranted!!)
            return
        mMap = map
        mMap?.setOnMapLongClickListener(this)
        mMap?.setOnInfoWindowClickListener(this)
        mMap?.setOnMyLocationButtonClickListener(this)
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        Toast.makeText(context, "map ready!", Toast.LENGTH_SHORT).show()
        setDefaultLoc()
        runBlocking {
            launch {
                setInfoWindowAdapter()
            }
        }

//        val tashkent = LatLng(41.2995, 69.2401)
        updateLocationUI()
    }

    private fun setDefaultLoc() {
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(41.2995,69.2401),
            10F
        ))
    }

    private fun createMarker(
        loc: LatLng
    ) {
//        val bitmapDescriptor =
//            BitmapDescriptorFactory.fromBitmap(
//                getBitmapFromDrawable(getDrawable(context!!,R.drawable.ic_maps_and_flags)!!)
//            )


//        Log.d("MainActivity", "${activity!!.getDrawable( R.drawable.ic_maps_and_flags)}")
//        Log.d("createMarker:", "$loc")
        val markerOptions = MarkerOptions()
        markerOptions.position(loc)
        markerOptions.title("My choose location")
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_and_flags))
        mMap?.addMarker(markerOptions)
    }

//    private fun getBitmapFromDrawable(d: Drawable): Bitmap {
//        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
//        val bitmap = Bitmap.createBitmap(
//            d.intrinsicWidth,
//            d.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        canvas.setBitmap(bitmap)
//        d.draw(canvas)
//        return bitmap
//    }

    private fun setInfoWindowAdapter() {
        if (mMap != null) {

            mMap?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                @SuppressLint("InflateParams")
                override fun getInfoContents(marker: Marker?): View {
                    val view = layoutInflater.inflate(R.layout.item_info_window, null, false)
                    val pos = marker?.position
                    val callFirebaseStorage = CallFirebaseStorage()
                    for (model in listMaydonlar) {
                        if (LatLng(model.manzili!!.latitude, model.manzili.longitude) == pos) {
                            view.tv_info_window_nomi.text = model.nomi
                            view.info_window_bahosi.text = model.bahosi.toString()
                            view.info_window_ratingbar.rating = model.bahosi
                            view.tv_info_window_manzili.text = model.address
                            callFirebaseStorage.downloadNewsImages(
                                model.imagesPath[0],
                                view.iv_info_window_image,
                                context!!
                            )
                        }
                    }
                    return view
                }

                override fun getInfoWindow(marker: Marker?): View? {
                    return null
                }

            })
            mMap?.moveCamera(CameraUpdateFactory.zoomBy(15F))
        }
    }


    private fun resolveLocationException(it: Exception) {
        if (it is ResolvableApiException) {
            try {
                it.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION_CODE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onMapLongClick(latLng: LatLng?) {

    }

    override fun onMyLocationButtonClick(): Boolean {
        setLocation(location)
        Log.d("onMyLocationButtonClick", "$location")
        return true
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted!!) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMapToolbarEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mLastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {

    }

    private fun setAllMarkers(listLatlng: MutableList<LatLng>) {
        Log.d("setAllMarers", "$listLatlng")
        if (listLatlng.isNotEmpty()) {
            Log.d("setAllMarkers", "$listLatlng")
            for (latlng in listLatlng) {
                Log.d("createMarker:", "$latlng")
                createMarker(latlng)
            }
        }
    }

    //    override fun getDataCallback(response: MutableList<ModelMaydon>) {
//        listMaydonlar = response
//        val listPos : ArrayList<LatLng> = arrayListOf()
//        for (model in response){
//            listPos.add(LatLng(model.manzili!!.latitude,model.manzili.longitude))
//        }
//        setAllMarkers(listPos)
//    }
    private fun getListMaydonlar() {
        val type = object : TypeToken<ArrayList<ModelMaydon>>() {}.type
        val json = preferences.getString(LIST_MAYDONLAR, "")
Log.d("getJson","json : $json")
        val listPos: ArrayList<LatLng> = arrayListOf()
        if (json != null) {
            listMaydonlar = gson.fromJson(json, type)
            Log.d("listMaydonlar","list: $listMaydonlar")
            for (model in listMaydonlar) {
                listPos.add(LatLng(model.manzili!!.latitude, model.manzili.longitude))
            }
            setAllMarkers(listPos)
        } else {
            CallFirebaseForMaydonlar().getMaydonlar {
                        listMaydonlar = it
               val jsonString = gson.toJson(listMaydonlar)
                preferences.edit()
                    .putString(LIST_MAYDONLAR,jsonString)
                    .apply()
                for (model in listMaydonlar) {
                    listPos.add(LatLng(model.manzili!!.latitude, model.manzili.longitude))
                }
                setAllMarkers(listPos)
            }
        }
    }


    override fun onClick(v: View?) {
        requireActivity().drawer_layout.openDrawer(requireActivity().nav_view)
    }

}