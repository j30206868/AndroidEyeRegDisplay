package cwz.basicopengl;

import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.app.ActivityManager;
import android.opengl.GLES20;

public class ThreeTriangle {
	private int vertaxCount;
	
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	
	private FloatBuffer mVertaxBuff;
	private FloatBuffer mColorBuff;
	
	public ThreeTriangle(MySurfaceView MV){
		initVertax();
		initShader(MV);
	}
	
	public void initVertax(){
		
		vertaxCount = 3 * 3; //3 * 3個點
		
		float[] vertaxs ={
			//第一個三角形
			-1.0f, -1.0f, 0.0f,
			1.0f, -1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			//第二個三角形
			-1.0f, -1.0f, 2.0f,
			1.0f, -1.0f, 2.0f,
			0.0f, 1.0f, 2.0f,
			//第三個三角形
			-1.0f, -1.0f, 4.0f,
			1.0f, -1.0f, 4.0f,
			0.0f, 1.0f, 4.0f
		};
		
		ByteBuffer vBuff = ByteBuffer.allocateDirect( vertaxs.length * 4 );
		vBuff.order(ByteOrder.nativeOrder());
		mVertaxBuff = vBuff.asFloatBuffer();
		mVertaxBuff.put(vertaxs);
		mVertaxBuff.position(0);//讀完回到起始點
		
		float[] colors ={
			//第一個三角形
			1.0f, 0.0f, 0.0f, 1.0f, //點1 r
			0.0f, 0.0f, 0.9f, 0.2f, //點2 
			0.0f, 0.0f, 0.9f, 0.2f, //點3 
			//第二個三角形
			1.0f, 0.0f, 0.0f, 1.0f, //r
			0.0f, 0.9f, 0.0f, 1.0f, //點2 
			0.0f, 0.9f, 0.0f, 1.0f, //點3 
			//第二個三角形
			1.0f, 0.0f, 0.0f, 1.0f, //r
			0.7f, 0.7f, 0.7f, 0.1f, //點2 
			0.7f, 0.7f, 0.7f, 0.1f, //點3 
		};
		
		ByteBuffer cBuff = ByteBuffer.allocateDirect( colors.length * 4 );
		cBuff.order(ByteOrder.nativeOrder());
		mColorBuff = cBuff.asFloatBuffer();
		mColorBuff.put(colors);
		mColorBuff.position(0);
	}

	public void initShader(MySurfaceView MV){
		String vertaxSource = ShaderUtil.readAsset("vertaxShader.sh", MV.getResources());
		String fragmentSource = ShaderUtil.readAsset("fragmentShader.sh", MV.getResources());
		
		mProgram = ShaderUtil.createProgram(vertaxSource, fragmentSource);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertaxBuff);
		
		GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, mColorBuff);
		
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		
		//通知頂點及顏色資料為array
		GLES20.glEnableVertexAttribArray(mPositionHandle);  
	        GLES20.glEnableVertexAttribArray(mColorHandle);  
	        
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertaxCount);
	}
}
