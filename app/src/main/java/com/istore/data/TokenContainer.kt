package com.istore.data

/**
 * object = singleton class
 * private set = just get
 */
object TokenContainer {
    var token: String? = null
        private set
    var refreshToken: String? = null
        private set

    fun update(token: String?, refreshToken: String?) {
        this.token = token
        this.refreshToken = refreshToken
    }
}