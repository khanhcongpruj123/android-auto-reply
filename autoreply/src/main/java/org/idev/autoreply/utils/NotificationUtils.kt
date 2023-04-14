package org.idev.autoreply.utils

import android.service.notification.StatusBarNotification
import android.app.PendingIntent
import org.idev.autoreply.data.NotificationWear
import java.util.*

object NotificationUtils {
    private const val MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 2 * 60 * 1000

    fun getMessage(sbn: StatusBarNotification): String? {
        var _msg = sbn.notification.extras.get("android.text")

        return when (_msg) {
            is android.text.SpannableString -> {
                _msg.toString()
            }
            is String -> {
                _msg
            }
            else -> {
                null
            }
        }
    }

    fun getTitle(sbn: StatusBarNotification): String? {
        var title: String?
        if (sbn.notification.extras.getBoolean("android.isGroupConversation")) {
            title = sbn.notification.extras.getString("android.hiddenConversationTitle")

            if (title == null) {
                title = sbn.notification.extras.getString("android.title")
                val index = title!!.indexOf(':')
                if (index != -1) {
                    title = title.substring(0, index)
                }
            }

            val b = sbn.notification.extras["android.messages"] as Array<*>?
            if (b != null && b.size > 1) {
                val startIndex = title!!.lastIndexOf('(')
                if (startIndex != -1) {
                    title = title.substring(0, startIndex)
                }
            }
        } else {
            title = sbn.notification.extras.getString("android.title")
        }
        return title
    }


    fun isNewNotification(sbn: StatusBarNotification): Boolean {

        return sbn.notification.`when` == 0L ||
                System.currentTimeMillis() - sbn.notification.`when` < MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS
    }

    fun extractWearNotification(statusBarNotification: StatusBarNotification): NotificationWear {
        var actions = statusBarNotification.notification.actions
        val remoteInputs: MutableList<android.app.RemoteInput> = ArrayList(actions.size)
        var pendingIntent: PendingIntent? = null
        for (act in actions) {
            if (act != null && act.remoteInputs != null) {
                for (x in act.remoteInputs!!.indices) {
                    val remoteInput = act.remoteInputs!![x]
                    remoteInputs.add(remoteInput)
                    pendingIntent = act.actionIntent
                }
            }
        }
        return NotificationWear(
            statusBarNotification.packageName,
            pendingIntent,
            remoteInputs,
            statusBarNotification.notification.extras,
            statusBarNotification.tag,
            UUID.randomUUID().toString()
        )
    }

    fun getTitleRaw(sbn: StatusBarNotification): String? {
        return sbn.notification.extras.getString("android.title")
    }
}