package com.unitbean.studentappkotlin.ui.lessonsSchedule.presentsers

import com.unitbean.studentappkotlin.di.DIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.InjectViewState
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext


@InjectViewState
class LessonsSchedulePresenter : MvpPresenter<>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main


    init {
        DIManager.getLessonsScheduleSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearAuthSubcomponent()
    }

    fun loadProfile() {
        launch {
            try {
                val user = withContext(Dispatchers.Default) {interactor.loadProfile()}
                viewState.onProfileLoaded(user)
            } catch (e: Exception) {
                if (e == IllegalStateException("User not loaded")) {
                    viewState.onProfileLoaded(
                        UserModel(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                }
                //LogUtils.e(TAG, e.message, e)
            }
        }
    }

    fun authUser (user: UserModel){
        launch {
            try {
                withContext(Dispatchers.Default){interactor.authUser(user)}
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