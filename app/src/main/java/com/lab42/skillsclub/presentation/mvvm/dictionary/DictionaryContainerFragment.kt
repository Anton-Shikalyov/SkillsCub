package com.lab42.skillsclub.presentation.mvvm.dictionary

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
import com.lab42.skillsclub.databinding.ContentDictionaryBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryContainerFragment : Fragment() {

    private var _binding: ContentDictionaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: DictionaryContainerViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radius = 15f
        val decorView = requireActivity().window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background
        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        navController = (childFragmentManager.findFragmentById(R.id.content_dictionary) as NavHostFragment).navController

        viewModel.route.observe(viewLifecycleOwner) {
            navigateTo(it)
        }

        viewModel.state.observe(viewLifecycleOwner) {
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
        try {

                navController.navigate(data.route, data.bundle)

        } catch (e: Exception) {
            Log.e("NavigationError", "Ошибка при навигации: ${e.localizedMessage}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}