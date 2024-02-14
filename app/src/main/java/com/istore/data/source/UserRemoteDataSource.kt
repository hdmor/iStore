package com.istore.data.source

import com.google.gson.JsonObject
import com.istore.data.MessageResponse
import com.istore.data.TokenResponse
import com.istore.services.http.ApiService
import io.reactivex.Single

const val CLIENT_ID = 2
const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"

class UserRemoteDataSource(private val apiService: ApiService) : UserDataSource {

    override fun login(username: String, password: String): Single<TokenResponse> =
        apiService.login(
            JsonObject().apply {
                addProperty("username", username)
                addProperty("password", password)
                addProperty("grant_type", "password")
                addProperty("client_id", CLIENT_ID)
                addProperty("client_secret", CLIENT_SECRET)
            }
        )

    override fun signup(username: String, password: String): Single<MessageResponse> =
        apiService.signup(JsonObject().apply {
            addProperty("email", username)
            addProperty("password", password)
        })

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun saveUsername(username: String) {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}