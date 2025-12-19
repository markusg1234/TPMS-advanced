package com.masselis.tpmsadvanced.feature.background.interfaces

import android.content.Context
import androidx.core.content.edit
import java.util.UUID

internal class BackgroundPreferences(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "BACKGROUND",
        Context.MODE_PRIVATE
    )

    fun isMonitoringEnabled(vehicleUuid: UUID): Boolean =
        sharedPreferences.getBoolean(vehicleUuid.toString(), false)

    fun setMonitoringEnabled(vehicleUuid: UUID, enabled: Boolean) {
        sharedPreferences.edit {
            putBoolean(vehicleUuid.toString(), enabled)
        }
    }
}
