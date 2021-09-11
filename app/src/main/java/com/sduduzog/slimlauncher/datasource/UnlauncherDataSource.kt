package com.sduduzog.slimlauncher.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import androidx.lifecycle.LifecycleCoroutineScope
import com.jkuester.unlauncher.datastore.QuickButtonPreferences

private val Context.quickButtonPreferencesStore: DataStore<QuickButtonPreferences> by dataStore(
    fileName = "unlauncher_data.pb",
    serializer = QuickButtonPreferencesSerializer,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context,
                "settings",
                setOf("quick_button_left", "quick_button_center", "quick_button_right")
            ) { sharedPrefs: SharedPreferencesView, currentData: QuickButtonPreferences ->
                val prefBuilder = currentData.toBuilder()
                if (!currentData.hasLeftButton()) {
                    prefBuilder.leftButton =
                        QuickButtonPreferences.QuickButton.newBuilder().setIconId(
                            sharedPrefs.getInt(
                                "quick_button_left",
                                QuickButtonPreferencesRepository.DEFAULT_ICON_LEFT
                            )
                        ).build()
                }

                if (!currentData.hasCenterButton()) {
                    prefBuilder.centerButton =
                        QuickButtonPreferences.QuickButton.newBuilder().setIconId(
                            sharedPrefs.getInt(
                                "quick_button_center",
                                QuickButtonPreferencesRepository.DEFAULT_ICON_CENTER
                            )
                        ).build()
                }
                if (!currentData.hasRightButton()) {
                    prefBuilder.rightButton =
                        QuickButtonPreferences.QuickButton.newBuilder().setIconId(
                            sharedPrefs.getInt(
                                "quick_button_right",
                                QuickButtonPreferencesRepository.DEFAULT_ICON_RIGHT
                            )
                        ).build()
                }
                prefBuilder.build()
            }
        )
    }
)

class UnlauncherDataSource(context: Context, lifecycleScope: LifecycleCoroutineScope) {
    val quickButtonPreferencesRepo =
        QuickButtonPreferencesRepository(context.quickButtonPreferencesStore, lifecycleScope)
}