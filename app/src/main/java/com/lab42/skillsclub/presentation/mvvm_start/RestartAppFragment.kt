package com.lab42.skillsclub.presentation.mvvm_start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.databinding.FragmentRestartAppBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RestartAppFragment : Fragment() {

    private var _binding: FragmentRestartAppBinding? = null
    private val binding get() = _binding!!
    private val splashVM: SplashViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRestartAppBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.restartApp.setOnClickListener {
            splashVM.checkToken()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}