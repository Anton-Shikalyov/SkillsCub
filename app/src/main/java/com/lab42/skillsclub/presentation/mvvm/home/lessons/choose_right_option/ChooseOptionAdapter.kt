package com.lab42.skillsclub.presentation.mvvm.home.lessons.choose_right_option

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.ItemPositionBinding
import com.lab42.skillsclub.presentation.mvvm.home.HomeViewModel

class ChooseOptionAdapter (
    private val answer: List<String>,
    private val homeViewModel: HomeViewModel
) :
    RecyclerView.Adapter<ChooseOptionAdapter.PositionAnswerViewHolder>() {

        private var selectedPosition: Int = -1
        private var selectedItemPos: Int = -1
        private var previousSelected: Int = -1

    private fun setSelectedPosition(position: Int) {
            previousSelected = selectedItemPos
            selectedItemPos = position
            selectedPosition = position
            homeViewModel.setAnswer(position)
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedItemPos)
    }


        class PositionAnswerViewHolder(private val binding: ItemPositionBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(name: String, isSelected: Boolean) {
                binding.positionText.text = name
                if (isSelected) {
                    binding.root.setBackgroundResource(R.drawable.sh_frame_position_active)
                } else {
                    binding.root.setBackgroundResource(R.drawable.sh_frame_position_padding)
                }
            }
        }

        fun getSelectedPosition(): Int {
            return selectedPosition
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionAnswerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPositionBinding.inflate(inflater, parent, false)
            return PositionAnswerViewHolder(binding)
        }


    override fun onBindViewHolder(holder: PositionAnswerViewHolder, position: Int) {
        val isSelected = position == selectedItemPos
        holder.bind(answer[position] ,isSelected)

        holder.itemView.setOnClickListener {
            setSelectedPosition(position)
        }
    }
        override fun getItemCount(): Int = answer.size
}

