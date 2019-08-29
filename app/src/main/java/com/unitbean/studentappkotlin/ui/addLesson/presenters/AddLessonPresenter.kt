package com.unitbean.studentappkotlin.ui.auth.presenters

import com.unitbean.studentappkotlin.di.DIManager
import com.unitbean.studentappkotlin.ui.auth.interactors.AddLessonInteractor
import com.unitbean.studentappkotlin.ui.auth.interactors.AuthInteractor
import com.unitbean.studentappkotlin.ui.auth.views.AddLessonView
import com.unitbean.studentappkotlin.ui.auth.views.AuthView
import com.unitbean.studentappkotlin.ui.lessonsSchedule.models.ILessonViewModel
import com.unitbean.studentappkotlin.utils.model.LessonModel
import com.unitbean.studentappkotlin.utils.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

@InjectViewState
class AddLessonPresenter : MvpPresenter<AddLessonView>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    @Inject
    lateinit var interactor: AddLessonInteractor

    init {
        DIManager.getAddLessonSubcomponent().inject(this)
    }

    override fun onDestroy() {
        DIManager.clearAddLessonSubcomponent()
    }

    /*fun loadLesson(date: Date) {
        launch {
            try {
                val newLessons = withContext(Dispatchers.IO) {interactor.loadLessons(thisDate!!)} ?: throw IllegalStateException("Dates not loaded")
                val lessons = ArrayList<ILessonViewModel>()
                lessons.addAll(newLessons)
                viewState.onLessonsLoaded(lessons)
            } catch (e: Exception) {
                dates.clear()
                //Log.e(TAG, e.message, e)
                /*if (e is IOException) {
                    viewState.onShowError(DIManager.appComponent.context.getString(R.string.error_network_connection))
                }*/
                //LogUtils.e(TAG, e.message, e)
            }

        }
    }*/

    fun registerLesson (lesson: LessonModel){
        launch {
            try {
                withContext(Dispatchers.Default){interactor.registerLesson(lesson)}
            } catch (e: Exception){

            }
        }
    }

    fun getCurrentDate (): Date{
        return interactor.getCurrentDate()
    }
    fun getPeriod (): Boolean{
        return interactor.getPeriod()
    }

    fun successLogin (){
        launch {
            try {
                viewState.onRegisterSuccess(true)
            } catch (e: Exception){

            }
        }
    }

    fun cancel (){
        viewState.onCancel()
    }
}