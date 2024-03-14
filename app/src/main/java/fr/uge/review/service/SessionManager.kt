package fr.uge.review.service

import android.content.Context
import android.content.SharedPreferences
import fr.uge.review.R
import fr.uge.review.dto.user.Role
import fr.uge.review.dto.user.UserDataDTO
import okhttp3.Credentials

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ID = "user_id"
        const val USER_ROLE = "user_role"
    }

    private fun createToken(username: String, password: String): String =
        Credentials.basic(username, password)

    fun setToken(username: String, password: String) {
        val token = createToken(username, password)
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? = prefs.getString(AUTH_TOKEN, null)

    fun isAuthenticated(): Boolean = getAuthToken() != null

    fun setUserData(userData: UserDataDTO) {
        val editor = prefs.edit()
        editor.putLong(USER_ID, userData.id)
        editor.putString(USER_ROLE, userData.role.name)
        editor.apply()
    }

    fun getUserId(): Long = prefs.getLong(USER_ID, -1)

    fun getUserRole(): Role? {
        val role = prefs.getString(USER_ROLE, null)
        return if (role == null) null else Role.valueOf(role)
    }

    fun clear() = prefs.edit().clear().apply()
}