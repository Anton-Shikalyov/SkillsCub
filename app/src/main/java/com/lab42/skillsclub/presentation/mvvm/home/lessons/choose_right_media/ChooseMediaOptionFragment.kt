package com.lab42.skillsclub.presentation.mvvm.home.lessons.choose_right_media

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.StepTasksData
import com.lab42.skillsclub.data.dto.request.TaskExercisesData
import com.lab42.skillsclub.databinding.FragmentChooseMediaOptionBinding
import com.lab42.skillsclub.databinding.TestToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.CreateHtml
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import com.lab42.skillsclub.presentation.mvvm.home.lessons.SkipLessonDialogFragment
import com.lab42.skillsclub.presentation.mvvm.home.lessons.choose_right_option.ChooseOptionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseMediaOptionFragment : Fragment() {

    private val viewModel: ChooseMediaOptionViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentChooseMediaOptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var chooseOptionAdapter: ChooseOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseMediaOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = TestToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.backBtn.setOnClickListener{
            homeViewModel.clearDataSilent()
            homeViewModel.setRoute(
                RouteBundle(R.id.StepsFragment, null)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            homeViewModel.setRoute(
                RouteBundle(R.id.StepsFragment, null)
            )
        }

        chooseOptionAdapter = ChooseOptionAdapter(emptyList(), homeViewModel)
        val recyclerView: RecyclerView = binding.answerOptions
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        homeViewModel.setState(AppState.Success(""))

        val lesson = homeViewModel.currentLessonStep.value
        binding.typeTask.text = lesson!!.taskType
        binding.textTask.text = lesson.exercisesTask.exerciseTask
        binding.amountOfLessons.text = homeViewModel.lessons.value?.size.toString()
        binding.currentLesson.text =
            if ((homeViewModel.skippedLessonsId.value?.size ?: 0) > 0 && homeViewModel.skipFlag.value!!) {
            (homeViewModel.skippedLessonsId.value!![homeViewModel.currentSkippedLessonId.value!!]+1).toString()
        } else {
            (homeViewModel.getCurrentLessonId() + 1).toString()
        }

        val webView: WebView = view.findViewById(R.id.webLesson)
        webView.settings.javaScriptEnabled = true;

        val htmlContent = CreateHtml.getHtml(requireContext(), lesson.exercisesTask.exerciseHtml)

        webView.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html",
            "UTF-8",
            null
        )

        chooseOptionAdapter = ChooseOptionAdapter(lesson.exercisesTask.exerciseSearch, homeViewModel)
        recyclerView.adapter = chooseOptionAdapter


        binding.btnConfirmAnswer.setOnClickListener {
            if (chooseOptionAdapter.getSelectedPosition() != -1) {

                val curLessonId = homeViewModel.currentLessonId.value?.toInt()
                val skipCurLessonId = homeViewModel.currentSkippedLessonId.value?.toInt()

                val score = if (
                    lesson.exercisesTask.exerciseAnswer[0] == lesson.exercisesTask.exerciseSearch[chooseOptionAdapter.getSelectedPosition()]
                ) 1 else 0

                if (score == 1) {
                    homeViewModel.increaseRightAnswer()
                }

                if (!homeViewModel.skipFlag.value!!) {
                    homeViewModel.addAnswerToLessonsScore(StepTasksData(
                        lesson.taskId,
                        TaskExercisesData(
                            lesson.exercisesTask.exerciseId,
                            score,
                            0,
                            lesson.exercisesTask.exerciseSearch[chooseOptionAdapter.getSelectedPosition()],
                            lesson.exercisesTask.exerciseAnswer.getOrNull(0)?: ""
                        )))
                    if (homeViewModel.showSkipDialog()) {
                        SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
                    } else {
                        homeViewModel.setCurrentLessonId((curLessonId?:0) + 1)
                    }
                } else {
                    homeViewModel.addAnswerToLessonsScoreById(
                        homeViewModel.skippedLessonsId.value!![homeViewModel.currentSkippedLessonId.value!!],
                        StepTasksData(
                            lesson.taskId,
                            TaskExercisesData(
                                lesson.exercisesTask.exerciseId,
                                score,
                                0,
                                lesson.exercisesTask.exerciseSearch[chooseOptionAdapter.getSelectedPosition()],
                                lesson.exercisesTask.exerciseAnswer.getOrNull(0)?: ""
                            )
                        )
                    )
                    homeViewModel.setSkippCurrentLessonId((skipCurLessonId?:0) + 1)
                }
//                 if (homeViewModel.showSkipDialog() && !homeViewModel.skipFlag.value!!) {
//                    Log.i("zzczcxzxcz", "1")
//                    SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
//                }
//                else if (homeViewModel.skipFlag.value!! && homeViewModel.currentSkippedLessonId.value!!+1 == homeViewModel.skippedLessonsId.value!!.size ) {
//                    Log.i("zzczcxzxcz", "2")
//                    SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
//                }
//                else  if (homeViewModel.skipFlag.value!!) {
//                    homeViewModel.setSkippCurrentLessonId((skipCurLessonId?:0) + 1)
//                }
            }
        }

        binding.btnSkipLesson.setOnClickListener{
            val curLessonId = homeViewModel.currentLessonId.value?.toInt()
            val skipCurLessonId = homeViewModel.currentSkippedLessonId.value?.toInt()

            if (!homeViewModel.skipFlag.value!!) {
                homeViewModel.addSkippedLessonId(curLessonId?:0)
                homeViewModel.addAnswerToLessonsScore(StepTasksData(
                    lesson.taskId,
                    TaskExercisesData(
                        lesson.exercisesTask.exerciseId,
                        0,
                        1,
                        "",
                        lesson.exercisesTask.exerciseAnswer.getOrNull(0)?: ""
                    )
                ))
            }
            if (homeViewModel.skipFlag.value!! && (homeViewModel.skippedLessonsId.value?.size?: 0) == 0) {
                homeViewModel.setRoute(
                    RouteBundle(
                        R.id.TestResultFragment,
                        null
                    )
                )
            }
            else if (homeViewModel.showSkipDialog() && !homeViewModel.skipFlag.value!!) {
                SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
            }
            else if (homeViewModel.skipFlag.value!! && homeViewModel.currentSkippedLessonId.value!!+1 == homeViewModel.skippedLessonsId.value!!.size ) {
                SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
            }
            else  if (homeViewModel.skipFlag.value!!) {
                homeViewModel.setSkippCurrentLessonId((skipCurLessonId?:0) + 1)
            }  else {
                homeViewModel.setCurrentLessonId((curLessonId?:0))
            }
        }
        homeViewModel.answer.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.btnConfirmAnswer.background = ContextCompat.getDrawable(requireContext(), R.drawable.sh_blue_btn)
                binding.btnConfirmAnswer.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                homeViewModel.setAnswer(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}