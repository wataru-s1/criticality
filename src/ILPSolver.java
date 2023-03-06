import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ILPSolver {

	public static boolean solfile_not_found = false;
	public static ArrayList<Double> timeList = new ArrayList<Double>();
	public static double solvingTime = 0.0;

	public static void solve(File cmdfile) {

		String command = "cplex -f \""+cmdfile.getAbsolutePath()+"\"";
		List<String> commandList = Arrays.stream(command.split(" ")).collect(Collectors.toList());
		long start = System.currentTimeMillis();
		try {
			ProcessBuilder pb = new ProcessBuilder(commandList);
			Process p  = pb.start();
			while (p.getInputStream().read() >= 0);
		} catch (IOException e) {
			System.out.println(e);
		}
		long end = System.currentTimeMillis();
		timeList.add((double)(end-start)/1000.0);
	}

	//CPLEXの時のみ使うメソッド

	public static void createCommandFile(File lpfile,File solfile) {
		try {
			File cmdfile = new File(Filemaker.Path+"\\ILP\\"+ FileRead.FileName + "\\cmd_test1.cmd");
//			File cmdfile = new File("C:\\Users\\eimiy\\Documents\\CPLEX_TEST\\cmd_test1.cmd");
			if (cmdfile.createNewFile()){
		        System.out.println("cmdファイルの作成に成功しました");
		    }else{
		    //System.out.println("**");
		    }

			FileWriter pw = new FileWriter(cmdfile);
			pw.write("read \""+lpfile.getAbsolutePath()+"\"\r\n");
			pw.write("optimize\r\n");
			pw.write("write \""+solfile.getAbsolutePath()+"\"\r\n");
			pw.write("y\r\n");
			pw.close();

			solve(cmdfile);

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static Map<String, Integer> getResult(File solfile) {
		Map<String, Double> result = new HashMap<>();
		Map<String, Integer> result2 = new HashMap<>();

		try{
			DocumentBuilder dr = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			/*dr.setErrorHandler(new ErrorHandler() {
	            @Override
	            public void warning(SAXParseException exception) throws SAXException {
	            }
	            @Override
	            public void error(SAXParseException exception) throws SAXException {

	            }
	            @Override
	            public void fatalError(SAXParseException exception) throws SAXException {
	            }
	        });;*/
			Document document = dr.parse(solfile);
			Element rootElement = document.getDocumentElement();
			NodeList variables = rootElement.getElementsByTagName("variable");
			for(int i = 0; i < variables.getLength(); i++){
				String key = variables.item(i).getAttributes().getNamedItem("name") .getNodeValue();
				String val = variables.item(i).getAttributes().getNamedItem("value").getNodeValue();
				//変更
				//resultに(x,結果)を格納する
				result.put(key, Double.parseDouble(val));
				//result.put(key, Integer.parseInt(val));
			}

			//たまに結果に1,0以外の数値があるので二値化する
			for(Entry<String,Double> entry : result.entrySet()) {
				if(entry.getValue()>0.5) {
					result2.put(entry.getKey(), 1);
				}else {
					result2.put(entry.getKey(), 0);
				}
			}
		} catch (Exception e) {
			System.out.println(solfile.getAbsolutePath()+" (sol file) not found.");
			solfile_not_found = true;
		}
		return result2;
	}
}
