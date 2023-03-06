import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class Test {
	public static void main(String[] arg) {
	  Filemaker.Path = "C:\\Users\\Someya Wataru\\Documents\\GraduationResearch\\Criticality\\test";
	  Filemaker.FileMake(1);
	  String path = "C:\\Users\\Someya Wataru\\Documents\\GraduationResearch\\Criticality\\test\\ILP\\";
	  String fileName = "sol_test1.txt";
	  File file = new File(path + fileName);
	  try {
	    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	    Element root = document.getDocumentElement();
	    NodeList variables = root.getElementsByTagName("variable");
	    System.out.println(variables.getLength());
	    for (int i=0; i<variables.getLength(); i++) {
	      Element variable = (Element)variables.item(i);
	      String name = variable.getAttribute("name");
	      String value = variable.getAttribute("value");
	      System.out.println(name + " " + value);
	    }
	  } catch (Exception e) {
		e.printStackTrace();
	  }
	}
}
