package com.hn.linky.valueobjects;

public class Constants
{
	public static final String SHARED_PREF_LINKED_NUMBER = "linkedNumber";
	public static final String SHARED_PREF_SOURCE_NUMBER = "sourceNumber";
	public static final String SHARED_PREF_IS_LOGGED_IN = "isLoggedIn";
	
	public static final String ACTION_SEND_MESSAGE = "com.hn.linky.action.SEND_MESSAGE";
	public static final String ACTION_SEND_BUZZ = "com.hn.linky.action.SEND_BUZZ";
	public static final String ACTION_AUTHENTICATE_BUZZ = "com.hn.linky.action.AUTHENTICATE_BUZZ";
	public static final String ACTION_UPDATE_DRUNK_LEVEL = "com.hn.linky.action.UPDATE_DRUNK_LEVEL";
	public static final String ACTION_UPDATE_SLEEPY_LEVEL = "com.hn.linky.action.UPDATE_SLEEPY_LEVEL";
	public static final String ACTION_UPDATE_MWAH_LEVEL = "com.hn.linky.action.UPDATE_MWAH_LEVEL";
	public static final String ACTION_UPDATE_HUGGLE_LEVEL = "com.hn.linky.action.UPDATE_HUGGLE_LEVEL";		
	public static final String ACTION_UPDATE_ALL_LEVELS = "com.hn.linky.action.UPDATE_ALL_LEVELS";
	public static final String ACTION_WIDGET_UPDATE_DRUNK_LEVEL = "com.hn.linky.action.WIDGET_UPDATE_DRUNK_LEVEL";	
	public static final String ACTION_WIDGET_UPDATE_SLEEPY_LEVEL = "com.hn.linky.action.WIDGET_UPDATE_SLEEPY_LEVEL";
	public static final String ACTION_INSTAPIC = "com.hn.linky.action.INSTAPIC";
	
	public static final String EXTRA_MESSAGE = "messageType";
	public static final String EXTRA_DRUNK_LEVEL = "drunkLevel";
	public static final String EXTRA_SLEEPY_LEVEL = "sleepyLevel";
	public static final String EXTRA_MWAH_LEVEL = "mwahLevel";
	public static final String EXTRA_HUGGLE_LEVEL = "huggleLevel";
	public static final String EXTRA_SMS_MESSAGE = "smsMessage";
	public static final String EXTRA_ORIGINATING_ADDRESS = "originatingAddress";
	
	public static final String BUZZ_KEY = "LINKY BUZZ!";
	
	public static final int MESSAGE_POKE = 0;	
	public static final int MESSAGE_KISS = 1;
	public static final int MESSAGE_HUG = 2;
	public static final int MESSAGE_TICKLE = 3;
	public static final int MESSAGE_HOLDHANDS = 4;
	public static final int MESSAGE_MISSESYOU = 5;
	public static final int MESSAGE_SMILE_LOW = 6;
	public static final int MESSAGE_SMILE_MEDIUM = 7;
	public static final int MESSAGE_SMILE_HIGH = 8;
	public static final int MESSAGE_WIDGET_POKE = 9;
	public static final int MESSAGE_WIDGET_KISS = 10;
	public static final int MESSAGE_WIDGET_HUG = 11;
	public static final int MESSAGE_WIDGET_TICKLE = 12;
}
