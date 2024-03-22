package com.crypto.ticker.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.crypto.ticker.R
import com.crypto.ticker.core.common.BaseActivity
import com.crypto.ticker.databinding.ActivityMainBinding
import com.crypto.ticker.util.ThemeHelper
import com.crypto.ticker.util.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(){

    private val viewModel: HomeActivityViewModel by viewModels()

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_coins_list,
            R.id.navigation_favourites,
            R.id.navigation_settings
        )
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeHelper.applyTheme(if (viewModel.isDarkModeOn()) ThemeMode.Dark else ThemeMode.Light)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.homeBottomNavView
        val navController = findNavController(R.id.homeNavHostFragment)
        appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        navView.setupWithNavController(navController)
    }

//    //Callback method to update the toolbar's title based on the selected bottom tab
//    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
//        toolbar.setupWithNavController(navController, appBarConfiguration)
//    }
}