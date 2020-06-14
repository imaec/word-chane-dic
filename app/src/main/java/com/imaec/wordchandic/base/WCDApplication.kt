package com.imaec.wordchandic.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class WCDApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }
}