import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DOMParser{
	 DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
	  //Load and parse XML file into DOM 
	  public Document parse(String filePath) { 
	     Document document = null; 
	     try { 
	        //DOM parser instance 
	        DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
	        //parse an XML file into a DOM tree 
	        document = builder.parse(new File(filePath)); 
	     }catch (Exception e) { 
	        e.printStackTrace(); 
	     } 
	     return document; 
	  } 
	    
	  public static void main(String[] args) { 
	        DOMParser parser = new DOMParser(); 
	        Document document = parser.parse("E:/test.txt"); 
	        //get root element 
	        Element rootElement = document.getDocumentElement(); 
	 
	        //traverse child elements 
	        NodeList nodes = rootElement.getChildNodes(); 
	        for (int i=0; i < nodes.getLength(); i++) 
	        { 
	           Node node = nodes.item(i); 
	           if (node.getNodeType() == Node.ELEMENT_NODE) {   
	              Element child = (Element) node; 
	              //process child element 
	              System.out.println(child.getAttribute("id"));
	           } 
	        } 
	 
	      
	  } 
}

