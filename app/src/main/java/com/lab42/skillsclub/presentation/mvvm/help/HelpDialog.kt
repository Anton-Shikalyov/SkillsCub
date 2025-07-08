package com.lab42.skillsclub.presentation.mvvm.help

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R
import com.lab42.skillsclub.presentation.mvvm.CreateHtml


class HelpDialog : DialogFragment() {

    private var extebded: String? = ""


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            val margin = (screenWidth * 0.1f).toInt()

            val layoutParams = window.attributes
            layoutParams.width = screenWidth - margin * 2
//            layoutParams.height = screenHeight - margin * 2

            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes = layoutParams
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            extebded = it.getString(NameSpace.EXTENDED) ?: ""
        }

        val webView: WebView = view.findViewById(R.id.webViewHelp)
        webView.settings.javaScriptEnabled = true;
        val htmlContent = CreateHtml.getHtml(requireContext(), extebded!!)


        webView.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html",
            "UTF-8",
            null
        )

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        view.findViewById<ImageButton>(R.id.btnHideExtended).setOnClickListener {
            dismiss()
        }
    }
}
