package com.unitbean.studentappkotlin.ui.splash.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unitbean.studentappkotlin.R
import com.unitbean.studentappkotlin.ui.main.activities.MainActivity
import com.unitbean.studentappkotlin.ui.profileHost.fragments.ProfileHostFragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SplashFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private var updateJob: Job? = null
    private var isPaused = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.hide()
        updateJob = launch {
            delay(1500)

            while (isPaused) {
                delay(100)
            }

            (activity as MainActivity?)?.apply {
                openScreen(ProfileHostFragment.TAG, isAddToBackStack = false)
                supportActionBar?.show()
                showNavigationBar(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        isPaused = false
    }

    override fun onPause() {
        super.onPause()

        isPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()

        updateJob?.cancel()
    }

    companion object {
        const val TAG = "SplashFragment"

        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }
}