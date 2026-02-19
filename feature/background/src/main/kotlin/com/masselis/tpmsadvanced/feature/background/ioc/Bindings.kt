package com.masselis.tpmsadvanced.feature.background.ioc

import android.content.Context
import com.masselis.tpmsadvanced.data.vehicle.model.Vehicle
import com.masselis.tpmsadvanced.feature.background.interfaces.BackgroundPreferences
import com.masselis.tpmsadvanced.feature.background.interfaces.viewmodel.BackgroundViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@Suppress("unused")
@BindingContainer
internal object Bindings {
    @Provides
    @SingleIn(AppScope::class)
    private fun backgroundPreferences(context: Context): BackgroundPreferences =
        BackgroundPreferences(context)

    @Provides
    private fun backgroundViewModel(
        vehicle: Vehicle,
        backgroundPreferences: BackgroundPreferences
    ): BackgroundViewModel =
        BackgroundViewModel(vehicle, backgroundPreferences)
}
