package com.ronaldbarrera.nine11;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    public static final String CHANNEL_ID = "10001";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onRecieve called");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, String.format("Error code : %d", geofencingEvent.getErrorCode()));
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        // Check which transition type has triggered this event
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d(TAG, "onReceive GEOFENCE_TRANSITION_ENTER");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "onReceive GEOFENCE_TRANSITION_EXIT");
        } else {
            // Log the error.
            Log.e(TAG, String.format("Unknown transition : %d", geofenceTransition));
            // No need to do anything else
            return;
        }
        sendNotification(context, geofenceTransition);
    }

    private void sendNotification(Context context, int transitionType) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        // Check the transition type to display the relevant icon image
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.logo))
                    .setContentTitle("You have entered the location");
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.logo))
                    .setContentTitle("You have exited the location");
        }

        // Continue building the notification
        builder.setContentText("This is setContentText");
        builder.setContentIntent(notificationPendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);
        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = "getString(R.string.channel_name)";
        String description = "getString(R.string.channel_description)";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
