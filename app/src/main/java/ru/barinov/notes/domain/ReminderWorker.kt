package ru.barinov.notes.domain

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.inject
import ru.barinov.R
import ru.barinov.notes.domain.userRepository.NotesRepository
import ru.barinov.notes.ui.notesActivity.ActivityViewModel

class ReminderWorker(
    val context: Context, params: WorkerParameters
) : Worker(context, params) {

    private lateinit var notesId: String
    override fun doWork(): Result {
        if (id == null) {
            return Result.failure()
        }
        notesId = inputData.getString(NOTES_ID)!!
        val noteId = inputData.getLong(notesId, 0).toInt()
        sendNotification(noteId)
        return Result.success()
    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, ActivityViewModel::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager = NotificationManagerCompat.from(context)
        val bitMap = makeBitmap()
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val repo = inject<NotesRepository>(NotesRepository::class.java)
        val subtitleNotification = repo.value.getById(notesId).title
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        val notification =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL).setLargeIcon(bitMap)
                .setContentTitle(titleNotification).setSmallIcon(R.drawable.ic_baseline_note_alt_24)
                .setContentText(subtitleNotification).setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent)
                .setAutoCancel(true)
        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)
            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id, notification.build())

    }

    private fun makeBitmap(): Bitmap {
        val icon = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_note_alt_24)
        return icon!!.toBitmap(200, 200)
    }

    companion object {

        const val NOTIFICATION_ID = "NoteBook_notification_id"
        const val NOTIFICATION_CHANNEL = "NoteBook_channel_01"
        const val NOTIFICATION_NAME = "NoteBook"
        const val NOTIFICATION_WORK = "NoteBook_notification_work"
        const val NOTES_ID = "Notes_id"
    }
}
