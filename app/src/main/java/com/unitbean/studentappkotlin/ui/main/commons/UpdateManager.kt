package com.unitbean.studentappkotlin.ui.main.commons

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.unitbean.studentappkotlin.R

class UpdateManager(private val activity: Activity, private val rootView: View) {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(activity) }
    private val listener by lazy {
        InstallStateUpdatedListener { state ->
            checkCodeIsDownloadAndShowSnackbar(state.installStatus())
        }
    }

    // Add state listener to app update info task.
    fun checkAndShow() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        activity,
                        REQUEST_CODE
                    )
                }

                checkCodeIsDownloadAndShowSnackbar(appUpdateInfo.installStatus())
            }
            .addOnFailureListener { e ->
                Log.e("UpdateManager", e.message, e)
            }
    }

    // At some point before starting an update, register a listener for updates.
    fun attachListener() {
        appUpdateManager.registerListener(listener)
    }

    // At some point when status updates are no longer needed, unregister the listener.
    fun detachListener() {
        appUpdateManager.unregisterListener(listener)
    }

    // If the process of downloading is finished, start the completion flow.
    private fun checkCodeIsDownloadAndShowSnackbar(code: Int) {
        if (code == InstallStatus.DOWNLOADED) {
            Snackbar.make(
                rootView,
                activity.getString(R.string.update_task_download_complete_and_need_relaunch),
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction(activity.getString(android.R.string.ok)) { appUpdateManager.completeUpdate() }
                show()
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 2415
    }
}