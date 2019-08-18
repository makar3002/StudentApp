package com.unitbean.studentappkotlin.ui.main.presenters

import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.main.interactors.MainInteractor
import com.unitbean.studentappkotlin.ui.main.views.MainView
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@InjectViewState
class MainPresenter: MvpPresenter<MainView>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main
    @Inject lateinit var interactor: MainInteractor

    val fragmentStack = Stack<Int>()
    val isAuth: Boolean
        get() = interactor.isAuth()


    init {
        DIManager.getMainSubcomponent().inject(this)
    }
    override fun onFirstViewAttach() {
        launch {
            try {
                withContext(Dispatchers.Default) {
                    interactor.loadSavedUser()
                }
            } catch (e: Exception) {
                //LogUtils.e(TAG, e.message, e)
            }
        }
        viewState.onFirstOpen()
    }

    fun onVkSignIn(res: VKAccessToken) {
        launch {
            //try {
                val isSuccess = withContext(Dispatchers.Default) { interactor.onVkSignIn(res) }
                if (isSuccess) withContext(Dispatchers.Default) { interactor.loadSavedUser() }
                viewState.onLoginSuccess(isSuccess)
            //} catch (e: Exception) {
                //LogUtils.e(TAG, e.message, e)
                //viewState.onShowError(e.message)
            //}
        }
    }
    fun onNotVkSignIn(userId: String) {
        launch {
            try {
                val isSuccess = interactor.onNotVkSignIn(userId)
                if (isSuccess) withContext(Dispatchers.Default) { interactor.loadSavedUser() }
                viewState.onLoginSuccess(isSuccess)
            } catch (e: Exception) {
                //LogUtils.e(TAG, e.message, e)
                //viewState.onShowError(e.message)
            }
        }
    }

    override fun onDestroy() {
        DIManager.clearMainSubcomponent()
    }

    fun addFragmentToStack(fragmentId: Int) {
        if (fragmentStack.contains(fragmentId)) {
            fragmentStack.remove(fragmentId)
        }

        fragmentStack.add(fragmentId)
    }

    companion object {
        private const val TAG = "MainPresenter"
    }
}
