package com.istore.feature.profile

import com.istore.common.IViewModel
import com.istore.data.TokenContainer
import com.istore.data.repo.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : IViewModel() {

    val username: String
        get() = userRepository.getUsername()

    val isAuthenticated: Boolean
        get() = TokenContainer.token != null

    fun logout() = userRepository.logout()
}