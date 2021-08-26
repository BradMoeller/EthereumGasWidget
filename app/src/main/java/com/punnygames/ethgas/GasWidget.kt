package com.punnygames.ethgas

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.os.SystemClock

import android.app.AlarmManager




class GasWidget : AppWidgetProvider() {

    init {

    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.e("GAS22", "ON onReceive CALLED")
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val intent = Intent(context, UpdateService::class.java)
        val pending = PendingIntent.getService(context, 145, intent, 0)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarm?.cancel(pending)
        val interval = (1000 * 60).toLong()
        alarm?.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            interval,
            pending
        )


        Log.e("GAS22", "ON UPDATE CALLED")
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(Intent(context, UpdateService::class.java))
        } else {
            context.startService(Intent(context, UpdateService::class.java))
        }

        // Perform this loop procedure for each App Widget that belongs to this provider
//        appWidgetIds.forEach { appWidgetId ->
//
//
//            // Get the layout for the App Widget and attach an on-click listener
//            // to the button
//            val views: RemoteViews = RemoteViews(
//                context.packageName,
//                R.layout.widget_layout
//            )
//
//            views.
//            // Create an Intent to launch ExampleActivity
//            val pendingIntent: PendingIntent = Intent(context, ExampleActivity::class.java)
//                .let { intent ->
//                    PendingIntent.getActivity(context, 0, intent, 0)
//            }
//            .apply {
//                setOnClickPendingIntent(R.id.button, pendingIntent)
//            }
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views)
//    }
    }
}