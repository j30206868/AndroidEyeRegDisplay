package Network;
import java.io.*;
import java.net.*;

import android.util.Log;
 
// 1. ���{�������P UdpServer.java �{���f�t����A������ UdpServer �A���楻�{���C
// 2. ���{���������@�ӰѼơA���w���A���� IP�C
// �Ϊk�d�ҡG java UdpClient 127.0.0.1
 
public class UdpClient extends Thread {
    int port;            // port : �s����
    InetAddress server; // InetAddress �O IP, ���B�� server �����O���A�� IP
    byte buffer[];            // ���ǰe���T���A�C�� UdpClient �u��ǰe�@�ӰT���C
    public boolean isEnd;
   
    //public static void main(String args[]) throws Exception {
    //    for (int i=0; i<100; i++) {
            // �إ� UdpClient�A�]�w�ǰe��H�P�ǰe�T���C
    //        UdpClient client = new UdpClient(args[0], 8080, "UdpClient : "+i+"th message");
    //        client.run(); // �Ұ� UdpClient �}�l�ǰe�C
    //    }
    //}
 
    public UdpClient(String pServer, int pPort, String pMsg) {
	        port = pPort;                             // �]�w�s����
	        try{
	        	server = InetAddress.getByName(pServer); // �N���A�����}�ഫ�� IP�C
	        }catch(Exception e){
	        	Log.d(null, "UdpClient Constrcut error! msg:"+e.getMessage());
	        }
	        buffer = pMsg.getBytes();                                 // �]�w�ǰe�T���C
	        isEnd = false;
    }
    public UdpClient(String pServer, int pPort, byte bytes[]) {
	        port = pPort;                             // �]�w�s����
	        try{
	        	server = InetAddress.getByName(pServer); // �N���A�����}�ഫ�� IP�C
	        }catch(Exception e){
	        	Log.d(null, "UdpClient Constrcut error! msg:"+e.getMessage());
	        }
	        buffer = bytes;                                 // �]�w�ǰe�T���C
	        isEnd = false;
    }
 
    public void run() {
		try {
		        // �ʸ˸Ӧ줸�ꦨ���ʥ] DatagramPacket�A�P�ɫ��w�ǰe��H�C
		        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, port); 
		        DatagramSocket socket = new DatagramSocket();    // �إ߶ǰe�� UDP Socket�C
		        socket.send(packet);                             // �ǰe
		        socket.close();                                 // ���� UDP socket.
	     	} catch (Exception e) { 
	     		e.printStackTrace(); 
	     	}    // �Y�����~���͡A�C�L��ƩI�s���|�C
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