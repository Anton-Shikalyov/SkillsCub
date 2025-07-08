package com.lab42.skillsclub.presentation.mvvm_start.create_pass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.LoginAndPassReqDTO
import com.lab42.skillsclub.databinding.FragmentCreatePasswBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import com.lab42.skillsclub.presentation.mvvm_start.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePasswFragment : Fragment() {

    private var _binding: FragmentCreatePasswBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePasswViewModel by viewModels()
    private val splashVM: SplashViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePasswBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = arguments?.getString(NameSpace.LOGIN)

        viewModel.authState.observe(viewLifecycleOwner){
            if (it.state is AppState.Error) {
                binding.passRepeatError .visibility = View.VISIBLE
                binding.passEditRepeat.background = ContextCompat.getDrawable(requireContext(), R.drawable.enter_field)
            }
            if (it.route == null) {
//                val i = Intent(
//                    requireContext(),
//                    MainActivity::class.java)
//                startActivity(i)
                splashVM.setAppRoute(StateBundle(AppState.SuccessAuth, null, null))
            }
            else {
                splashVM.setAppRoute(it)
            }
        }

        binding.btnCreatePass.setOnClickListener {
            if (binding.newPass.text?.isNotEmpty() == true && binding.passEditRepeat.text?.isNotEmpty() == true) {
                if (binding.newPass.text.toString().trim() == binding.passEditRepeat.text.toString().trim()) {
                    viewModel.createPass(LoginAndPassReqDTO(login.toString().trim(), binding.passEditRepeat.text.toString().trim()))
                } else {
                    splashVM.emitError(1)
                }
            }
        }


        binding.btnCreatePassBack.setOnClickListener {
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}