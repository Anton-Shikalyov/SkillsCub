package com.lab42.skillsclub.presentation.mvvm.home

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ContentHomeBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.RouteBundle
import com.lab42.skillsclub.presentation.mvvm.home.lesson.LessonType
import com.lab42.skillsclub.presentation.mvvm.home.lessons.SkipLessonDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeContainerFragment : Fragment() {

    private var _binding: ContentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = (childFragmentManager.findFragmentById(R.id.fragment_home_lesson) as NavHostFragment).navController

        val radius = 15f
        val decorView = requireActivity().window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowBackground = decorView.background

        binding.blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
            .setBlurAutoUpdate(false)


        viewModel.route.observe(viewLifecycleOwner) {
            navigateTo(it)
        }

        viewModel.openResult.observe(viewLifecycleOwner) {
            if (it == 1) {
                Log.i("zxvczxfzcxc", viewModel.lessons.value?.size.toString())
                if (viewModel.lessons.value?.size != null && (viewModel.lessons.value?.size ?: 0) >= 1) {
                    viewModel.setRoute(
                        RouteBundle(
                            R.id.TestResultFragment,
                            null
                        )
                    )
                } else {
                    viewModel.setRoute(
                        RouteBundle(
                            R.id.FeedbackFragment,
                            null
                        )
                    )
                }

            }
            if (it == 2) {
                viewLifecycleOwner.lifecycleScope.launch {
                    binding.homeErrorMessage.visibility = View.VISIBLE
                    delay(2000)
                    binding.homeErrorMessage.visibility = View.GONE
                }
            }
        }

        viewModel.currentSkippedLessonId.observe(viewLifecycleOwner) {
            if (viewModel.skippedLessonsId.value!!.isEmpty()) {
                viewModel.updateStepPass()
            } else if (it!! + 1 > viewModel.skippedLessonsId.value!!.size && viewModel.skippedLessonsId.value!!.size > 1) {
                SkipLessonDialogFragment().show(parentFragmentManager, "Dialog")
            } else {
                val lesson = viewModel.lessons.value?.get((viewModel.skippedLessonsId.value?.get(it))!!)
                if (lesson != null) {
                    viewModel.setCurrentLessonStep(lesson)
                    routeLesson(lesson.taskType)
                }
            }

//            if (viewModel.skippedLessonsId.value!!.isNotEmpty() && it!! <
//                viewModel.skippedLessonsId.value!!.size+1)
//            {
//                val lesson = viewModel.lessons.value?.get((viewModel.skippedLessonsId.value?.get(it))!!)
//
//                if (lesson != null) {
//                    viewModel.setCurrentLessonStep(lesson)
//                    routeLesson(lesson.taskType)
//                }
//
//            } else {
//                viewModel.setRoute(
//                    RouteBundle(
//                        R.id.TestResultFragment,
//                        null
//                    )
//                )
//            }
        }

        viewModel.currentLessonId.observe(viewLifecycleOwner) {
            if (viewModel.lessons.value != null && viewModel.lessons.value!!.isEmpty()) {
                viewModel.updateStepPass()
            }
            else if (it != null && it <= viewModel.lessons.value!!.size - 1) {
                val lesson = viewModel.lessons.value?.get(it.toInt())
                if (lesson != null) {
                    viewModel.setCurrentLessonStep(lesson)
                    routeLesson(lesson.taskType)

                }
            }
            else if (it != null && !viewModel.skipFlag.value!!) {
                viewModel.updateStepPass()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            val animator = ObjectAnimator.ofFloat(binding.launchIc, View.ROTATION, 0f, 360f)
            if (it is AppState.Loading) {
                binding.launchBlur.visibility = View.VISIBLE
                binding.blurView.setBlurEnabled(true)
                animator.duration = 1000
                animator.repeatCount = ValueAnimator.INFINITE
                animator.interpolator = LinearInterpolator()
                animator.start()
            } else {
                animator.pause()
                binding.launchBlur.visibility = View.GONE
                binding.blurView.setBlurEnabled(false)

            }
        }
    }

    private fun routeLesson (taskType: String) {
        when (taskType) {
            LessonType.TRUE_FALSE.toString() -> {
                viewModel.setRoute(
                    RouteBundle(
                        R.id.LessonTrueFalse,
                        null
                    )
                )
            }
            LessonType.MEDIA_CHOOSE_RIGHT_OPTION .toString() -> {
                viewModel.setRoute(
                    RouteBundle(
                        R.id.LessonMediaOption,
                        null
                    )
                )
            }
            else -> {
                viewModel.setRoute(
                    RouteBundle(
                        R.id.LessonRightOption,
                        null
                    )
                )
            }
        }
    }

//    fun openFragment(currentIdFragment: Int, fragment: Fragment, bundle: Bundle) {
//        fragment.arguments = bundle
//        parentFragmentManager.beginTransaction()
//            .replace(currentIdFragment, fragment)
//            .addToBackStack(null)
//            .commit()
//    }


    private fun navigateTo(data: RouteBundle) {
        try {
            viewModel.setState(AppState.Loading)
            navController.navigate(data.route, data.bundle)
        } catch (e: Exception) {
            Log.e("12123123123123Error", "Ошибка при навигации: ${e.localizedMessage}")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
