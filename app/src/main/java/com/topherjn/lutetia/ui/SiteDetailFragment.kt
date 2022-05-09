package com.topherjn.lutetia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.topherjn.lutetia.LutetiaApplication
import com.topherjn.lutetia.R
import com.topherjn.lutetia.databinding.FragmentSiteDetailBinding
import com.topherjn.lutetia.databinding.FragmentSiteListBinding
import com.topherjn.lutetia.viewmodels.SiteViewModel
import com.topherjn.lutetia.viewmodels.SiteViewModelFactory
import kotlinx.coroutines.launch

class SiteDetailFragment : Fragment() {

    private var siteIdParam: Int? = null

    private var _binding: FragmentSiteDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SiteViewModel by activityViewModels {
        SiteViewModelFactory((activity?.application as LutetiaApplication).database.siteDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            siteIdParam = it.getInt("site_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Sites Details"

        lifecycle.coroutineScope.launch {
            val site = viewModel.getSite(siteIdParam!!)

            binding.siteNameEditText.setText(site.siteName)
            binding.siteArrondissementEditText.setText(site.arrondissement.toString())
            binding.siteNotesEditText.setText(site.notes)
        }
    }
}