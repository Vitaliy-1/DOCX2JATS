package doc.transformation.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class transformerMeta extends docIngestion {
	
	public static void textTransformMetaImpl(Document document) throws XPathExpressionException {
		
		  XPath xPath =  XPathFactory.newInstance().newXPath();
		 //adding Journal-Id
	    Node journalIdValue = (Node) xPath.compile("/article/front/journal-meta/journal-id").evaluate(document, XPathConstants.NODE);
	    Text textJournalId = document.createTextNode("Psychosomatic Medicine and General Practice");
	    journalIdValue.appendChild(textJournalId);
	    docIngestion.writeDocument(document);
	    
	    //adding ISSN
	    Node journalIssn = (Node) xPath.compile("/article/front/journal-meta/issn").evaluate(document, XPathConstants.NODE);
	    Text textJournalIssn = document.createTextNode("2519-8572");
	    journalIssn.appendChild(textJournalIssn);
	    docIngestion.writeDocument(document);
	    
	    //adding publisher name
	    Node publisherName = (Node) xPath.compile("/article/front/journal-meta/publisher/publisher-name").evaluate(document, XPathConstants.NODE);
	    Text textPublisherName = document.createTextNode("Private Publisher 'Chaban O. S.'");
	    publisherName.appendChild(textPublisherName);
	    docIngestion.writeDocument(document);
	    
	    //adding article-id
	    Node articleIdParent = (Node) xPath.compile("/article/front/article-meta").evaluate(document, XPathConstants.NODE);
	    Node articleIdPublisher = document.createElement("article-id");
	    		((Element) articleIdPublisher).setAttribute("pub-id-type", "publisher-id");
	    articleIdParent.insertBefore(articleIdPublisher, articleIdParent.getFirstChild());
	    docIngestion.writeDocument(document);
	    
	    //removing subtitle
	    Node articleSubtitle = (Node) xPath.compile("/article/front/article-meta/title-group/subtitle").evaluate(document, XPathConstants.NODE);
	    articleSubtitle.getParentNode().removeChild(articleSubtitle);
	    docIngestion.writeDocument(document);
	    
	    //removing alt-title
	    NodeList articleAltTitle = (NodeList) xPath.compile("/article/front/article-meta/title-group/alt-title").evaluate(document, XPathConstants.NODESET);
	    for (int i = 0; i < articleAltTitle.getLength(); i++) {
	    	Node altTitle = articleAltTitle.item(i);
	        altTitle.getParentNode().removeChild(altTitle);
	        docIngestion.writeDocument(document);
	    }
	       
	    
	    //adding Ukrainian title
	    Node articleTitleParent = (Node) xPath.compile("/article/front/article-meta/title-group").evaluate(document, XPathConstants.NODE);
	    Node articleTitleUkrainian = document.createElement("article-title");
	             ((Element) articleTitleUkrainian).setAttribute("xml:lang", "uk-UA");
	    articleTitleParent.appendChild(articleTitleUkrainian);
	    docIngestion.writeDocument(document);
	    
	    //adding attributes to article tag
	    Node articleElement = (Node) xPath.compile("/article").evaluate(document, XPathConstants.NODE);
	    ((Element) articleElement).setAttribute("xml:lang", "uk");
	    ((Element) articleElement).setAttribute("dtd-version", "1.1");
	    ((Element) articleElement).setAttribute("article-type", "review");
	    ((Element) articleElement).setAttribute("specific-use", "production");
	    docIngestion.writeDocument(document);
	    
	    //adding name, surname, given-names
	    Node authorNameParent = (Node) xPath.compile("/article/front/article-meta/contrib-group/contrib").evaluate(document,  XPathConstants.NODE);
	    Element authorName = document.createElement("name");
	    authorName.setAttribute("name-style", "western");
	    authorNameParent.insertBefore(authorName, authorNameParent.getFirstChild());
	    
	    Element authorSurname = document.createElement("surname");
	    Element authorGivenNames = document.createElement("given-names");
	    authorName.insertBefore(authorGivenNames, authorName.getFirstChild());
	    authorName.insertBefore(authorSurname, authorName.getFirstChild());
	    docIngestion.writeDocument(document);
	    
	    //adding xref
	    Element authorXrefAff = document.createElement("xref");
	    authorXrefAff.setAttribute("aff", "aff1");
	    authorNameParent.appendChild(authorXrefAff);
	    Element authorXrefFn = document.createElement("xref");
	    authorXrefFn.setAttribute("fn", "conf1");
	    authorNameParent.appendChild(authorXrefFn);
	    Element authorXrefCorresp = document.createElement("xref");
	    authorXrefCorresp.setAttribute("corresp", "cor1");
	    authorNameParent.appendChild(authorXrefCorresp);
	    docIngestion.writeDocument(document);
	    
	    //transferring aff
	    Node authorAffil = (Node) xPath.compile("/article/front/article-meta/contrib-group/contrib/aff").evaluate(document,  XPathConstants.NODE);
	    authorAffil.getParentNode().removeChild(authorAffil);
	    Element AuthorAffNew = document.createElement("aff");
	    AuthorAffNew.setAttribute("id", "aff1");
	    Node contribGroup = (Node) xPath.compile("/article/front/article-meta/contrib-group").evaluate(document, XPathConstants.NODE);
	    ((Element) contribGroup).appendChild(AuthorAffNew);
	    docIngestion.writeDocument(document);
	    
	    //adding elements to aff
	    Element authorInstitution = document.createElement("institution");
	    ((Element) authorInstitution).setAttribute("content-type", "dept");
	    AuthorAffNew.appendChild(authorInstitution);
	    Element authorAddrLine = document.createElement("addr-line");
	    
	    AuthorAffNew.appendChild(authorAddrLine);
	    Element authorNamedContent = document.createElement("named-content");
	    ((Element) authorNamedContent).setAttribute("content-type", "city");
	    authorAddrLine.appendChild(authorNamedContent);
	    
	    Element authorCountry = document.createElement("country");
	    AuthorAffNew.appendChild(authorCountry);
	    docIngestion.writeDocument(document);
	    
	    //adding author-notes
	    Element authorNotes = document.createElement("author-notes");
	    contribGroup.getParentNode().insertBefore(authorNotes, contribGroup.getNextSibling());
	    
	    Element authorCorresp = document.createElement("corresp");
	    authorCorresp.setAttribute("id", "cor1");
	    authorNotes.appendChild(authorCorresp);
	    
	    Element authorEmail = document.createElement("email");
	    authorCorresp.appendChild(authorEmail);
	    docIngestion.writeDocument(document);
	    
	    //adding pub-date node
	    Node articlePubDate = (Node) xPath.compile("/article/front/article-meta/pub-date").evaluate(document, XPathConstants.NODE);
	    ((Element) articlePubDate).setAttribute("date-type", "pub");
	    ((Element) articlePubDate).setAttribute("iso-8601-date", "");
	    ((Element) articlePubDate).setAttribute("publication-format", "print");
	    
	    Element articlePubDateDay = document.createElement("day");
	    articlePubDate.appendChild(articlePubDateDay);
	    
	    Element articlePubDateMonth = document.createElement("month");
	    articlePubDate.appendChild(articlePubDateMonth);
	    
	    Element articlePubDateYear = document.createElement("year");
	    articlePubDate.appendChild(articlePubDateYear);
	    docIngestion.writeDocument(document);
	    
	    //adding permissions node
	    Element articlePermissions = document.createElement("permissions");
	    articleIdParent.appendChild(articlePermissions);
	    
	    Element articlePermissionsCopStat = document.createElement("copyright-statement");
	    articlePermissions.appendChild(articlePermissionsCopStat);
	    
	    Element articlePermissionsCopYear = document.createElement("copyright-year");
	    articlePermissions.appendChild(articlePermissionsCopYear);
	    
	    Element articlePermissionsCopHolder = document.createElement("copyright-holder");
	    articlePermissions.appendChild(articlePermissionsCopHolder);
	    docIngestion.writeDocument(document);
	}

}
