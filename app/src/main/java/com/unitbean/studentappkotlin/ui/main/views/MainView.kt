package com.unitbean.studentappkotlin.ui.main.views

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun onFirstOpen()
    fun onLoginSuccess(isAuth: Boolean)
    //fun onShowError(message: String?)
    //fun onBuyLinkReady(buyLinkResponse: BuyLinkResponse, id: String, type: String, data: String, price: Int)
}