package com.istore.data.source

import com.istore.data.MessageResponse
import com.istore.data.TokenResponse
import io.reactivex.Single

interface UserDataSource {

    /**
     * DataSource return type is different than repository
     */
    fun login(username: String, password: String): Single<TokenResponse>
    fun signup(username: String, password: String): Single<MessageResponse>
    fun loadToken()
    fun saveToken(token: String, refreshToken: String)
    fun saveUsername(username: String)
    fun getUsername(): String
    fun logout()
}