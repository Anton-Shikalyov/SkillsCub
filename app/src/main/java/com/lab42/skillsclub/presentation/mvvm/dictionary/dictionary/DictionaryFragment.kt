package com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.databinding.FragmentDictionaryBinding
import com.lab42.skillsclub.databinding.TitleToolbarBinding
import com.lab42.skillsclub.presentation.AppState
import com.lab42.skillsclub.presentation.MainViewModel
import com.lab42.skillsclub.presentation.mvvm.dictionary.DictionaryContainerViewModel
import com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.adapters.ABCbookAdapter
import com.lab42.skillsclub.presentation.mvvm.dictionary.dictionary.adapters.DictionaryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DictionaryFragment : Fragment() {

    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DictionaryViewModel by viewModels()
    private val dictionaryViewModel: DictionaryContainerViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FragmentLifecycle", "DictionaryFragment created")
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alphabetList = ('A'..'Z').map { it.toString() }
        val recyclerAlphabet = binding.recyclerAlphabet
        val recyclerDictionary = binding.recyclerDictionary

        setTopBar()

        recyclerAlphabet.layoutManager = LinearLayoutManager(requireActivity())
        recyclerAlphabet.adapter = ABCbookAdapter(alphabetList, viewModel,0)
        recyclerDictionary.layoutManager = LinearLayoutManager(requireActivity())
        recyclerDictionary.adapter = DictionaryAdapter(emptyList(), viewModel)

        viewModel.dictList.observe(viewLifecycleOwner) {
            Log.i("zczczczcx", it.toString())

            dictionaryViewModel.setState(AppState.Success(""))
            recyclerDictionary.adapter = DictionaryAdapter(it, viewModel)
        }

        viewModel.searchList.observe(viewLifecycleOwner) {
            recyclerDictionary.adapter = DictionaryAdapter(it, viewModel)
        }

        viewModel.pickChar.observe(viewLifecycleOwner) {
            recyclerDictionary.smoothScrollToPosition(it)
        }

        viewModel.pickWord.observe(viewLifecycleOwner) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.DictionaryFragment, false)
                .build()

            findNavController().navigate(R.id.action_DictionaryFragment_to_TerminFragment, Bundle().apply {
                putString(NameSpace.TITLE, it.word)
                putString(NameSpace.DESCRIPTION, it.description)
            }, navOptions)

        }

        recyclerAlphabet.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                recyclerAlphabet.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val recyclerHeight = recyclerAlphabet.height
                recyclerAlphabet.adapter = ABCbookAdapter(alphabetList, viewModel, (recyclerHeight-28*dpToPx(16))/26/2)
            }
        })


        viewLifecycleOwner.lifecycleScope.launch {
            binding.dictionarySearch.textChangesFlow()
                .collect { text ->
                    viewModel.findWords(text)
                }
        }

        viewModel.logOut.observe(viewLifecycleOwner) {
            mainViewModel.logOut()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
        }
    }

    private fun setTopBar() {
        val container = requireActivity().findViewById<FrameLayout>(R.id.app_bar_container)
        container.removeAllViews()
        val toolbarBinding = TitleToolbarBinding.inflate(layoutInflater, container, false)
        container.addView(toolbarBinding.root)

        toolbarBinding.title.text = getString(R.string.title_dictionary)

    }

    private fun EditText.textChangesFlow(): Flow<CharSequence?> = callbackFlow {
        val watcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }
        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(watcher) }
    }

    fun dpToPx(dp: Int): Int {
        val density = context?.resources?.displayMetrics!!.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
