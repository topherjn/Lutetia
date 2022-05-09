package com.topherjn.lutetia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topherjn.lutetia.LutetiaApplication
import com.topherjn.lutetia.SiteAdapter
import com.topherjn.lutetia.databinding.FragmentSiteListBinding
import com.topherjn.lutetia.viewmodels.SiteViewModel
import com.topherjn.lutetia.viewmodels.SiteViewModelFactory
import kotlinx.coroutines.launch

class SiteListFragment : Fragment() {
    private var arrondissementParam: Int? = null

    private var _binding: FragmentSiteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: SiteViewModel by activityViewModels {
        SiteViewModelFactory((activity?.application as LutetiaApplication).database.siteDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arrondissementParam = it.getInt("arrondissement")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Sites in Arrondissement $arrondissementParam"

        recyclerView = binding.siteRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        val siteAdapter = SiteAdapter {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it.url)
            startActivity(intent)
        }

        recyclerView.adapter = siteAdapter

        siteAdapter.submitList(viewModel.getSites(arrondissementParam!!))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}