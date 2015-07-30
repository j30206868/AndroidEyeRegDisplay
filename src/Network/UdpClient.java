package Network;
import java.io.*;
import java.net.*;

import android.util.Log;
 
// 1. 本程式必須與 UdpServer.java 程式搭配執行，先執行 UdpServer 再執行本程式。
// 2. 本程式必須有一個參數，指定伺服器的 IP。
// 用法範例： java UdpClient 127.0.0.1
 
public class UdpClient extends Thread {
    int port;            // port : 連接埠
    InetAddress server; // InetAddress 是 IP, 此處的 server 指的是伺服器 IP
    byte buffer[];            // 欲傳送的訊息，每個 UdpClient 只能傳送一個訊息。
    public boolean isEnd;
   
    //public static void main(String args[]) throws Exception {
    //    for (int i=0; i<100; i++) {
            // 建立 UdpClient，設定傳送對象與傳送訊息。
    //        UdpClient client = new UdpClient(args[0], 8080, "UdpClient : "+i+"th message");
    //        client.run(); // 啟動 UdpClient 開始傳送。
    //    }
    //}
 
    public UdpClient(String pServer, int pPort, String pMsg) {
	        port = pPort;                             // 設定連接埠
	        try{
	        	server = InetAddress.getByName(pServer); // 將伺服器網址轉換為 IP。
	        }catch(Exception e){
	        	Log.d(null, "UdpClient Constrcut error! msg:"+e.getMessage());
	        }
	        buffer = pMsg.getBytes();                                 // 設定傳送訊息。
	        isEnd = false;
    }
    public UdpClient(String pServer, int pPort, byte bytes[]) {
	        port = pPort;                             // 設定連接埠
	        try{
	        	server = InetAddress.getByName(pServer); // 將伺服器網址轉換為 IP。
	        }catch(Exception e){
	        	Log.d(null, "UdpClient Constrcut error! msg:"+e.getMessage());
	        }
	        buffer = bytes;                                 // 設定傳送訊息。
	        isEnd = false;
    }
 
    public void run() {
		try {
		        // 封裝該位元串成為封包 DatagramPacket，同時指定傳送對象。
		        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, port); 
		        DatagramSocket socket = new DatagramSocket();    // 建立傳送的 UDP Socket。
		        socket.send(packet);                             // 傳送
		        socket.close();                                 // 關閉 UDP socket.
	     	} catch (Exception e) { 
	     		e.printStackTrace(); 
	     	}    // 若有錯誤產生，列印函數呼叫堆疊。
		isEnd = true;
    }
    
    public static DatagramPacket getPacketByMsg(String addr, int port, String msg) throws Exception{
	    byte bytes[] = msg.getBytes();
	    return new DatagramPacket(bytes, bytes.length, InetAddress.getByName(addr), port);
    }
    public static DatagramPacket getPacketByBytes(String addr, int port, byte bytes[]) throws Exception{
	    return new DatagramPacket(bytes, bytes.length, InetAddress.getByName(addr), port);
    }
}