import java.util.ArrayList;
import java.util.List;

//使ってる

public class Preprocessing {

	public static ArrayList<Integer> PrepCritical_int = new ArrayList<Integer>();
	public static ArrayList<Integer> PrepRedundant_int = new ArrayList<Integer>();

	public static void critical_processing() {

		ArrayList<Integer> Nodes = new ArrayList<Integer>();
		Nodes =  FileRead.Node_int;
		double[][] matrixForPrep = new double[Nodes.size()][Nodes.size()];

		//前処理用に隣接行列を複製
		for(int i = 0; i < Nodes.size(); i++) {
			for(int j = 0; j < Nodes.size(); j++) {
				matrixForPrep[i][j] = Algorithm.Adjacency_matrix[i][j];
			}
		}

//		inDegree = 0 のノードを探す
		PrepCritical_int = calcZeroDegreeNodes("in", matrixForPrep);
//		System.out.println("CriticalPre1(In Degree=0)");
//		for (int i=0; i<PrepCritical_int.size(); i++) {
//			System.out.print(PrepCritical_int.get(i) + " ");
//		}
//		System.out.println(PrepCritical_int.size());
		System.out.println("*".repeat(100));
		while (true) {
//			outDegree = 0ノードを探す
			List<Integer> notargetNodes = calcZeroDegreeNodes("out", matrixForPrep);

			List<Integer> leaveNodes = new ArrayList<>();
			List<Integer> newCritical = new ArrayList<>();
			List<Integer> newRedundant = new ArrayList<>();

//			outDegree = 0, inDegree = 1のノードを探す
			for (int i=0; i<notargetNodes.size(); i++) {
				int inDegree = 0;
				for (int j=0; j<Nodes.size(); j++) {
					if (matrixForPrep[j][notargetNodes.get(i)] == 1) {
						inDegree++;
						if (inDegree >= 2) {
							break;
						}
					}
				}
				if (inDegree == 1) {
					leaveNodes.add(notargetNodes.get(i));
				}
			}

//			CriticalNodeを探す
			for (int i=0; i<Nodes.size(); i++) {
				if (PrepCritical_int.contains(i) || PrepRedundant_int.contains(i)) continue;
				int leaveCount = 0;
				for (int j=0; j<leaveNodes.size(); j++) {
					if (matrixForPrep[i][leaveNodes.get(j)] == 1) {
						leaveCount++;
					}
				}
				if (leaveCount >= 2) {
					newCritical.add(i);
				}
			}
//			newCriticalNodeへのリンクの削除
			deleteLinksToNode(newCritical, matrixForPrep);

			PrepCritical_int.addAll(newCritical);

//			System.out.println("CriticalPre2");
//			System.out.println(PrepCritical_int.size());

//			RedundantNodeを探す
			for (int i=0; i<notargetNodes.size(); i++) {
				if (PrepRedundant_int.contains(notargetNodes.get(i)) ||
						PrepCritical_int.contains(notargetNodes.get(i))) continue;
				boolean isLinkedByCritical = false;
				for (int j=0; j<PrepCritical_int.size(); j++) {
					if (matrixForPrep[PrepCritical_int.get(j)][notargetNodes.get(i)] == 1) {
						isLinkedByCritical = true;
						break;
					}
				}
				if (isLinkedByCritical) {
					newRedundant.add(notargetNodes.get(i));
				}
			}
			deleteLinksToNode(newRedundant, matrixForPrep);

			PrepRedundant_int.addAll(newRedundant);

//			System.out.println("notargetNode");
//			for (int i=0; i<notargetNodes.size(); i++) {
//				System.out.print(notargetNodes.get(i) + " ");
//			}
//			System.out.println();
//			System.out.println("newCritical");
//			for (int i=0; i<newCritical.size(); i++) {
//				System.out.print(newCritical.get(i) + " ");
//			}
//			System.out.println();
//			System.out.println("newRedundant");
//			for (int i=0; i<newRedundant.size(); i++) {
//				System.out.print(newRedundant.get(i) + " ");
//			}
//			System.out.println();
//			System.out.println("*".repeat(100));
			if(newCritical.size()==0 && newRedundant.size()==0) break;
		}
		System.out.println("Critical Node Determined By Preprocessing");
//		for (int c : PrepCritical_int) {
//			System.out.print(FileRead.intToName.get(c)+ " ");
//		}
		System.out.println(PrepCritical_int.size());
		System.out.println("\nRedundant Node Determined By Preprocessing");
//		for (int r : PrepRedundant_int) {
//			System.out.print(FileRead.intToName.get(r) + " ");
//		}
		System.out.println(PrepRedundant_int.size());
		System.out.println("\n" + "*".repeat(100));
	}

	public static ArrayList<Integer> calcZeroDegreeNodes(String dType, double[][] matrix) {
		int size = FileRead.Node_int.size();
		ArrayList<Integer> nodes = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			int degree = 0;
			for(int j = 0; j < size; j++) {
				if (dType.equals("in")) {
					if(matrix[j][i] == 1) {
						degree = 1;
						break;
					}
				} else {
					if(matrix[i][j] == 1) {
						degree = 1;
						break;
					}
				}
			}
			if(degree == 0) {
				nodes.add(i);
			}
		}
		return nodes;
	}

	public static void deleteLinksToNode(List<Integer> nodes, double[][] matrix) {
		int size = FileRead.Node_int.size();
		for (int i=0; i<nodes.size(); i++) {
			for (int j=0; j<size; j++) {
				matrix[j][nodes.get(i)] = 0;
			}
		}
	}

}

