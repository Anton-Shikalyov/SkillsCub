package com.lab42.skillsclub.presentation.mvvm.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.data.dto.response.HelpData

class HelpAdapter (
    private val items: List<HelpData>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SHORT = 0
        private const val TYPE_EXTENDED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type == 0) TYPE_SHORT else TYPE_EXTENDED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SHORT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_help, parent, false)
            ShortItem(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_help_extended, parent, false)
            ExtendedItem(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is ShortItem) {
            holder.helpTitle.text = item.question
            holder.openAnswerBtn.setOnClickListener{
                items[position].type = TYPE_EXTENDED
                notifyItemChanged(position)
            }
        } else if (holder is ExtendedItem) {
            holder.helpTitleExtended.text = item.question
            holder.answer.text = item.answer

            holder.btnHideExtended.setOnClickListener{
                items[position].type = TYPE_SHORT
                notifyItemChanged(position)
            }
            if (item.extended.isNotEmpty()) {
                holder.learnMore.visibility = View.VISIBLE
            } else {
                holder.learnMore.visibility = View.GONE
            }

            holder.learnMore.setOnClickListener{
                val bundle = Bundle().apply {
                    putString(NameSpace.EXTENDED, item.extended)
                }
                val dialog = HelpDialog()
                dialog.arguments = bundle
                dialog.show(fragmentManager, "Dialog")
            }
        }
    }


    override fun getItemCount() = items.size

    class ShortItem(view: View) : RecyclerView.ViewHolder(view) {
        val helpTitle: TextView = view.findViewById(R.id.helpTitle)
        val openAnswerBtn: ImageButton = view.findViewById(R.id.openAnswer)
    }

    class ExtendedItem(view: View) : RecyclerView.ViewHolder(view) {
        val helpTitleExtended: TextView = view.findViewById(R.id.helpTitleExtended)
        val answer: TextView = view.findViewById(R.id.answer)
        val btnHideExtended: ImageButton = view.findViewById(R.id.btnHideExtended)
        val learnMore: Button = view.findViewById(R.id.learn_more)
    }
}
