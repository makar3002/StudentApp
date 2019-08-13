package com.unitbean.studentappkotlin.ui.profile.presenters

import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profile.fragments.ProfileFragment
import com.unitbean.studentappkotlin.ui.profile.interactors.ProfileInteractor
import com.unitbean.studentappkotlin.ui.profile.views.ProfileView
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@InjectViewState
class ProfilePresenter : MvpPresenter<ProfileView>(), CoroutineScope{

    @Inject
    lateinit var interactor: ProfileInteractor

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    init {
        DIManager.getProfileSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearProfileSubcomponent()
    }

    fun loadProfile() {
        launch {
            try {
                val user = withContext(Dispatchers.Default) {interactor.loadProfile()}
                viewState.onProfileLoaded(user)
            } catch (e: Exception) {
                //LogUtils.e(TAG, e.message, e)
            }
        }
    }

    fun logout() {
        interactor.logout()
        viewState.onLogout()
    }
}
