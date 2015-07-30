package cwz.basicopengl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

import android.content.res.Resources;
import android.util.Log;
import demos.nehe.lesson33.TGALoader;
import demos.nehe.lesson33.Texture;

public class FileLoader {
	public static BufferedReader getBufferedReader(String fname, Resources res) throws IOException{
		InputStream is = res.getAssets().open(fname);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		
		return reader;
	}
	
	public static GameObject loadGameObjectFromFIle(String fname, Resources res, MySurfaceView MV){
		GameObject newGObj = null;
		
		ArrayList<Float> vArray = new ArrayList<Float>();
    		ArrayList<Float> vArrayResult = new ArrayList<Float>();
    		ArrayList<Float> cArray = new ArrayList<Float>();
    		
		try{
			BufferedReader reader = getBufferedReader(fname, res);
			String newLine = "";
				while( (newLine = reader.readLine()) != null ){
				
				if( newLine.length() < 2){
					continue;
				}

				String blocks[] = newLine.split("[ ]+");

				//check first word
				if( blocks[0].trim().equals("v") ){// v
					vArray.add(Float.parseFloat(blocks[1]));
					vArray.add(Float.parseFloat(blocks[2]));
					vArray.add(Float.parseFloat(blocks[3]));

				}else if( blocks[0].trim().equals("vt") ){// vt
									
				}else if( blocks[0].trim().equals("vn") ){// vn
					
				}else if( blocks[0].trim().equals("f") ){

					//  first second third
					//f 1//1  2//2   3//3
					String indexA[][] = new String[3][];
					indexA[0] = blocks[1].split("/"); // index of first triangle
					indexA[1] = blocks[2].split("/"); // index of second triangle
					indexA[2] = blocks[3].split("/"); // index of third triangle

					//get a triangle
					for(int i=0 ; i < indexA.length ; i++){
						//add one vertex (3 axis's coordinates)
						int index = Integer.parseInt(indexA[i][0])-1;
						vArrayResult.add( vArray.get(index*3    ) );
						vArrayResult.add( vArray.get(index*3 + 1) );
						vArrayResult.add( vArray.get(index*3 + 2) );

						//set colors
						cArray.add(0.7f);
						cArray.add(0.7f);
						cArray.add(0.7f);
						cArray.add(1.0f);
					}

				}else if( blocks[0].trim().equals("g") ){
				
				}else if( blocks[0].trim().equals("s") ){

				}
			}

			float vResult[] = new float[vArrayResult.size()];
			for(int i=0 ; i<vArrayResult.size() ; i++){
				vResult[i] = vArrayResult.get(i);
			}
			
			float cResult[] = new float[cArray.size()];
			for(int i=0 ; i<cArray.size() ; i++){
				cResult[i] = cArray.get(i);
			}

			newGObj = new GameObject(MV, vArrayResult.size()/3, vResult, cResult);
			
		}catch(Exception ioe){
			Log.e("FileLoader: loadModelFromObjFile", "Load model error: "+ioe.getMessage());
		}
		
		if( newGObj != null){
			return newGObj;
		}else{
			return null;
		}
	}

