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

        val action = LocationFragmentDirections.actionLocationFragmentToSiteListFragment(4)

        binding.arrondissementTextView.text = "88"
        binding.arrondissementTextView.setOnClickListener { view -> view.findNavController().navigate(action)}

    }











}