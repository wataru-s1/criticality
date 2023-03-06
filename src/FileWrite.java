import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWrite {

	public static int counter = 0;
	public static double writingTime = 0.0;
	public static ArrayList<Double> timeList = new ArrayList<Double>();

	public static void filewrite(ArrayList<String> C, int Count){
		try{
			File lpfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\dom_test1_" + Count + ".lp");
			File solfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + Count + ".sol");
			solfile.createNewFile();

			if (checkBeforeWritefile(lpfile)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(lpfile)));
				String str = "";
				pw.println("\\Problem name: dom_test1.lp");
				pw.println("Minimize");
				for(int i = 0; i < (C.size()-1); i++){
					str += " x" + (i+1) + " +" ;
				}
				str += " x" + (C.size()) ;
				pw.println(str);
				pw.println("Subject To");
				for(int i = 0; i < C.size(); i++){
					pw.println(C.get(i) + " >= "+ 1);
				}

				pw.println("Binaries");
				for(int i = 1; i <= C.size(); i++) {
					pw.println(" x" + i);
				}

				pw.println("END");
				pw.close();

				ILPSolver.createCommandFile(lpfile,solfile);
			}else{
				System.out.println("***");
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	public static void filewrite_CMDS(ArrayList<String> C, int Count){
		try{
			File lpfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\dom_test1_" + Count + ".lp");
			File solfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + Count + ".sol");

			if (checkBeforeWritefile(lpfile)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(lpfile)));
				String str = "";
				pw.println("\\Problem name: dom_test1.lp");
				pw.println("Minimize");
				for(int i = 0; i < (Algorithm.Adjacency_matrix.length-1); i++){
					str += " x" + (i+1) + " +" ;
				}
				str += " x" + (Algorithm.Adjacency_matrix.length) ;
				pw.println(str);
				pw.println("Subject To");
				for(int i = 0; i < Algorithm.Adjacency_matrix.length; i++){
					pw.println(C.get(i) + " >= 1");
				}
				pw.println(C.get(Algorithm.Adjacency_matrix.length));

				pw.println("Binaries");
				for(int i = 1; i <= Algorithm.Adjacency_matrix.length; i++) {
					pw.println(" x" + i);
				}
				pw.println("END");
				pw.close();
				//counter++;

				ILPSolver.createCommandFile(lpfile,solfile);
			}else{
				System.out.println("****");
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	public static void filewrite_MDSLoop(ArrayList<String> C, int Count){
		try{
			File lpfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\dom_test1_" + Count + ".lp");
			File solfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + Count + ".sol");

			long writingStart = System.currentTimeMillis();
			if (checkBeforeWritefile(lpfile)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(lpfile)));
				String str = "";

				pw.println("\\Problem name: dom_test1.lp");
				pw.println("Minimize");

				for(int i = 1; i < (Algorithm.Adjacency_matrix.length); i++){
					str += " x" + i + " +" ;
				}
				str += " x" + (Algorithm.Adjacency_matrix.length) ;
				pw.println(str);

				pw.println("Subject To");
				for(int i = 0; i < C.size(); i++){
					pw.println(C.get(i));
				}
				//pw.println(C.get(Algorithm.Adjacency_matrix.length));

				pw.println("Binaries");
				for(int i = 1; i <= Algorithm.Adjacency_matrix.length; i++) {
					pw.println(" x" + i);
				}

				pw.println("END");
				pw.close();
				//counter++;

				ILPSolver.createCommandFile(lpfile,solfile);
			}else{
				System.out.println("****");
			}
			long writingEnd = System.currentTimeMillis();
			timeList.add((double)(writingEnd-writingStart)/1000.0);
		}catch(IOException e){
			System.out.println(e);
		}
	}

	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}
		return false;
	}
}
