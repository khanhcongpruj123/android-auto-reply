package org.idev.autoreply.models

import java.io.Serializable
import java.time.LocalDateTime

/**
 * @author idev
 * Present message on notification
 * */
class MessageOnNotification : Serializable {

    var sender: String? = null
    var message: String? = null
    var receivedAt: LocalDateTime? = null
    var app: App? = null // ex: com.zing.zalo,...
}