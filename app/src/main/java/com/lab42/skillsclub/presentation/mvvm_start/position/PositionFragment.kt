package com.lab42.skillsclub.presentation.mvvm_start.position

import PositionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentPositionBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.StateBundle
import com.lab42.skillsclub.presentation.mvvm_start.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PositionFragment : Fragment() {

    private var _binding: FragmentPositionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PositionViewModel by viewModels()
    private val splashVM: SplashViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var positionAdapter: PositionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPositionBinding.inflate(inflater, container, false)

        positionAdapter = PositionAdapter(emptyList())
        val recyclerView: RecyclerView = binding.recyclerViewProfile
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = positionAdapter

        viewModel.localState.observe(viewLifecycleOwner) {
            viewModel.pressF(true)
            if (it is AppState.SuccessAuth) {
                viewModel.setPositionDB(positionAdapter.getSelectedPosition())
                splashVM.setAppRoute(StateBundle(it, null, null))
            } else {
                splashVM.emitError(1)
            }

        }

        viewModel.state.observe(viewLifecycleOwner) {
            splashVM.setAppRoute(it)
        }

        viewModel.positions.observe(viewLifecycleOwner) { positions ->
            positionAdapter = PositionAdapter((positions ?: emptyList()))
            recyclerView.adapter = positionAdapter
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            binding.positionName.text = it
        }

        binding.btnPositionSend.setOnClickListener {
            if (positionAdapter.getSelectedPosition() >= 0 && viewModel.pressedFlag.value!!) {
                viewModel.pressF(false)
                viewModel.sendSelectedPosition(positionAdapter.getSelectedPosition())
                viewModel.setPositionNameDB(positionAdapter.getSelectedPositionName())
            } else {
                Toast.makeText(this.requireContext(), "Please select a position", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.logOut.observe(viewLifecycleOwner) {
            mainViewModel.logOut()
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
