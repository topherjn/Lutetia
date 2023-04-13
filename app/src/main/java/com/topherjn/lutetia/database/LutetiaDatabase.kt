package com.topherjn.lutetia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [Site::class], version = 2)
abstract class LutetiaDatabase: RoomDatabase() {
    abstract fun siteDao(): SiteDao

    companion object {
        @Volatile
        private var INSTANCE: LutetiaDatabase? = null

        fun getDatabase(context: Context): LutetiaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    LutetiaDatabase::class.java,
                    "lutetia_app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}