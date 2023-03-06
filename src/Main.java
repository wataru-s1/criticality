import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		try {

//			エッジファイルをDATAという名前のディレクトリにおいてください。
//			Filemaker.PathにはDATAの親ディレクトリのPATHを指定してください。
//			Filemakderで指定したディレクトリ内のRESULTとILPという名前のディレクトリに結果が出力されます。
//			該当箇所にRESULTとILPディレクトリがない場合は自動で作成されます。
//			すでに同名のディレクトリが存在する場合は、その中に結果が作成されるので注意してください。
			Filemaker.Path = "C:\\Users\\Someya Wataru\\Documents\\MDS\\LP_test";

			File resultFolder = new File(Filemaker.Path + "\\RESULT");
			File ilpFolder = new File(Filemaker.Path + "\\ILP");
			if (resultFolder.exists() == false) resultFolder.mkdir();
			if (ilpFolder.exists() == false) ilpFolder.mkdir();

			File[] files2 = {
//					"pathway_RTK_test_final"
//					"cell2cell"
//					"net_500_2.5_1_499_2_0_directed_edgelist",
//					"net_500_2.5_1_499_2_1_directed_edgelist",
//					"net_500_2.5_1_499_2_2_directed_edgelist",
//					"net_500_2.5_1_499_2_3_directed_edgelist",
//					"net_500_2.5_1_499_2_4_directed_edgelist",
//					"undirected_scalefree_net_1"
//					"directed_random_net_1"
//					"directed_scalefree_net_1"
			};

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
