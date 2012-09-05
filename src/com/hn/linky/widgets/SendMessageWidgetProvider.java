package com.hn.linky.widgets;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.R;
import com.hn.linky.utilities.Constants;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SendMessageWidgetProvider extends AppWidgetProvider 
{
	public static final String POKE_ACTION = "com.hn.linky.POKE_ACTION";
	public static final String HUG_ACTION = "com.hn.linky.HUG_ACTION";
	public static final String KISS_ACTION = "com.hn.linky.KISS_ACTION";
	
    @Override
    public void onReceive(Context context, Intent intent) 
    {	
    	Intent newIntent = new Intent(context, LinkyIntentService.class);
    	
    	String action = intent.getAction();
    	if (action.equals(POKE_ACTION))
    	{
    		newIntent.setAction(Constants.ACTION_SEND_MESSAGE);
    		newIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_WIDGET_POKE);
    	}
    	else if (action.equals(HUG_ACTION))
    	{
    		newIntent.setAction(Constants.ACTION_SEND_MESSAGE);
    		newIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_WIDGET_HUG);
    	}
    	else if (action.equals(KISS_ACTION))
    	{
    		newIntent.setAction(Constants.ACTION_SEND_MESSAGE);
    		newIntent.putExtra(Constants.EXTRA_MESSAGE, Constants.MESSAGE_WIDGET_KISS);
    	}
    	else if (action.equals(Constants.ACTION_SEND_BUZZ))
    	{
    		newIntent.setAction(Constants.ACTION_SEND_BUZZ);
    	} 	
    	
        context.startService(newIntent);
        super.onReceive(context, intent);   
    }
    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{        
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.send_message_widget);   
        
        /** pokeButton intent **/
        Intent pokeIntent = new Intent(context, SendMessageWidgetProvider.class);
        pokeIntent.setAction(POKE_ACTION);        
        PendingIntent pokePendingIntent = PendingIntent.getBroadcast(context, 0, pokeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button1, pokePendingIntent);
        
        /** hugButton intent **/
        Intent hugIntent = new Intent(context, SendMessageWidgetProvider.class);
        hugIntent.setAction(HUG_ACTION);        
        PendingIntent hugPendingIntent = PendingIntent.getBroadcast(context, 0, hugIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button2, hugPendingIntent);
        
        /** kissButton intent **/
        Intent kissIntent = new Intent(context, SendMessageWidgetProvider.class);
        kissIntent.setAction(KISS_ACTION);        
        PendingIntent kissPendingIntent = PendingIntent.getBroadcast(context, 0, kissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button3, kissPendingIntent);
        
        /** buzzButton intent **/
        Intent buzzIntent = new Intent(context, SendMessageWidgetProvider.class);
        buzzIntent.setAction(Constants.ACTION_SEND_BUZZ);        
        PendingIntent buzzPendingIntent = PendingIntent.getBroadcast(context, 0, buzzIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button4, buzzPendingIntent);      
        
        ComponentName thisWidget = new ComponentName(context, SendMessageWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);        
        manager.updateAppWidget(thisWidget, remoteViews); 
		
        super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}