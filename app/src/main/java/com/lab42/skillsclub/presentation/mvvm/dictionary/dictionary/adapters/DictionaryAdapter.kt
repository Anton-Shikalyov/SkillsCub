package com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.mvvm.dictionary.DictList
import com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.DictionaryViewModel

class DictionaryAdapter(
    private val items: List<DictList>,
    private val viewModel: DictionaryViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_WORD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].type == 0) TYPE_HEADER else TYPE_WORD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_abc_title, parent, false)
            LetterViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_abc_word, parent, false)
            WordViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is LetterViewHolder) {
            holder.letterText.text = item.word
        } else if (holder is WordViewHolder) {
            holder.letterText.text = item.word
            holder.letterText.setOnClickListener{
                viewModel.setPickedWord(DictList(item.word, item.description, 1))
            }
        }
    }


    override fun getItemCount() = items.size

    class LetterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val letterText: TextView = view.findViewById(R.id.textTitle)
    }

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val letterText: TextView = view.findViewById(R.id.textWord)
    }
}
