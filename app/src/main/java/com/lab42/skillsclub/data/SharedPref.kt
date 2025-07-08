package com.lab42.skillsclub.data

import android.content.Context
import android.content.SharedPreferences
import com.lab42.skillsclub.NameSpace.SHARED_PREF
import javax.inject.Inject

interface SharedPrefInterface {
    fun setToken(token: String)
    fun getToken(): String?
    fun setTheme(theme: String)
    fun getTheme(): String?

}
class SharedPrefImpl @Inject constructor(
    context: Context
): SharedPrefInterface {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    override fun setToken(token: String) {
        sharedPreferences.edit()
            .putString("TOKEN_KEY", token)
            .apply()
    }

    override fun getToken(): String? {
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    override fun setTheme(theme: String) {
        sharedPreferences.edit()
            .putString("THEME", theme)
            .apply()
    }

    override fun getTheme(): String? {
        return sharedPreferences.getString("THEME", null)
    }


}