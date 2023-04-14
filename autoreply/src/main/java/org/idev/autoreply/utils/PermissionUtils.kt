package org.idev.autoreply.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import org.idev.autoreply.services.ForegroundNotificationService

object PermissionUtils {

    fun launchNotificationAccessSettings(activity: Activity, requestCode: Int) {
        enableService(true, activity)
        val NOTIFICATION_LISTENER_SETTINGS: String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
            } else {
                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
            }
        val i = Intent(NOTIFICATION_LISTENER_SETTINGS)
        activity.startActivityForResult(i, requestCode)
    }

    private fun enableService(enable: Boolean, context: Context) {
        val packageManager = context.packageManager
        val componentName = ComponentName(context, ForegroundNotificationService::class.java)
        val settingCode =
            if (enable) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        // enable dummyActivity (as it is disabled in the manifest.xml)
        packageManager.setComponentEnabledSetting(componentName, settingCode, PackageManager.DONT_KILL_APP)
    }
}