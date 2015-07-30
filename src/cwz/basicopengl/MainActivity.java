package cwz.basicopengl;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.opengl.*;


public class MainActivity extends Activity {

	private CameraView cameraView;
	private MySurfaceView MV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		MV = new MySurfaceView(this);
		
		setContentView(MV);
		MV.requestFocus();
		MV.setFocusableInTouchMode(true);
		
		cameraView = new CameraView( this );
		addContentView( cameraView, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		MV.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		MV.onResume();
	}
}
