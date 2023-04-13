package com.topherjn.lutetia.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.topherjn.lutetia.database.Site
import com.topherjn.lutetia.database.SiteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SiteViewModel(private val siteDao: SiteDao) : ViewModel() {

    fun getSites(arrondissement: Int): List<Site> = siteDao.getSitesByArrondissement(arrondissement)

    fun getSite(siteId: Int): Site = siteDao.getSiteBySiteId(siteId)

    fun addSite(
        siteId: Int,
        siteName: String,
        arrondissement: Int,
        url: String,
        notes: String,
    ) {
        val site = Site(
            siteId = siteId,
            siteName = siteName,
            arrondissement = arrondissement,
            url = url,
            notes = notes
        )

        viewModelScope.launch(Dispatchers.IO) { siteDao.insertSite(site) }
    }

    fun updateSite(
        siteId: Int,
        siteName: String,
        arrondissement: Int,
        url: String,
        notes: String,
    ) {
        val site = Site(
            siteId = siteId,
            siteName = siteName,
            arrondissement = arrondissement,
            url = url,
            notes = notes
        )

        viewModelScope.launch(Dispatchers.IO) {
            siteDao.updateSite(site)
        }
    }


    fun deleteSite(site: Site) {
        viewModelScope.launch(Dispatchers.IO) {
            siteDao.delete(site)
        }
    }
}

class SiteViewModelFactory(private val siteDao: SiteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SiteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SiteViewModel(siteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}