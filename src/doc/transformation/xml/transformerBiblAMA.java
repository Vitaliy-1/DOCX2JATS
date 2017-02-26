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
			    	int p = j + 1;
			    	Element ref = document.createElement("ref");
			    	ref.setAttribute("id", "bib" + p);
			    	Node reflist = (Node) xPath.compile("/article/back/ref-list").evaluate(document, XPathConstants.NODE);
			    	reflist.appendChild(ref);
			    	Element elementCitation = document.createElement("element-citation");
			    	ref.appendChild(elementCitation);
			    	// todo set attribute ro element-citation
			    	Element personGroup = document.createElement("person-group");
			    	personGroup.setAttribute("person-group-type", "author");
			    	elementCitation.appendChild(personGroup);
			    	
			    	// patterns for matching authors names
			    	Pattern k2 = Pattern.compile("(?:\\G|^)[^.]+?\\b([A-Z\\-]{1,6})\\b"); //all before first dot
			    	Matcher m2 = k2.matcher(references.item(j).getTextContent());
			    	Pattern k3 = Pattern.compile("(?:\\G|^)[^.]+?\\b([A-Z][a-z\\-]+)\\b"); //all before first dot
			    	Matcher m3 = k3.matcher(references.item(j).getTextContent());
			    	
			    	while (m2.find() && m3.find()) {
			    		Element name = document.createElement("name");
			    		personGroup.appendChild(name);
			    		Element surname = document.createElement("surname");
			    		name.appendChild(surname);
			    		Element givenNames = document.createElement("given-names");
			    		name.appendChild(givenNames);
			    		givenNames.setTextContent(m2.group(1));
			    		surname.setTextContent(m3.group(1));
			    	}
			    	
			    	// TODO if authors are institutions (contrib) 
			    	
			    	// patterns for journals. TODO pattern for books, chapters and conference 
			    	if (references.item(j).getTextContent().contains("[")) {
			    		
			    	} else {
			    		elementCitation.setAttribute("publication-type", "journal");
			    		Pattern k4 = Pattern.compile("(.*?)\\.(.*?)\\.(.*?)\\.(.\\d+?)[\\.;](\\d+)[:\\(](?:(\\d+))?[\\);](?:(\\d+)-)?(?:(\\d+)\\.)?"); //all before first dot
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		if (m4.find()) {
			    			Element articleTitle = document.createElement("article-title");
			    			articleTitle.setTextContent(m4.group(2));
			    			elementCitation.appendChild(articleTitle);
			    			
			    			Element source = document.createElement("source");
			    			source.setTextContent(m4.group(3));
			    			elementCitation.appendChild(source);
			    			
			    			Element articleYear = document.createElement("year");
			    			articleYear.setTextContent(m4.group(4));
			    			elementCitation.appendChild(articleYear);
			    			
			    			Element articleVolume = document.createElement("volume");
			    			articleVolume.setTextContent(m4.group(5));
			    			elementCitation.appendChild(articleVolume);
			    			
			    		    Element articleIssue = document.createElement("issue");
			    		    articleIssue.setTextContent(m4.group(6));
			    		    elementCitation.appendChild(articleIssue);
			    		    
			    		    Element articleFirstPage = document.createElement("fpage");
			    		    articleFirstPage.setTextContent(m4.group(7));
			    		    elementCitation.appendChild(articleFirstPage);
			    		    
			    		    Element articleLastPage = document.createElement("lpage");
			    		    articleLastPage.setTextContent(m4.group(8));
			    		    elementCitation.appendChild(articleLastPage);
			    		    System.out.println(m4.group());
			    		}
			    	}
			    	
			    }
			    
			}
		}
 	}

}
