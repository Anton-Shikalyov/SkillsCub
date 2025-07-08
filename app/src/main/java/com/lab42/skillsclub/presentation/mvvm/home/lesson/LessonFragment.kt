package com.lab42.skillsclub.presentation.mvvm.home.lesson

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.GetStepByIdReqDTO
import com.lab42.skillsclub.data.dto.request.GetStepTasksReqDTO
import com.lab42.skillsclub.databinding.ExerciseToolbarBinding
import com.lab42.skillsclub.databinding.FragmentLessonBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.CreateHtml
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var customView: View? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = arguments?.getInt(NameSpace.STEP)
        val position = arguments?.getInt(NameSpace.POSITION)
        val positionLesson = arguments?.getInt(NameSpace.TITLE)

        val recyclerView: RecyclerView = binding.lessonDictionary
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = LessonDictionaryAdapter(emptyList())

        setToolBar(positionLesson.toString())
        homeViewModel.setState(AppState.Success(""))

        viewModel.getStepById(GetStepByIdReqDTO(position ?: 0, step ?: 0))

        viewModel.lesson.observe(viewLifecycleOwner) {
            val lesson = it
            val webView: WebView = binding.webViewLesson
            val htmlContent = CreateHtml.getHtml(requireContext(), lesson.text)

            binding.webViewLesson.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            Log.i("zxczxczc", htmlContent)

            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.mediaPlaybackRequiresUserGesture = false


            webView.webChromeClient = object : WebChromeClient() {
                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    if (customView != null) {
                        onHideCustomView()
                        return
                    }
                    customView = view
                    customViewCallback = callback

                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                    (requireActivity().window.decorView as FrameLayout).addView(
                        customView,
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                }

                override fun onHideCustomView() {
                    (requireActivity().window.decorView as FrameLayout).removeView(customView)
                    customView = null
                    customViewCallback?.onCustomViewHidden()
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }



            if (lesson.dictionary.isNotEmpty()) {
                binding.titleDictionary.visibility = View.VISIBLE
                recyclerView.adapter = LessonDictionaryAdapter(lesson.dictionary)
            }

            binding.btnPassTest.apply {
                if (lesson.hasTasks == 0) {
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    text = getString(R.string._continue)
                } else if (lesson.isPassed == 0) {
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    text = getString(R.string.pass_test)
                } else if (lesson.canRepass == 0) {
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn_noactive)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                } else {
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }

            if (!lesson.repassDate.isNullOrEmpty()) {
                binding.textLostH.visibility = View.VISIBLE
                binding.textLostH.text = viewModel.getRemainingTime(lesson.repassDate)
            }

            binding.btnPassTest.setOnClickListener {
                when {
                    lesson.hasTasks > 0 && lesson.isPassed == 0 -> {
                        homeViewModel.getLessons(GetStepTasksReqDTO(position!!, step!!, 0))
                    }

                    lesson.hasTasks > 0 && lesson.canRepass == 1 -> {
                        homeViewModel.getLessons(GetStepTasksReqDTO(position!!, step!!, 1))
                    }

                    lesson.hasTasks == 0 -> {
                        homeViewModel.updateStepPass()
                    }
                }
            }


        }
    }

    private fun setToolBar(id: String) {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = ExerciseToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.backText.text = getString(R.string.steps)
        toolbarBinding.titleText.text = "${getString(R.string.steps)} $id"

        toolbarBinding.backBtn.setOnClickListener {
            backBtn()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            backBtn()
        }
    }

    private fun backBtn() {
        homeViewModel.clearDataSilent()
        homeViewModel.setRoute(
            RouteBundle(R.id.StepsFragment, homeViewModel.stepsBundle.value)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
