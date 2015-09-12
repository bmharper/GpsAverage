package com.example.gpsaverage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.*;

//import android.gms.location.LocationRequest;

public class MainActivity extends ActionBarActivity implements LocationListener {
	
	static final String LOG_TAG = "gpsavg";
	
	OutputStream trackFile;
	Button recordBtn;
	TextView txtLabel;
	int nLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        txtLabel = (TextView) findViewById(R.id.textView1);
        nLocations = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void recordBtnClick(View v) {
    	if (trackFile == null) {
	    	try {
	    		//File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "tracks.csv");
	    		nLocations = 0;
	    		File path = new File(Environment.getExternalStorageDirectory(), "tracks.csv");
	    		trackFile = new FileOutputStream(path);
	    		trackFile.write("time,lat,lon\n".getBytes());
	        	Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
	        	startGPS();
	        	recordBtn.setText("Stop");
	    	}
	    	catch (Exception e) {
	        	Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	    	}
    	} else {
    		try {
    	    	stopGPS();
    			trackFile.close();
            	Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
	        	recordBtn.setText("Start Recording");
    		}
    		catch (Exception e) {
	        	Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    		}
    		trackFile = null;
    	}
    }    

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	}
	
	@Override
	public void onLocationChanged(Location location) {
		if (trackFile != null) {
			GregorianCalendar c = new GregorianCalendar();
			String msg = String.format("%tT,%f,%f\n", c, location.getLatitude(), location.getLongitude());
			nLocations++;
			txtLabel.setText("updates: " + nLocations);
			try {
				trackFile.write(msg.getBytes());
			}
			catch (Exception e) {
			}
		}
	}
    
    public void startGPS() {
    	LocationManager m = (LocationManager) getSystemService(LOCATION_SERVICE);
    	m.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    public void stopGPS() {
    	LocationManager m = (LocationManager) getSystemService(LOCATION_SERVICE);
    	m.removeUpdates(this);
    }
    
    public File getStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "gps-tracks");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }    
}
