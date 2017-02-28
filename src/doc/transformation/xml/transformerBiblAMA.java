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
			    	
			    	/* Patterns for matching authors names 
			    	 * check if there is authors initials and add authors surnames and given-names
			    	 * else treat as collaboration group
			    	 * */
			    	Pattern k2add = Pattern.compile("(?:\\G|^)[^.]+?(?<givenNames>\\b([A-Z\\-]{1,6})\\b)"); //all author initialls before first dot
			    	Matcher m2add = k2add.matcher(references.item(j).getTextContent());
			    	
			    	Pattern k2 = Pattern.compile("(.*?)(?:\\[.*\\])?\\.(.*?).*"); // Assume that all authors are before forst dot
			    	Matcher m2 = k2.matcher(references.item(j).getTextContent());
			    	
			    	if (m2add.find()) {
				    	while (m2.find()) {
				    		String [] articleNames = m2.group(1).trim().split(",");
				    		for (int y = 0; y < articleNames.length; y++) {
				    			System.out.println(articleNames[y]);
				    		   
					    		Element name = document.createElement("name");
					    		personGroup.appendChild(name);
					    		Element surname = document.createElement("surname");
					    		name.appendChild(surname);
					    		Element givenNames = document.createElement("given-names");
					    		name.appendChild(givenNames);
					    		String[] namesOrSurnames = articleNames[y].trim().split(" ", 2);
					    		for (int yy=0; yy < namesOrSurnames.length; yy++) {
						    		givenNames.setTextContent(namesOrSurnames[1]);
				                    surname.setTextContent(namesOrSurnames[0]);
					    		}
				    		}
				    	}
			    	} else {
			    		while (m2.find()) {
			    			Element collab = document.createElement("collab");
			    			collab.setTextContent(m2.group(1).trim());
			    			personGroup.appendChild(collab);
			    		}
			    	}
			    	
			    	
			    	// patterns for journals. TODO pattern for books, chapters and conference 
			    	if (references.item(j).getTextContent().contains("[book]")) {
			    		elementCitation.setAttribute("publication-type", "book");
			    		Pattern k4 = Pattern.compile("(.*?)\\.(?<title>.*?)\\.(?<ed>:\\.)?\\s?(?:(?<loc>.*?)[:;])?\\s?(?<pub>.*?)[;\\.]\\s?(?<year>\\d+)\\.");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		if (m4.find()) {
					    	Element bookTitle = document.createElement("source");
					    	bookTitle.setTextContent(m4.group("title"));
					    	elementCitation.appendChild(bookTitle);
					    	
					    	Element bookLoc = document.createElement("publisher-loc");
					    	bookLoc.setTextContent(m4.group("loc"));
					    	elementCitation.appendChild(bookLoc);
					    	
					    	Element bookPub = document.createElement("publisher-name");
					    	bookPub.setTextContent(m4.group("pub"));
					    	elementCitation.appendChild(bookPub);
					    	
					    	Element bookYear = document.createElement("year");
					    	bookYear.setTextContent(m4.group("year"));
					    	elementCitation.appendChild(bookYear);
			    		}
			    	} else {
			    		elementCitation.setAttribute("publication-type", "journal");
			    		//Pattern k4 = Pattern.compile("(.*?)\\.(.*?)\\.(.*?)(?<year>\\d+)[;]\\s?(?<volume>\\d+)(?:\\((?<issue>\\d+)\\))?\\s?:(?<fpage>\\d+|[A-Za-z]+\\d+)(?:-(?<lpage>\\d+))?");
			    		/* valid are: 
			    		 * Author IU, Author I-U. Article Title. Journal Name. 2017.
			    		 *                                    ...Journal Name. 2017;4:e11386.
			    		 *                                    ...Journal Name. 2017;4(3):152-159.
			    		 * Most whitespaces are ignored
			    		 * */
			    		Pattern k4 = Pattern.compile("(.*?)\\.(.*?)\\.(.*?)(?<year>\\d+)\\s*?;?\\s*?(?:(?<volume>\\d+))?(?:\\((?<issue>\\d+)\\))?\\s*?(?::\\s*?(?<fpage>\\d+|[A-Za-z]+\\d+))?(?:[\\-\\–](?<lpage>\\d+))?\\.");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		if (m4.find()) {
			    			Element articleTitle = document.createElement("article-title");
			    			articleTitle.setTextContent(m4.group(2).trim());
			    			elementCitation.appendChild(articleTitle);
			    			
			    			Element source = document.createElement("source");
			    			source.setTextContent(m4.group(3).replace(".", "").trim());
			    			elementCitation.appendChild(source);
			    			
			    			Element articleYear = document.createElement("year");
			    			articleYear.setTextContent(m4.group("year"));
			    			elementCitation.appendChild(articleYear);
			    			
			    	
			    			Element articleVolume = document.createElement("volume");
			    			articleVolume.setTextContent(m4.group("volume"));
			    			elementCitation.appendChild(articleVolume);
			    		  
			    			if (m4.group("issue") != null) {
			    				Element issue = document.createElement("issue");
			    				issue.setTextContent(m4.group("issue"));
			    				elementCitation.appendChild(issue);
			    			}
			    			
			    			if (m4.group("fpage") !=null) {
			    				Element fpage = document.createElement("fpage");
			    				fpage.setTextContent(m4.group("fpage"));
			    				elementCitation.appendChild(fpage);
			    			}
			    			
			    			if (m4.group("lpage") != null) {
			    				Element lpage = document.createElement("lpage");
			    				lpage.setTextContent(m4.group("lpage"));
			    				elementCitation.appendChild(lpage);
			    			}
			    		}
			    	} // end of separate journal/book/conference parsing
			    	
			    	 /* pattern for DOI, PMID or URL */
			    	Pattern pattern = Pattern.compile("(?i)(?<=(?<doi>DOI:)|(?<pmid>PMID:)|(?<uri>URL:))\\s*?(?<url>(http|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-]))");
			    	Matcher urlType = pattern.matcher(references.item(j).getTextContent());
	    			while (urlType.find()) {
	    				if (urlType.group("doi") != null) {
	    					Element doi = document.createElement("pub-id");
	    					doi.setTextContent(urlType.group("url").replace("https://doi.org/", ""));
	    					doi.setAttribute("pub-id-type", "doi");
	    					elementCitation.appendChild(doi);
	    				}
	    				else if (urlType.group("pmid") != null) {
	    					Element pmid = document.createElement("pub-id");
	    					pmid.setTextContent(urlType.group("url"));
	    					pmid.setAttribute("pub-id-type", "pmid");
	    					elementCitation.appendChild(pmid);
	    				}
	    				else {
	    					Element url = document.createElement("ext-link");
	    					url.setTextContent(urlType.group("url"));
	    					url.setAttribute("ext-link-type", "uri");
	    					elementCitation.appendChild(url);
	    				}
	    			}
			    	
			    }   
			    
			    Node secReferences = internal.getParentNode();
			    secReferences.getParentNode().removeChild(secReferences);
			}
		}	             
 	}

}
