package br.com.wcabral.myweather.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.wcabral.myweather.R
import br.com.wcabral.myweather.databinding.ActivityMainBinding
import br.com.wcabral.myweather.ui.main.favorite.FavoriteFragment
import br.com.wcabral.myweather.ui.main.search.SearchFragment
import br.com.wcabral.myweather.ui.main.settings.SettingsPreferencesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val searchFragment = SearchFragment()
    private val favoriteFragment = FavoriteFragment()
    private val settingsFragment = SettingsPreferencesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_search -> setFragment(searchFragment)
                R.id.menu_favorite -> setFragment(favoriteFragment)
                R.id.menu_settings -> setFragment(settingsFragment)
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.menu_search
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.containerView.id, fragment)
            .addToBackStack(null)
            .commit()
    }


}