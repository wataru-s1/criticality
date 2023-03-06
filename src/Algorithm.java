import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Algorithm {

	public static double[][] Adjacency_matrix;

	/*
	 * ハミング距離計算用変数
	 */
	public static int type = 0;
	public static int hamming_k;
	public static int hamming_count;

	/*
	 * MDS情報格納用変数
	 */
	public static int M = 0;
	public static ArrayList<String> MDS = new ArrayList<String>();
	public static ArrayList<String> CMDS = new ArrayList<String>();
	public static ArrayList<String> RMDS = new ArrayList<String>();
	public static ArrayList<String> IMDS = new ArrayList<String>();
	public static ArrayList<String> MDS_next = new ArrayList<String>();
	public static ArrayList<String> mds_x_index = new ArrayList<String>();
	public static Map<String, Integer> result = new HashMap<>();

	/*
	 * ILP制約式格納用変数
	 */
	public static HashSet<String> Constraint = new HashSet<String>();
	public static HashSet<String> Hamming = new HashSet<String>();

	/*
	 * Criticality計算用変数
	 */
//	public static int MDSs = 0;
	public static ArrayList<ArrayList<String>> MDSset = new ArrayList<ArrayList<String>>();
	public static HashMap<String,String> Criticality = new HashMap<String,String>();
	public static HashMap<String,String> Criticality_1before = new HashMap<String,String>();

	public static int STOP = 0;

	/*
	 * 計算時間計測用変数
	 */
	public static double totalTime = 0.0;

	public static void main_Algorithm(int Count) {

//		ArrayList<String> Non_MDS = new ArrayList<String>();
//		ArrayList<String> Non_MDSandRedundant = new ArrayList<String>();
//		ArrayList<String> Nonmds_x_index = new ArrayList<String>();

//		result: x1 → 1 or 0 のデータ(Hamming距離で使う)

//		HashSet<String> Critical = new HashSet<String>(CMDS);
//		HashSet<String> Redundant = new HashSet<String>(RMDS);

		/*
		 * 各Kの段階で新たにMDSに参加したノードの集合
		 */
		Set<String> newNodeInMDS = new HashSet<>();

		if (Count == 0) {
			long start = System.currentTimeMillis();
			Filemaker.FileMake(Count);;
			File solfile = new File(Filemaker.Path + "\\ILP\\" + FileRead.FileName + "\\sol_test1_" + Count + ".sol");

			/*
			 * 隣接行列の作成
			 */
			Adjacency_matrix = Matrix.Adjacency_matrix_maker();

			/*
			 * 前処理
			 */
			long t3 = System.currentTimeMillis();
			Preprocessing.critical_processing();
			long t4 = System.currentTimeMillis();
			System.out.println((t4-t3)/1000.0);

			/*
			 * 最初のILP
			 */
			Matrix.Ad_Matrix(Count);
//
			MDS = ILPResult.ILP_get_result(Count);

			/*
			 * SampleファイルからのMDSの読み込み
			 */
//			File sample = new File(Filemaker.Path + "\\DATA\\mds_sample.txt");
//			try {
//				BufferedReader br = new BufferedReader(new FileReader(sample));
//				String line = br.readLine();
//				while (line != null) {
//					String[] data = line.split("\t");
//					line = br.readLine();
//					switch (data[0]) {
//					case "MDS" :
//						MDS.add(data[1]);
//						break;
//					case "MDS_x" :
//						mds_x_index.add(data[1]);
//						break;
//					case "CMDS" :
//						CMDS.add(data[1]);
//						break;
//					case "RMDS" :
//						RMDS.add(data[1]);
//						break;
//					case "IMDS" :
//						IMDS.add(data[1]);
//						break;
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			System.out.println("MDS.size=" + MDS.size());
//			mds_x_index0 = ILPResult.MDS_x_index();
			mds_x_index = ILPResult.MDS_x_index();

//			MDSs++;
			M = MDS.size();;

			/*
			 * IMDSのみでMDSを回す場合の処理
			 */
//			MDS.removeAll(CMDS);
//			mds_x_index.removeAll(FileRead.fromNameToX(CMDS));

//			result0 = ILPSolver.getResult(solfile);
			result = ILPSolver.getResult(solfile);

			/*
			 * 同上
			 */
//			for (int i=1; i<Adjacency_matrix.length+1; i++) {
//				int isMds = (mds_x_index.contains("x"+i)) ? 1 : 0;
//				result.put("x"+(i), isMds);
//			}

			ArrayList<String> noPrepCriticalMDS_x = new ArrayList<String>();
			ArrayList<String> noCategorized_x = new ArrayList<String>();
			ArrayList<String> noPrepCriticalMDS_name = new ArrayList<String>();
			ArrayList<String> noCategorized_name = new ArrayList<String>();

			//		エラー探し
			/*
			 * MDS-前処理Criticalを求める(Criticalになりうるノード)
			 */
			for(String a : mds_x_index){
				if(Matrix.PrepCritical_x.contains(a) != true){
					noPrepCriticalMDS_x.add(a);
				}
			}

			/*
			 * Node-MDS-前処理Redundantを求める(Redundantになりうるノード)
			 */
			ArrayList<String> Node_x = FileRead.fromIntToX(FileRead.Node_int);
			for(String a : Node_x){
				if(mds_x_index.contains(a) != true && Matrix.PrepRedundant_x.contains(a) != true){
					noCategorized_x.add(a);
				}
			}

			noPrepCriticalMDS_name = FileRead.fromXtoName(noPrepCriticalMDS_x);
			noCategorized_name = FileRead.fromXtoName(noCategorized_x);
//
//			System.out.println("前処理後にCriticalのためにILPを回す数 = "+MDS_Prep_CPMDS.size());

			/*
			 * Criticalを求める処理
			 */
			int M_next = 0;
			for(int i = 0; i < noPrepCriticalMDS_x.size(); i++) {
//				System.out.println(Critical_PMDS_FileWriter.counter);
				File_delete.file_delete(Count);
				Matrix.CMD_Ad_Matrix(noPrepCriticalMDS_x.get(i), Count);
				MDS_next = ILPResult.ILP_get_result(Count);
				M_next = MDS_next.size();

				if( ILPSolver.solfile_not_found == true) {
					CMDS.add(noPrepCriticalMDS_name.get(i));
					ILPSolver.solfile_not_found = false;
				}

				if(M_next > M) {
					CMDS.add(noPrepCriticalMDS_name.get(i));
				}
				MDS_next.clear();
			}

			/*
			 * Redundant_PMDSを求める処理
			 */
			for(int i = 0; i < noCategorized_x.size(); i++) {
				File_delete.file_delete(Count);
				Matrix.RMD_Ad_Matrix(noCategorized_x.get(i), Count);
				MDS_next = ILPResult.ILP_get_result(Count);
				M_next = MDS_next.size();

				if(M_next > M) {
					RMDS.add(noCategorized_name.get(i));
				}
				MDS_next.clear();
			}

			CMDS.addAll(FileRead.fromIntToName(Preprocessing.PrepCritical_int));
			RMDS.addAll(FileRead.fromIntToName(Preprocessing.PrepRedundant_int));

			System.out.println("Criticalの同定終了, |CMDS|=" + CMDS.size());
			System.out.println("Redundantの同定終了, |RMDS|=" + RMDS.size());

			/*
			 * Intermittentを求める処理
			 */
			ArrayList<String> Node_name = FileRead.fromIntToName(FileRead.Node_int);
			for(int s = 0; s < Node_name.size(); s++) {
				if(CMDS.contains(Node_name.get(s)) == false && RMDS.contains(Node_name.get(s)) == false ) {
					IMDS.add(Node_name.get(s));
				}
			}

			System.out.println("Intermittentの同定終了, |IMDS|=" + IMDS.size());
			System.out.print("最初のMDS: ");
			for (String x : MDS) {
				System.out.print(x + " ");
			}
			System.out.println("\n|MDS|=" + MDS.size());

			MDSset.add(MDS);
			newNodeInMDS.addAll(MDS);

			Stream.concat(CMDS.stream(), IMDS.stream())
				.forEach(x -> Criticality.put(x, null));

			/*
			 * テスト用にMDS, CMDS, RMDS, IMDSが書かれたファイルを作成します
			 */
//			File mdsSample = new File(Filemaker.Path + "\\DATA\\mds_sample.txt");
//			try {
//				if (mdsSample.exists() == false) mdsSample.createNewFile();
//				PrintWriter pwSample = new PrintWriter(new BufferedWriter(new FileWriter(mdsSample)));
//				for (String name : MDS) {
//					pwSample.println("MDS	" + name);
//				}
//				for (String x : mds_x_index) {
//					pwSample.println("MDS_x	" + x);
//				}
//				for (String name : CMDS) {
//					pwSample.println("CMDS	" + name);
//				}
//				for (String name : RMDS) {
//					pwSample.println("RMDS	" + name);
//				}
//				for (String name : IMDS) {
//					pwSample.println("IMDS	" + name);
//				}
//				System.out.println("mds_sample.txtを作成しました");
//				pwSample.close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			long end = System.currentTimeMillis();
			System.out.println((end-start)/1000.0);
		}

		if(hamming_count==0) {
			int restMDSsize = M-CMDS.size();
			hamming_k = Math.min(restMDSsize, IMDS.size()-restMDSsize);
			type = (hamming_k == restMDSsize) ? 0 : 1;
//			hamming_k = IMDS.size()/2;
//			hamming_k = 1;
		}else {
//			hamming_k = (type==0) ? hamming_k : hamming_k-1;
			hamming_k--;
		}

		double K = (type==0) ? hamming_k - hamming_count - CMDS.size():
		   2*hamming_k - MDS.size();
//
		Filemaker.FileMake(hamming_k);
		File_delete.file_delete(hamming_k);

		/*
		 * 前のMDSはダメという制約式をILPに追加して全てのMDSの組み合わせを調べる.
		 * CriticalとRedundantの情報もILPに追加することで無駄な計算を減らせる？
		 * 解が得られなくなったら終了
		 * */

		ArrayList<String> Int_Critical = FileRead.fromNameToX(CMDS);
		ArrayList<String> Int_Redundant = FileRead.fromNameToX(RMDS);

//		result = new HashMap<>(result0);
//		mds_x_index = (ArrayList<String>)mds_x_index0.clone();

		long totalStart = System.currentTimeMillis();

		for(;;) {
			ILPSolver.solfile_not_found = false;
//			-((|MDS|-|CMDS|)-hamming_k) + hamming_k - |CMDS| = 2*hamming_k - |MDS|
//			double K = (type==0) ? hamming_k - hamming_count - CMDS.size() :
//								   2*hamming_k - MDS.size() - CMDS.size();
			/*
			 * すでに計算したMDSとは違う解を求めるという条件式の追加
			 */
//			String m = "";
//			for(int i = 0; i < mds_x_index.size()-1; i++) {
//				m += mds_x_index.get(i) + " + " ;
//			}
//			m += mds_x_index.get(mds_x_index.size()-1) + " <= " + (MDS.size()-1);
//			Constraint.add(m);

			/*
			 * ハミング距離の制約式の追加
			 */
			String hamming = "";

			for(int i = 1; i < Adjacency_matrix.length+1; i++) {
//				if(IMDS.contains(FileRead.xToName.get("x"+i))) {
					if(result.get("x" + (i))==1) {
						//					K--;
						hamming += " - x" + (i);
					}else {
						hamming += " + x" + (i);
					}
//				}
			}

			Hamming.add(hamming);
			//System.out.println(hamming);

			Matrix.Loop_Ad_Matrix(Constraint, Int_Critical, Int_Redundant, M, Hamming, hamming_k, K);

			MDS_next = ILPResult.ILP_get_result(hamming_k);

			if( ILPSolver.solfile_not_found == true) {
				break;
			}

			/*
			 * 同定したMDSのxインデックスをファイルに出力
			 */
//			File mdsFolder = new File(Filemaker.Path + "\\RESULT\\" + FileRead.FileName +"\\MDS");
//			if(mdsFolder.exists() == false) mdsFolder.mkdir();
//			File file = new File(mdsFolder.getAbsolutePath() + "\\" + FileRead.FileName + "_K" + hamming_k + "_" + I +".txt");
//			try {
//				file.createNewFile();
//				BufferedWriter br = new BufferedWriter(new FileWriter(file));
//
//				List<String> MDS_next_x = FileRead.fromNameToX(MDS_next);
//				List<String> mdsList = MDS_next_x.stream()
//											.filter(x -> FileRead.fromNameToX(IMDS).contains(x))
//											.sorted()
//											.collect(Collectors.toList());
//				for (String mds : mdsList) {
//					br.write(mds);
//					br.newLine();
//				}
//				br.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			newNodeInMDS.addAll(MDS_next);
			MDSset.add(MDS_next);

//			System.out.print("MDS: ");
//			for (String x : MDS_next) {
//				System.out.print(x + " ");
//			}
//			System.out.println();

			mds_x_index = ILPResult.MDS_x_index();
			File solfile2 = new File(Filemaker.Path + "\\ILP\\" + FileRead.FileName + "\\sol_test1_" + hamming_k + ".sol");
			result = ILPSolver.getResult(solfile2);
			File_delete.file_delete(hamming_k);
			if (IMDS.size()==0) {
				break;
			}
		}

		/*
		 * MDSになり得るNodeリスト
		 */
//		List<String> NodeInMDS = Stream.concat(CMDS.stream(), IMDS.stream())
//											.collect(Collectors.toList());

		/*
		 * Criticalityの計算
		 */
		for(String name : newNodeInMDS) {
			double count = 0.0;
			for(int j = 0; j < MDSset.size(); j++) {
				if(MDSset.get(j).contains(name)) {
					count++;
				}
			}
			count = count/(double)MDSset.size();
			//String[] str = {NodeInMDS.get(i) , String.valueOf(count)};
			Criticality.replace(name, String.valueOf(count));
		}
		long totalEnd = System.currentTimeMillis();
		totalTime = (double)(totalEnd-totalStart)/1000.0;
		double avgWritingTime = 0.0;
		double avgSolvingTime = 0.0;
		for (int i=0; i<FileWrite.timeList.size(); i++) {
			avgWritingTime += FileWrite.timeList.get(i);
			avgSolvingTime += ILPSolver.timeList.get(i);
		}
		FileWrite.writingTime = avgWritingTime/FileWrite.timeList.size();
		ILPSolver.solvingTime = avgSolvingTime/ILPSolver.timeList.size();
		FileWrite.timeList.clear();
		ILPSolver.timeList.clear();

		System.out.println("Criticalityの計測終了");

		System.out.println("****RESULT****\n"
						 + "K="+hamming_k);
		/*
		 * HamminigDistance KとK-1のCriticalityが、各頂点について+-0.1未満の差になったらストップ
		 * or
		 * 距離が一定数以下になったら終了
		 */
//		if(hamming_count==0) {
//			Criticality_1before = Criticality;
//		}else {
//			for(int i = 0; i < IMDS.size(); i++) {
//				if(Criticality.get(IMDS.get(i))==null || Criticality_1before.get(IMDS.get(i))==null) {
//					Criticality_1before = Criticality;
//					STOP = 0;
//					break;
//				}else {
//					if(Math.abs(Double.parseDouble(Criticality.get(IMDS.get(i)))-Double.parseDouble(Criticality_1before.get(IMDS.get(i)))) < 0.05) {
//		        		STOP = 1;
//		        	}else {
//		        		Criticality_1before = Criticality;
//		        		STOP = 0;
//		        		break;
//		        	}
//				}
//	        }
//		}
		double currentDis = 0;
		final double DISTANCE = 0.02;
		String msg = "Criticalityはnullを含んでいます";
		if(hamming_count==0) {
			Criticality_1before = (HashMap<String, String>)Criticality.clone();
		}else {
			int count = 0;
			for(int i = 0; i < IMDS.size(); i++) {
				if(Criticality.get(IMDS.get(i))==null || Criticality_1before.get(IMDS.get(i))==null) {
//					Criticality_1before = Criticality;
//					STOP = 0;
					break;
				}else {
					double a = Double.parseDouble(Criticality.get(IMDS.get(i)));
					double b = Double.parseDouble(Criticality_1before.get(IMDS.get(i)));
					currentDis += (a-b)*(a-b);
//					STOP = 1;
					count++;
				}
	        }
			currentDis = Math.sqrt(currentDis/IMDS.size());
			if(currentDis < DISTANCE && count == IMDS.size()) {
				msg = "currentDis = " + currentDis + " < DISTANCE = " + DISTANCE;
				System.out.println(msg);
				STOP = 1;
//				d=0;
			} else if (count != IMDS.size()) {
				System.out.println(msg);
				STOP = 0;
			} else {
				msg = "currentDis = " + currentDis + " > DISTANCE = " + DISTANCE;
				System.out.println(msg);
				STOP = 0;
			}
			Criticality_1before = (HashMap<String, String>)Criticality.clone();
		}

		//終了
		//System.out.println(Nodes);

		//System.out.println("");
		//System.out.println("");
		//System.out.println("--前処理によって同定された頂点--");
		//System.out.println("Prep_Critical_MDS = " +FileRead.PrepMDS_StringName_return(Preprocessing.PrepCritical_int_name));
		//System.out.println("Prep_Redundant_MDS = "  + FileRead.PrepMDS_StringName_return(Preprocessing.PrepRedundant_int_name));
		//System.out.println("前処理Critical数 = "+Prep_CPMDS.size());
		//System.out.println("前処理Redundant数 = "+Prep_RPMDS.size());
		//System.out.println("");
		System.out.println("|MDS| = " + (MDS.size()));
		System.out.println("|CMDS| = " + (CMDS.size()));
		System.out.println("|RMDS| = " + (RMDS.size()));
		System.out.println("|IMDS| = " + (IMDS.size()));
		System.out.println("|MDSs| = " + MDSset.size());

		//結果テキストファイル作成
		File ResultFile = new File(Filemaker.Path + "\\RESULT\\" +FileRead.FileName+"\\" +FileRead.FileName+"_K"+hamming_k+"_result.txt");
		try {
			ResultFile.createNewFile();
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ResultFile)));
			pw.println("\\Network name: "+FileRead.FileName+".txt");
			pw.println(msg);
			pw.println("|MDS| = " + M);
			pw.println("|CMDS| = " + CMDS.size());
			pw.println("|MDSs| = " + MDSset.size());
			pw.println("Node	Category	Criticality");
			//pw.println(" CRITICAL PMDS");
			for(int i = 0; i < CMDS.size(); i++) {
				pw.println(CMDS.get(i) + "	Critical	1.0");
			}
			//pw.println(" REDUNDANT PMDS")+
			for(int i = 0; i < RMDS.size(); i++) {
				pw.println(RMDS.get(i) + "	Redundant	0.0");
			}
			//pw.println(" INTERMITTENT PMDS");
			Collections.sort(IMDS, (x, y)->{Optional<String> c2 = Optional.ofNullable(Criticality.get(y));
											Optional<String> c1 = Optional.ofNullable(Criticality.get(x));
											double dis = Double.parseDouble(c2.orElse("0.0")) -
														Double.parseDouble(c1.orElse("0.0"));
											int flag = (dis>0) ? 1 : -1;
											if (dis==0) flag = 0;
											return flag;});

			for(int i = 0; i < IMDS.size(); i++) {
				pw.println(IMDS.get(i) + "	Intermittent	" + Criticality.get(IMDS.get(i)));
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
