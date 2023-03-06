import java.util.ArrayList;
import java.util.HashSet;
public class Matrix {

	//前処理Critical
	public static ArrayList<String> PrepCritical_x = new ArrayList<String>();
	//前処理Redundant
	public static ArrayList<String> PrepRedundant_x = new ArrayList<String>();

	public static double[][] Adjacency_matrix_maker(){
		ArrayList<double[]> input_data = new ArrayList<double[]>();
		input_data = FileRead.file_reader();
		int Matrix_size = FileRead.file_size_count();
		double Ad_Matrix[][] = new double[Matrix_size][Matrix_size];
//		for(int i = 0; i < Matrix_size; i++){
//			for(int s = 0; s < Matrix_size; s++){
//				Ad_Matrix[i][s] = 0;
//			}
//		}
		for(int i = 0; i < input_data.size(); i++){
			Ad_Matrix[(int)input_data.get(i)[0]][(int)input_data.get(i)[1]] = 1;
			//有向グラフでは下の行を削除
			//Ad_Matrix[(int)input_data.get(i)[1]][(int)input_data.get(i)[0]] = 1;
		}
		return Ad_Matrix;
	}

	public static void Ad_Matrix(int Count) {

//		前処理の結果を加えてPMDSを求める

//		前処理Critical
		PrepCritical_x = FileRead.fromIntToX(Preprocessing.PrepCritical_int);
//		前処理Redundant
		PrepRedundant_x = FileRead.fromIntToX(Preprocessing.PrepRedundant_int);

		ArrayList<String> C = new ArrayList<String>();

		String[] contents = new String[Algorithm.Adjacency_matrix.length];
		for(int i = 0; i < Algorithm.Adjacency_matrix.length; i++){
			contents[i] = " x" + (i+1) ;
			for(int t = 0; t < Algorithm.Adjacency_matrix.length; t++){
				if(Algorithm.Adjacency_matrix[t][i] == 1) {
					contents[i] += " + x" + (t+1);
				}
			}
		}
		for(int i = 0; i < contents.length; i++) {
			C.add(contents[i]);
		}
		FileWrite.filewrite(C, Count);
	}

	public static void CMD_Ad_Matrix(String m_v, int Count) {

		ArrayList<String> C = new ArrayList<String>();
		String[] contents = new String[Algorithm.Adjacency_matrix.length];
		for(int i = 0; i < Algorithm.Adjacency_matrix.length; i++){
			contents[i] = " x" + (i+1) ;
			for(int t = 0; t < Algorithm.Adjacency_matrix.length; t++){
				if(Algorithm.Adjacency_matrix[t][i] == 1) {
					contents[i] += " + x" + (t+1);
				}
			}
		}
		for(int i = 0; i < contents.length; i++) {
			C.add(contents[i]);
		}
		C.add(m_v + " = 0");
		FileWrite.filewrite_CMDS(C, Count);
	}

	public static void RMD_Ad_Matrix(String m_v, int Count) {


		ArrayList<String> C = new ArrayList<String>();
		String[] contents = new String[Algorithm.Adjacency_matrix.length];
		for(int i = 0; i < Algorithm.Adjacency_matrix.length; i++){
			contents[i] = " x" + (i+1) ;
			for(int t = 0; t < Algorithm.Adjacency_matrix.length; t++){
				if(Algorithm.Adjacency_matrix[t][i] == 1) {
					contents[i] += " + x" + (t+1);
				}
			}

		}
		for(int i = 0; i < contents.length; i++) {
			C.add(contents[i]);
		}

		C.add(m_v + " = 1");

		FileWrite.filewrite_CMDS(C, Count);
	}

	public static void Loop_Ad_Matrix(HashSet<String> Constraint, ArrayList<String> Critical,ArrayList<String> Redundant,int M, HashSet<String> Hamming, int Count, double K) {

		//System.out.println("ここ");
		ArrayList<String> C = new ArrayList<String>();
		String[] contents = new String[Algorithm.Adjacency_matrix.length];

		String str = "";
        for(int i = 1; i < (Algorithm.Adjacency_matrix.length); i++){
        	str += " x" + (i) + " +" ;
        }
        str += " x" + (Algorithm.Adjacency_matrix.length) + " = "+ M;

        C.add(str);
		for(int i = 0; i < Algorithm.Adjacency_matrix.length; i++) {
			contents[i] = " x" + (i+1) ;
			for(int t = 0; t < Algorithm.Adjacency_matrix.length; t++){
				if(Algorithm.Adjacency_matrix[t][i] == 1) {
					contents[i] += " + x" + (t+1);
				}
			}
		}

		for(int i = 0; i < contents.length; i++) {
			C.add(contents[i] + " >= 1");
		}

		for(int i = 0; i < Critical.size(); i++) {
			C.add(" " + Critical.get(i) + " >= 1");
		}

		for(int i = 0; i < Redundant.size(); i++) {
			C.add(" " + Redundant.get(i) + " <= 0");
		}

//		for(String Const : Constraint) {
//			C.add(" " + Const);
//		}

		for(String Ham : Hamming) {
			C.add(Ham + " >= " + K);
		}
		FileWrite.filewrite_MDSLoop(C, Count);
	}
}
