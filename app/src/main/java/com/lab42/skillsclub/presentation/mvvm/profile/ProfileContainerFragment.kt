package com.lab42.skillsclub.presentation.mvvm.profile

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ContentProfileBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileContainerFragment : Fragment() {

    private var _binding: ContentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = (childFragmentManager.findFragmentById(R.id.fragment_profile_myProfile) as NavHostFragment).navController

        val radius = 15f
        val decorView = requireActivity().window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        viewModel.route.observe(viewLifecycleOwner) {
            navigateTo(it)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            Log.i("STATE!!!!", it.toString())
            val animator = ObjectAnimator.ofFloat(binding.launchIc, View.ROTATION, 0f, 360f)
            if (it is AppState.Loading) {
                binding.launchBlur.visibility = View.VISIBLE
                animator.duration = 1000
                animator.repeatCount = ValueAnimator.INFINITE
                animator.interpolator = LinearInterpolator()
                animator.start()
            } else {
                animator.pause()
                binding.launchBlur.visibility = View.GONE
            }
        }

    }
    private fun navigateTo(data: RouteBundle) {
        Log.i("State123123", data.toString())
        try {
            viewModel.setState(AppState.Loading)
            navController.navigate(data.route, data.bundle)
        } catch (e: Exception) {
            Log.e("Error", "Ошибка при навигации: ${e.localizedMessage}")
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}