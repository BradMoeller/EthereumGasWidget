package com.punnygames.ethgas

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.MathContext


class UpdateService: Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onStart(intent: Intent?, startId: Int) {
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(notificationId, createNotification())
        }

        // Build the widget update for today
        Log.e("GAS22", "SERVICE CALLED")
        val updateViews: RemoteViews = buildUpdate(this)

        // Push update for this widget to the home screen
        val widget = ComponentName(this, GasWidget::class.java)
        AppWidgetManager.getInstance(this).updateAppWidget(widget, updateViews)

        stopSelf()
    }

    private fun buildUpdate(context: Context): RemoteViews {
        val web3 = Web3j.build(HttpService("https://mainnet.infura.io/v3/!!!REDACTED!!!"));

        val view = RemoteViews(context.packageName, R.layout.widget_layout)

        try {
            val clientVersion = web3.web3ClientVersion().sendAsync().get()
            if (!clientVersion.hasError()) {
                val gasPrice = web3.ethGasPrice().sendAsync().get()
                val gwei = Convert.fromWei(gasPrice.gasPrice.toBigDecimal(), Convert.Unit.GWEI)
                view.setTextViewText(R.id.textview2, gwei.intValueExact().toString())
            }
        } catch (e: Exception) { }

        return view
    }

    private fun createNotification(): Notification {
        createChannel()

        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Updating Eth Gas Prices")
            .setProgress(0, 0, true)
            .build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val androidChannel = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            androidChannel.setSound(null, null)
            (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(androidChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private val notificationId = 1947
        private val channelId = "com.punnygames.ethgas.updateservice"
        private val channelName = "Eth Gas Price"
    }
}