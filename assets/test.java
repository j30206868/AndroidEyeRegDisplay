import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class test{

	public static void main(String args[]){
		FloatBuffer mVertaxBuff;
		FloatBuffer mColorBuff;

		try{
			ArrayList<Float> vArray = new ArrayList<Float>();
			ArrayList<Float> cArray = new ArrayList<Float>();
    		ArrayList<Float> vArrayResult = new ArrayList<Float>();

    		Integer vC = 0;
    		Integer vtC = 0;
    		Integer vnC = 0;
    		Integer fC = 0;
    		Integer gC = 0;
    		Integer sC = 0;

    		Integer max = 0;

			FileReader fr = new FileReader("Combat_Android.obj");
			BufferedReader reader = new BufferedReader(fr);
			String newLine = "";
			while( (newLine = reader.readLine()) != null ){
				
				if( newLine.length() < 2){
					continue;
				}

				String blocks[] = newLine.split("[ ]+");

				//check first word
				if( blocks[0].trim().equals("v") ){// v
					vC++;

					vArray.add(Float.parseFloat(blocks[1]));
					vArray.add(Float.parseFloat(blocks[2]));
					vArray.add(Float.parseFloat(blocks[3]));

				}else if( blocks[0].trim().equals("vt") ){// vt
					vtC++;	
									
				}else if( blocks[0].trim().equals("vn") ){// vn
					vnC++;
					
				}else if( blocks[0].trim().equals("f") ){
					fC++;

					//  first second third
					//f 1//1  2//2   3//3
					String indexA[][] = new String[3][];
					indexA[0] = blocks[1].split("/");
					indexA[1] = blocks[2].split("/");
					indexA[2] = blocks[3].split("/");

					//get three triangles of a face
					for(int i=0 ; i < indexA.length ; i++){
						//add one triangle (3 coordinates)
						int index = Integer.parseInt(indexA[i][0])-1;
						vArrayResult.add( vArray.get(index*3    ) );
						vArrayResult.add( vArray.get(index*3 + 1) );
						vArrayResult.add( vArray.get(index*3 + 2) );

						//set colors
						cArray.add(0.7f);
						cArray.add(0.7f);
						cArray.add(0.7f);
						cArray.add(1.0f);

						if(index > max){
							max = index;
						}
					}

				}else if( blocks[0].trim().equals("g") ){
					gC++;
				
				}else if( blocks[0].trim().equals("s") ){
					sC++;
					
				}

			}
			System.out.println("vC = "+vC);
			System.out.println("vtC = "+vtC);
			System.out.println("vnC = "+vnC);
			System.out.println("fC = "+fC);
			System.out.println("gC = "+gC);
			System.out.println("sC = "+sC);
			System.out.println("max = "+max);

			float vResult[] = new float[vArrayResult.size()];
			for(int i=0 ; i<vArrayResult.size() ; i++){
				vResult[i] = vArrayResult.get(i);
			}
			float cResult[] = new float[cArray.size()];
			for(int i=0 ; i<cArray.size() ; i++){
				cResult[i] = cArray.get(i);
			}

			System.out.println("last = "+cResult[vArray.size()-1]);

			System.out.println("v l = "+vArrayResult.size());
			System.out.println("c l = "+cArray.size());

			ByteBuffer vBuff = ByteBuffer.allocateDirect( vArrayResult.size() * 4 );
			vBuff.order(ByteOrder.nativeOrder());
			mVertaxBuff = vBuff.asFloatBuffer();
			mVertaxBuff.put(vResult);
			mVertaxBuff.position(0);

			ByteBuffer cBuff = ByteBuffer.allocateDirect( cArray.size() * 4 );
			cBuff.order(ByteOrder.nativeOrder());
			mColorBuff = cBuff.asFloatBuffer();
			mColorBuff.put(cResult);
			mColorBuff.position(0);

		}catch(Exception ioe){
			System.out.println("Load model error: "+ioe.getMessage());
		}
	}
}