package Image;

import android.util.Log;

public class ImgInfo{
	public byte[] pixels;
	public float[] gravity;
	public int id;
	public ImgInfo(){}
	public ImgInfo(int _id, float[] _gravity, byte[] data){
		pixels = data.clone();
		id = _id;
		gravity = _gravity.clone();
	}
	
	public ImgInfo clone(){
		Log.d(null, "Clone id "+id);
		return new ImgInfo(this.id, this.gravity, this.pixels);
	}
}