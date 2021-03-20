package br.com.wcabral.myweather.ui.main.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import br.com.wcabral.myweather.R


class SettingsPreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_screen)
    }
}