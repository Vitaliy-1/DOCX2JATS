package doc.transformation.xml;

/**
 * @file /src/doc/transformation/xml/customMethods.java
 *
 * Copyright (c) 2017 Vitaliy Bezsheiko
 * 
 * Distributed under the GNU GPL v3.
 */

import java.util.regex.Matcher;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class customMethods {
	
	public static int groupNotNullCount(Matcher mChapter) {
		int CountNotNull = 0;
    	while (mChapter.find()) {
    		for (int i = 0; i < mChapter.groupCount(); i++) {
	    		if (mChapter.group() != null) {
	    			CountNotNull++;
	    		} else {
	    			break;
	    		}
    		}
	    }    
    	return CountNotNull;	
	}

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
