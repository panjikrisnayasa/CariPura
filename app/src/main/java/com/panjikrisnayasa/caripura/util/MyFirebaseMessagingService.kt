package com.panjikrisnayasa.caripura.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.view.MainActivity
import com.panjikrisnayasa.caripura.view.TempleRequestDetailActivity
import com.panjikrisnayasa.caripura.view.TempleRequestHistoryDetailContributorActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("hyperLoop", "on new token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if (p0.data.isNotEmpty()) {
            val intent: Intent
            when {
                p0.data["requestType"] == "add_request" -> {
                    intent = Intent(this, TempleRequestDetailActivity::class.java)
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_REQUEST_TYPE,
                        p0.data["requestType"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                p0.data["requestType"] == "edit_request" -> {
                    intent = Intent(this, TempleRequestDetailActivity::class.java)
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_REQUEST_TYPE,
                        p0.data["requestType"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                p0.data["requestType"] == "delete_request" -> {
                    intent = Intent(this, TempleRequestDetailActivity::class.java)
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_REQUEST_TYPE,
                        p0.data["requestType"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestDetailActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                p0.data["requestType"] == "history_add_request" -> {
                    intent = Intent(this, TempleRequestHistoryDetailContributorActivity::class.java)
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_REQUEST_TYPE,
                        "add_request"
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                p0.data["requestType"] == "history_edit_request" -> {
                    intent = Intent(this, TempleRequestHistoryDetailContributorActivity::class.java)
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_REQUEST_TYPE,
                        "edit_request"
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                p0.data["requestType"] == "history_delete_request" -> {
                    intent = Intent(this, TempleRequestHistoryDetailContributorActivity::class.java)
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_REQUEST_TYPE,
                        "delete_request"
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_CONTRIBUTOR_ID,
                        p0.data["contributorId"]
                    )
                    intent.putExtra(
                        TempleRequestHistoryDetailContributorActivity.EXTRA_TEMPLE_ID,
                        p0.data["templeId"]
                    )
                }
                else -> intent = Intent(this, MainActivity::class.java)
            }

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            val notificationId = Random().nextInt(3000)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                setupChannel(notificationManager)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, notificationId, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val largeIcon = BitmapFactory.decodeResource(
                resources,
                R.drawable.image_app_logo
            )

            val notificationSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.image_app_logo)
                .setLargeIcon(largeIcon)
                .setContentTitle(p0.data["title"])
                .setContentText(p0.data["message"])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.color =
                    ContextCompat.getColor(this, R.color.colorLayoutBackground)
            }
            notificationManager.notify(notificationId, notificationBuilder)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannel(notificationManager: NotificationManagerCompat) {
        val notificationChannelName = "Notification"
        val notificationChannelDescription = "Description"

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            notificationChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = notificationChannelDescription
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
