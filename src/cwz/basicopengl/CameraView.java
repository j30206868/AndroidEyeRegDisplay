package cwz.basicopengl;

import java.io.IOException;
import java.util.List;

import Sensor.GravitySensor;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class CameraView extends GLSurfaceView implements SurfaceHolder.Callback {
	private Camera camera;
	private CustomPreviewCallback myPreviewCallback;
	private int ImageCount;
	
	private Size optimalPreviewSize;
	private Context mContext;
	
	private GravitySensor mGravitySensor;
	private ImgUDPProcessor myProcessor;

	public CameraView( Context context ) {
	        super( context );
	        // We're implementing the Callback interface and want to get notified
	        // about certain surface events.
	        getHolder().addCallback( this );
	        // We're changing the surface to a PUSH surface, meaning we're receiving
	        // all buffer data from another component - the camera, in this case.
	        getHolder().setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
	        
	        ImageCount = 0;
	        camera = null;
	        
	        mContext = context;
    	}
	
    	public void surfaceCreated( SurfaceHolder holder ) {
	        // Once the surface is created, simply open a handle to the camera hardware.
    		final int cameraID = 0;
	        camera = Camera.open(cameraID);
	        
	        Log.d(null, "Surface created.");
    	}
 
    	public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
    		 Log.d(null, "Surface changed.");
    		setWillNotDraw(false); 
    		setMeasuredDimension(width, height);
	        Camera.Parameters p = camera.getParameters();	        
		/*try{
			List<Size> sizes = p.getSupportedPreviewSizes();
			for (int i=0;i<sizes.size();i++){
				Size tmp = sizes.get(i);
				Log.i("PictureSize", "Supported Size: " +tmp.width+", "+tmp.height);
				//if(tmp.width <= 700 && tmp.width >= 600){
				if( tmp.width <= 400 ){
					p.setPreviewSize(tmp.width , tmp.height);
					p.setPictureSize(tmp.width , tmp.height);
					break;
				}
			}
			camera.setParameters(p);
			camera.setPreviewDisplay(holder); 
			Log.d(null, "Camera param set! ");
		}catch(RuntimeException e){
			Log.d(null, "ERROR!!!!!!!!!!!!!!!! "+e.getMessage());
		}catch(IOException ioe){
			Log.d(null, "camera.setPreviewDisplay error. msg:"+ioe.getMessage());
		}*/
	        optimalPreviewSize = getOptimalPreviewSize(p.getSupportedPreviewSizes(), width, height);
	        
	        mGravitySensor = new GravitySensor((Activity)mContext);
	        
	        myPreviewCallback = new CustomPreviewCallback(this, myProcessor, mGravitySensor);
	        camera.setPreviewCallback(myPreviewCallback);
	        
	      //set img udp processor
	        myProcessor = new ImgUDPProcessor(this);
		myProcessor.start();
	        
	        // ...and start previewing. From now on, the camera keeps pushing preview
	        // images to the surface.
	        //camera.startPreview();
		setOptimalPreviewSize(holder);
    	}
 
    	public void surfaceDestroyed( SurfaceHolder holder ) {
	      //destroy img udp processor
	        myProcessor.end();
	        myProcessor = null;
	        
	        //clear preview object
	        myPreviewCallback = null;
	        
	        // Once the surface gets destroyed, we stop the preview mode and release
	        // the whole camera since we no longer need it.
	        camera.stopPreview();
	        camera.release();
	        camera = null;
	        
	        mGravitySensor.unregisterSensor();
    	}
    
    	public int getImageCount() {
		return ImageCount;
	}

	public void setImageCount(int imageCount) {
		ImageCount = imageCount;
	}
    	
	 public  Camera.Parameters getCameraParameters(){
		 return camera.getParameters();
	}
	    
	public  void setCameraParameters(Camera.Parameters p){
		camera.setParameters( p );
	}
	
	private void setOptimalPreviewSize(SurfaceHolder holder){
		try { 
			//camera.stopPreview(); 
			
			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
			camera.setParameters(parameters);
			
			camera.setPreviewDisplay(holder); 
			camera.startPreview(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio=(double)h / w;
		
		Log.d(VIEW_LOG_TAG, "w: "+w+", "+"h: "+h);
		
		if (sizes == null) return null;
		
		Camera.Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		
		int targetHeight = h;
		
		for (Camera.Size size : sizes) {
		        double ratio = (double) size.width / size.height;
		        if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
		        if (Math.abs(size.height - targetHeight) < minDiff) {
		            optimalSize = size;
		            minDiff = Math.abs(size.height - targetHeight);
		        }
		}
		
		if (optimalSize == null) {
		        minDiff = Double.MAX_VALUE;
		        for (Camera.Size size : sizes) {
		            if (Math.abs(size.height - targetHeight) < minDiff) {
		                optimalSize = size;
		                minDiff = Math.abs(size.height - targetHeight);
		            }
		        }
		}
		return optimalSize;
	}
}