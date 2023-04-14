package org.idev.autoreply.utils

import org.idev.autoreply.models.App


object Constants {
    val SUPPORTED_APPS: List<App> = mutableListOf(
            App("Zalo", "com.zing.zalo"),
            App("Facebook Messenger", "com.facebook.orca")
    )
}