	public static CombatMan loadCombatManFromFIle(String fname, Resources res, MySurfaceView MV){
		CombatMan newGObj = null;
		
		ArrayList<Float> vArray = new ArrayList<Float>();
		ArrayList<Float> vArrayResult = new ArrayList<Float>();
    		ArrayList<Float> tArray = new ArrayList<Float>();
    		ArrayList<Float> tArrayResult = new ArrayList<Float>();
    		
		try{
			BufferedReader reader = getBufferedReader(fname, res);
			String newLine = "";
				while( (newLine = reader.readLine()) != null ){
				
				if( newLine.length() < 2){
					continue;
				}

				String blocks[] = newLine.split("[ ]+");

				//check first word
				if( blocks[0].trim().equals("v") ){// v
					vArray.add(Float.parseFloat(blocks[1]));
					vArray.add(Float.parseFloat(blocks[2]));
					vArray.add(Float.parseFloat(blocks[3]));

				}else if( blocks[0].trim().equals("vt") ){// vt
					tArray.add(Float.parseFloat(blocks[1]));
					tArray.add(Float.parseFloat(blocks[2]));
					
				}else if( blocks[0].trim().equals("vn") ){// vn
					
				}else if( blocks[0].trim().equals("f") ){

					//  first second third
					//f 1//1  2//2   3//3
					String indexA[][] = new String[3][];
					indexA[0] = blocks[1].split("/"); // index of first triangle
					indexA[1] = blocks[2].split("/"); // index of second triangle
					indexA[2] = blocks[3].split("/"); // index of third triangle

					//get a triangle
					for(int i=0 ; i < indexA.length ; i++){
						//add one vertex (3 axis's coordinates)
						int index = Integer.parseInt(indexA[i][0])-1;
						vArrayResult.add( vArray.get(index*3    ) );
						vArrayResult.add( vArray.get(index*3 + 1) );
						vArrayResult.add( vArray.get(index*3 + 2) );

						//add texture coords
						//tArray
						index = Integer.parseInt(indexA[i][1])-1;
						tArrayResult.add( tArray.get( index*2 ) );
						tArrayResult.add( tArray.get( index*2 + 1) );
					}

				}else if( blocks[0].trim().equals("g") ){
				
				}else if( blocks[0].trim().equals("s") ){

				}
			}

			float vResult[] = new float[vArrayResult.size()];
			for(int i=0 ; i<vArrayResult.size() ; i++){
				vResult[i] = vArrayResult.get(i);
			}
			
			float tResult[] = new float[tArrayResult.size()];
			for(int i=0 ; i<tArrayResult.size() ; i++){
				tResult[i] = tArrayResult.get(i);
			}

			newGObj = new CombatMan(MV, vArrayResult.size()/3, vResult, tResult);
			
		}catch(Exception ioe){
			Log.e("FileLoader: loadModelFromObjFile", "Load model error: "+ioe.getMessage());
		}
		
		if( newGObj != null){
			return newGObj;
		}else{
			return null;
		}
	}
	
	public static Texture[] loadMtl(String fname, Resources res){
		Texture textures[] = null;
		ArrayList<String> tStringInfo = new ArrayList<String>();

		int textureNum = 0;
		
		try
		{
			BufferedReader reader = FileLoader.getBufferedReader(fname, res);
			String newLine = "";
			
			while( (newLine = reader.readLine()) != null )
			{
				String blocks[] = newLine.split("[ ]+");
				
				if( blocks[0].trim().equals("newmtl") )
				{// a new mtl obj name
					textureNum++;
					tStringInfo.add(blocks[1].trim());
					
				}else if( blocks[0].trim().equals("map_Kd") )
				{// add tga file name
					tStringInfo.add(blocks[1].trim());
				}
			}
			
			if( tStringInfo.size() % 2 != 0 || (tStringInfo.size()/2) != textureNum)
			{ //wrong data format or data
				Log.e("FileLoader loadMtl", "Error: newMtlObj number and provided files number not matched. Mtl file may provide wrong information.");
				textures = null;
			}else
			{ // String info in pairs and match correct number
				
				textures =new Texture[1];
				// new textures
				String groupName = tStringInfo.get(0*2);		
				String tgaFileName = tStringInfo.get(0*2 + 1);
				
				Log.d("FileLoader loadMtl", "call loadTGA ; groupName: "+groupName);
				Log.d("FileLoader loadMtl", "call loadTGA ; tgaFileName: "+tgaFileName);
				textures[0] = new Texture();
				TGALoader.loadTGA(textures[0], tgaFileName, res);
				Log.d("FileLoader loadMtl", "after loadTGA");
				textures[0].setgName( groupName );
				Log.d("FileLoader loadMtl", "after setgName");
				
				/*textures =new Texture[textureNum];
				for(int i=0 ; i<textureNum ; i++)
				{// new textures
					String groupName = tStringInfo.get(i*2);		
					String tgaFileName = tStringInfo.get(i*2 + 1);
					
					Log.d("FileLoader loadMtl", "call loadTGA ; groupName: "+groupName);
					Log.d("FileLoader loadMtl", "call loadTGA ; tgaFileName: "+tgaFileName);
					textures[i] = new Texture();
					TGALoader.loadTGA(textures[i], tgaFileName, res);
					Log.d("FileLoader loadMtl", "after loadTGA");
					textures[i].setgName( groupName );
					Log.d("FileLoader loadMtl", "after setgName");
				}*/
			}
			
		}catch(Exception e){
			Log.e("FileLoader loadMtl", "detail: "+e.getLocalizedMessage()+" ; thrower: "+e.getCause().toString());
		}	
		
		return textures;
	}
}
