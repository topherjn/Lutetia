package com.topherjn.lutetia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.topherjn.lutetia.SiteAdapter.SiteViewHolder
import com.topherjn.lutetia.database.Site
import com.topherjn.lutetia.databinding.SiteItemBinding

class SiteAdapter(private val onItemClicked: (Site) -> Unit): ListAdapter<Site,
        SiteViewHolder>(DiffCallback) {

    class SiteViewHolder(private var binding: SiteItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(site: Site) {
            binding.siteNameTextView.text = site.siteName
            binding.siteNotesTextView.text = site.notes
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Site>() {
            override fun areItemsTheSame(oldItem: Site, newItem: Site): Boolean {
                return oldItem.siteId == newItem.siteId
            }

            override fun areContentsTheSame(oldItem: Site, newItem: Site): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteViewHolder {
        val viewHolder = SiteViewHolder(
            SiteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: SiteViewHolder, position: Int) {
        holder.bind(getItem(position))

    }
}