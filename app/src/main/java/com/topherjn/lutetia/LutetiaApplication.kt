package com.topherjn.lutetia

import android.app.Application
import com.topherjn.lutetia.database.LutetiaDatabase

class LutetiaApplication: Application() {
    val database: LutetiaDatabase by lazy {LutetiaDatabase.getDatabase(this)}
}