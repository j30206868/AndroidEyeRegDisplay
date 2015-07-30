package cwz.basicopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import demos.nehe.lesson33.Texture;
import android.graphics.BitmapFactory;

public class CombatMan extends GameObject {
	protected final String mtlFileName = "Combat_Android.mtl";
	protected Texture mTextures[];
	protected int mTexCoorHandle;
	protected FloatBuffer mTextureBuff;
	
	public FloatBuffer getmTextureBuff() {
		return mTextureBuff;
	}

	public void setmTextureBuff(FloatBuffer mTextureBuff) {
		this.mTextureBuff = mTextureBuff;
	}

	public CombatMan(MySurfaceView MV, int vertexCount, float[] vResult, float[] tResult){
		super(MV, vertexCount, vResult); // do nothing
		initVertax(vertexCount, vResult, tResult);
		initShader(MV);
		
		initMTexture(mtlFileName, MV.getResources());
	}
	
	public void initVertax(int vertexCount, float[] vResult, float[] tResult){
		super.initVertax(vertexCount, vResult);
	
		//set vertex buffer
		//Log.e("CombatMan initVertax", "tResult len="+tResult.length);
		ByteBuffer tBuff = ByteBuffer.allocateDirect( tResult.length * 4 );
		tBuff.order(ByteOrder.nativeOrder());
		mTextureBuff = tBuff.asFloatBuffer();
		mTextureBuff.put(tResult);
		mTextureBuff.position(0);
		//Log.e("CombatMan initVertax", "mTextureBuff capacity="+mTextureBuff.capacity());
		
		//this.setmTextureBuff(mTextureBuff);
	}
	
