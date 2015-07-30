package Sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GravitySensor {
	SensorManager mySensorManager;
	Sensor myGravitySensor;
	public float gravity[];
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//开发实现了SensorEventListener接口的传感器监听器
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		@Override
		public void onSensorChanged(SensorEvent event){
			float []values=event.values;	
			//Log.d(null, "X:"+values[0]+" Y:"+values[1]+" Z:"+values[2]);
			gravity[0] = values[0];
			gravity[1] = values[1];
			gravity[2] = values[2];
		}
	};
	
	public GravitySensor(Activity act) {
		mySensorManager = (SensorManager)act.getSystemService(Activity.SENSOR_SERVICE);	
		myGravitySensor=mySensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mySensorManager.registerListener(mySensorListener, myGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
		gravity = new float[3];
		gravity[0] = 0;
		gravity[1] = 0;
		gravity[2] = 0;
	}
	
	public void unregisterSensor(){
		mySensorManager.unregisterListener(mySensorListener);
	}
}
