package cwz.basicopengl;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import Image.ImgInfo;
import Sensor.GravitySensor;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.View;

class CustomPreviewCallback implements Camera.PreviewCallback{
	CameraView myView;
	File folder;
	File newFile;
	String FilePath;
	ImgUDPProcessor myProcessor;
	Thread myThread;
	GravitySensor mGravitySensor;
	
	public CustomPreviewCallback(CameraView view, ImgUDPProcessor processor, GravitySensor gravitySensor){
		myView = view;
		
		Log.d(null, "myProcessor start");
		
		Camera.Parameters param = myView.getCameraParameters();

		myProcessor = processor;
		//myProcessor = new ImgUDPProcessor(param.getPictureFormat(), param.getPreviewSize(), myView);
		//myProcessor.start();
		
		mGravitySensor = gravitySensor;
		Log.d(null, "CustomPreviewCallback Created !!!!!");
	}
	
	public void onPreviewFrame (byte[] data, Camera camera){
		//做yuv subsample
		if(ImgUDPProcessor.isPreviewInfoSet == false){
			ImgUDPProcessor.previewFormat = camera.getParameters().getPreviewFormat();
			ImgUDPProcessor.previewWidth = camera.getParameters().getPreviewSize().width;
			ImgUDPProcessor.previewHeight = camera.getParameters().getPreviewSize().height;
			ImgUDPProcessor.isPreviewInfoSet = true;
		}
		//將影像插入queue
		if(myView.getImageCount() % 4 == 0){
			boolean isInserted = ImgUDPProcessor.insert( new ImgInfo(myView.getImageCount(), mGravitySensor.gravity, data) );
		}
		Log.d(null,  "insert "+"firstOne : "+ ImgUDPProcessor.firstOne + " | lastOne: "+ ImgUDPProcessor.lastOne);
		myView.setImageCount( myView.getImageCount() + 1);
		try{
    			myView.postInvalidate();
    		}catch(Exception e){
    			Log.d(null, "Invalidate fail: "+ e.getMessage());
    		}
	}
}
