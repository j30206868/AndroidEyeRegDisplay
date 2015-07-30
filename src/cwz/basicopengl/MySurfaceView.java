package cwz.basicopengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MySurfaceView extends GLSurfaceView {
	private SceneRenderer mRenderer;
	
	public MySurfaceView(Context context){
		super(context);
		
		this.setEGLContextClientVersion(2);
		
		this.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
		this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		mRenderer = new SceneRenderer(this);
		this.setRenderer(mRenderer);
		//this.setRenderMode(RENDERMODE_CONTINUOUSLY);
		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		Log.d(null, "MySurfaceView: creation finished!");
	}
}
