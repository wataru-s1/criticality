import java.io.File;
public class File_delete {

	public static void file_delete(int Count) {
		File solfile = new File(Filemaker.Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + Count + ".sol");
		solfile.delete();
	}

}
