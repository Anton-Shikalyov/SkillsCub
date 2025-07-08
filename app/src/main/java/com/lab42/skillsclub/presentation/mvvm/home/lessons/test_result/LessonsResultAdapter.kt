package com.lab42.skillsclub.presentation.mvvm.home.lessons.test_result

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.request.StepTasksData
import com.lab42.skillsclub.data.dto.response.GetStepTasksData
import com.lab42.skillsclub.databinding.LessonItemResultBinding

class LessonsResultAdapter(
    private val result: List<StepTasksData>,
    private val lessons: List<GetStepTasksData>
) : RecyclerView.Adapter<LessonsResultAdapter.PositionAnswerViewHolder>() {

    inner class PositionAnswerViewHolder(val binding: LessonItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionAnswerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LessonItemResultBinding.inflate(inflater, parent, false)
        return PositionAnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PositionAnswerViewHolder, position: Int) {
        val item = result[position]
        val context = holder.itemView.context

        val background: Drawable
        val icResult: Int
        val textResult: String
        val textResultColor: Int


        if (item.taskExercises.score == 1) {
            background = ContextCompat.getDrawable(context, R.drawable.sh_frame_position_active)!!
            icResult = R.drawable.ic_correct
            textResult = getString(context, R.string.correct)
            textResultColor = ContextCompat.getColor(context, R.color.blue_corner)
        } else {
            background = ContextCompat.getDrawable(context, R.drawable.sh_frame_position_active_incorrect)!!
            icResult =  R.drawable.ic_incorrect
            textResult = getString(context, R.string.incorrect)
            textResultColor = ContextCompat.getColor(context, R.color.red_error)

        }

        holder.binding.root.background = background
        holder.binding.resultText.text = textResult
        holder.binding.resultText.setTextColor(textResultColor)
        holder.binding.lessonProgressStatus.setImageResource(icResult)
        holder.binding.lessonProgressStatus.drawable
        holder.binding.currentLesson.text = (position+1).toString()
        holder.binding.amountOfLessons.text = result.size.toString()
        holder.binding.taskType.text = lessons[position].taskName
        holder.binding.question.text = lessons[position].exercisesTask.exerciseTask
        holder.binding.answer.text = result[position].taskExercises.userAnswer
    }

    override fun getItemCount(): Int = minOf(result.size, lessons.size)

}
