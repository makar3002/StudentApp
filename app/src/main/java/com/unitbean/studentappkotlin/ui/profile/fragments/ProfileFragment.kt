package com.unitbean.studentappkotlin.ui.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.fragments.AuthFragment
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profile.presenters.ProfilePresenter
import com.unitbean.studentappkotlin.ui.profile.views.ProfileView
import com.unitbean.studentappkotlin.utils.repository.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_auth_first.*
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
        b_profile_logout.setOnClickListener(this)

        presenter.loadProfile()
    }

    override fun onProfileLoaded(user: UserModel) {
        //Snackbar.make(cl_profile_fragment, user.id+"/"+user.firstName+"/"+user.lastName, Snackbar.LENGTH_SHORT).show()
        if (user.firstName == "" || user.lastName == "" || user.institute == "" || user.course == "" || user.group == "" || user.recordBook == "") {
            (requireActivity() as MainActivity).apply{
                openScreen(AuthFragment.TAG, isAddToBackStack = false)
                showNavigationBar(false)
            }
        } else {
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

    companion object {
        val TAG = "ProfileFragment"
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
