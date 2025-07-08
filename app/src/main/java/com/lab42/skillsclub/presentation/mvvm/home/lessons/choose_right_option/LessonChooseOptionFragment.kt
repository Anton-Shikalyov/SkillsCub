package com.lab42.skillsclub.presentation.mvvm.home.lessons.choose_right_option

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.StepTasksData
import com.lab42.skillsclub.data.dto.request.TaskExercisesData
import com.lab42.skillsclub.databinding.FragmentLessonChooseOptionBinding
import com.lab42.skillsclub.databinding.TestToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel
import com.lab42.skillsclub.presentation.mvvm.home.lessons.SkipLessonDialogFragment

class LessonChooseOptionFragment : Fragment() {

    private var _binding: FragmentLessonChooseOptionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonChooseOptionViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var chooseOptionAdapter: ChooseOptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonChooseOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        chooseOptionAdapter = ChooseOptionAdapter(lesson.exercisesTask.exerciseSearch, homeViewModel)
            recyclerView.adapter = chooseOptionAdapter

        if (lesson.exercisesTask.exerciseImg != null) {
            val imageUrl = lesson.exercisesTask.exerciseImg.replace("\\/", "/")

            Glide
                .with(this)
                .load(imageUrl)
                .into(binding.imageLesson)
        }

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
            RouteBundle(R.id.StepsFragment, null)
        }

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
                        ))
                    )

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
                    homeViewModel.setSkippCurrentLessonId((skipCurLessonId?:0))
                }
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
                        lesson.exercisesTask.exerciseAnswer.getOrNull(0)?.lowercase() ?: ""
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
                Log.i("zzczcxzxcz", "1")
                SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
            }
            else if (homeViewModel.skipFlag.value!! && homeViewModel.currentSkippedLessonId.value!!+1 == homeViewModel.skippedLessonsId.value!!.size ) {
                Log.i("zzczcxzxcz", "2")
                SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
            }
            else  if (homeViewModel.skipFlag.value!!) {
                homeViewModel.setSkippCurrentLessonId((skipCurLessonId?:0) + 1)
            }  else {
                homeViewModel.setCurrentLessonId((curLessonId?:0) + 1)
            }
        }
        homeViewModel.answer.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i("xxxxxxx", "123123")
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