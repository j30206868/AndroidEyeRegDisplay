package cwz.basicopengl;
import android.opengl.Matrix;

public class MatrixState {
	private static float[] mProjMatrix = new float[16];//4x4矩陣 投影用
	private static float[] mCamMatrix = new float[16];//4x4矩陣 Camera用
	private static float[] cObjMatrix;//当前变换矩阵
	
	private static float[] finalMVPMatrix = new float[16];
	
	private static float[][] mStack = new float[10][16];//變換矩陣的stack
	private static int sTop = -1;
	
	public static void setInitStack(){
		cObjMatrix = new float[16];
		Matrix.setRotateM(cObjMatrix, 0, 0, 1, 0, 0);
	}
	
	public static void pushMatrix(){
		sTop++;
		for(int i=0 ; i< 16 ; i++){
			mStack[sTop][i] = cObjMatrix[i];
		}
	}
	
	public static float[] getFinalMatrix(){
		Matrix.multiplyMM(finalMVPMatrix, 0, mCamMatrix, 0, cObjMatrix, 0);
		Matrix.multiplyMM(finalMVPMatrix, 0, mProjMatrix, 0, finalMVPMatrix, 0);
		
		return finalMVPMatrix;
	}
	
	public static float[] getMatrix(){
		return cObjMatrix;
	}
	
	public static void popMatrix(){
		for(int i = 0 ; i < 16 ; i++){
			cObjMatrix[i] = mStack[sTop][i];
		}
		sTop--;
	}
	
	public static void translate(float x, float y, float z){
		Matrix.translateM(cObjMatrix, 0, x, y, z);
	}
	
	public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far){
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}
	
	public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz){
		Matrix.setLookAtM(mCamMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
	}
}
