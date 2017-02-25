package doc.transformation.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class transformerFigRefs {
	
	static void transformerFigRefsImpl (Document document) throws XPathExpressionException {
		
        XPath xPath =  XPathFactory.newInstance().newXPath();
        String expression1 = "/article/body/sec/p/text()|/article/body/sec/sec/p/text()|/article/body/sec/sec/list/list-item/p/text()|/article/body/sec/sec/fig/caption/title/text()|/article/body/sec/fig/caption/title/text()|/article/body/sec/table-wrap/caption/title/text()|article/body/sec/sec/table-wrap/caption/title/text()|/article/body/sec/table-wrap/caption/p/text()|/article/body/sec/sec/table-wrap/caption/p/text()";
        
        NodeList nodeList = (NodeList) xPath.compile(expression1).evaluate(document, XPathConstants.NODESET);
        
        for (int i = 0; i < nodeList.getLength(); i++) {
	    	Text textNode = (Text) nodeList.item(i);
	   	    
	   	    int prevSplitOffset = 0;
			Pattern k = Pattern.compile("(?i)(рис.\\s(\\d+)|[Рр]исунок\\s(\\d+)|[Рр]исунку\\s(\\d+)|fig.\\s(\\d+)|[fF]igure\\s(\\d+))");
		    Matcher m = k.matcher(textNode.getTextContent());
		    
		    //creating nodes and grouping pattern results
	        while(m.find()) {
		      Text number = textNode.splitText(m.start(1) - prevSplitOffset);
		    
		      textNode = number.splitText(m.group(1).length());
		      Element xref = document.createElement("xref");
		      for (int c = 2; c<=m.groupCount(); c++) {
		    	  if (m.group(c) != null) {
		          xref.setAttribute("rid", "fig" + m.group(c));
		          break;
		    	  }
		      }
		      xref.setAttribute("ref-type", "fig");
		      number.getParentNode().replaceChild(xref, number);
		      xref.appendChild(number);
		      prevSplitOffset = m.end(1);
		      
	        }
	    }
	}

}
