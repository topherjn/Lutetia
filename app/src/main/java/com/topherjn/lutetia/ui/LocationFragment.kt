package com.topherjn.lutetia.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.topherjn.lutetia.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    // location objects
    private var client: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    private var isTracking = false

    // ui binding
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // change title in title bar
        (activity as AppCompatActivity).supportActionBar?.title = "Arrondissement"

        val arrondissements =
            arrayOf("Change Arrondissement","1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20")

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrondissements
        ).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.arrondissementSpinner.adapter = adapter
        }

        binding.arrondissementSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerChange()
            }
        }

        startLocationUpdates()
    }

    // turn location updating on
    private fun startLocationUpdates() {

        // fused location reference
        client = LocationServices.getFusedLocationProviderClient(requireContext())

        // create and configure location request
        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 5000
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.isWaitForAccurateLocation = true
        locationRequest!!.smallestDisplacement = 0f

        // create location callback and response to getting location
        // i.e. update the textview
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("STARTLOCATION","location gotten")
                    updateLocationTextBox(getArrondissement(location))
                }
            }
        }

        // get location permission (required)
        Log.d("STARTLOCATION","locationcallback")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            @Suppress("DEPRECATION")
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )

            return
        }


        // turn the location updates on
        client!!.requestLocationUpdates(locationRequest!!, locationCallback!!, Looper.getMainLooper())

        // get last location - sometimes makes things faster, but it's last known location
        client!!.lastLocation
            .addOnSuccessListener { location: Location? ->
                updateLocationTextBox(getArrondissement(location!!)) }

        isTracking = true
    }

    // turn off location updates
    private fun stopLocationUpdates() {
        if(isTracking) {
            client!!.removeLocationUpdates(locationCallback!!)
            client = null
            isTracking = false
        }
    }

    private fun getArrondissement(lastLocation: Location): Int {

        val geocoder = Geocoder(context)
        var arrondissement = 0

        try {
            val addresses =
                geocoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1)
            var postalCode = addresses[0].postalCode

            if (postalCode.length > 1) {
                postalCode = postalCode.substring(postalCode.length - 2)
                if (postalCode[0] == '0') postalCode = postalCode.substring(postalCode.length - 1)
            }
            arrondissement = postalCode.toInt()
            if(arrondissement > 20) arrondissement = arrondissement.mod(20) + 1


        } catch (e: Exception) {
            binding.arrondissementTextView.text = e.message
        }

        return arrondissement
    }

    // after getting the location, use Geocoder library to get postcode
    // last two digits of Parisian postcodes correspond to the arrondissement
    // 1 - 20
    // when not in Paris mod the postcode to fit in 1 - 20 for built-in testing
    private fun updateLocationTextBox(arrondissement: Int) {

            // set textview to arrondissement and  make clickable to get site list
            binding.arrondissementTextView.text = arrondissement.toString()
            binding.arrondissementTextView.isEnabled = true

            val action =
                LocationFragmentDirections.actionLocationFragmentToSiteListFragment(arrondissement)
            binding.arrondissementTextView
                .setOnClickListener { view -> view.findNavController().navigate(action)}
    }

    private fun spinnerChange() {
        val arrondissement = binding.arrondissementSpinner.selectedItem.toString()

        if(!arrondissement.equals("Change Arrondissement")) {
            binding.arrondissementTextView.setText(arrondissement)
            updateLocationTextBox(arrondissement.toInt())
        }
    }

    override fun onResume() {
        super.onResume()

        if(!isTracking)
            startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()

        stopLocationUpdates()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }
}