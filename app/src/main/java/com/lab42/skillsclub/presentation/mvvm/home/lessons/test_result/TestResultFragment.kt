package com.lab42.skillsclub.presentation.mvvm.home.lessons.test_result

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentTestResultBinding
import com.lab42.skillsclub.databinding.TestToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestResultFragment : Fragment() {


    private var _binding: FragmentTestResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TestResultViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.setState(AppState.Success(""))

        val percent = (homeViewModel.lessonsScoreTotal.value!!.toFloat() / homeViewModel.lessonsScore.value!!.size.toFloat() * 100).toInt()
        viewModel.config.observe(viewLifecycleOwner) {
            if (it != null) {
                val color: Int
                val confRes: Drawable
                if (percent >= it.testSuccessThreshold) {
                    confRes = ContextCompat.getDrawable(requireContext(), R.drawable.progress)!!
                    color = ContextCompat.getColor(requireContext(), R.color.blue_corner)
                } else {
                    confRes = ContextCompat.getDrawable(requireContext(), R.drawable.progress_less)!!
                    color = ContextCompat.getColor(requireContext(), R.color.red_error)
                }
                binding.progressBar.progressDrawable = confRes
                binding.passedText.setTextColor(color)
                binding.percentResult.setTextColor(color)
            }
        }

        binding.progressBar.min = 0
        binding.progressBar.max = homeViewModel.lessonsScore.value!!.size
        binding.progressBar.progress = homeViewModel.lessonsScoreTotal.value!!
        binding.percentResult.text = "$percent %"

        val recyclerView: RecyclerView = binding.testResultRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = LessonsResultAdapter(homeViewModel.lessonsScore.value!!, homeViewModel.lessons.value!!)

        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = TestToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.backBtn.setOnClickListener{
            homeViewModel.clearDataSilent()
            homeViewModel.setRoute(
                RouteBundle(R.id.StepsFragment, null)
            )
        }

        binding.btnResultTest.setOnClickListener {
            backBtn()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            backBtn()
        }
    }


    private fun backBtn() {
        homeViewModel.setRoute(RouteBundle(R.id.StepsFragment, null))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}