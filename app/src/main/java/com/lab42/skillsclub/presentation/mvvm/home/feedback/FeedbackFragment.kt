package com.lab42.skillsclub.presentation.mvvm.home.feedback

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.RateStepReqDTO
import com.lab42.skillsclub.databinding.FragmentFeedbackBinding
import com.lab42.skillsclub.databinding.TestToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedbackViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = TestToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.titleText.text = getText(R.string.feedback)
        toolbarBinding.backText.text = getText(R.string.steps)
        toolbarBinding.backBtn.setOnClickListener{
            homeViewModel.setRoute(
                RouteBundle(R.id.StepsFragment, null)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            homeViewModel.setRoute(
                RouteBundle(R.id.StepsFragment, null)
            )        }

        homeViewModel.setState(AppState.Success(""))


        binding.btnYes.setOnClickListener{
            binding.btnYes.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_frame_theme_ic_active)
            binding.btnNo.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_frame_theme_ic)
            viewModel.setAnswer(1)
        }

        binding.btnNo.setOnClickListener{
            binding.btnYes.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_frame_theme_ic)
            binding.btnNo.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_frame_theme_ic_active)
            viewModel.setAnswer(0)
        }

        viewModel.feedback.observe(viewLifecycleOwner) {
            if (it is AppState.Success<*>) {
                homeViewModel.clearDataSilent()
                homeViewModel.setRoute(
                    RouteBundle(R.id.StepsFragment, null)
                )
            } else if (it is AppState.Error) {
                 if (it.code == "401") {
                     mainViewModel.logOut()
                 }
            }
        }

        binding.btnSubmitFeedback.setOnClickListener {
            Log.i("czczczc" ,"11111")
            val step = homeViewModel.lesson.value?.getInt(NameSpace.STEP)
            if (viewModel.answer.value != -1 && binding.ratingBar.rating > 0) {
                viewModel.sendFeedback(RateStepReqDTO(step!!, viewModel.answer.value!!,  binding.ratingBar.rating.toInt() , binding.editTextFeedback.text.toString()))
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    binding.splashErrorMessage.visibility = View.VISIBLE
                    delay(1500)
                    binding.splashErrorMessage.visibility = View.GONE
                }
            }
        }
    }

}