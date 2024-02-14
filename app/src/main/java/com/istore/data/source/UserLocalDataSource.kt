package com.istore.data.source

import android.content.SharedPreferences
import com.istore.data.MessageResponse
import com.istore.data.TokenContainer
import com.istore.data.TokenResponse
import io.reactivex.Single

class UserLocalDataSource(private val sharedPreferences: SharedPreferences) : UserDataSource {

    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signup(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() =
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )


    override fun saveToken(token: String, refreshToken: String) =
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
        }.apply()

    override fun saveUsername(username: String) =
        sharedPreferences.edit().apply {
            putString("username", username)
        }.apply()


    override fun getUsername(): String = sharedPreferences.getString("username", "") ?: ""

    /**
     * do not implement logout in server side so
     * we just clear sharedPreferences information in local app
     */
    override fun logout() = sharedPreferences.edit().apply { clear() }.apply()
}