package com.topherjn.lutetia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.topherjn.lutetia.database.Site
import com.topherjn.lutetia.database.SiteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SiteViewModel(private val siteDao: SiteDao): ViewModel() {

    fun getSites(arrondissement: Int): List<Site> = siteDao.getSitesByArrondissement(arrondissement)
}

class SiteViewModelFactory(private val siteDao: SiteDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SiteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SiteViewModel(siteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}