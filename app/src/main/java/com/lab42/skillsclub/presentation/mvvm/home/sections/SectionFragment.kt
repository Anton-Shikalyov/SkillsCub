package com.lab42.skillsclub.presentation.mvvm.home.sections

import android.os.Bundle
import android.util.Log
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
import com.lab42.skillsclub.data.dto.request.GetSectionsReqDTO
import com.lab42.skillsclub.databinding.FragmentSectionsBinding
import com.lab42.skillsclub.databinding.ModuleToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SectionFragment : Fragment() {

    private val viewModel: SectionViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    private var _binding: FragmentSectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progId = homeViewModel.getSectionsBundle()?.getString(NameSpace.PROGRAM)
        val title = homeViewModel.getSectionsBundle()?.getString(NameSpace.TITLE)
        viewModel.getSections(GetSectionsReqDTO(homeViewModel.profile.value?.currentPosition.toString(), progId.toString()))
        Log.i("xxxxx", homeViewModel.profile.value!!.currentPosition.toString() + " |||| " +progId.toString())
        setTopBar(title.toString())

        val recyclerView: RecyclerView = binding.recyclerViewSections
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = SectionAdapter(emptyList(), viewModel)

        viewModel.sections.observe(viewLifecycleOwner) {
            homeViewModel.setState(AppState.Success(""))
            recyclerView.adapter = SectionAdapter((it ?: emptyList()),  viewModel)
        }

        viewModel.item.observe(viewLifecycleOwner) {
            homeViewModel.setRoute(RouteBundle(R.id.action_SectionFragment_to_StepsFragment, null))
            homeViewModel.setStepsBundle(
                Bundle().apply {
                    putString(NameSpace.STEP, it.id.toString())
                    putString(NameSpace.TITLE, it.name)
                }
            )
        }

        viewModel.logOut.observe(viewLifecycleOwner) {
            mainViewModel.logOut()
        }
    }

    private fun setTopBar(title: String) {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ModuleToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.titleHighText.text = getString(R.string.sections)
        toolbarBinding.backText.text = getString(R.string.title_home)
        toolbarBinding.titleLowText.text = title

        toolbarBinding.backBtn.setOnClickListener {
            backBtn()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            backBtn()
        }
    }



    private fun backBtn() {
        homeViewModel.setRoute(
            RouteBundle(R.id.DashboardFragment, null)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}