package com.istore.services.http

import android.util.Log
import com.google.gson.JsonObject
import com.istore.common.TAG
import com.istore.data.TokenContainer
import com.istore.data.TokenResponse
import com.istore.data.source.CLIENT_ID
import com.istore.data.source.CLIENT_SECRET
import com.istore.data.source.UserDataSource
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * this class created for handle refreshToken
 * if your request response get 401 Unauthenticated error
 * this authenticate function called
 * and you can send refreshToken to server for getting new accessToken
 *
 * to injecting apiService without having Context, we must use implementing KoinComponent
 */
class IAuthenticator : okhttp3.Authenticator, KoinComponent {

    private val apiService: ApiService by inject()
    private val userLocalDataSource: UserDataSource by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenContainer.token != null && TokenContainer.refreshToken != null &&
            !response.request().url().pathSegments().last().equals("token", false)
        )
            try {
                val token = refreshToken()
                if (token.isEmpty()) return null
                return response.request().newBuilder().header("Authorization", "Bearer $token")
                    .build()
            } catch (e: Exception) {
                Log.i(TAG, "authenticate: ${e.message}")
            }
        return null
    }

    private fun refreshToken(): String {
        val response: retrofit2.Response<TokenResponse> =
            apiService.refreshToken(JsonObject().apply {
                addProperty("grant_type", "refresh_token")
                addProperty("refresh_token", TokenContainer.refreshToken)
                addProperty("client_id", CLIENT_ID)
                addProperty("client_secret", CLIENT_SECRET)
            }).execute()
        response.body()?.let {
            TokenContainer.update(it.access_token, it.refresh_token)
            userLocalDataSource.saveToken(it.access_token, it.refresh_token)
            return it.access_token
        }
        return ""
    }
}