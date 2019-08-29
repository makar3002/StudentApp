package com.unitbean.studentappkotlin.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.presenters.AuthPresenter
import com.unitbean.studentappkotlin.ui.auth.views.AuthView
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profileHost.fragments.ProfileHostFragment
import com.unitbean.studentappkotlin.utils.model.UserModel
import kotlinx.android.synthetic.main.fragment_auth_first.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class AuthFragment : MvpAppCompatFragment(), AuthView, View.OnClickListener  {

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_auth_accept.setOnClickListener(this)
        b_auth_back.setOnClickListener(this)
        presenter.loadProfile()
    }

    override fun onClick(v: View?) {
        when (v) {
            b_auth_accept -> {
                if (et_auth_first_name.text.toString() == "" || et_auth_last_name.text.toString() == "" || et_auth_institute.text.toString() == "" || et_auth_course.text.toString() == "" || et_auth_group.text.toString() == "" || et_auth_recordBook.text.toString() == "") {
                    Snackbar.make(cl_auth_fragment, "Все поля должны быть заполнены", Snackbar.LENGTH_SHORT).show()
                } else {
                    presenter.authUser(
                        UserModel(
                            "",
                            et_auth_first_name.text.toString(),
                            et_auth_last_name.text.toString(),
                            et_auth_institute.text.toString(),
                            et_auth_course.text.toString(),
                            et_auth_group.text.toString(),
                            et_auth_recordBook.text.toString(),
                            et_auth_semester.text.toString()
                        )
                    )
                    presenter.successLogin()
                }
            }
            b_auth_back -> presenter.logout()
        }

    }
    override fun onLoginSuccess (isAuth: Boolean){
        if(isAuth) {
            et_auth_first_name.setText("")
            et_auth_last_name.setText("")
            et_auth_institute.setText("")
            et_auth_course.setText("")
            et_auth_group.setText("")
            et_auth_recordBook.setText("")
            et_auth_semester.setText("")
            (requireActivity() as? MainActivity)?.apply {
                openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
                showNavigationBar(true)
            }
        }
    }

    override fun onProfileLoaded(user: UserModel) {
        et_auth_first_name.setText(user.firstName)
        et_auth_last_name.setText(user.lastName)
        et_auth_institute.setText(user.institute)
        et_auth_course.setText(user.course)
        et_auth_group.setText(user.group)
        et_auth_recordBook.setText(user.recordBook)
        et_auth_semester.setText(user.semester)
    }

    override fun onLogout() {
        (requireActivity() as? MainActivity)?.apply{
            openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
            showNavigationBar(true)
        }
    }

    companion object {
        val TAG = "AuthFragment"
        fun newInstance(): AuthFragment {
            return AuthFragment()
        }
    }
}