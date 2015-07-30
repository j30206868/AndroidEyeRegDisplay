package cwz.basicopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class GameObject {
	protected int vertaxCount;

	protected int mProgram;
	protected int mPositionHandle;
	protected int mColorHandle;
	protected int mMVPMatrixHandle;
	
	protected FloatBuffer mVertaxBuff;
	//private FloatBuffer mColorBuff;
	
	public GameObject(MySurfaceView MV, int vertexCount, float[] vResult, float[] cResult){
		//initVertax(vertexCount, vResult);
		//initColor(cResult);
		//initShader(MV);
	}
	public GameObject(MySurfaceView MV, int vertexCount, float[] vResult){
		initVertax(vertexCount, vResult);
		initShader(MV);
	}
	
	public int getVertaxCount() {
		return vertaxCount;
	}
	public void setVertaxCount(int vertaxCount) {
		this.vertaxCount = vertaxCount;
	}
	/*public void setMColorBuff(FloatBuffer cb){
		mColorBuff = cb;
	}
	public FloatBuffer getMColorBuff(){
		return mColorBuff;
	}*/
	public void setMVertaxBuff(FloatBuffer vb){
		mVertaxBuff = vb;
	}
	public FloatBuffer getMVertaxBuff(){
		return mVertaxBuff;
	}
	
	public void initVertax(int vertexCount, float[] vResult){
		setVertaxCount(vertexCount);
	
		FloatBuffer mVertaxBuff;

		//set vertex buffer
		ByteBuffer vBuff = ByteBuffer.allocateDirect( vResult.length * 4 );
		vBuff.order(ByteOrder.nativeOrder());
		mVertaxBuff = vBuff.asFloatBuffer();
		mVertaxBuff.put(vResult);
		mVertaxBuff.position(0);

		this.setMVertaxBuff(mVertaxBuff);
		
	}
	
	/*public void initColor(float[] cResult){
		FloatBuffer mColorBuff;
		
		//set color buffer
		ByteBuffer cBuff = ByteBuffer.allocateDirect( cResult.length * 4 );
		cBuff.order(ByteOrder.nativeOrder());
		mColorBuff = cBuff.asFloatBuffer();
		mColorBuff.put(cResult);
		mColorBuff.position(0);
		
		this.setMColorBuff(mColorBuff);
	}*/

	public void initShader(MySurfaceView MV){
		String vertaxSource = ShaderUtil.readAsset("vertaxShader.sh", MV.getResources());
		String fragmentSource = ShaderUtil.readAsset("fragmentShader.sh", MV.getResources());
		
		mProgram = ShaderUtil.createProgram(vertaxSource, fragmentSource);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		
		//mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	/*public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		
		//通知頂點及顏色資料為array
		GLES20.glEnableVertexAttribArray(mPositionHandle);  
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertaxBuff);
		
		GLES20.glEnableVertexAttribArray(mColorHandle);  
		GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, mColorBuff);
		
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
	        
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertaxCount);
	}*/
}
