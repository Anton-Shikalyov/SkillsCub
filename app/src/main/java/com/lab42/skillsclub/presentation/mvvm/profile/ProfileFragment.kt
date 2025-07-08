package com.lab42.skillsclub.presentation.mvvm.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.data_base.entity.AppUsageEntity
import com.lab42.skillsclub.databinding.FragmentProfileBinding
import com.lab42.skillsclub.databinding.ProfileToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.profile.dialog_windows.DeactivationDialogFragment
import com.lab42.skillsclub.presentation.mvvm.profile.dialog_windows.ThemeDialogFragment
import com.lab42.skillsclub.presentation.utils.AppUsageTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var usages:  List<AppUsageEntity> = emptyList()
    @Inject
    lateinit var timerService: AppUsageTime
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setState(AppState.Success(""))


//        viewLifecycleOwner.lifecycleScope.launch {
//            usages = mainViewModel.getAppUsageList()?: emptyList()
//            if (usages.isNotEmpty()) {
//                val sessionTimeInSeconds = (((System.currentTimeMillis() - timerService.startTime) / 1000).toInt()) +  usages[0].mins
//
//                if (sessionTimeInSeconds >= 60) {
//                    val x = sessionTimeInSeconds/60
//                    binding.time.text = "$x min"
//                } else {
//                    binding.time.text = "$sessionTimeInSeconds sec"
//                }
//            }
//
//        }



        viewModel.profile.observe(viewLifecycleOwner) {
            val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
            container.removeAllViews()
            val toolbarBinding = ProfileToolbarBinding.inflate(layoutInflater, container, false)
            container.addView(toolbarBinding.root)
            toolbarBinding.profileName.text = it.name
            toolbarBinding.login.text = it.login
        }
        viewModel.setState(AppState.Success(""))

        mainViewModel.theme.observe(viewLifecycleOwner) {
            binding.currentTheme.text = it.toString()
        }

        viewModel.logOutState.observe(viewLifecycleOwner) {
            if (it is AppState.Success<*>) {
                mainViewModel.logOut()
            }
        }

        binding.changePosBtn.setOnClickListener{
            viewModel.setRoute(RouteBundle(R.id.action_ProfileFragment_to_positionFragment, null))
        }

        binding.updatePass.setOnClickListener{
            viewModel.setRoute(RouteBundle(R.id.action_ProfileFragment_to_changePassFragment, null))
        }

        binding.changeTheme.setOnClickListener {
            showThemeDialog()
        }

        binding.deactivationReq.setOnClickListener {
            showDeactivationDialog()
        }

        binding.logoutContainer.setOnClickListener {
            viewModel.logOut()
        }

        requireActivity().onBackPressedDispatcher.addCallback {

        }
    }

    private fun showThemeDialog() {
        ThemeDialogFragment().show(parentFragmentManager, "Dialog")
    }

    private fun showDeactivationDialog() {
        DeactivationDialogFragment().show(parentFragmentManager, "Dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}