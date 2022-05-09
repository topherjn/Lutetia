package com.topherjn.lutetia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteDao {
    @Query("SELECT * FROM site WHERE site_arrondissement = :arrondissement ORDER BY site_name")
    fun getSitesByArrondissement(arrondissement: Int): List<Site>

    @Query("SELECT * FROM site WHERE siteId = :siteId")
    fun getSiteBySiteId(siteId: Int): List<Site>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: Site)

    @Update
    suspend fun updateSite(site: Site)

    @Delete
    suspend fun delete(site: Site)
}
