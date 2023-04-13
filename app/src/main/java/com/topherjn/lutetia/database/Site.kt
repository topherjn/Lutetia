package com.topherjn.lutetia.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Site (
    @PrimaryKey(autoGenerate = true) val siteId: Int,
    @NonNull @ColumnInfo(name="site_name") var siteName: String,
    @NonNull @ColumnInfo(name="site_arrondissement") val arrondissement: Int,
    @ColumnInfo(name="site_url") val url: String?,
    @ColumnInfo(name="site_notes") val notes: String?
)
