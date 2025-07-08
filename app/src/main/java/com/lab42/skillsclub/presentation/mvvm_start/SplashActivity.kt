package com.lab42.skillsclub.presentation.mvvm_start

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.data_base.db_use_case.DBProfileUseCase
import com.lab42.skillsclub.databinding.ActivitySplashBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySplashBinding

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var dbProfileUseCase: DBProfileUseCase
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_splash) as NavHostFragment
        navController = navHostFragment.navController
        backStackNav()

        val radius = 15f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        viewModel.route.observe(this) { route ->
            val animator = ObjectAnimator.ofFloat(binding.launchIc, View.ROTATION, 0f, 360f)

            when (route.state) {
                is AppState.Loading -> {
                    binding.launchBlur.visibility = View.VISIBLE
                    animator.duration = 1000
                    animator.repeatCount = ValueAnimator.INFINITE
                    animator.interpolator = LinearInterpolator()
                    animator.start()

                }
                is AppState.Splash -> {
                    animator.pause()
                    binding.launchBlur.visibility = View.GONE

                    navigateTo(R.id.SplashFragment, null)
                }
                is AppState.Error -> {
                    animator.pause()
                    binding.launchBlur.visibility = View.GONE
                    when (route.state.code) {
                        "404" -> {
                            navController.navigate(R.id.NotAuthFragment)
                        }
                        "400" -> {
                            navController.navigate(R.id.LoginFragment)
                        }
                        "nullPosition" -> {
                            navController.navigate(R.id.PositionFragment)
                        }
                        else -> {
                            navigateTo(R.id.RestartAppFragment, null)
                        }
                    }
                }

                is AppState.SuccessAuth -> {
                    animator.pause()
                    binding.launchBlur.visibility = View.GONE

                    val i = Intent(
                        this,
                        MainActivity::class.java)
                    startActivity(i)
                    finish()
                }

                is AppState.Success<*> -> {
                    animator.pause()
                    binding.launchBlur.visibility = View.GONE
                    if (route.route != null) {
                        navigateTo(route.route, route.bundle)
                    }
                }
                else -> {
                    animator.pause()
                    binding.launchBlur.visibility = View.GONE
                    navigateTo(R.id.RestartAppFragment, null)
                }
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

        viewModel.showError.observe(this) {
            lifecycleScope.launch {
                binding.splashErrorMessage.visibility = View.VISIBLE
                delay(2000)
                binding.splashErrorMessage.visibility = View.GONE
            }
        }
    }



    private fun backStackNav() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(navController.currentDestination?.id) {
                    R.id.LoginFragment -> {
                        isEnabled = true
                    }

                    R.id.CreatePassFragment -> {
                        navController.navigate(R.id.LoginFragment)
                    }

                    R.id.EnterPassFragment -> {
                        navController.navigate(R.id.LoginFragment)
                    }
                }
            }
        })
    }


    private fun navigateTo(fragmentID: Int, bundle: Bundle?) {
        if (navController.currentDestination?.id != fragmentID) {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(fragmentID, true)
                .build()
            navController.navigate(fragmentID, bundle, navOptions)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_splash)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}