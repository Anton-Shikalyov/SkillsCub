package com.lab42.skillsclub.presentation.mvvm.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentDashboardBinding
import com.lab42.skillsclub.databinding.ToolbarDashboardBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
//  THIS can help with right routing! ->  navGraphViewModels(R.id.nav_home) save view model and data
    private val viewModel: DashboardViewModel by viewModels() // navGraphViewModels(R.id.nav_home) { defaultViewModelProviderFactory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()



    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ToolbarDashboardBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        val recyclerView: RecyclerView = binding.recyclerViewPrograms
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = ProgramsAdapter(emptyList(), viewModel)

        val position = homeViewModel.currentPosition.value
        if (position != null && !viewModel.currentPosition.equals(homeViewModel.currentPosition.value)) {
            viewModel.getPrograms(position.toInt())
        }

        viewModel.userName.observe(viewLifecycleOwner) {
            toolbarBinding.positionName.text = it
        }

        viewModel.currentPosition.observe(viewLifecycleOwner) {
            toolbarBinding.position.text = it
        }

        viewModel.programs.observe(viewLifecycleOwner) {
            homeViewModel.setState(AppState.Success(""))
            recyclerView.adapter = ProgramsAdapter((it ?: emptyList()), viewModel)
        }

        viewModel.item.observe(viewLifecycleOwner) {
            homeViewModel.setRoute(RouteBundle(R.id.SectionFragment,
                Bundle().apply {
                putString(NameSpace.PROGRAM, it.id.toString())
                putString(NameSpace.TITLE, it.name)
            }))
            homeViewModel.setSectionsBundle(
                Bundle().apply {
                    putString(NameSpace.PROGRAM, it.id.toString())
                    putString(NameSpace.TITLE, it.name)
                }
            )
        }

        viewModel.logOut.observe(viewLifecycleOwner) {
            mainViewModel.logOut()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
