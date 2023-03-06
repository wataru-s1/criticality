import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ILPResult {
	public static int m = 0;
	public static Map<String, Integer> result = new HashMap<>();

	public static ArrayList<String> ILP_get_result(int Count){

//		File solfile = new File("C:\\Users\\eimiy\\Documents\\CPLEX_TEST\\sol_test1.sol");
		File solfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + Count + ".sol");
		//result: x1 → 1 or 0 のデータ
		result = ILPSolver.getResult(solfile);
		ArrayList<String> A = new ArrayList<String>();

		for(Map.Entry<String, Integer> entry : result.entrySet()){
			A.add(entry.getKey());
		}

		ArrayList<String> B = new ArrayList<String>();
		//xに対する元の頂点名を取得
		B = FileRead.fromXtoName(A);

		int i = 0;
		//int m = 0;
		ArrayList<String> MDS = new ArrayList<String>();

		//MDSを元の頂点名で作る
		for(Map.Entry<String, Integer> entry : result.entrySet()){
			if(entry.getValue() == 1) {
				//m++;
				MDS.add(B.get(i));
			}
			//System.out.println(B.get(i) + ":" + entry.getValue());
			i++;
		}

		return MDS;
	}


	public static ArrayList<String> Nodes(){

//		File solfile = new File("C:\\Users\\eimiy\\Documents\\CPLEX_TEST\\sol_test1.sol");
		ArrayList<String> A = new ArrayList<String>();

		for(Map.Entry<String, Integer> entry : result.entrySet()){
			A.add(entry.getKey());
		}

		ArrayList<String> B = new ArrayList<String>();
		B = FileRead.fromXtoName(A);

		int i = 0;
		//int m = 0;
		ArrayList<String> Node = new ArrayList<String>();

		for(Map.Entry<String, Integer> entry : result.entrySet()){

			Node.add(B.get(i));

			i++;
		}
		//System.out.println("PMDS = " + Critical_PMDS_FileReader.MDS_return(MDS));

		//System.out.println("|PMDS| = "+MDS.size());

		return Node;
	}

	public static ArrayList<String> MDS_x_index() {

		ArrayList<String> MDS_x_Index = new ArrayList<String>();

		for(Map.Entry<String, Integer> entry : result.entrySet()){
			if(entry.getValue() == 1) {
				MDS_x_Index.add(entry.getKey());

			}
		}
		return MDS_x_Index;
	}

	public static ArrayList<String> NonMDS_x_index() {

		ArrayList<String> NonMDS_x_Index = new ArrayList<String>();

		for(Map.Entry<String, Integer> entry : result.entrySet()){
			if(entry.getValue() == 0) {
				NonMDS_x_Index.add(entry.getKey());
			}
		}
		return NonMDS_x_Index;
	}
}