	public void initShader(MySurfaceView MV){
		super.initShader(MV);
		
		mTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
	}
	
	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		
		//通知頂點及顏色資料為array
		GLES20.glEnableVertexAttribArray(mPositionHandle);  
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertaxBuff);
		
		GLES20.glEnableVertexAttribArray(mTexCoorHandle);  

		GLES20.glVertexAttribPointer(mTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2*4, mTextureBuff); // send texture into pipeline (2*4: only S and T axis)	
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
		
		GLES20.glActiveTexture( Texture.glTextureIDs[0] );
        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0].getTexID());
		
		/*for(int i=0 ; i<mTextures.length ; i++){
			GLES20.glActiveTexture( Texture.glTextureIDs[i] );
	        	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i].getTexID());
	        	
	        	Log.d("CombatMan iniMTexture","glActiveTexture: "+Texture.glTextureIDs[i]+" ; glBindTexture: "+mTextures[i].getTexID());
		}*/
		
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertaxCount);
	}
	
	private void initMTexture(String mtlName, Resources res){
		mTextures = FileLoader.loadMtl(mtlName, res);
		
		if( mTextures == null){
			Log.e("InitMTexture", "null textures received from FileLoader.loadMtl() ");
		}

		int textures[] = new int[mTextures.length];		
		
		//test code
			GLES20.glGenTextures(1, textures, 0);
			mTextures[0].setTexID( textures[0] );
			//Log.d("CombatMan iniMTexture","mTexture["+0+"] id: "+mTextures[0].getTexID());
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0].getTexID());
			//set MIN and MAG setting to adjust different texture size
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
			//set Axis S and T stretch setting
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
			
			//Log.e("InitMTexture", "after bitmap decoded, bpp = "+mTextures[0].getBpp());
			
			//set subsample number
			int subsample = 8;
			//convert from bytebuffer into byte array
			//subsample the texture
			byte bArray[] = bmpSubSample(mTextures[0].getImageData(), mTextures[0].getWidth(), mTextures[0].getHeight(), subsample, mTextures[0].getBpp());
			//Log.e("InitMTexture", "after subSample bArray len: "+bArray.length);
			
			//create bitmap
			int bWidth = mTextures[0].getWidth() / (subsample/2);
			int bHeight = mTextures[0].getHeight() / (subsample/2);
			Bitmap bmp = Bitmap.createBitmap( bWidth, bHeight, Bitmap.Config.ARGB_8888);
			
			//Log.e("InitMTexture", "after bitmap creation; bmp len="+bmp.getByteCount());
			
			if( mTextures[0].getBpp() == 24)
			{ // no alpha, need to add alpha value manually
				//Log.e("InitMTexture", "start adding alpha value into byte array");
				byte resultBArray[] = new byte[ (bmp.getWidth() * bmp.getHeight() * 4 )]; //number of pixels multiplied by 4(RGBA)
				//Log.e("InitMTexture", "resultBArray len="+resultBArray.length);
				for(int i = 0, j=0 ; i < resultBArray.length ; i+=4, j+=3){
					resultBArray[i] = bArray[j];
					resultBArray[i+1] = bArray[j+1];
					resultBArray[i+2] = bArray[j+2];
					resultBArray[i+3] = (byte)255; // alpha
				}
				
				ByteBuffer bf = ByteBuffer.allocateDirect( resultBArray.length );
				bf.order(ByteOrder.nativeOrder());
				bf.put(resultBArray);
				bf.position(0);
				bmp.copyPixelsFromBuffer(bf);
			}else if(  mTextures[0].getBpp() == 32 )
			{//color depth is 32 bits
				ByteBuffer bf = ByteBuffer.allocateDirect( bArray.length );
				bf.order(ByteOrder.nativeOrder());
				bf.put(bArray);
				bf.position(0);
				bmp.copyPixelsFromBuffer(bf);
			}else{
				Log.e("InitMTexture", "Textrue has abnormal color encode: "+mTextures[0].getBpp()+" bits");
			}
			Log.e("InitMTexture", "after bitmap decoded, bmp length: "+bmp.getByteCount());
			
			//實際加載貼圖
			GLUtils.texImage2D( GLES20.GL_TEXTURE_2D, 0, bmp,0);
			bmp.recycle();
		//test code
		
		/*GLES20.glGenTextures(textures.length, textures, 0);
		for(int i=0 ; i<mTextures.length ; i++)
		{
			mTextures[i].setTexID( textures[i] );
			Log.d("CombatMan iniMTexture","mTexture["+i+"] id: "+mTextures[i].getTexID());
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i].getTexID());
			//set MIN and MAG setting to adjust different texture size
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
			//set Axis S and T stretch setting
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
			
			//convert from bytebuffer into byte array
			byte bArray[] = mTextures[i].getImageData().array();
			Bitmap bmp = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
			
			GLUtils.texImage2D( GLES20.GL_TEXTURE_2D, 0, bmp,0);
			bmp.recycle();
		}*/
	}
	
	public byte[] bmpSubSample(ByteBuffer oBuffer, int oW, int oH, int subsample, int colorDepth){
		byte bArray[] = oBuffer.array();
		
		// assign subsample equally to width and height
		subsample = subsample/2;
		
		int pNum = colorDepth/8; // argb = 4, rgb = 3 (number of bytes for each pixels)
		int newW = oW/subsample;
		int newH = oH/subsample;
		
		int resultYXMap[][] = makeYXIndexMap( newW, newH );
		int bArrayYXMap[][] = makeYXIndexMap( oW, oH );
		
		int newArrayLen = newW * newH * pNum;
		byte resultArray[] = new byte[ newArrayLen ];
		
		for(int i=0 ; i<newH ; i++){
			for(int j=0 ; j<newW; j++)
			{
				//yxMap():convert two dimensional index(j, i) into one dimentional index(idx)
				int x = j;
				int y = i;
				
				if( resultYXMap[y][x] >= newArrayLen ){
					Log.e("bmpSubSample null pointer", "resultYXMap[y][x]="+resultYXMap[y][x]+" ; x:"+x+"; y="+y+" ; newW="+newW+", newH="+newH+" ; newArrayLen="+newArrayLen);
				}
				
				int resultIndex = resultYXMap[y][x] * pNum;
				int bIndex = bArrayYXMap[(y)*subsample][(x)*subsample] * pNum;
				for(int z=0 ; z<pNum ; z++){
					resultArray[ resultIndex+z ] = bArray[ bIndex+z ]; // first bit of pixel
				}
			}	
		}
		
		return resultArray;
	}
	
	protected int[][] makeYXIndexMap(int width, int height){
		int[][] yx = new int[height][width];
		
		int oneDimensionIndex = 0;
		for(int i = 0 ; i < height ; i++){
			for(int j = 0 ; j < width ; j++){
				yx[i][j] = oneDimensionIndex;
				oneDimensionIndex++;
			}
		};
		
		return yx;
	}
}
