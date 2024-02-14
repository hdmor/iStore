package com.istore.feature.auth

import com.istore.common.IViewModel
import com.istore.data.repo.UserRepository
import io.reactivex.Completable

/**
 * we create one viewModel for both fragments [loginFragment and signupFragment]
 */

class AuthViewModel(private val userRepository: UserRepository) : IViewModel() {

    fun login(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.login(email, password)
            .doFinally { progressBarLiveData.postValue(false) }
    }

    fun signup(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.signup(email, password)
            .doFinally { progressBarLiveData.postValue(false) }
    }

}