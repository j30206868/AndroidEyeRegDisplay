package cwz.basicopengl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

public class ShaderUtil {
	public static int loadShader(int shaderType, String source){
		int shader = GLES20.glCreateShader(shaderType);
		
		if( shader != 0){//нYжие\
			GLES20.glShaderSource(shader, source);
			
			GLES20.glCompileShader(shader);
			
			int[] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			
			//check compile status
			if( compiled[0] == 0 ){
				Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
				Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
				
				shader = 0;
			}
		}
		
		return shader;
	} 

	public static int createProgram( String vertaxSource, String fragmentSource){
		int vertaxShader = loadShader(GLES20.GL_VERTEX_SHADER, vertaxSource);
		if( vertaxShader == 0){
			return 0;
		}

		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if( pixelShader == 0){
			return 0;
		}
		
		int program = GLES20.glCreateProgram();
		
		if( program != 0){
			GLES20.glAttachShader(program, vertaxShader);
			checkGLError("glAttachShader");
			
			GLES20.glAttachShader(program, pixelShader);
			checkGLError("glAttachShader");
		
			GLES20.glLinkProgram(program);
			
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if (linkStatus[0] != GLES20.GL_TRUE) {
				Log.e("ES20_ERROR", "Could not link program: ");
		                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
		                GLES20.glDeleteProgram(program);
		                program = 0;
			}
		}
		
		return program;
	}
	
	public static void checkGLError(String op){
		int error;
		while( (error = GLES20.glGetError()) != GLES20.GL_NO_ERROR ){
			Log.e("ES20_ERROR", op + ": glError " + error);
		        throw new RuntimeException(op + ": glError " + error);
		}
	}
	
	public static String readAsset(String name, Resources r){
		String result = null;
		try{
			InputStream im = r.getAssets().open(name);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while( (ch = im.read()) != -1){
				baos.write(ch);
			}
			
			byte[] buff = baos.toByteArray();
			result = new String(buff, "UTF-8");
			
			//change nextline notation
			//result = result.replaceAll("\\r\\n", "\\n");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return result;
	}
}
