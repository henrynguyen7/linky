package com.hn.linky.widgets;

import com.hn.linky.LinkyIntentService;
import com.hn.linky.R;
import com.hn.linky.activities.MainActivity;
import com.hn.linky.valueobjects.Constants;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Class to provide support for Android widget functionality. Similar to activities,
 * this class merely passes intents on to the LinkyIntentService for processing. The only
 * exception is Instapic which requires that the intent be started by an Activity.
 * 
 * @author henry
 *
 */
public class LinkyWidgetProvider extends AppWidgetProvider 
{	
    @Override
    public void onReceive(Context context, Intent intent) 
    {	
    	super.onReceive(context, intent);
    	
    	String action = intent.getAction();
    	
    	if (action.equals(Constants.ACTION_INSTAPIC))
        {
            Intent instapicIntent = new Intent(context, MainActivity.class);
            instapicIntent.setAction(Constants.ACTION_INSTAPIC);
            instapicIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            instapicIntent.putExtra("shouldStartInstapic", true);
            context.startActivity(instapicIntent);
        }
    	else
    	{
    	    Intent messageIntent = new Intent(context, LinkyIntentService.class);
    	    
    	    if (action.equals(Constants.ACTION_SEND_WIDGET_POKE))
            {
                messageIntent.setAction(Constants.ACTION_SEND_WIDGET_POKE);
            }
            else if (action.equals(Constants.ACTION_SEND_WIDGET_HUGGLE))
            {
                messageIntent.setAction(Constants.ACTION_SEND_WIDGET_HUGGLE);
            }
            else if (action.equals(Constants.ACTION_SEND_WIDGET_MWAH))
            {
                messageIntent.setAction(Constants.ACTION_SEND_WIDGET_MWAH); 
            }
            else if (action.equals(Constants.ACTION_SEND_BUZZ))
            {
                messageIntent.setAction(Constants.ACTION_SEND_BUZZ);
            }
    	    
    	    context.startService(messageIntent);
    	}
    }
    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{        
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);   
        
//        /** pokeWidgetButton intent **/
//        Intent pokeIntent = new Intent(context, LinkyWidgetProvider.class);
//        pokeIntent.setAction(Constants.ACTION_SEND_WIDGET_POKE);        
//        PendingIntent pokePendingIntent = PendingIntent.getBroadcast(context, 0, pokeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.pokeWidgetButton, pokePendingIntent);
        
        /** instapicWidgetButton intent **/
        Intent instapicIntent = new Intent(context, LinkyWidgetProvider.class);
        instapicIntent.setAction(Constants.ACTION_INSTAPIC);        
        PendingIntent instapicPendingIntent = PendingIntent.getBroadcast(context, 0, instapicIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.pokeWidgetButton, instapicPendingIntent);
        
        /** huggleWidgetButton intent **/
        Intent hugIntent = new Intent(context, LinkyWidgetProvider.class);
        hugIntent.setAction(Constants.ACTION_SEND_WIDGET_HUGGLE);        
        PendingIntent hugPendingIntent = PendingIntent.getBroadcast(context, 0, hugIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.huggleWidgetButton, hugPendingIntent);
        
        /** mwahWidgetButton intent **/
        Intent kissIntent = new Intent(context, LinkyWidgetProvider.class);
        kissIntent.setAction(Constants.ACTION_SEND_WIDGET_MWAH);        
        PendingIntent kissPendingIntent = PendingIntent.getBroadcast(context, 0, kissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.mwahWidgetButton, kissPendingIntent);
        
        /** buzzWidgetButton intent **/
        Intent buzzIntent = new Intent(context, LinkyWidgetProvider.class);
        buzzIntent.setAction(Constants.ACTION_SEND_BUZZ);        
        PendingIntent buzzPendingIntent = PendingIntent.getBroadcast(context, 0, buzzIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buzzWidgetButton, buzzPendingIntent);      
        
        ComponentName thisWidget = new ComponentName(context, LinkyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);        
        manager.updateAppWidget(thisWidget, remoteViews); 
		
        super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}