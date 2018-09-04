package th.potikorn.firebaseplayground.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orhanobut.logger.Logger
import java.lang.Exception

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Logger.i("From: " + remoteMessage?.from)
        remoteMessage?.let { remoteMsg ->
            Logger.i("Message data payload : ${remoteMsg.data}")
        }
        remoteMessage?.notification?.let { remoteMessageNotification ->
            Logger.i("Message Notification Body : $remoteMessageNotification")
        }
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Logger.e(p0.toString())
    }
}