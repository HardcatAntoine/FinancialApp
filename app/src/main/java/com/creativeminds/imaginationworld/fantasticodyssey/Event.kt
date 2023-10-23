package com.creativeminds.imaginationworld.fantasticodyssey

import android.content.Context
import androidx.annotation.AnyThread
import com.appsflyer.AppsFlyerLib
import com.my.tracker.MyTracker

class Event {

    @AnyThread
    fun trackEventMyTracker(name: String) {
        MyTracker.trackEvent(name)
    }
    @AnyThread
    fun trackEventAppsFlyer(context: Context, name: String, eventParams: Map<String, String>?) {
        AppsFlyerLib.getInstance().logEvent(context, name, eventParams)
    }
}