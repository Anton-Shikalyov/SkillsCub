package com.lab42.skillsclub.presentation.mvvm.profile.dialog_windows

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeDialogFragment : DialogFragment() {
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        view.findViewById<LinearLayout>(R.id.lightTheme).setOnClickListener {
            applyTheme(NameSpace.LIGHT_THEME)
        }

        view.findViewById<LinearLayout>(R.id.darkTheme).setOnClickListener {
            applyTheme(NameSpace.DARK_THEME)
        }

        view.findViewById<LinearLayout>(R.id.autoTheme).setOnClickListener {
            applyTheme(NameSpace.SYSTEM_THEME)
        }

        view.findViewById<ImageButton>(R.id.closeButtonTheme).setOnClickListener {
            dismiss()
        }
    }

    private fun applyTheme(themeName: String) {
        dismiss()
        mainViewModel.setTheme(themeName)
    }
}