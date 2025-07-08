package com.lab42.skillsclub.presentation.mvvm.help

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentHelpBinding
import com.lab42.skillsclub.databinding.TitleToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HelpViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
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

        setTopBar()
        val recyclerHelp = binding.helpList

        recyclerHelp.layoutManager = LinearLayoutManager(requireActivity())
        recyclerHelp.adapter = HelpAdapter(emptyList(), parentFragmentManager)
        viewModel.helpData.observe(viewLifecycleOwner) {
            viewModel.setState(AppState.Success(""))
            recyclerHelp.adapter = HelpAdapter(it, parentFragmentManager)
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

        viewModel.logOut.observe(viewLifecycleOwner) {
            mainViewModel.logOut()
        }
    }

    private fun setTopBar() {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = TitleToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.title.text = getString(R.string.title_help)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}