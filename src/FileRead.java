import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class FileRead {

	public static HashMap<String,Integer> nameToInt = new HashMap<String,Integer>();
	public static HashMap<Integer,String> intToName = new HashMap<Integer,String>();
	public static HashMap<String,String> xToName = new HashMap<String,String>();
	public static HashMap<String,String> nameToX = new HashMap<String,String>();
	public static HashMap<Integer,String> intToX = new HashMap<Integer,String>();

	public static ArrayList<Integer> Node_int = new ArrayList<Integer>();

	public static String FileName = "";

	public static ArrayList<double[]> file_reader() {
		ArrayList<double[]> lastdata = new ArrayList<double[]>();

		try {
			File file = new File(Filemaker.Path + "\\DATA\\"+FileName+".txt");
			//            File file = new File("C:\\Users\\eimiy\\Documents\\CPLEX_TEST\\test2.txt");

			HashSet<String> firstdata = new HashSet<String>();
			ArrayList<String[]> splitdata = new ArrayList<String[]>();
			HashSet<String> hs1 = new HashSet<String>();

			if (!file.exists()) {
				System.out.print("ファイルが開けません");
			}

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String data;
			while ((data = bufferedReader.readLine()) != null) {
				firstdata.add(data);
			}

			//network data を splitしてsplitdata(string型)に入れる
			//            for(int i = 0; i < firstdata.size(); i++) {
			//            	String[] pair = firstdata.get(i).split("		");
			//            	splitdata.add(pair.clone());
			//            }
			for(String edge : firstdata) {
				String[] pair = edge.split("\t");
				splitdata.add(pair.clone());
			}
			//System.out.println(splitdata.get(0));
			//hs1:重複なしの全頂点集合(String型)
			for(int i = 0; i < splitdata.size(); i++) {
				hs1.add(splitdata.get(i)[0]);
				hs1.add(splitdata.get(i)[1]);
			}

			//全頂点集合に対してx1,x2,x3...と名前を付ける
			String[] x_array = new String[hs1.size()];
			for(int i = 0; i < x_array.length; i++){
				x_array[i] = "x" + (i+1);
			}

			Iterator<String> iterator1 = hs1.iterator();
			Iterator<String> iterator2 = hs1.iterator();
			Iterator<String> iterator3 = hs1.iterator();
			Iterator<String> iterator4 = hs1.iterator();

			//index_1:(元の頂点名,int型のindex)
			//index_2:(int型のindex,元の頂点名)
			//index_3:(x,元の頂点名)
			//index_4:(元の頂点名,x)
			//index_5:(int型のindex,x)
			for(int i = 0; i < hs1.size(); i++) {
				nameToInt.put(iterator1.next(), i);
				intToName.put(i, iterator2.next());
				xToName.put(x_array[i], iterator3.next());
				nameToX.put(iterator4.next(), x_array[i] );
				intToX.put(i, x_array[i]);
			}

			//lastdata:(int型のindex,int型のindex,失敗確率)→ネットワークデータのint型化が完了
			double[] pair2=new double[2];
			for(int i = 0; i < splitdata.size(); i++) {
				pair2[0]=nameToInt.get(splitdata.get(i)[0]);
				pair2[1]=nameToInt.get(splitdata.get(i)[1]);
				//pair2[2]=Double.parseDouble(splitdata.get(i)[2]);

				lastdata.add(pair2.clone());
			}
			fileReader.close();

			//全頂点集合（int）を作成
			ArrayList<String> Node_String = new ArrayList<String>(hs1);
			Node_int = fromNameToInt(Node_String);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastdata;
	}

	public static int file_size_count(){
		return nameToInt.size();
	}

	//xに対する元の頂点名を返す(旧名:result_return)
	public static ArrayList<String> fromXtoName(ArrayList<String> xIndexes){
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < xIndexes.size();i++){
			names.add(xToName.get(xIndexes.get(i)));
		}
		return names;
	}

	//元の頂点名に対するxを返す(旧名:PrepMDS_Xindex_return)
	public static ArrayList<String> fromNameToX(ArrayList<String> names){
		ArrayList<String> xIndexes = new ArrayList<String>();
		for(int i = 0; i < names.size();i++){
			xIndexes.add(nameToX.get(names.get(i)));
		}
		return xIndexes;
	}

	//元の頂点名に対するint型のindexを返す(旧名:MDS_return)
	public static ArrayList<Integer> fromNameToInt(ArrayList<String> names){
		ArrayList<Integer> intIndexes = new ArrayList<Integer>();
		for(int i = 0; i < names.size();i++){
			intIndexes.add(nameToInt.get(names.get(i)));
		}
		return intIndexes;
	}

	//int型のindexに対する元の頂点名を返す(旧名:PrepMDS_StringName_return)
	public static ArrayList<String> fromIntToName(ArrayList<Integer> intIndexes){
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < intIndexes.size();i++){
			names.add(intToName.get(intIndexes.get(i)));
		}
		return names;
	}

	//int型のindexに対するxを返す(旧名:PrepMDS_StringName_return)
	public static ArrayList<String> fromIntToX(ArrayList<Integer> intIndexes){
		ArrayList<String> xIndexes = new ArrayList<String>();
		for(int i = 0; i < intIndexes.size();i++){
			xIndexes.add(intToX.get(intIndexes.get(i)));
		}
		return xIndexes;
	}
}
