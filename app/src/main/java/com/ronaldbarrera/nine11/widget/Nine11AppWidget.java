package com.ronaldbarrera.nine11.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ronaldbarrera.nine11.MainActivity;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.ui.center.CenterFragment;

/**
 * Implementation of App Widget functionality.
 */
public class Nine11AppWidget extends AppWidgetProvider {

    public static String ACTION_SEND_SMS = "send_sms_action";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.nine11_app_widget);

        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_SEND_SMS);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);


        // Create an Intent to launch MainActivity when clicked
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setAction(ACTION_SEND_SMS);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Widgets allow click handlers to only launch pending intents
        //views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);


//        Intent intent = new Intent(context, Nine11AppWidget.class);
//        intent.setAction(ACTION_SEND_SMS);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//        views.setOnClickPendingIntent(R.id.appwidget_button, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.d("Nine11AppWidget", "onReceive called before");
//
//        super.onReceive(context, intent);
//        Log.d("Nine11AppWidget", "onReceive called");
//
//        if(intent.getAction().equals(ACTION_SEND_SMS))
//            Log.d("Nine11AppWidget", "ACTION_SEND_SMS");
//    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);//add this line
//        Log.d("Nine11AppWidget", "onReceive called");
//        if (ACTION_SEND_SMS.equals(intent.getAction())){
//            //your onClick action is here
//            //display in short period of time
//            Toast.makeText(context, "msg msgasdasd", Toast.LENGTH_SHORT).show();
//
//        }
//    }
}

