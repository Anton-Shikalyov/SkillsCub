package com.lab42.skillsclub.presentation.mvvm.home.lessons

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel

class SkipLessonDialogFragment : DialogFragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_skip_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        view.findViewById<Button>(R.id.btn_skip).setOnClickListener {
            dismiss()
            homeViewModel.setSkipFlag(true)
            homeViewModel.setSkippCurrentLessonId(0)
        }

        view.findViewById<Button>(R.id.btn_complete).setOnClickListener {
            homeViewModel.updateStepPass()
            dismiss()
            homeViewModel.setRoute(
                RouteBundle(
                    R.id.TestResultFragment,
                    null
                )
            )
        }
        
    }

}