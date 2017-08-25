
package com.SimpleFlash;

import android.app.*;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.content.pm.*;
import android.hardware.camera2.*;
import android.util.*;

public class SimpleFlash extends Activity
implements OnClickListener
{
	private static final String promptOn = "Tap to turn on.";
	private static final String promptOff = "Tap to turn off.";
	private static final int black = 0xff000000;
	private static final int white = 0xffffffff;

	private TextView tv;
	private boolean isOn;
	private CameraManager cameraManager;
	private String cameraId;
	    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		checkFlash();
		
        /* Initialization */
		isOn = false;
		setCamera();
        tv = new TextView(this);
		tv.setText(promptOn);
		tv.setBackgroundColor(white);
		tv.setTextColor(black);       
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(40.0f);
		tv.setOnClickListener(this);

		/* Show text */
        setContentView(tv);

    }

	//Check if the device has camera flash light.
    private void checkFlash(){
		boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		
        if (!isFlashAvailable) {        
            AlertDialog alert = new AlertDialog.Builder(this)
                    .create();
            alert.setTitle("Error:");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", 
                            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
        }
    }

	//Find a camera with a flash unit. 
	private void setCamera(){
		String[] camIds;
		CameraCharacteristics camChar;
		cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		try{
			camIds = cameraManager.getCameraIdList();
			for(int i = 0; i < camIds.length; ++i){
				camChar = cameraManager.getCameraCharacteristics(camIds[i]);
				if(camChar.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)){
					cameraId = camIds[i];
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//turn on or off
	@Override
    public void onClick(View view){
	try{
		if(!isOn){
			cameraManager.setTorchMode(cameraId, true);
			isOn = true;
			tv.setText(promptOff);
			tv.setBackgroundColor(black);
			tv.setTextColor(white);
    	}
    	else{
			cameraManager.setTorchMode(cameraId, false);
			isOn = false;
			tv.setText(promptOn);
			tv.setBackgroundColor(white);
			tv.setTextColor(black);
    	}
	}catch(Exception e){
		e.printStackTrace();
	}
    }
}
