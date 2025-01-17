package com.example.instant_message.domain.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SessionManager private constructor(context: Context) {

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_TOKEN = "key_token"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_CONNECTED = "isConnected"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // 保存 token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    // 获取 token
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun saveUUID(uuid: String){
        sharedPreferences.edit().putString("uuid", uuid).apply()
    }

    fun getUUID(): String? {
        return sharedPreferences.getString("uuid", null)
    }

    // 清除 session（例如登出时）
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
        isLoggedInLiveData.value = false
    }

    fun setLoggedIn(loggIn : Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggIn).apply()
        isLoggedInLiveData.value = loggIn
    }

    fun setUsername(username: String){
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun setConnected(connected: Boolean){
        sharedPreferences.edit().putBoolean(KEY_IS_CONNECTED, connected).apply()
        isConnectedLiveData.value = connected
    }

    private val isConnectedLiveData : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = sharedPreferences.getBoolean(KEY_IS_CONNECTED, false)
        }
    }
    fun isConnected(): LiveData<Boolean> {
        return isConnectedLiveData
    }

    private val isLoggedInLiveData : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        }
    }
    fun isLoggedIn(): LiveData<Boolean> {
        return isLoggedInLiveData
    }
}
