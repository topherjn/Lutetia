package com.topherjn.lutetia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.topherjn.lutetia.LutetiaApplication
import com.topherjn.lutetia.R
import com.topherjn.lutetia.database.Site
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

            // set up read
            binding.siteNameEditText.setText(site.siteName)
            binding.siteArrondissementEditText.setText(site.arrondissement.toString())
            binding.urlEditText.setText(site.url)
            binding.siteNotesEditText.setText(site.notes)

            // set up delete
            binding.deleteSiteButton.setOnClickListener { deleteSite(site) }

            // set up create
            binding.newSiteButton.setOnClickListener { editUI(true)  }

            // set up update
        }
    }

    private fun deleteSite(site: Site) {
        viewModel.deleteSite(site)
        goToSiteList()
    }

    private fun editUI(isNewSite: Boolean) {
        if(isNewSite) {
            binding.siteNameEditText.setText("")
            binding.siteArrondissementEditText.setText("")
            binding.urlEditText.setText("")
            binding.siteNotesEditText.setText("")
        }

        binding.siteNameEditText.isEnabled = true
        binding.siteArrondissementEditText.isEnabled = true
        binding.urlEditText.isEnabled = true
        binding.urlEditText.isVisible = true
        binding.siteNotesEditText.isEnabled = true

        binding.newSiteButton.setText("Save")
        binding.newSiteButton.setOnClickListener { addSite()}
    }

    private fun addSite() {

        binding.newSiteButton.setText("New Site")
        binding.newSiteButton.setOnClickListener{editUI(true)}

       viewModel.addSite(
           0,
           binding.siteNameEditText.text.toString(),
           binding.siteArrondissementEditText.text.toString().toInt(),
           binding.urlEditText.text.toString(),
           binding.siteNotesEditText.text.toString()
       )
        goToSiteList()

    }

    private fun goToSiteList() {
        val action = SiteDetailFragmentDirections
            .actionSiteDetailFragmentToSiteListFragment(
                binding.siteArrondissementEditText.text.toString().toInt()
            )
        findNavController().navigate(action)
    }
}