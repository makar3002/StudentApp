package com.unitbean.studentappkotlin.ui.signIn.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.auth.fragments.AuthFragment
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.vk.api.sdk.VK
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_vk_login.setOnClickListener(this)
        b_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            b_vk_login -> VK.login(requireActivity())
            b_login -> (requireActivity() as MainActivity).apply{
                openScreen(AuthFragment.TAG, isAddToBackStack = false)
                showNavigationBar(false)
            }
        }

    }

    fun showProgress(isShow: Boolean) {
        if (isShow) {
            b_vk_login.visibility = View.GONE
            b_login.visibility = View.GONE
            tv_empty_text.visibility = View.GONE
            pb_sign_in_progress.visibility = View.VISIBLE
        } else {
            b_vk_login.visibility = View.VISIBLE
            b_login.visibility = View.VISIBLE
            tv_empty_text.visibility = View.VISIBLE
            pb_sign_in_progress.visibility = View.GONE
        }
    }

    companion object {
        val TAG = "SignInFragment"
        fun newInstance(extras: Bundle? = null): SignInFragment {
            return SignInFragment().apply{
                arguments = extras
            }
        }
    }
}
