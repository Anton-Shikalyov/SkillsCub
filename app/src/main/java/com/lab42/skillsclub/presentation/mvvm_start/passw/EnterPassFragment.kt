package com.lab42.skillsclub.presentation.mvvm_start.passw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lab42.skillsclub.NameSpace.LOGIN
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.LoginAndPassReqDTO
import com.lab42.skillsclub.databinding.FragmentEnterPassBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import com.lab42.skillsclub.presentation.mvvm_start.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterPassFragment : Fragment() {

    private var _binding: FragmentEnterPassBinding? = null
    private val binding get() = _binding!!

    private val splashVM: SplashViewModel by activityViewModels()
    private val viewModel: EnterPassViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  login = arguments?.getString(LOGIN)
        binding.btnLogIn.setOnClickListener {
            if (viewModel.pressedFlag.value == true) {
                viewModel.pressF(false)
                viewModel.logIn(
                    LoginAndPassReqDTO(
                        login!!,
                        binding.passwordEdit.text.toString()
                    )
                )
            }
        }

        binding.btnEnterPassBack.setOnClickListener {
                StateBundle(AppState.Loading, null, null)
                splashVM.setAppRoute(StateBundle(AppState.Success("back"), R.id.action_PasswordFragment_to_LoginFragment, null))
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            StateBundle(AppState.Loading, null, null)
            splashVM.setAppRoute(StateBundle(AppState.Success("back"), R.id.action_PasswordFragment_to_LoginFragment, null))
        }

        viewModel.authState.observe(viewLifecycleOwner){
            viewModel.pressF(true)
            if (it.state is AppState.Error) {
                binding.passError.visibility = View.VISIBLE
                binding.passwordEdit.background = ContextCompat.getDrawable(requireContext(), R.drawable.enter_field)
            } else {
                if (it.route == null) {
                    splashVM.setAppRoute(StateBundle(AppState.SuccessAuth, null, null))
                }
                else {
                    splashVM.setAppRoute(it)
                }
            }
        }

        viewModel.pressedFlag.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnLogIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                binding.btnLogIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            } else {
                binding.btnLogIn.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn_noactive)
                binding.btnLogIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}