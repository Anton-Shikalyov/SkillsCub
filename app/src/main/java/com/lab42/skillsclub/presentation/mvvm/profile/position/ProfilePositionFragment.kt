package com.lab42.skillsclub.presentation.mvvm.profile.position

import PositionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ExerciseToolbarBinding
import com.lab42.skillsclub.databinding.FragmentProfilePositionBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import com.lab42.skillsclub.presentation.mvvm.profile.ProfileViewModel
import com.lab42.skillsclub.presentation.mvvm_start.position.PositionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfilePositionFragment : Fragment() {

    private var _binding: FragmentProfilePositionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PositionViewModel by viewModels()
    private val viewModelProfile: ProfileViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var positionAdapter: PositionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePositionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopBar()

        positionAdapter = PositionAdapter(emptyList())
        val recyclerView: RecyclerView = binding.recyclerViewProfile
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = positionAdapter

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            positionAdapter = PositionAdapter((positions ?: emptyList()))
            recyclerView.adapter = positionAdapter
            viewModelProfile.setState(AppState.Success(""))

        }

        viewModel.state.observe(viewLifecycleOwner) {
            if (it.state is AppState.Error) {
                mainViewModel.logOut()
            }
        }

        viewModel.localState.observe(viewLifecycleOwner) {
            viewModel.pressF(true)
            if (it is AppState.SuccessAuth) {
                viewLifecycleOwner.lifecycleScope.launch {
                        Toast.makeText(requireActivity(), "Position changed", Toast.LENGTH_LONG).show()
                        delay(500)
                        viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
                        homeViewModel.setCurrentPosition(positionAdapter.getSelectedPosition().toString(), positionAdapter.getSelectedPositionName())
                }
            } else {
                Toast.makeText(requireActivity(), "Something went wrong try again", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnPositionSend.setOnClickListener {
            if (positionAdapter.getSelectedPosition() != -1 && viewModel.pressedFlag.value!!) {
                viewModel.pressF(false)
                viewModel.sendSelectedPosition(positionAdapter.getSelectedPosition())
                viewModel.setPositionNameDB(positionAdapter.getSelectedPositionName())
            } else {
                Toast.makeText(this.requireContext(), "Please select a position", Toast.LENGTH_LONG).show()
            }
        }


        viewModel.pressedFlag.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnPositionSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                binding.btnPositionSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.btnPositionSend.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn_noactive)
                binding.btnPositionSend.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }
        }

    }

    private fun setTopBar() {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ExerciseToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.titleText.text = getText(R.string.change_position)
        toolbarBinding.backText.text = getText(R.string.title_profile)
        toolbarBinding.backBtn.setOnClickListener{
            viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            viewModelProfile.setRoute(RouteBundle(R.id.ProfileFragment, null))
        }
    }


}