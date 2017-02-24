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

public class transformerAbstractKey {
	
	protected static void transformerAbstractKeyImpl (Document document) throws XPathExpressionException {
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		//adding abstract node
		Node abstractParent = (Node) xPath.compile("/article/front/article-meta").evaluate(document, XPathConstants.NODE);
		Element articleAbstract = document.createElement("abstract");
		articleAbstract.setAttribute("abstract-type", "section");
		abstractParent.appendChild(articleAbstract);
		
		//checking if abstract is in the article text
		NodeList listOfSecTitles = (NodeList) xPath.compile("/article/body/sec/title").evaluate(document,  XPathConstants.NODESET);
		
		for(int i = 0; i<listOfSecTitles.getLength(); i++) {
			Node textNode = (Node) listOfSecTitles.item(i);
			String textNodeText = textNode.getTextContent();
			
			//matches only if title contains word "abstract" case and space insensitive 
			Pattern k = Pattern.compile("(?i)^\\s*abstract\\s*$");
			Matcher m = k.matcher(textNodeText);
			if(m.find()) {
		    Node nodeToCopy = document.importNode(textNode.getParentNode(), true);
		    articleAbstract.appendChild(nodeToCopy);
		    Node textNodeParent = textNode.getParentNode();
		    textNodeParent.getParentNode().removeChild(textNodeParent);
		    
			}
			 
		}
		
		//adding keywords
		
	}

}
