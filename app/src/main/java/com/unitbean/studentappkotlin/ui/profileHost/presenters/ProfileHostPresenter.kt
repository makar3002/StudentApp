package com.unitbean.studentappkotlin.ui.profileHost.presenters

import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.profileHost.interactors.ProfileHostInteractor
import com.unitbean.studentappkotlin.ui.profileHost.views.ProfileHostView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@InjectViewState
class ProfileHostPresenter:  MvpPresenter<ProfileHostView>(), CoroutineScope{
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    @Inject
    lateinit var interactor: ProfileHostInteractor

    init {
        DIManager.getProfileHostSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearProfileHostSubcomponent()
    }

    fun isShowSignIn(): Boolean {
        return !interactor.isUserLogged()
    }
}
