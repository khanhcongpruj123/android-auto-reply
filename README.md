# Auto Repy by Notification

## Usage
1. Add moduel ```autoreply``` to your ```app``` module.
1. Add this config to AndroidManifest.xml
```xml
<service
            android:name="org.idev.autoreply.services.ForegroundNotificationService"
            android:exported="true"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
            <intent-filter>
                <action android:name="android.service.notification.NotificationListenerService" />
            </intent-filter>
        </service>
        <service
            android:name="org.idev.autoreply.services.KeepAliveService"
            android:stopWithTask="false"/> <!-- https://techstop.github.io/android-broadcastreceiver/ -->
        <receiver
            android:name="org.idev.autoreply.receivers.NotificationServiceRestartReceiver"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>

                <category android:name="android.intent.category.DEFAULT"/>

                <action android:name="android.intent.action.QUICKBOOT_POWERON"/>
                <!-- For HTC devices -->
                <action android:name="com.htc.intent.action.QUICKBOOT_POWERON"/>
            </intent-filter>
        </receiver>
```
2. Request READ NOTIFICATION PERMISSION
Use this code to request
```
PermissionUtils.launchNotificationAccessSettings(context, requestCode)
```