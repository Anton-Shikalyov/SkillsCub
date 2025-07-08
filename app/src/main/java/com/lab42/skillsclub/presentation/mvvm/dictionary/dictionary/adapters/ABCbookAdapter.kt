package com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.DictionaryViewModel

class ABCbookAdapter(
    private val letters: List<String>,
    private val viewModel: DictionaryViewModel,
    private val pad: Int
) : RecyclerView.Adapter<ABCbookAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textWord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alphabet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = letters[position]
        holder.textView.setPadding( 0, pad, 0, pad)
        holder.textView.setOnClickListener{
            viewModel.setPickedLetter(letters[position].first().uppercaseChar())
        }
    }


    override fun getItemCount(): Int = letters.size
}
