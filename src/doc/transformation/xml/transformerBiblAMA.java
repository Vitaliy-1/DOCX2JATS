package doc.transformation.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
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
			    	// TODO set attribute to element-citation
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
			    	
			    	authorsParsing(document, personGroup, m2add, m2);
			    	
			    	
			    	// patterns for journals. TODO pattern for books, chapters and conference 
			    	if (references.item(j).getTextContent().contains("[cha")) {
			    		elementCitation.setAttribute("publication-type", "chapter");
			    		Pattern k4 = Pattern.compile("(.*?)\\.(?<title>.*?)\\.(?:\\s*?[Ii]n)?:?(?<authors>.*?)(?:ed|eds?)?\\.(?<book>.*?)\\.(?:\\s*?(?<edition>\\d+)\\w+.*?\\.)?\\s*?(?:\\s*?(?<city>[A-Za-z]*?)\\s*?:)?\\s*?(?:\\s*(?<pub>[A-Za-z\\s]*?);)?(?:\\s*?(?<year>\\d{4})\\s*?:\\s*?)?(?:(?<fpage>\\d+)\\s*?)?[\\-\\–]?(?:\\s*?(?<lpage>\\d+))\\.");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		chapterParsing(document, elementCitation, m4);
			    		
			    	} else if (references.item(j).getTextContent().contains("[con")) {
			    		elementCitation.setAttribute("publication-type", "conference");
			    		Pattern k4 = Pattern.compile("(.*?)\\.(?<title>.*?)\\.(?:.*?:)?(?<conference>.*?);\\s*?(?:.*?,)?(?:\\s*?(?<year>\\d+))?\\.?\\s*?(?:(?<city>.*?),)?(?:(?<country>.*?)\\.)?");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
				    	conferenceParsing(document, elementCitation, m4);
				    	
			    	} else if (references.item(j).getTextContent().contains("[book]")) {
			    		elementCitation.setAttribute("publication-type", "book");
			    		Pattern k4 = Pattern.compile("(.*?)\\.(?<title>.*?)\\.(?<ed>:\\.)?\\s?(?:(?<loc>.*?)[:;])?\\s?(?<pub>.*?)[;\\.]\\s?(?<year>\\d+)\\.");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		bookParsing(document, elementCitation, m4);
			    		
			    	} else {
			    		elementCitation.setAttribute("publication-type", "journal");
			    		//Pattern k4 = Pattern.compile("(.*?)\\.(.*?)\\.(.*?)(?<year>\\d+)[;]\\s?(?<volume>\\d+)(?:\\((?<issue>\\d+)\\))?\\s?:(?<fpage>\\d+|[A-Za-z]+\\d+)(?:-(?<lpage>\\d+))?");
			    		/* valid are: 
			    		 * Author IU, Author I-U. Article Title. Journal Name. 2017.
			    		 *                                    ...Journal Name. 2017;4:e11386.
			    		 * ...Journal Name. 2017;4(3):152-159.
			    		 * Most whitespaces are ignored
			    		 * */
			    		Pattern k4 = Pattern.compile("(.*?)\\.(.*?)\\.(.*?)(?<year>\\d+)\\s*?;?\\s*?(?:(?<volume>\\d+))?(?:\\((?<issue>\\d+)\\))?\\s*?(?::\\s*?(?<fpage>\\d+|[A-Za-z]+\\d+))?(?:[\\-\\–](?<lpage>\\d+))?\\.");
				    	Matcher m4 = k4.matcher(references.item(j).getTextContent());
			    		journalParsing(document, elementCitation, m4);
			    		
			    	} // end of separate journal/book/conference parsing
			    	
			    	 /* pattern for DOI, PMID or URL */
			    	Pattern pattern = Pattern.compile("(?i)(?<=(?<doi>DOI:)|(?<pmid>PMID:)|(?<uri>URL:))\\s*?(?<url>(http|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-]))");
			    	Matcher urlType = pattern.matcher(references.item(j).getTextContent());
	    			linksParsing(document, elementCitation, urlType);
			    	
			    }   
			    
			    Node secReferences = internal.getParentNode();
			    secReferences.getParentNode().removeChild(secReferences);
			}
		}	             
 	}

	private static void authorsParsing(Document document, Element personGroup, Matcher m2add, Matcher m2)
			throws DOMException {
		if (m2add.find()) {
			while (m2.find()) {
				String [] articleNames = m2.group(1).trim().split(",");
				for (int y = 0; y < articleNames.length; y++) {
		    		Element name = document.createElement("name");
		    		personGroup.appendChild(name);
		    		Element surname = document.createElement("surname");
		    		name.appendChild(surname);
		    		Element givenNames = document.createElement("given-names");
		    		name.appendChild(givenNames);
		    		String[] namesOrSurnames = articleNames[y].trim().split(" ", 2);
		    		for (int yy=0; yy < namesOrSurnames.length; yy++) {
			    		givenNames.setTextContent(namesOrSurnames[1].trim());
		                surname.setTextContent(namesOrSurnames[0].trim());
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
	}
	
	private static void chapterParsing(Document document, Element elementCitation, Matcher m4) throws DOMException {
		if (m4.find()) {
			Element chapter_title = document.createElement("chapter-title");
			chapter_title.setTextContent(m4.group("title"));
			elementCitation.appendChild(chapter_title);
			if (m4.group("authors") != null) {
				Element person_group = document.createElement("person-group");
				person_group.setAttribute("person-group-type", "editor");
				elementCitation.appendChild(person_group);
				
				String[] authors = m4.group("authors").trim().split(",");
				for (int i = 0; i<authors.length; i ++) {
					Element authorName = document.createElement("name");
					Element surname = document.createElement("surname");
					Element given_names = document.createElement("given-names");
					elementCitation.appendChild(authorName);
					String author = authors[i].trim();
					String[] name = author.split("\\s", 2);
				    for (int j = 0; j < name.length; j++) {
				    	surname.setTextContent(name[0].trim());
				    	authorName.appendChild(surname);
				    	given_names.setTextContent(name[1].trim());
				    	authorName.appendChild(given_names);
				    }
				}
			} // end of names count
			
			Element source = document.createElement("source");
			source.setTextContent(m4.group("book"));
			elementCitation.appendChild(source);
			
			if (m4.group("edition") != null) {
				Element edition = document.createElement("edition");
				edition.setTextContent(m4.group("edition"));
				elementCitation.appendChild(edition);
			}
			
			if (m4.group("city") != null) {
				Element publisher_loc = document.createElement("publisher-loc");
				publisher_loc.setTextContent(m4.group("city"));
				elementCitation.appendChild(publisher_loc);
			}
			
			if (m4.group("pub") != null) {
				Element publisher_name = document.createElement("publisher-name");
				publisher_name.setTextContent(m4.group("pub"));
				elementCitation.appendChild(publisher_name);
			}
			
			if (m4.group("year") != null) {
				Element year = document.createElement("year");
				year.setTextContent(m4.group("year"));
				elementCitation.appendChild(year);
			}
			
			if (m4.group("fpage") != null) {
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
	}
	
	private static void conferenceParsing(Document document, Element elementCitation, Matcher m4) throws DOMException {
		if (m4.find()) {
			Element source = document.createElement("source");
			source.setTextContent(m4.group("title").trim());
			elementCitation.appendChild(source);
			
			Element conf_name = document.createElement("conf-name");
			conf_name.setTextContent(m4.group("conference").trim());
			elementCitation.appendChild(conf_name);
			
			Element conf_loc = document.createElement("conf-loc");
			if (m4.group("city") != null && m4.group("country") != null) {
				conf_loc.setTextContent(m4.group("city").trim() + ", " + m4.group("country").trim());
				elementCitation.appendChild(conf_loc);
			} else if (m4.group("city") !=null) {
				conf_loc.setTextContent(m4.group("city").trim());
				elementCitation.appendChild(conf_loc);
			} else if (m4.group("country") !=null) {
				conf_loc.setTextContent(m4.group("country").trim());
				elementCitation.appendChild(conf_loc);
			} 
		}
	}

	private static void bookParsing(Document document, Element elementCitation, Matcher m4) throws DOMException {
		if (m4.find()) {
			Element bookTitle = document.createElement("source");
			bookTitle.setTextContent(m4.group("title").trim());
			elementCitation.appendChild(bookTitle);
			
			Element bookLoc = document.createElement("publisher-loc");
			bookLoc.setTextContent(m4.group("loc").trim());
			elementCitation.appendChild(bookLoc);
			
			Element bookPub = document.createElement("publisher-name");
			bookPub.setTextContent(m4.group("pub").trim());
			elementCitation.appendChild(bookPub);
			
			Element bookYear = document.createElement("year");
			bookYear.setTextContent(m4.group("year").trim());
			elementCitation.appendChild(bookYear);
		}
	}

	
	private static void journalParsing(Document document, Element elementCitation, Matcher m4) throws DOMException {
		if (m4.find()) {
			Element articleTitle = document.createElement("article-title");
			articleTitle.setTextContent(m4.group(2).trim());
			elementCitation.appendChild(articleTitle);
			
			Element source = document.createElement("source");
			source.setTextContent(m4.group(3).replace(".", "").trim());
			elementCitation.appendChild(source);
			
			Element articleYear = document.createElement("year");
			articleYear.setTextContent(m4.group("year").trim());
			elementCitation.appendChild(articleYear);
			
		    if (m4.group("volume") != null) {
			Element articleVolume = document.createElement("volume");
			articleVolume.setTextContent(m4.group("volume").trim());
			elementCitation.appendChild(articleVolume);
		    }
			
			if (m4.group("issue") != null) {
				Element issue = document.createElement("issue");
				issue.setTextContent(m4.group("issue").trim());
				elementCitation.appendChild(issue);
			}
			
			if (m4.group("fpage") !=null) {
				Element fpage = document.createElement("fpage");
				fpage.setTextContent(m4.group("fpage").trim());
				elementCitation.appendChild(fpage);
			}
			
			if (m4.group("lpage") != null) {
				Element lpage = document.createElement("lpage");
				lpage.setTextContent(m4.group("lpage").trim());
				elementCitation.appendChild(lpage);
			}
		}
	}

	
	private static void linksParsing(Document document, Element elementCitation, Matcher urlType) throws DOMException {
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

}
