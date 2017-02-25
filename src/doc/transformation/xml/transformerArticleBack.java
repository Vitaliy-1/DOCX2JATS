package doc.transformation.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class transformerArticleBack {
	
   static void transformerArticleBack (Document document) throws XPathExpressionException {
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		Node articleBack = (Node) xPath.compile("/article/back").evaluate(document, XPathConstants.NODE);
		
		// add competing-interestss
		Element fnGroup = document.createElement("fn-group");
		articleBack.appendChild(fnGroup);
		Element fnTitle = document.createElement("title");
		fnTitle.setTextContent("Competing interests");
		fnGroup.appendChild(fnTitle);
		Element fnChild = document.createElement("fn");
		fnChild.setAttribute("fn-type", "conflict");
		fnChild.setAttribute("id", "conf1");
		fnGroup.appendChild(fnChild);
		Element fnPar = document.createElement("p");
		fnPar.setTextContent("The author declare that no competing interests exist.");
		fnChild.appendChild(fnPar);
		
		// add reflist
		Element reflist = document.createElement("ref-list");
		articleBack.appendChild(reflist);
		Element refTitle = document.createElement("title");
		refTitle.setTextContent("References (Список використаної літератури)");
		reflist.appendChild(refTitle);
		
	 }

}
