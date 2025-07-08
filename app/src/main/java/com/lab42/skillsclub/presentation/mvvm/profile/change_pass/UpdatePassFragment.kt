package com.lab42.skillsclub.presentation.mvvm.profile.change_pass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.UpdatePasswordReqDTO
import com.lab42.skillsclub.databinding.ExerciseToolbarBinding
import com.lab42.skillsclub.databinding.FragmentChangePassBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePassFragment : Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels()
    private val viewModelProfile: ProfileViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setState(AppState.Success(""))

        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ExerciseToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.backText.text = getText(R.string.title_profile)
        toolbarBinding.titleText.text = getText(R.string.change_password)
        toolbarBinding.backBtn.setOnClickListener{
            viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
        }

        binding.btnChangePass.setOnClickListener {
            if (binding.newPassChange.text?.isNotEmpty() == true && binding.repeatNewPass.text?.isNotEmpty() == true) {
                if (binding.newPassChange.text.toString().trim() == binding.repeatNewPass.text.toString().trim()) {
                    viewModel.updatePass(UpdatePasswordReqDTO(binding.newPassChange.text.toString()))
                } else {
                    binding.passRepeatError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.updatePassRes.observe(viewLifecycleOwner) {
            if (it is AppState.Success<*>) {
                viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
            } else if (it is AppState.Error) {
                if (it.code == "401") {
                    mainViewModel.logOut()
                }
            }
            else {
                binding.passRepeatError.visibility = View.VISIBLE
                binding.passRepeatError.text = getText(R.string.error_response)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}