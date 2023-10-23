package com.creativeminds.imaginationworld.fantasticodyssey

import android.app.Application
import android.util.Log
import com.creativeminds.imaginationworld.fantasticodyssey.api.ApiProvider
import com.yandex.metrica.AppMetricaDeviceIDListener
import com.yandex.metrica.ReporterConfig
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class BankApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApiProvider.getInstance(this)
        initAppMetrica()
    }

    private fun initAppMetrica() {
        val config = YandexMetricaConfig
            .newConfigBuilder(Store.dataMap[Store.APPMETRICA_KEY]!!)
            .build()
        val reporterConfig = ReporterConfig.newConfigBuilder(Store.dataMap[Store.APPMETRICA_KEY]!!)
            .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.activateReporter(applicationContext, reporterConfig)
        YandexMetrica.enableActivityAutoTracking(this)
        YandexMetrica.requestAppMetricaDeviceID(object : AppMetricaDeviceIDListener {
            override fun onLoaded(p0: String?) {
                Store.deviceId.put(Store.APPMETRICA_DEVICE_ID, p0.toString())
                Log.d("DeviceId", "AppMetricaDeviceId: $p0")
            }

            override fun onError(p0: AppMetricaDeviceIDListener.Reason) {
            }
        })
    }
}