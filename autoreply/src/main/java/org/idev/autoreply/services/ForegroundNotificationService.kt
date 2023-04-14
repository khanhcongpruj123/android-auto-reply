package org.idev.autoreply.services

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import org.idev.autoreply.utils.Constants
import org.idev.autoreply.utils.NotificationUtils
import java.text.DateFormat
import java.util.*

class ForegroundNotificationService : NotificationListenerService() {

    private val TAG = ForegroundNotificationService::class.java.simpleName

    private val mapSenderAndStatusBarNotification = mutableMapOf<String, StatusBarNotification>()


    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        Log.d(TAG, "Receiver notification: ${sbn.packageName}")

        if (canReply(sbn)) {
            updateMapSenderAndStatusBarNotification(sbn)
            saveMessage(sbn)
            // TODO broad cast to notify new message
        }
    }

    private fun saveMessage(sbn: StatusBarNotification) {
        val date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(
            Date()
        )
        val sender = sbn.notification.extras.getString("android.title")
        val msg = NotificationUtils.getMessage(sbn)

        if (sender != null && msg != null) {
            Log.d(TAG, "Save message: ${sbn.packageName};${sender};${msg}")
            // TODO save it to database
        }
    }

    private fun updateMapSenderAndStatusBarNotification(sbn: StatusBarNotification) {
        val sender = sbn.notification.extras.getString("android.title")

        var msg = NotificationUtils.getMessage(sbn)

        if (sender != null && msg != null) {
            mapSenderAndStatusBarNotification[sender] = sbn
        }
    }

    private fun canReply(sbn: StatusBarNotification): Boolean {
        return isSupportedPackage(sbn) && NotificationUtils.isNewNotification(sbn)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val reply = intent.getStringExtra("reply_content")
        val to = intent.getStringExtra("reply_to")
        if (!reply.isNullOrBlank() && !to.isNullOrBlank()) {
            sendReply(reply, to)
        }
        //START_STICKY  to order the system to restart your service as soon as possible when it was killed.
        return START_STICKY
    }

    /**
     * @param reply: content to reply
     * @param to: username sender
     * */
    private fun sendReply(reply: String, to: String) {
        val sbn = mapSenderAndStatusBarNotification[to]
        if (sbn != null) {
            Log.d(TAG, "Send reply to $to : $reply")
            val (_, pendingIntent, remoteInputs1) = NotificationUtils.extractWearNotification(sbn)
            if (remoteInputs1.isEmpty()) {
                return
            }
            val remoteInputs = arrayOfNulls<android.app.RemoteInput>(remoteInputs1.size)
            val localIntent = Intent()
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val localBundle = Bundle()
            for ((i, remoteIn) in remoteInputs1.withIndex()) {
                remoteInputs[i] = remoteIn
                localBundle.putCharSequence(remoteInputs[i]!!.resultKey, reply)
            }
            android.app.RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle)
            try {
                if (pendingIntent != null) {
                    pendingIntent.send(this, 0, localIntent)
                    cancelNotification(sbn.key)
                }
            } catch (ex: PendingIntent.CanceledException) {
                Log.d(TAG, "Cancel exception when reply")
            }
        } else {
            Log.d(TAG, "Cannot reply. Status bar notificaion is null")
        }
    }

    private fun isSupportedPackage(sbn: StatusBarNotification): Boolean {
        return sbn.packageName in Constants.SUPPORTED_APPS.map { it.packageName }
    }
}