
public class Clear {
	public static void clear() {
		Algorithm.CMDS.clear();
		Algorithm.IMDS.clear();
		Algorithm.RMDS.clear();
		Algorithm.Constraint.clear();
		Algorithm.Hamming.clear();
		Algorithm.MDSset.clear();
		Algorithm.Criticality.clear();
		Algorithm.STOP=0;
		FileRead.nameToInt.clear();
		FileRead.intToName.clear();
		FileRead.xToName.clear();
		FileRead.nameToX.clear();
		FileRead.intToX.clear();
		FileRead.Node_int.clear();
//		FileWrite.counter = 0;
		Matrix.PrepCritical_x.clear();
		Matrix.PrepRedundant_x.clear();
//		ILPResult.m = 0;
		ILPSolver.solfile_not_found = false;
		Preprocessing.PrepCritical_int.clear();
		Preprocessing.PrepRedundant_int.clear();
	}

}
