package com.lab42.skillsclub.presentation.mvvm.home.steps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.StepsSection
import com.lab42.skillsclub.databinding.ItemStepBinding


class StepListAdapter (
    private val dataSet: List<StepsSection>,
    private val viewModel: StepListViewModel
): RecyclerView.Adapter<StepListAdapter.StepViewHolder>() {

    private var selectedPosition: Int = -1

    class StepViewHolder(private val binding: ItemStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StepsSection, isSelected: Boolean) {

            binding.titleProgram.text = item.name
            binding.titleDescription.text = item.descriptor
            binding.stepProgressBar.min = 0
            binding.stepProgressBar.max = 100
            binding.stepProgressBar.progress = item.passScore
            binding.result2.text = item.passScore.toString()
            binding.bigId.text = if (item.ordering < 10) "0${item.ordering}" else item.ordering.toString()

            if (item.available == 0) {
                binding.titleProgram.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.titleDescription.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.result1.visibility = View.GONE
                binding.result2.visibility = View.GONE
                binding.result3.visibility = View.GONE
                binding.bigId.visibility = View.GONE
                binding.stepProgressBar.visibility = View.GONE
                binding.lock.visibility = View.VISIBLE
            }

            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position_active)
            } else {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStepBinding.inflate(inflater, parent, false)
        return StepViewHolder(binding)
    }


    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(dataSet[position], isSelected)

        holder.itemView.setOnClickListener {
            if (dataSet[position].available == 1) {
                viewModel.setItem(dataSet[position], position + 1)
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

}
