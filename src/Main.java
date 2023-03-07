import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {

		    Scanner scanner = new Scanner(System.in);

		    System.out.println("Please enter DATA folder path.");
		    Filemaker.Path = scanner.next();

		    if (!Filemaker.Path.endsWith("DATA")) {
		    	System.out.println("folder is must be ended with \"DATA\"");
		    	System.exit(0);
		    }
		    
			Filemaker.Path = Filemaker.Path.split("\\\\DATA")[0].replace("\\", "\\\\");

			File resultFolder = new File(Filemaker.Path + "\\RESULT");
			File ilpFolder = new File(Filemaker.Path + "\\ILP");
			if (resultFolder.exists() == false) resultFolder.mkdir();
			if (ilpFolder.exists() == false) ilpFolder.mkdir();

			File[] files = new File(Filemaker.Path + "\\DATA").listFiles();

			for(File file : files) {
				FileRead.FileName = file.getName().split(".txt")[0];

				//計算時間出力用変数
				//ArrayList<Long> Time = new ArrayList<Long>();
				File folderForResult = new File(Filemaker.Path + "\\RESULT\\"+FileRead.FileName);
				if (folderForResult.exists() == false) folderForResult.mkdir();
				File folderForIlp = new File(Filemaker.Path + "\\ILP\\"+FileRead.FileName);
				if (folderForIlp.exists() == false) folderForIlp.mkdir();

				File timeResultFile = new File(folderForResult.getAbsolutePath()+"\\"+FileRead.FileName+"_TimeResult.txt");
				timeResultFile.createNewFile();

				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(timeResultFile)));

				pw.println("K	Total Time(s)	Writing Time(s)	Solving time(s)");
				//				pw.println("\\Network name: "+FileRead.FileName+".txt");
				//				pw.println("処理時間：" + (endTime - startTime) + " ms");
				pw.close();

				/*
				 * Hamming Distanceを自動で更新するようにする
				 * スタートは|Intermittent|/2で設定する
				 * スコアの変動が小さくなったらストップ(条件未定)
				 */
				Clear.clear();

				System.out.println("----START----");
				System.out.println("NetworkName : "+FileRead.FileName);
				for(int i=0; i<10000; i++) {
					ILPSolver.solfile_not_found = false;
					Algorithm.hamming_count = i;
//					Clear.clear();
//					Filemaker.FileMake(i);
					Algorithm.main_Algorithm(i);
					System.out.println("処理時間：" + Algorithm.totalTime + " s");
					//Time.add((endTime - startTime)/1000);
					//File ResultFile = new File(Filemaker.Path + "\\RESULT\\test\\" +FileRead.FileName+"_TimeResult.txt");
					//ResultFile.createNewFile();
					pw = new PrintWriter(new BufferedWriter(new FileWriter(timeResultFile,true)));
					pw.println(Algorithm.hamming_k+"\t"+Algorithm.totalTime+"\t"+FileWrite.writingTime+"\t"+ILPSolver.solvingTime);
					//				pw.println("\\Network name: "+FileRead.FileName+".txt");
					//				pw.println("処理時間：" + (endTime - startTime) + " ms");
					pw.close();
					//Criticalityが各頂点について0.05未満の変動になったら終了
//					if(Algorithm.STOP == 1 && (endTime - startTime)>600000) {
//						System.out.println("スコアの変動が小さくなったのでストップします。");
//						break;
//					}
					if(Algorithm.STOP == 1) {
						System.out.println("条件を満たしたので終了します");
						break;
					}
					if(Algorithm.hamming_k<=1) {
						System.out.println("K=1になったので終了します");
						break;
					}

					System.out.println("*".repeat(100));
					//Algorithm.hamming_k--;
				}
				Algorithm.hamming_k=0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
