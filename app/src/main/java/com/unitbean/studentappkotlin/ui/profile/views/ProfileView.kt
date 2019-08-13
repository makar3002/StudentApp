package com.unitbean.studentappkotlin.ui.profile.views

import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface ProfileView : MvpView {
    fun onProfileLoaded(user: UserModel)
    fun onLogout()
}