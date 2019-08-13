package com.unitbean.studentappkotlin.ui.auth.presenters

import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.auth.interactors.AuthInteractor
import com.unitbean.studentappkotlin.ui.auth.views.AuthView
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import kotlinx.android.synthetic.main.fragment_auth_first.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
@InjectViewState
class AuthPresenter : MvpPresenter<AuthView>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    @Inject
    lateinit var interactor: AuthInteractor

    init {
        DIManager.getAuthSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearAuthSubcomponent()
    }

    fun loadProfile() {
        launch {
            try {
                val user = interactor.loadProfile()
                viewState.onProfileLoaded(user)
            } catch (e: Exception) {
                if (e == IllegalStateException("User not loaded")) {

                }
                //LogUtils.e(TAG, e.message, e)
            }
        }
    }

    fun authUser (user: UserModel){
        launch {
            try {
                interactor.authUser(user)
            } catch (e: Exception){

            }
        }
    }

    fun successLogin (){
        launch {
            try {
                viewState.onLoginSuccess(true)
            } catch (e: Exception){

            }
        }
    }

    fun logout (){
        interactor.logout()
        viewState.onLogout()
    }
}