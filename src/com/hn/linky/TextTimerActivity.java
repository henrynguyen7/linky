package com.hn.linky;

import com.hn.linky.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class TextTimerActivity extends Activity
{
	private Context mContext;
	private Linky mLinky;
	
	private int scheduledYear;
	private int scheduledMonth;
	private int scheduledDay;
	private int scheduledHour;
	private int scheduledMinute;
	
	static final int TIME_DIALOG_ID = 0;
	
    public void onCreate(Bundle savedInstanceState)
    {	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.texttimer);
    	
    	mContext = this.getApplicationContext();
    	mLinky = new Linky(mContext);
    	
    	TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
    	descriptionTextView.setText("TextTimer");
    	    	
    	DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
    	scheduledYear = datePicker.getYear();
    	scheduledMonth = datePicker.getMonth();
    	scheduledDay = datePicker.getDayOfMonth();
    	
    	TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    	timePicker.setOnClickListener(new View.OnClickListener() 
    	{	
			@Override
			public void onClick(View v) 
			{
				showDialog(TIME_DIALOG_ID);
			}
		});
    	
    	TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() 
    	{
    		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
    		{
    			scheduledHour = hourOfDay;
    			scheduledMinute = minute;
    		}
    	};
    	    	
    	final EditText messageEditText = (EditText) findViewById(R.id.messageEditText);
    	
    	Button startTimerButton = (Button) findViewById(R.id.startTimerButton);    	
    	startTimerButton.setText("Schedule Text");
    	startTimerButton.setOnClickListener(new OnClickListener()
    	{  
    	    public void onClick(View v) 
    	    {
    	    	String message = messageEditText.getText().toString(); 

    	    }  
    	});
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
            moveTaskToBack(true);
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
        	case TIME_DIALOG_ID:
        		
            //return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
        }
        
        return null;
    }
        
//    public void onActivityResult(int requestCode, int resultCode, Intent data) 
//	{
//	    if (resultCode == RESULT_OK) 
//	    {
//	        if (requestCode == SELECT_PICTURE) 
//	        {
//	            
//	        }
//	    }
//	}
}