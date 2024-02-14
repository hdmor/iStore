package com.istore.common.exception

import androidx.annotation.StringRes

/**
 *  custom exception with 3 constructor parameter
 *  1. type of enum exception message
 *  2. self app throwable messages as android xml string message [offline message]
 *  3. string message response from the server. [online message]
 */

class IException(
    val type: Type,
    @StringRes val userFriendlyMessage: Int = 0,
    val serverMessage: String? = null
) : Throwable() {

    enum class Type { SIMPLE, DIALOG, AUTH }
}