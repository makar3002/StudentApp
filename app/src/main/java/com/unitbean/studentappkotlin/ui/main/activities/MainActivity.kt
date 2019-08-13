package com.unitbean.studentappkotlin.ui.main.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.fragments.AuthFragment
import com.unitbean.studentappkotlin.ui.main.listeners.BackStackChangeListener
import com.unitbean.studentappkotlin.ui.main.presenters.MainPresenter
import com.unitbean.studentappkotlin.ui.main.views.MainView
import com.unitbean.studentappkotlin.ui.profileHost.fragments.ProfileHostFragment
import com.unitbean.studentappkotlin.ui.signIn.fragments.SignInFragment
import com.unitbean.studentappkotlin.ui.splash.fragments.SplashFragment
import com.unitbean.studentappkotlin.utils.FragmentNavigation
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class MainActivity : MvpAppCompatActivity(), MainView, BackStackChangeListener, FragmentNavigation {

    @InjectPresenter
    lateinit var presenter: MainPresenter
    //private val updateManager: UpdateManager by lazy { UpdateManager(this, cl_main_root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnv_main_navigation.setOnNavigationItemSelectedListener { item ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.menu_events -> openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
                R.id.menu_calendar -> openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
                R.id.menu_partner -> openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
                R.id.menu_profile -> openScreen(ProfileHostFragment.TAG,  isAddToBackStack = false)
                else -> false
            }
        }

        val savedVisibility: Boolean?
        if (savedInstanceState != null) {
            savedVisibility = savedInstanceState.getBoolean(NAVIGATION_IS_VISIBLE)
        } else
            savedVisibility = null
        if (savedVisibility != null) {
            showNavigationBar(savedVisibility)
        }
    }

    override fun onBackStackChange(fragment: Fragment) {
        when (fragment){
            is ProfileHostFragment -> {
                showNavigationBar(true)
                bnv_main_navigation!!.menu.findItem(R.id.menu_profile).isChecked = true
            }
            is AuthFragment -> {
                showNavigationBar(false)
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VK.onActivityResult(requestCode, resultCode, data, object : VKAuthCallback {
                override fun onLogin(token: VKAccessToken) {
                    presenter.onVkSignIn(token)
                    (supportFragmentManager.primaryNavigationFragment as? ProfileHostFragment)?.let {
                        (it.childFragmentManager.primaryNavigationFragment as? SignInFragment)?.showProgress(true)
                    } ?: (supportFragmentManager.primaryNavigationFragment as? SignInFragment)?.showProgress(true)
                }
                override fun onLoginFailed(errorCode: Int) {
                    if (errorCode != VKAuthCallback.AUTH_CANCELED) {
                        Snackbar.make(fl_main_container, R.string.sign_in_error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            })) {
            super.onActivityResult(requestCode, resultCode, data)
            //callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(NAVIGATION_IS_VISIBLE, bnv_main_navigation.visibility == View.VISIBLE)
    }

    fun showNavigationBar(isShow: Boolean) {
        if (isShow)
            bnv_main_navigation!!.visibility = View.VISIBLE
        else
            bnv_main_navigation!!.visibility = View.GONE

    }

    fun openScreen(
        tag: String,
        extras: Bundle? = null,
        isAddToBackStack: Boolean = true,
        sharedElements: Map<String, View>? = null
    ): Boolean {
        return when (tag) {
            ProfileHostFragment.TAG -> {
                supportFragmentManager.attachFragment(
                    fl_main_container.id,
                    ProfileHostFragment.newInstance(),
                    tag,
                    isAddToBackStack
                )
                true
            }
            SplashFragment.TAG -> {
                supportFragmentManager.attachFragment(
                    fl_main_container.id,
                    SplashFragment.newInstance(),
                    tag,
                    isAddToBackStack)
                true
            }
            AuthFragment.TAG -> {
                supportFragmentManager.attachFragment(
                    fl_main_container.id,
                    AuthFragment.newInstance(),
                    tag,
                    isAddToBackStack)
                true
            }
            else -> false
        }

    }

    override fun onLoginSuccess(isAuth: Boolean) {
        (supportFragmentManager.primaryNavigationFragment as? ProfileHostFragment)?.let {
            (it.childFragmentManager.primaryNavigationFragment as? SignInFragment)?.showProgress(false)
            it.updateFragment()
        } ?: (supportFragmentManager.primaryNavigationFragment as? SignInFragment)?.let {
            it.showProgress(false)
        }
    }

    fun logout() {
        (supportFragmentManager.primaryNavigationFragment as? ProfileHostFragment)?.updateFragment()
    }

    override fun onFirstOpen() {
        openScreen(SplashFragment.TAG, isAddToBackStack = false)
        showNavigationBar(false)
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        private const val NAVIGATION_IS_VISIBLE = "navigation_is_visible"
    }
}
