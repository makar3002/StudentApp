package com.unitbean.studentappkotlin.ui.profileHost.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.main.listeners.BackStackChangeListener
import com.unitbean.studentappkotlin.ui.profile.fragments.ProfileFragment
import com.unitbean.studentappkotlin.ui.profileHost.presenters.ProfileHostPresenter
import com.unitbean.studentappkotlin.ui.profileHost.views.ProfileHostView
import com.unitbean.studentappkotlin.ui.signIn.fragments.SignInFragment
import com.unitbean.studentappkotlin.utils.FragmentNavigation
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class ProfileHostFragment : MvpAppCompatFragment(), ProfileHostView, FragmentNavigation {

    @InjectPresenter
    lateinit var presenter: ProfileHostPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateFragment()

        (activity as? BackStackChangeListener)?.onBackStackChange(this)
    }

    fun updateFragment() {
        val isSignIn = presenter.isShowSignIn()
        val fragment: Fragment = if (isSignIn) {
            SignInFragment.newInstance()
        } else {
            ProfileFragment.newInstance()
        }

        val tag: String = if (isSignIn) {
            SignInFragment.TAG
        } else {
            ProfileFragment.TAG
        }

        if (childFragmentManager.primaryNavigationFragment?.tag == tag) return
        childFragmentManager.attachFragment(R.id.fl_profile_host_container, fragment, tag)
    }


    companion object {
        val TAG = "SignInFragment"
        fun newInstance(): ProfileHostFragment {
            return ProfileHostFragment()
        }
    }
}
