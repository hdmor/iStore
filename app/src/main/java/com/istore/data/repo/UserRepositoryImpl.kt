package com.istore.data.repo

import com.istore.data.TokenContainer
import com.istore.data.TokenResponse
import com.istore.data.source.UserDataSource
import io.reactivex.Completable

class UserRepositoryImpl(
    private val userRemoteDataSource: UserDataSource,
    private val userLocalDataSource: UserDataSource
) : UserRepository {

    override fun login(username: String, password: String): Completable =
        userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()

    override fun signup(username: String, password: String): Completable =
        userRemoteDataSource.signup(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(username, it)
        }.ignoreElement()

    override fun loadToken() = userLocalDataSource.loadToken()

    override fun getUsername(): String = userLocalDataSource.getUsername()

    override fun logout() {
        userLocalDataSource.logout()
        TokenContainer.update(null, null)
    }

    private fun onSuccessfulLogin(username: String, tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUsername(username)
    }

}