package com.lab42.skillsclub.presentation.mvvm.home.sections

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.DataSection
import com.lab42.skillsclub.databinding.ItemSectionBinding


class SectionAdapter (
    private val dataSet: List<DataSection>,
    private val viewModel: SectionViewModel
): RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    private var selectedPosition: Int = -1

    class SectionViewHolder(private val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataSection, isSelected: Boolean) {
            binding.stepsCount.text = item.stepCount
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSectionBinding.inflate(inflater, parent, false)
        return SectionViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        holder.bind(dataSet[position], isSelected)

        holder.itemView.setOnClickListener {
            viewModel.setItem(dataSet[position])
        }
    }

    override fun getItemCount(): Int = dataSet.size

}
