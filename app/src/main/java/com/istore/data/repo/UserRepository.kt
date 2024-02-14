package com.istore.data.repo

import io.reactivex.Completable

interface UserRepository {

    /**
     * reason for using Completable as Single return type:
     * repository can handle saving auth token in db
     * because there aren't necessary view know about this token
     */
    fun login(username: String, password: String): Completable
    fun signup(username: String, password: String): Completable
    fun loadToken()
    fun getUsername(): String
    fun logout()
}