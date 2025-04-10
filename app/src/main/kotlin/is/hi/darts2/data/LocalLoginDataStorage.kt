package `is`.hi.darts2.data

import android.content.Context
import android.content.SharedPreferences

class LocalLoginDataStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun saveCredentials(username: String, password: String) {
        prefs.edit().apply {
            putString("username", username)
            putString("password", password)
            apply() // or commit() if you want synchronous saving
        }
    }

    fun getUsername(): String? = prefs.getString("username", null)

    fun getPassword(): String? = prefs.getString("password", null)

    fun clearCredentials() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}
