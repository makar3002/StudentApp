package com.unitbean.studentappkotlin.ui.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.fragments.AuthFragment
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profile.presenters.ProfilePresenter
import com.unitbean.studentappkotlin.ui.profile.views.ProfileView
import com.unitbean.studentappkotlin.utils.model.UserModel
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class ProfileFragment : MvpAppCompatFragment(), ProfileView, View.OnClickListener {

    @InjectPresenter
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.showProgress(true)
        b_profile_logout.setOnClickListener(this)

        presenter.loadProfile()
    }

    override fun onProfileLoaded(user: UserModel) {
        if (user.firstName == "" || user.lastName == "" || user.institute == "" || user.course == "" || user.group == "" || user.recordBook == "") {
            (requireActivity() as MainActivity).apply{
                openScreen(AuthFragment.TAG, isAddToBackStack = false)
                showNavigationBar(false)
            }
        } else {
            presenter.showProgress(false)
            tv_profile_firstname.text = user.firstName
            tv_profile_lastname.text = user.lastName
            tv_profile_institute.text = user.institute
            tv_profile_course.text = user.course
            tv_profile_group.text = user.group
            tv_profile_recordBook.text = user.recordBook
        }
    }

    override fun onLogout() {
        (requireActivity() as MainActivity).logout()
    }

    override fun onClick(v: View?) {
        when (v) {
            b_profile_logout -> presenter.logout()
        }
    }
    override fun showProgress(isShow: Boolean) {
        if (isShow) {
            b_profile_logout.visibility = View.GONE
            tv_profile_firstname.visibility = View.GONE
            tv_profile_lastname.visibility = View.GONE
            tv_profile_institute.visibility = View.GONE
            tv_profile_course.visibility = View.GONE
            tv_profile_group.visibility = View.GONE
            tv_profile_recordBook.visibility = View.GONE
            pb_profile_progress.visibility = View.VISIBLE
        } else {
            b_profile_logout.visibility = View.VISIBLE
            tv_profile_firstname.visibility = View.VISIBLE
            tv_profile_lastname.visibility = View.VISIBLE
            tv_profile_institute.visibility = View.VISIBLE
            tv_profile_course.visibility = View.VISIBLE
            tv_profile_group.visibility = View.VISIBLE
            tv_profile_recordBook.visibility = View.VISIBLE
            pb_profile_progress.visibility = View.GONE
        }
    }

    companion object {
        val TAG = "ProfileFragment"
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
