package com.lab42.skillsclub.presentation.mvvm.dictionary.termin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ExerciseToolbarBinding
import com.lab42.skillsclub.databinding.FragmentTerminBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.dictionary.DictionaryContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerminFragment : Fragment() {

    private var _binding: FragmentTerminBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TerminViewModel by viewModels()
    private val dictionaryContainerViewModel: DictionaryContainerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTerminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(NameSpace.TITLE)
        val description = arguments?.getString(NameSpace.DESCRIPTION)

        binding.terminTitle.text = title
        binding.terminDescription.text = description
        setTopBar()

    }

    private fun setTopBar() {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ExerciseToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.titleText.text = getString(R.string.termin)
        toolbarBinding.backText.text = getString(R.string.title_dictionary)

        toolbarBinding.backBtn.setOnClickListener {
            dictionaryContainerViewModel.setState(AppState.Loading)
            dictionaryContainerViewModel.setRoute(
                RouteBundle(R.id.action_TerminFragment_to_DictionaryFragment, null)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            RouteBundle(R.id.action_TerminFragment_to_DictionaryFragment, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}