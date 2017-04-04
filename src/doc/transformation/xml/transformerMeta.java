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
	
	static void textTransformMetaImpl(Document document) throws XPathExpressionException {
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		/* adding Journal-Id */
	    Node journalIdValue = (Node) xPath.compile("/article/front/journal-meta/journal-id").evaluate(document, XPathConstants.NODE);
	    Text textJournalId = document.createTextNode("Psychosomatic Medicine and General Practice");
	    journalIdValue.appendChild(textJournalId);
	    
	    /* adding ISSN */
	    Node journalIssn = (Node) xPath.compile("/article/front/journal-meta/issn").evaluate(document, XPathConstants.NODE);
	    Text textJournalIssn = document.createTextNode("2519-8572");
	    journalIssn.appendChild(textJournalIssn);    
	    
	    /* adding publisher name */
	    Node publisherName = (Node) xPath.compile("/article/front/journal-meta/publisher/publisher-name").evaluate(document, XPathConstants.NODE);
	    Text textPublisherName = document.createTextNode("Private Publisher 'Chaban O. S.'");
	    publisherName.appendChild(textPublisherName);
	       
	    /* adding article-id */
	    Node articleMeta = (Node) xPath.compile("/article/front/article-meta").evaluate(document, XPathConstants.NODE);
	    Node articleIdPublisher = document.createElement("article-id");
	    		((Element) articleIdPublisher).setAttribute("pub-id-type", "publisher-id");
	    		articleIdPublisher.setTextContent(" ");
	    articleMeta.insertBefore(articleIdPublisher, articleMeta.getFirstChild());
	       
	    /* removing subtitle */
	    Node articleSubtitle = (Node) xPath.compile("/article/front/article-meta/title-group/subtitle").evaluate(document, XPathConstants.NODE);
	    articleSubtitle.getParentNode().removeChild(articleSubtitle);  
	    
	    /* removing alt-title */
	    NodeList articleAltTitle = (NodeList) xPath.compile("/article/front/article-meta/title-group/alt-title").evaluate(document, XPathConstants.NODESET);
	    for (int i = 0; i < articleAltTitle.getLength(); i++) {
	    	Node altTitle = articleAltTitle.item(i);
	        altTitle.getParentNode().removeChild(altTitle);
	        
	    }
	       
	    //adding Ukrainian title
	    Node articleTitleParent = (Node) xPath.compile("/article/front/article-meta/title-group").evaluate(document, XPathConstants.NODE);
	    Node articleTitleUkrainian = document.createElement("article-title");
	             ((Element) articleTitleUkrainian).setAttribute("xml:lang", "uk-UA");
	             ((Element) articleTitleUkrainian).setTextContent(" ");
	    articleTitleParent.appendChild(articleTitleUkrainian);
	    
	    /* adding attributes to article tag */
	    Node articleElement = (Node) xPath.compile("/article").evaluate(document, XPathConstants.NODE);
	    ((Element) articleElement).setAttribute("xml:lang", "uk");
	    ((Element) articleElement).setAttribute("dtd-version", "1.1");
	    ((Element) articleElement).setAttribute("article-type", "review");
	    ((Element) articleElement).setAttribute("specific-use", "production");
	    ((Element) articleElement).setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
	    
	    /* adding name, surname, given-names */
	    Node authorNameParent = (Node) xPath.compile("/article/front/article-meta/contrib-group/contrib").evaluate(document,  XPathConstants.NODE);
	    Element authorName = document.createElement("name");
	    authorName.setAttribute("name-style", "western");
	    authorNameParent.insertBefore(authorName, authorNameParent.getFirstChild());
	    
	    Element authorSurname = document.createElement("surname");
	    authorSurname.setTextContent(" ");
	    Element authorGivenNames = document.createElement("given-names");
	    authorGivenNames.setTextContent(" ");
	    authorName.insertBefore(authorGivenNames, authorName.getFirstChild());
	    authorName.insertBefore(authorSurname, authorName.getFirstChild());
	    
	    /* adding xref */
	    Element authorXrefAff = document.createElement("xref");
	    authorXrefAff.setTextContent(" ");
	    authorXrefAff.setAttribute("ref-type", "aff");
	    authorXrefAff.setAttribute("rid", "aff1");
	    authorNameParent.appendChild(authorXrefAff);
	    Element authorXrefFn = document.createElement("xref");
	    authorXrefFn.setAttribute("ref-type", "fn");
	    authorXrefFn.setAttribute("rid", "conf1");
	    authorNameParent.appendChild(authorXrefFn);
	    Element authorXrefCorresp = document.createElement("xref");
	    authorXrefCorresp.setAttribute("ref-type", "corresp");
	    authorXrefCorresp.setAttribute("rid", "cor1");
	    
	    authorNameParent.appendChild(authorXrefCorresp);
	    
	    /* transferring aff */
	    Node authorAffil = (Node) xPath.compile("/article/front/article-meta/contrib-group/contrib/aff").evaluate(document,  XPathConstants.NODE);
	    authorAffil.getParentNode().removeChild(authorAffil);
	    Element AuthorAffNew = document.createElement("aff");
	    AuthorAffNew.setAttribute("id", "aff1");
	    Node contribGroup = (Node) xPath.compile("/article/front/article-meta/contrib-group").evaluate(document, XPathConstants.NODE);
	    ((Element) contribGroup).appendChild(AuthorAffNew);
	   
	    /* adding elements to aff */
	    Element authorInstitution = document.createElement("institution");
	    authorInstitution.setAttribute("content-type", "dept");
	    authorInstitution.setTextContent(" ");
	    AuthorAffNew.appendChild(authorInstitution);
	    Element authorAddrLine = document.createElement("addr-line");
	    
	    AuthorAffNew.appendChild(authorAddrLine);
	    Element authorNamedContent = document.createElement("named-content");
	    authorNamedContent.setAttribute("content-type", "city");
	    authorNamedContent.setTextContent(" ");
	    authorAddrLine.appendChild(authorNamedContent);
	    
	    Element authorCountry = document.createElement("country");
	    authorCountry.setTextContent(" ");
	    AuthorAffNew.appendChild(authorCountry);
	    
	    /* adding author-notes */
	    Element authorNotes = document.createElement("author-notes");
	    contribGroup.getParentNode().insertBefore(authorNotes, contribGroup.getNextSibling());
	    
	    Element authorCorresp = document.createElement("corresp");
	    authorCorresp.setAttribute("id", "cor1");
	    authorNotes.appendChild(authorCorresp);
	    
	    Element authorEmail = document.createElement("email");
	    authorEmail.setTextContent(" ");
	    authorCorresp.appendChild(authorEmail);
	    
	    /* adding pub-date node */
	    Node articlePubDate = (Node) xPath.compile("/article/front/article-meta/pub-date").evaluate(document, XPathConstants.NODE);
	    ((Element) articlePubDate).setAttribute("date-type", "pub");
	    ((Element) articlePubDate).setAttribute("iso-8601-date", "");
	    ((Element) articlePubDate).setAttribute("publication-format", "print");
	    
	    Element articlePubDateDay = document.createElement("day");
	    articlePubDateDay.setTextContent(" ");
	    articlePubDate.appendChild(articlePubDateDay);
	    
	    Element articlePubDateMonth = document.createElement("month");
	    articlePubDateMonth.setTextContent(" ");
	    articlePubDate.appendChild(articlePubDateMonth);
	    
	    Element articlePubDateYear = document.createElement("year");
	    articlePubDateYear.setTextContent(" ");
	    articlePubDate.appendChild(articlePubDateYear);
	    
	    /* adding permissions node */
	    Element articlePermissions = document.createElement("permissions");
	    articleMeta.appendChild(articlePermissions);
	    
	    Element articlePermissionsCopStat = document.createElement("copyright-statement");
	    articlePermissionsCopStat.setTextContent("© 2017,");
	    articlePermissions.appendChild(articlePermissionsCopStat);
	    
	    Element articlePermissionsCopYear = document.createElement("copyright-year");
	    articlePermissionsCopYear.setTextContent("2017");
	    articlePermissions.appendChild(articlePermissionsCopYear);
	    
	    Element articlePermissionsCopHolder = document.createElement("copyright-holder");
	    articlePermissionsCopHolder.setTextContent(" ");
	    articlePermissions.appendChild(articlePermissionsCopHolder);
	    
	    Element license = document.createElement("license");
	    license.setAttribute("xlink:href", "https://creativecommons.org/licenses/by/4.0/");
	    articlePermissions.appendChild(license);
	    
	    Element license_p = document.createElement("license-p");
	    license.appendChild(license_p);
	    
	    Text licenseText1 = document.createTextNode("This article is distributed under the terms of the ");
	    Element licenseLink = document.createElement("ext-link");
	    licenseLink.setAttribute("ext-link-type", "uri");
	    licenseLink.setAttribute("xlink:href", "http://creativecommons.org/licenses/by/4.0/");
	    licenseLink.setTextContent("Creative Commons Attribution License");
	    Text licenseText2 = document.createTextNode(", which permits unrestricted use and redistribution provided that the original author and source are credited.");
	    license_p.appendChild(licenseText1);
	    license_p.appendChild(licenseLink);
	    license_p.appendChild(licenseText2);
	    
	    /* add abstract node */
	    Element articleAbstract = document.createElement("abstract");
	    articleAbstract.setAttribute("abstract-type", "section");
		articleMeta.appendChild(articleAbstract);
		
		/* add translated abstract node */
		Element articleTransAbstract = document.createElement("trans-abstract");
		articleTransAbstract.setAttribute("xml:lang", "uk");
		articleMeta.appendChild(articleTransAbstract);
		
		/* add kwd-group node, title and keywords */
		Element kwdGroup = document.createElement("kwd-group");
		kwdGroup.setAttribute("kwd-group-type", "author-keywords");
		articleMeta.appendChild(kwdGroup);
	    
		Element kwdGroupTitle = document.createElement("title");
		kwdGroupTitle.setTextContent("Keywords");
		kwdGroup.appendChild(kwdGroupTitle);
		//we may have several keywords actually
		for (int i = 0; i < 4; i++) {
			Element keywords = document.createElement("kwd");
			keywords.setTextContent(" ");
			kwdGroup.appendChild(keywords);
		}
		
		
	}

}
