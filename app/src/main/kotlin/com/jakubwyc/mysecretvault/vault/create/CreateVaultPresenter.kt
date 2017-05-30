package com.jakubwyc.mysecretvault.vault.create

import com.jakubwyc.mysecretvault.Presenter
import com.jakubwyc.mysecretvault.R
import com.jakubwyc.mysecretvault.SystemContext
import com.jakubwyc.mysecretvault.model.User
import com.jakubwyc.mysecretvault.sha256
import rx.subscriptions.CompositeSubscription

class CreateVaultPresenter(val systemContext: SystemContext) : Presenter<CreateVaultView> {

    private var view: CreateVaultView? = null
    private val subscriptions = CompositeSubscription()

    private var login: String = ""
    private var password: String = ""
    private var passwordRepeat: String = ""
    private val userRepository
        get() = systemContext.userRepository

    override fun attachView(view: CreateVaultView) {
        this.view = view
    }

    override fun detachView() {
        subscriptions.clear()
        view = null
    }

    fun onLoginChanged(value: CharSequence) {
        login = value.toString()
    }

    fun onPasswordChanged(value: CharSequence) {
        password = value.toString()
    }

    fun onPasswordRepeatChanged(value: CharSequence) {
        passwordRepeat = value.toString()
    }

    fun createVault() {
        if (isUserDataValid) {
            val user = User(login, sha256(password))
            saveUser(user)
        }
    }

    private val isUserDataValid: Boolean
        get() {
            var valid = false
            if (login.isBlank()) {
                view?.showToast(R.string.login_empty)
            } else if (password.isBlank()) {
                view?.showToast(R.string.password_empty)
            } else if (passwordRepeat.isBlank()) {
                view?.showToast(R.string.password_repeat_empty)
            } else if (password != passwordRepeat) {
                view?.showToast(R.string.passwords_not_equal)
            } else {
                valid = true
            }
            return valid
        }

    private fun saveUser(user: User) {
        subscriptions.add(userRepository.saveUser(user)
                .subscribe {
                    view?.showToast(R.string.user_saved)
                    view?.goToMainScreen()
                }
        )
    }
}
