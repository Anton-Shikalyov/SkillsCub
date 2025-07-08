package com.lab42.skillsclub.presentation.mvvm.home.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.data.dto.response.Dictionary
import com.lab42.skillsclub.databinding.FragmentTerminBinding

class LessonDictionaryAdapter(
    private val dictionary: List<Dictionary>
) : RecyclerView.Adapter<LessonDictionaryAdapter.LessonDictionaryViewHolder>() {

    inner class LessonDictionaryViewHolder(val binding: FragmentTerminBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonDictionaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentTerminBinding.inflate(inflater, parent, false)
        return LessonDictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonDictionaryViewHolder, position: Int) {

        holder.binding.terminTitle.text = dictionary[position].word
        holder.binding.terminDescription.text = dictionary[position].description

    }

    override fun getItemCount(): Int = minOf(dictionary.size)

}
