package doc.transformation.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class transformerBiblAMA {
	
	static void transformerBiblFinder (Document document) throws XPathExpressionException {
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		NodeList articleSectionTitles = (NodeList) xPath.compile("/article/body/sec/title/text()").evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i<articleSectionTitles.getLength(); i++) {
			Text articleSectionTitle = (Text) articleSectionTitles.item(i);
			Pattern k1 = Pattern.compile("[Rr]eference|(?=.*?[Сc]писок)(?=.*?літератури).*$");
			Matcher m1 = k1.matcher(articleSectionTitle.getTextContent());
			while (m1.find()) {
			    Node internal = articleSectionTitle.getParentNode();
			    Node List = customMethods.getNextElement(internal);
			    //NodeList references = List.getChildNodes();
			    NodeList references = (NodeList) xPath.evaluate("list-item", List, XPathConstants.NODESET);
			    for (int j = 0; j<references.getLength(); j++) {
			    	System.out.println(references.item(j));
			    }
			    
			}
		}
 	}

}
