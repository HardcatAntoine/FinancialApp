package com.creativeminds.imaginationworld.fantasticodyssey.api

import android.content.Context
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.telephony.TelephonyManager
import com.creativeminds.imaginationworld.fantasticodyssey.data.ParamsForQuery
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.my.tracker.MyTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ParamsHelper {

    suspend fun generateLink(context: Context): ParamsForQuery {
        val p1 = getSimCountryIso(context) ?: "null"
        val p2 = getFirebaseRemoteConfigValue() ?: "null"
        val p3 = isDeviceRooted() ?: "null"
        val p4 = getAppLocale().toString() ?: "null"
        val p5 = Store.dataMap[Store.APPMETRICA_KEY] ?: "null"
        val p6 = getAndroidID(context) ?: "null"
        val p7 = getFirebaseMessagingToken() ?: "null"
        val p8 = getGAID(context).toString() ?: "null"
        val p9 = MyTracker.getInstanceId(context) ?: "null"
        val p10 = getAppVersionCode(context).toString() ?: "null"
        val p11 = getUserMCC(context) ?: "null"
        val p12 = getUserMNC(context) ?: "null"
        val p13 = getBatteryLevel(context).toString() ?: "null"
        val p14 = getWifiSignalLevel(context) ?: "null"

        return ParamsForQuery(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
    }

    private fun getSimCountryIso(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountryIso = telephonyManager.simCountryIso
        return if (simCountryIso.isNullOrEmpty()) null else simCountryIso
    }

    private suspend fun getFirebaseRemoteConfigValue(): String? {
        return suspendCoroutine { continuation ->
            FirebaseRemoteConfig.getInstance().fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val color = FirebaseRemoteConfig.getInstance().getString("color")
                    continuation.resume(color)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }


    private suspend fun getFirebaseMessagingToken(): String? {
        return suspendCoroutine { continuation ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    continuation.resume(null)
                }
            }
        }
    }

    suspend fun getMyTrackerAttribution(): String? {
        return suspendCoroutine { continuation ->
            MyTracker.AttributionListener { attribution ->
                if (attribution.deeplink != null) {
                    continuation.resume(attribution.deeplink.toString())
                } else {
                    continuation.resume("")
                }
            }
        }
    }

    private fun isDeviceRooted(): String {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
            "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return "granted"
        }
        return "null"
    }


    private fun getAppLocale(): java.util.Locale {
        return java.util.Locale.getDefault()
    }


    private fun getAndroidID(context: Context): String {
        val androidID = android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        )
        return androidID ?: ""
    }


    private suspend fun getGAID(context: Context) = withContext(Dispatchers.IO) {
        AdvertisingIdClient.getAdvertisingIdInfo(context).id
    }


    private fun getAppVersionCode(context: Context): Int {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionCode
    }

    private fun getUserMCC(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator = telephonyManager.networkOperator
        return if (networkOperator.length >= 3) networkOperator.substring(0, 3) else null
    }

    private fun getUserMNC(context: Context): String? {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator = telephonyManager.networkOperator
        return if (networkOperator.length >= 5) networkOperator.substring(3, 5) else null
    }

    private fun getBatteryLevel(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun getWifiSignalLevel(context: Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val signalLevel = wifiInfo.rssi
        return if (signalLevel != 0) {
            signalLevel.toString()
        } else {
            "phone_internet"
        }
    }
}