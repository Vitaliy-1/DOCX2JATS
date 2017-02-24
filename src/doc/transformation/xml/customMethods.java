package doc.transformation.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class customMethods {

	//removes all child nodes
	public static void removeChilds(Node node) {
	    while (node.hasChildNodes())
	        node.removeChild(node.getFirstChild());
	}

	//removes whitespaces while getting previous sibling
	public static Element getPreviousElement(Node node) {
	      Node prevSibling = node.getPreviousSibling();
	      while (prevSibling != null) {
	          if (prevSibling.getNodeType() == Node.ELEMENT_NODE) {
	              return (Element) prevSibling;
	          }
	          prevSibling = prevSibling.getPreviousSibling();
	      }
	
	      return null;  
	  }
	//removes whitespaces while getting next sibling
	public static Element getNextElement(Node node) {
	      Node prevSibling = node.getNextSibling();
	      while (prevSibling != null) {
	          if (prevSibling.getNodeType() == Node.ELEMENT_NODE) {
	              return (Element) prevSibling;
	          }
	          prevSibling = prevSibling.getNextSibling();
	      }
	
	      return null;  
	  }
}
