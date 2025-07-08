package com.lab42.skillsclub.presentation.mvvm

import android.content.Context
import androidx.core.content.ContextCompat
import com.lab42.skillsclub.NameSpace
import com.lab42.skillsclub.R

class CreateHtml {
    companion object {
        fun getHtml(context: Context, html: String) : String {
            val backgroundColorInt = ContextCompat.getColor(context, R.color.dn_background_main)
            val backgroundColorHex = String.format("#%06X", 0xFFFFFF and backgroundColorInt)

            val textColorInt = ContextCompat.getColor(context, R.color.dn_text)
            val textColorHex = String.format("#%06X", 0xFFFFFF and textColorInt)

            return NameSpace.HTML_HEADER + backgroundColorHex + NameSpace.HTML_BODY_BACKGROUND + textColorHex + NameSpace.HTML_BODY_TEXT_COLOR + html.trimIndent() + NameSpace.HTML_FOOTER
        }
    }
}