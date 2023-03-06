import java.io.File;
import java.io.IOException;

public class Filemaker {
	public static String Path = "";
	public static void FileMake(int i) {
		try{
			File lpfile = new File(Path + "\\ILP\\"+ FileRead.FileName + "\\dom_test1_" + i + ".lp");
			File solfile = new File(Path + "\\ILP\\"+ FileRead.FileName + "\\sol_test1_" + i + ".sol");

			if (lpfile.createNewFile()){
//				System.out.println("lpファイルの作成に成功しました");
			}else{
//				System.out.println("lpファイルの作成に失敗しました");
			}
			if (solfile.createNewFile()){
//				System.out.println("solファイルの作成に成功しました");
			}else{
//				System.out.println("solファイルの作成に失敗しました");
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

}
