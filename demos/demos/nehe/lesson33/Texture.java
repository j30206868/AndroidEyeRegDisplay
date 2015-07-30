package demos.nehe.lesson33;

import java.nio.ByteBuffer;

import android.opengl.GLES20;

public class Texture {
	ByteBuffer imageData;								// Image Data (Up To 32 Bits)
	int bpp;											// Image Color Depth In Bits Per Pixel
	int width;											// Image width
    	int height;											// Image height
    	int[] texID = new int[1];											// Texture ID Used To Select A Texture
	int type;											// Image Type (GL_RGB, GL_RGBA)
    	String gName; 										// group name of the texture
    	
    	public int getBpp() {
		return bpp;
	}
    	public void setBpp(int bpp) {
		this.bpp = bpp;
	}
    	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public int getTexID() {
		return texID[0];
	}
	public void setTexID(int texID) {
		this.texID[0] = texID;
	}
	public ByteBuffer getImageData() {
		return imageData;
	}
	public void setImageData(ByteBuffer imageData) {
		this.imageData = imageData;
	}

	public static int glTextureIDs[] = {
		GLES20.GL_TEXTURE0,		GLES20.GL_TEXTURE1,
		GLES20.GL_TEXTURE2,		GLES20.GL_TEXTURE3,
		GLES20.GL_TEXTURE4,		GLES20.GL_TEXTURE5,
		GLES20.GL_TEXTURE6,		GLES20.GL_TEXTURE7,
		GLES20.GL_TEXTURE8,		GLES20.GL_TEXTURE9,
		GLES20.GL_TEXTURE10,	GLES20.GL_TEXTURE11,
		GLES20.GL_TEXTURE12,	GLES20.GL_TEXTURE13,
		GLES20.GL_TEXTURE14,	GLES20.GL_TEXTURE15,
		GLES20.GL_TEXTURE16,	GLES20.GL_TEXTURE17,
		GLES20.GL_TEXTURE18,	GLES20.GL_TEXTURE19,
		GLES20.GL_TEXTURE20,	GLES20.GL_TEXTURE21,
		GLES20.GL_TEXTURE22,	GLES20.GL_TEXTURE23,
		GLES20.GL_TEXTURE24,	GLES20.GL_TEXTURE25,
		GLES20.GL_TEXTURE26,	GLES20.GL_TEXTURE27,
		GLES20.GL_TEXTURE28,	GLES20.GL_TEXTURE29,
		GLES20.GL_TEXTURE30,	GLES20.GL_TEXTURE31,

	};
}
