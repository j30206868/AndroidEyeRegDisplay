package cwz.basicopengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class SceneRenderer implements GLSurfaceView.Renderer{

	private CombatMan combatMan;
	private ThreeTriangle tt;
	private MySurfaceView MV;
	
	public SceneRenderer(MySurfaceView MV){
		this.MV = MV;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		//深度檢測
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		//背景剪裁
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		combatMan = FileLoader.loadCombatManFromFIle("Combat_Android.obj", MV.getResources(), MV);
		
		Log.d(null, "SceneRenderer: Surface created!");
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		GLES20.glViewport(0, 0, width, height);
		
		Global.ratio = (float)width/height;
		
		MatrixState.setProjectFrustum(-Global.ratio, Global.ratio, -1, 1, 1, 400);
		
		MatrixState.setCamera(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		//MatrixState.setCamera(-16f, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		MatrixState.setInitStack();
		
		Log.d(null, "SceneRenderer: Surface changed!");
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//清除深度緩衝 與 顏色緩衝
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT );
		//GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
		MatrixState.pushMatrix();
		if( combatMan != null){
			MatrixState.translate(2, 0, 0);
			combatMan.drawSelf();
		}else{
			Log.e("Ondrawframe", "Combat is null!");
		}
		MatrixState.popMatrix();
	}

}
