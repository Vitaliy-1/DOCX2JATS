package doc.transformation.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class transformerAbstractKey {
	
	static void transformerAbstractKeyImpl (Document document) throws XPathExpressionException {
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		//adding abstract node
		Node articleAbstract = (Node) xPath.compile("/article/front/article-meta/abstract").evaluate(document, XPathConstants.NODE);
		
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
		
        Node articleAbstractTrans = (Node) xPath.compile("/article/front/article-meta/trans-abstract").evaluate(document, XPathConstants.NODE);
		
		//checking if transled abstract is in the article text
		NodeList listOfSecTitlesTrans = (NodeList) xPath.compile("/article/body/sec/title").evaluate(document,  XPathConstants.NODESET);
		
		for(int i = 0; i<listOfSecTitlesTrans.getLength(); i++) {
			Node textNode = (Node) listOfSecTitlesTrans.item(i);
			String textNodeText = textNode.getTextContent();
			
			//matches only if title contains word "анотація" case and space insensitive 
			Pattern k = Pattern.compile("(?i)^\\s*[Аа]нотація\\s*$");
			Matcher m = k.matcher(textNodeText);
			if(m.find()) {
		    Node nodeToCopy = document.importNode(textNode.getParentNode(), true);
		    articleAbstractTrans.appendChild(nodeToCopy);
		    Node textNodeParent = textNode.getParentNode();
		    textNodeParent.getParentNode().removeChild(textNodeParent);
		    
			}
			 
		}
		
		// TODO adding keywords
		
	}

}
