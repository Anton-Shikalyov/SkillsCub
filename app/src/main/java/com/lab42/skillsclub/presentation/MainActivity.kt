package com.lab42.skillsclub.presentation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ActivityMainBinding
import com.lab42.skillsclub.presentation.mvvm_start.SplashActivity
import com.lab42.skillsclub.presentation.utils.AppUsageTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var timerService: AppUsageTime
    private val viewModel: MainViewModel by viewModels()

    private var startTime: Long = 0
    private var currentDateFormatted: String = ""
    private var formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.logOut.observe(this) {
            val i = Intent(
                this,
                SplashActivity::class.java)
            startActivity(i)
        }

        viewModel.recreate.observe(this) {
          recreate()
        }


        viewModel.showError.observe(this) {
            lifecycleScope.launch {
                binding.mainErrorMessage.visibility = View.VISIBLE
                delay(2000)
                binding.mainErrorMessage.visibility = View.GONE
            }
        }

        viewModel.theme.observe(this) {
            when(it) {
                NameSpace.LIGHT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                NameSpace.DARK_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
                Configuration.UI_MODE_NIGHT_YES -> WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
            }
        }

        onBackPressedDispatcher.addCallback(this) {

        }

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            timerService.initialiseValues(viewModel,  System.currentTimeMillis())
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            timerService.appUsageSend()
        }
    }


}
