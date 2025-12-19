package com.masselis.tpmsadvanced.feature.background.interfaces

import android.Manifest.permission.BLUETOOTH_SCAN
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.startForegroundService
import androidx.startup.Initializer
import com.masselis.tpmsadvanced.core.common.AppContextInitializer
import com.masselis.tpmsadvanced.core.common.appContext
import com.masselis.tpmsadvanced.data.vehicle.ioc.DataVehicleComponent
import com.masselis.tpmsadvanced.feature.background.interfaces.MonitorService.Companion.intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

public class FeatureBackgroundInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val backgroundPreferences = BackgroundPreferences(context)
        
        // Use a coroutine to restore monitoring state for vehicles
        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            // Get the list of all vehicles
            val vehicles = DataVehicleComponent.vehicleDatabase
                .selectAll()
                .asFlow()
                .firstOrNull()
                ?: emptyList()

            vehicles.forEach { vehicle ->
                // Check if monitoring was previously enabled for this vehicle
                if (backgroundPreferences.isMonitoringEnabled(vehicle.uuid)) {
                    // Check if required permissions are granted
                    val hasNotificationPermission = if (SDK_INT >= TIRAMISU) {
                        checkSelfPermission(appContext, POST_NOTIFICATIONS) == PERMISSION_GRANTED
                    } else true

                    val hasBluetoothPermission = if (SDK_INT >= UPSIDE_DOWN_CAKE) {
                        checkSelfPermission(appContext, BLUETOOTH_SCAN) == PERMISSION_GRANTED
                    } else true

                    // Only restart service if permissions are still granted
                    if (hasNotificationPermission && hasBluetoothPermission) {
                        startForegroundService(appContext, intent(vehicle))
                    }
                }
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        AppContextInitializer::class.java
    )
}
