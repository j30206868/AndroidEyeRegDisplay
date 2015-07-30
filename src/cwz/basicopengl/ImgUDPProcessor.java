package cwz.basicopengl;

import Image.ImgInfo;
import Network.UdpClient;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.view.View;

public class ImgUDPProcessor extends Thread {
	
	
	// data needed for maintain the queue
	public static int qLength = 100;
	public static ImgInfo[] dataQueue = new ImgInfo[qLength];
	public static int lastOne = 0;
	public static int firstOne = -1;
	
	//camera preview format
	public static boolean isPreviewInfoSet = false;
	public static int previewFormat;
	public static int previewWidth;
	public static int previewHeight;
	
	public static int finishedCounter = 0;
	
	//object attr
	private boolean EndThread;
	private CameraView mView;
	private DatagramSocket ds;
	
	public ImgUDPProcessor(CameraView view){
		super();
		
		//set thread control
		EndThread = false;
		
		mView = view;
		try{
			ds = new DatagramSocket();
		}catch(Exception e){
			Log.d(null, "Create datagram socket failed! msg:"+e.getMessage());
		}
	}
	
	public void run(){
		while(1==1){
			if(EndThread)
				break;
			
			ImgInfo dataInfo = pull();
	
			if(dataInfo != null ){	
				byte[] data = dataInfo.pixels;
				
				Log.d(null, "format:"+previewFormat+" ; width: "+previewWidth+" ; height: "+previewHeight);
				Log.d(null, "run data length:"+data.length);
				YuvImage im = new YuvImage(data, previewFormat, previewWidth, previewHeight, null);
				Rect rect = new Rect(0, 0, previewWidth, previewHeight);
		        	ByteArrayOutputStream myBaos = new ByteArrayOutputStream();
		    		im.compressToJpeg(rect, 80,  myBaos);
		    		
		    		byte[] jdata = myBaos.toByteArray();
		    		BitmapFactory.Options options=new BitmapFactory.Options();
	    			options.inSampleSize = 4;
	    			Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, options);
	    			
	    			// rotate
	        		//Matrix matrix = new Matrix();
	        		//matrix.postRotate(90);
	        		//bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	        		
	        		//Log.d(null, "after subsample width: "+bmp.getWidth()+" ; height: "+bmp.getHeight());
	        		
	        		//bmp to byte array
	        		//int bytes = bmp.getByteCount();
	    			//ByteBuffer bBuffer = ByteBuffer.allocate(bytes);
	    			//bmp.copyPixelsToBuffer(bBuffer);
	    			//byte[] bPixels = bBuffer.array();
	    
	        		ByteArrayOutputStream finalOut = new ByteArrayOutputStream();
	        		bmp.compress(Bitmap.CompressFormat.JPEG, 80, finalOut);
	        		byte[] bPixel = finalOut.toByteArray();
	        		
	        		bmp.recycle();
	    			
	    			try{
	    				//Log.d(null, "after subsample width: "+bmp.getWidth()+" ; height: "+bmp.getHeight() + "bPixels length:"+bPixels.length);
	    				Log.d(null, "bPixel length:"+bPixel.length);
	    				String addr = "192.168.0.103";
	    				int port = 8000;
	    				InetAddress intAddr =  InetAddress.getByName(addr);
	    				
	    				DatagramPacket headerPack = UdpClient.getPacketByMsg(addr, port, dataInfo.gravity[0]+","+dataInfo.gravity[1]+","+dataInfo.gravity[2]);
	    				ds.send(headerPack);
	    				
	    				DatagramPacket imgPack = UdpClient.getPacketByBytes(addr, port, bPixel);
	    			        ds.send(imgPack);
	    			        
	    			}catch(Exception e){
	    				Log.d(null, "Send udp packet failed. msg:"+e.getMessage());
	    			}
	    			/*String lengthContent = "<"+bmp.getWidth()+","+bmp.getHeight();
	    			UdpClient lenSender = new UdpClient("140.120.14.52", 8000, lengthContent);
	    			lenSender.start(); 
	    			while(lenSender.isEnd == false){}
	    			//等到長度送出後 才送圖
	    			UdpClient imgSender = new UdpClient("140.120.14.52", 8000, bPixels);
	    			imgSender.start();
	    			
	    			while(imgSender.isEnd == false){
	    				try{
	    					sleep(10);
	    				}catch(Exception e){}
	    			}*/
	    			//傳送結束
	    			
	    			finishedCounter++;
	    			Log.d(null, "finished number:"+finishedCounter);
			}
		}
	}
	
	public void end(){
		EndThread = true;
		ds.close();
	}
	
	public static ImgInfo pull(){
		Log.d(null, "pull()");
		ImgInfo result = null;
		
		if( firstOne != -1 ){
			result = ImgUDPProcessor.dataQueue[firstOne];
			ImgUDPProcessor.dataQueue[firstOne] = null;
			
			//get the next first one
			for(int i = firstOne ; i < qLength ; i++){
				if( ImgUDPProcessor.dataQueue[i] != null ){
					firstOne = i;
					return result;
				}
			}
			
			//get the next first one from the begin
			for(int i = 0 ; i < firstOne ; i++){
				if( ImgUDPProcessor.dataQueue[i] != null ){
					firstOne = i;
					return result;
				}
			}
			
			//the queue is empty after this
			firstOne = -1;
			return result;
		}else{
			//The queue is empty now
			return null;
		}
	}
	
	public static boolean insert(ImgInfo data){
		boolean isSet = false;
		
		// to find an empty place in the Queue
		for(int i = lastOne; i < qLength ; i++){
			if( ImgUDPProcessor.dataQueue[i] == null ){
				isSet = true;
				ImgUDPProcessor.dataQueue[i] = data;
				lastOne = i;
				
				if(firstOne == -1){
					firstOne = lastOne;
				}
				
				break;
			}
		}
		
		if(isSet){
			return true;
		}
		
		//no empty position at the back, find an empty one all over
		for(int i = 0; i < lastOne ; i++){
			if( ImgUDPProcessor.dataQueue[i] == null ){
				isSet = true;
				ImgUDPProcessor.dataQueue[i] = data;
				ImgUDPProcessor.lastOne = i;
				
				if(firstOne == -1){
					firstOne = lastOne;
				}
				
				return true;
			}
		}
		
		//The queue is full
		return false;
	}
}
