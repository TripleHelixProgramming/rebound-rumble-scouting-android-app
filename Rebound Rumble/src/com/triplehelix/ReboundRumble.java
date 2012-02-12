package com.triplehelix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ReboundRumble extends Activity {
    
	private int formNum = 1;
	
	private int tN;
	private int hHoop1, hHoop2, hHoop3;
	private int tHoop1, tHoop2, tHoop3;
	private String balan, comms;
	
	private String file = "device";
	private String fileType = ".txt";
	private String folderName = "REBOUND RUMBLE DATA";
	
    public void onCreate(Bundle savedInstanceState)
    {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        formNum = getInitialMatch();      
        loadForm();
    }
  
    public void onResume()
    {
    	super.onResume();
    	
    	if(readData() == null)
    	{
    		formNum = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("newMatchNum", "1"));
    		loadForm();
    	}else
    	{
    		formNum = getInitialMatch();
    	}
    }
    
    private void submitData()
    {
    	//***************************************************************
    	//DATA SUBMISSION
    	
    	EditText eData = (EditText) findViewById(R.id.num);
    	if(eData.getText().toString().equals(""))
    	{
    		tN = 0;
    	}else
    	{
    		tN = Integer.parseInt(eData.getText().toString());
    	}
    	
    	RadioButton rb = (RadioButton) findViewById(R.id.balanceYes);
    	balan = Boolean.toString(rb.isChecked());
    	
    	eData = (EditText) findViewById(R.id.comments);
    	if(eData.getText().toString().equals(""))
    	{
    		comms = "none";
    	}else
    	{
    		comms = eData.getText().toString();
    	}
    	
    	writeData();
    	
    	//***************************************************************
    	//***************************************************************
    	
    	playSound();
    	
    	formNum++;
    	loadForm();
    }
    
    private void loadForm()
    {
    	ScrollView sc = (ScrollView) findViewById(R.id.scroll);
    	sc.fullScroll(ScrollView.FOCUS_UP);
    	
    	TextView title = (TextView) findViewById(R.id.title);
    	title.setText("Scouting Form [" + formNum + "]");
    	
    	EditText teamNumber = (EditText) findViewById(R.id.num);
    	teamNumber.setText("");
    	teamNumber.setHint("Team #");
		
    	hHoop1 = 0;
    	TextView text = (TextView) findViewById(R.id.hHoop1);
		text.setText(hHoop1 + " " + "in Hoop 1");
		
    	hHoop2 = 0;
    	text = (TextView) findViewById(R.id.hHoop2);
		text.setText(hHoop2 + " " + "in Hoop 2");
		
    	hHoop3 = 0;
    	text = (TextView) findViewById(R.id.hHoop3);
		text.setText(hHoop3 + " " + "in Hoop 3");
		
    	tHoop1 = 0;
    	text = (TextView) findViewById(R.id.tHoop1);
		text.setText(tHoop1 + " " + "in Hoop 1");
		
    	tHoop2 = 0;
    	text = (TextView) findViewById(R.id.tHoop2);
		text.setText(tHoop2 + " " + "in Hoop 2");
		
    	tHoop3 = 0;
    	text = (TextView) findViewById(R.id.tHoop3);
		text.setText(tHoop3 + " " + "in Hoop 3");
		
		RadioButton rb = (RadioButton) findViewById(R.id.balanceYes);
    	rb.setChecked(false);
    	rb = (RadioButton) findViewById(R.id.balanceNo);
    	rb.setChecked(true);
    	
    	EditText comments = (EditText) findViewById(R.id.comments);
    	comments.setText("");
    	comments.setHint("Comments...");
    }
    
    
    private BufferedReader readData()
    {
    	SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
    	file = preference.getString("dev", "device");
    	folderName = "REBOUND RUMBLE DATA";
    	int numMultiple = Integer.parseInt(preference.getString("mult", "0"));
    	
    	File root = Environment.getExternalStorageDirectory();
    	
    	if(root.canRead())
    	{
    		File fold = new File(root + "/" + folderName);
    		
    		if(!fold.exists())
    		{
    			return null;
    		}
    			
    			String fileS = file + Integer.toString(numMultiple) + fileType;
    			File file = new File(fold, fileS);
    			
    			if(file.exists())
    			{
    				FileReader tRead;
					try
					{
						tRead = new FileReader(file);
						BufferedReader input = new BufferedReader(tRead);
						return input;
					} catch (FileNotFoundException e)
					{
						return null;
					}
    				
    			}else
    			{
    				return null;
    			}
    	}
    	
    	return null;
    }
    
    
    
    private void writeData()
    {
    	SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
    	file = preference.getString("dev", "device");
    	//folderName = preference.getString("fold", "REBOUND RUMBLE DATA");
    	int numMultiple = Integer.parseInt(preference.getString("mult", "0"));
    	
    	File root = Environment.getExternalStorageDirectory();
    	
    	//for(int i = 0; i < root.list().length; i++)
    	//	toast(root.list()[i]);
    	
    	if(root.canWrite())
    	{
    		File fold = new File(root + "/" + folderName);
    		
    		if(!fold.exists())
    		{
    			fold.mkdir();
    			toast("Directory Created...");
    		}
    		
    		
    		//***********************************************************************************
    		//WRITE DATA
    		try
    		{	
    			String fileS = file + Integer.toString(numMultiple) + fileType;
    			File file = new File(fold, fileS);
    			FileWriter tWrite = new FileWriter(file, true); //append to file
    			BufferedWriter output = new BufferedWriter(tWrite);
    			
				output.append(formNum + " " + tN + " " + hHoop1 + " " + hHoop2 + " " + hHoop3 
									  + " " + tHoop1 + " " + tHoop2 + " " + tHoop3
									  + " " + balan + " " + comms + "\n");
				
				output.flush();
				output.close();
				
				toast("Auto Save...");
			} catch (IOException e)
			{
				toast("Error Writing File");
			}
    		//***********************************************************************************
    		//***********************************************************************************
    		
    	}else
    	{
    		toast("COULD NOT WRITE TO MEMORY!");
    	}
    	
    }
    
    
    private int getInitialMatch()
    {
    	BufferedReader input = readData();
    	
    	if(input != null)
    	{
    		try
    		{
    			String line, lastLine = "";
    		
				while((line = input.readLine()) != null)
				{
					lastLine = line;
				}
				
				return Integer.parseInt(lastLine.substring(0, lastLine.indexOf(' '))) + 1;
			} catch (IOException e)
			{
				return 1;
			}
    	}else
    	{
    		return 1;
    	}
    }
    
    
    
    
    
    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //PREFERENCES
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			//case R.id.exit:
			//				this.finish();
			//			break;
			case R.id.preferences:
							preferenceWindow();
							break;

		}
		return true;
	}
		
    private void preferenceWindow()
    {
    	Intent intent = new Intent();
    	intent.setClassName("com.triplehelix", "com.triplehelix.PreferenceWindow");
    	startActivity(intent);
    }
    
    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    
    //********************************************************************************************************
    //BUTTONS
    
    public void hHoop1M(View button)
    {
    	//toast("hHoop1M");
    	
    	if(hHoop1 > 0)
    	{
    		hHoop1--;
    		TextView text = (TextView) findViewById(R.id.hHoop1);
    		text.setText(hHoop1 + " " + "in Hoop 1");
    	}
    }
    public void hHoop1P(View button)
    {
    	//toast("hHoop1P");
    	hHoop1++;
		TextView text = (TextView) findViewById(R.id.hHoop1);
		text.setText(hHoop1 + " " + "in Hoop 1");
    }
    public void hHoop2M(View button)
    {
    	//toast("hHoop2M");

    	if(hHoop2 > 0)
    	{
    		hHoop2--;
    		TextView text = (TextView) findViewById(R.id.hHoop2);
    		text.setText(hHoop2 + " " + "in Hoop 2");
    	}
    }
    public void hHoop2P(View button)
    {
    	//toast("hHoop2P");
    	hHoop2++;
		TextView text = (TextView) findViewById(R.id.hHoop2);
		text.setText(hHoop2 + " " + "in Hoop 2");
    }
    public void hHoop3M(View button)
    {
    	//toast("hHoop3M");

    	if(hHoop3 > 0)
    	{
    		hHoop3--;
    		TextView text = (TextView) findViewById(R.id.hHoop3);
    		text.setText(hHoop3 + " " + "in Hoop 3");
    	}
    }
    public void hHoop3P(View button)
    {
    	//toast("hHoop3P");
    	hHoop3++;
		TextView text = (TextView) findViewById(R.id.hHoop3);
		text.setText(hHoop3 + " " + "in Hoop 3");
    }
    public void tHoop1M(View button)
    {
    	//toast("tHoop1M");
    	
    	if(tHoop1 > 0)
    	{
    		tHoop1--;
    		TextView text = (TextView) findViewById(R.id.tHoop1);
    		text.setText(tHoop1 + " " + "in Hoop 1");
    	}
    }
    public void tHoop1P(View button)
    {
    	//toast("tHoop1P");
    	tHoop1++;
		TextView text = (TextView) findViewById(R.id.tHoop1);
		text.setText(tHoop1 + " " + "in Hoop 1");
    }
    public void tHoop2M(View button)
    {
    	//toast("tHoop2M");

    	if(tHoop2 > 0)
    	{
    		tHoop2--;
    		TextView text = (TextView) findViewById(R.id.tHoop2);
    		text.setText(tHoop2 + " " + "in Hoop 2");
    	}
    }
    public void tHoop2P(View button)
    {
    	//toast("tHoop2P");
    	tHoop2++;
		TextView text = (TextView) findViewById(R.id.tHoop2);
		text.setText(tHoop2 + " " + "in Hoop 2");
    }
    public void tHoop3M(View button)
    {
    	//toast("tHoop3M");

    	if(tHoop3 > 0)
    	{
    		tHoop3--;
    		TextView text = (TextView) findViewById(R.id.tHoop3);
    		text.setText(tHoop3 + " " + "in Hoop 3");
    	}
    }
    public void tHoop3P(View button)
    {
    	//toast("tHoop3P");
    	tHoop3++;
		TextView text = (TextView) findViewById(R.id.tHoop3);
		text.setText(tHoop3 + " " + "in Hoop 3");
    }
    public void balYes(View button)
    {
    	RadioButton rb = (RadioButton) findViewById(R.id.balanceNo);
    	rb.setChecked(false);
    }
    public void balNo(View button)
    {
    	RadioButton rb = (RadioButton) findViewById(R.id.balanceYes);
    	rb.setChecked(false);
    }
    
    public void submit(View button)
    {
    	final AlertDialog.Builder b = new AlertDialog.Builder(this);
    	b.setIcon(android.R.drawable.ic_dialog_alert);
    	b.setTitle("Are You Sure?");
    	b.setMessage("Submit match data? Cancel to stay on current match.");
    	b.setPositiveButton(android.R.string.yes, new SubmitClicked());
    	b.setNegativeButton(android.R.string.no, null);
    	b.show();
    }
    //********************************************************************************************************
    //********************************************************************************************************
    
    private class SubmitClicked implements OnClickListener
    {

		public void onClick(DialogInterface dialog, int which)
		{
			submitData();
		}
    	
    }
    
    private void toast(String msg)
    {
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    
    private void playSound()
    {
    	///*
    	Uri noise = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	
    	if(noise != null)
    	{
    		MediaPlayer mediaPlayer = new MediaPlayer();
    		mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
    		try {
				mediaPlayer.setDataSource(getApplicationContext(), noise);
				mediaPlayer.setLooping(false);
				mediaPlayer.prepare();
	    		mediaPlayer.start();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	//*/
    }
}