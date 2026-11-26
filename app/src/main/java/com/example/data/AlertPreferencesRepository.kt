package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AlertPreferencesStore {
  val alertsEnabled: Flow<Boolean>

  suspend fun setAlertsEnabled(enabled: Boolean)
}

private val Context.alertDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "rk_gallery_alerts_prefs")

class AlertPreferencesRepository(private val context: Context) : AlertPreferencesStore {
  private object Keys {
    val ALERTS_ENABLED = booleanPreferencesKey("alerts_enabled")
  }

  override val alertsEnabled: Flow<Boolean> =
    context.alertDataStore.data.map { prefs -> prefs[Keys.ALERTS_ENABLED] ?: false }

  override suspend fun setAlertsEnabled(enabled: Boolean) {
    context.alertDataStore.edit { prefs -> prefs[Keys.ALERTS_ENABLED] = enabled }
  }
}
