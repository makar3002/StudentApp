package com.unitbean.studentappkotlin.ui.auth.views

import com.unitbean.studentappkotlin.utils.model.UserModel
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface AddLessonView : MvpView{
    //fun onProfileLoaded(user: UserModel)
    fun onCancel()
    fun onRegisterSuccess(isAuth: Boolean)
}