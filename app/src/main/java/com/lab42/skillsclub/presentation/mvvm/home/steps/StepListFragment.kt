package com.lab42.skillsclub.presentation.mvvm.home.steps

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
import com.lab42.skillsclub.data.dto.request.GetStepsReqDTO
import com.lab42.skillsclub.databinding.FragmentStepListBinding
import com.lab42.skillsclub.databinding.ModuleToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepListFragment : Fragment() {

    private var _binding: FragmentStepListBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: StepListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progId = homeViewModel.getStepsBundle()?.getString(NameSpace.STEP)
        val title = homeViewModel.getStepsBundle()?.getString(NameSpace.TITLE)

        setTopBar(title.toString())

        val recyclerView: RecyclerView = binding.recyclerViewSteps
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = StepListAdapter(emptyList(), viewModel)

        viewModel.getSteps(GetStepsReqDTO(homeViewModel.profile.value!!.currentPosition.toString(), progId.toString()))

        viewModel.steps.observe(viewLifecycleOwner) {
            recyclerView.adapter = StepListAdapter((it ?: emptyList()),  viewModel)
            homeViewModel.setState(AppState.Success(""))

        }


        viewModel.item.observe(viewLifecycleOwner) {
            Log.i("sfsfsdfsdf", viewModel.itemPos.value.toString())

            val bundle = Bundle().apply {
                putInt(NameSpace.POSITION, homeViewModel.profile.value?.currentPosition?: 0)
                putInt(NameSpace.STEP, it.id)
                putInt(NameSpace.TITLE, viewModel.itemPos.value?: 0)
            }
            homeViewModel.setLessonBundle(bundle)
            homeViewModel.setRoute(RouteBundle(R.id.action_StepsFragment_to_LessonFragment, bundle))
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

        toolbarBinding.titleHighText.text = getString(R.string.steps)
        toolbarBinding.titleLowText.text = title
        toolbarBinding.backText.text = getString(R.string.sections)

        toolbarBinding.backBtn.setOnClickListener {
            backBtn()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            backBtn()
        }
    }


    private fun backBtn() {
        homeViewModel.setRoute(
            RouteBundle(R.id.action_StepsFragment_to_SectionFragment, null)
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}