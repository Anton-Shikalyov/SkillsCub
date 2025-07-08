package com.lab42.skillsclub.presentation.mvvm_start.login

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lab42.skillsclub.NameSpace.LOGIN
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentLoginBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.StateBundle
import com.lab42.skillsclub.presentation.mvvm_start.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels()
    private val splashVM: SplashViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginState.observe(viewLifecycleOwner) { state ->

            if (state.state is AppState.Error) {
                splashVM.setAppRoute(
                    StateBundle(AppState.Success(""), null, null))
                if (state.state.code == "400") {
                    binding.passError.visibility = View.VISIBLE
                    binding.loginField.background = ContextCompat.getDrawable(requireContext(), R.drawable.enter_field_error)
                } else {
                    binding.logInErrorMessage.visibility = View.VISIBLE
                }
            } else {
                splashVM.setAppRoute(
                    StateBundle(
                        state.state,
                        state.route,
                        Bundle().apply {
                            putString(LOGIN, binding.loginField.text.toString())
                        }
                    )
                )
            }
        }

        binding.loginField.setOnClickListener {
            binding.loginField.background = ContextCompat.getDrawable(requireContext(), R.drawable.enter_field)
            binding.passError.visibility = View.GONE
        }

        binding.btnLoginSend.setOnClickListener {
            splashVM.setAppRoute(
                StateBundle(AppState.Loading, null, null))
            viewModel.logIn(binding.loginField.text.toString().trim())
            hideKeyboard()
        }

        setupKeyboardHiding()
    }

    private fun setupKeyboardHiding() {
        binding.loginField.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                hideKeyboard()
                binding.loginField.clearFocus()
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginField.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
