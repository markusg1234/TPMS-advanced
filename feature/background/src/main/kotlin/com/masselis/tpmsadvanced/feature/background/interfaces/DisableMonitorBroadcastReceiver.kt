package com.masselis.tpmsadvanced.feature.background.interfaces

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.masselis.tpmsadvanced.core.common.appContext
import java.util.UUID

internal class DisableMonitorBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val vehicleUuid = intent.getStringExtra(EXTRA_VEHICLE_UUID)?.let { UUID.fromString(it) }
        appContext.stopService(Intent(appContext, MonitorService::class.java))
        
        // Clear the monitoring preference for this vehicle
        vehicleUuid?.let {
            BackgroundPreferences(context).setMonitoringEnabled(it, false)
        }
    }

    internal companion object {
        private const val EXTRA_VEHICLE_UUID = "VEHICLE_UUID"
        
        fun intent(vehicleUuid: UUID) = Intent(appContext, DisableMonitorBroadcastReceiver::class.java)
            .putExtra(EXTRA_VEHICLE_UUID, vehicleUuid.toString())
    }
}
