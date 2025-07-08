package com.lab42.skillsclub.presentation.mvvm.profile.dialog_windows

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.mvvm.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeactivationDialogFragment : DialogFragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_deactivation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.deactivationReqState.observe(viewLifecycleOwner) {
            if (it is AppState.Success<*>) {
                dismiss()
                Toast.makeText(requireActivity(), "Deactivation request was send", Toast.LENGTH_LONG).show()
                mainViewModel.logOut()
            } else {
                Toast.makeText(requireActivity(), "Something went wrong, try again", Toast.LENGTH_LONG).show()
            }
        }

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        view.findViewById<Button>(R.id.btn_skip).setOnClickListener {
            profileViewModel.deactivationAccount()
        }

        view.findViewById<ImageButton>(R.id.closeButtonDeactivation).setOnClickListener {
            dismiss()
        }
    }

}