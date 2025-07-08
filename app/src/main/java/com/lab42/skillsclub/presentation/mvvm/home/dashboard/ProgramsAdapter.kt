package com.lab42.skillsclub.presentation.mvvm.home.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.ProgramPositionItemDTO
import com.lab42.skillsclub.databinding.ItemProgramBinding


class ProgramsAdapter (
    private val dataSet: List<ProgramPositionItemDTO>,
    private val viewModel: DashboardViewModel
): RecyclerView.Adapter<ProgramsAdapter.ProgramsViewHolder>() {

    private var selectedPosition: Int = -1

    class ProgramsViewHolder(private val binding: ItemProgramBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProgramPositionItemDTO, isSelected: Boolean) {
            binding.titleProgram.text = item.name
            binding.titleDescription.text = item.descriptor

            if (item.available == 0) {
                binding.titleProgram.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.titleDescription.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray))
                binding.lock.visibility = View.VISIBLE
                binding.doc.visibility = View.GONE
            }

            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position_active)
            } else {
                binding.root.setBackgroundResource(R.drawable.sh_frame_position)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProgramBinding.inflate(inflater, parent, false)
        return ProgramsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProgramsViewHolder, position: Int) {
            val isSelected = position == selectedPosition
            holder.bind(dataSet[position], isSelected)

            holder.itemView.setOnClickListener {
                viewModel.setItem(dataSet[position])
            }
        }

        override fun getItemCount(): Int = dataSet.size

}
