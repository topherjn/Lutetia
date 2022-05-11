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
                    updateLocationTextBox(location)
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
        Log.d("STARTLOCATION","return called")
        client!!.requestLocationUpdates(locationRequest!!, locationCallback!!, Looper.getMainLooper())

        // get last location - sometimes makes things faster, but it's last known location
        client!!.lastLocation
            .addOnSuccessListener { location: Location? -> updateLocationTextBox(location!!) }

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

    // after getting the location, use Geocoder library to get postcode
    // last two digits of Parisian postcodes correspond to the arrondissement
    // 1 - 20
    // when not in Paris mod the postcode to fit in 1 - 20 for built-in testing
    private fun updateLocationTextBox(lastLocation: Location) {
        //Toast.makeText(requireContext(), "updateLocationTextBox", Toast.LENGTH_SHORT).show()
        val geocoder = Geocoder(context)

        try {
            val addresses =
                geocoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1)
            var postalCode = addresses[0].postalCode

            Log.d("STARTLOCATION",postalCode.toString())
            //Toast.makeText(requireContext(), postalCode.toString(), Toast.LENGTH_LONG).show()
            if (postalCode.length > 1) {
                postalCode = postalCode.substring(postalCode.length - 2)
                if (postalCode[0] == '0') postalCode = postalCode.substring(postalCode.length - 1)
            }
            var arrondissement = postalCode.toInt()
            if(arrondissement > 20) arrondissement = arrondissement.mod(20) + 1

            // set textview to arrondissement and  make clickable to get site list
            binding.arrondissementTextView.text = arrondissement.toString()
            binding.arrondissementTextView.isEnabled = true

            val action = LocationFragmentDirections.actionLocationFragmentToSiteListFragment(arrondissement)
            binding.arrondissementTextView.setOnClickListener { view -> view.findNavController().navigate(action)}

        } catch (e: Exception) {
            //Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            binding.arrondissementTextView.text = e.message
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d("STARTLOCATION","onResume called")
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