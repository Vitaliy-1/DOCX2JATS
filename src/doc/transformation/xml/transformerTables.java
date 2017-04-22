package doc.transformation.xml;

/**
 * @file /src/doc/transformation/xml/transformerTables.java
 *
 * Copyright (c) 2017 Vitaliy Bezsheiko
 * 
 * Distributed under the GNU GPL v3.
 */

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

public class transformerTables {
	
	static void transformerTablesImpl (Document document) throws XPathExpressionException {
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		NodeList tables = (NodeList) xPath.compile("/article/body/sec/table-wrap|/article/body/sec/sec/table-wrap").evaluate(document, XPathConstants.NODESET );
		for (int i = 0; i<tables.getLength(); i++) {
			Node table = tables.item(i);
			
			//set table "id" attribute
			int p = i+1;
			((Element) table).setAttribute("id", "tbl" + p);
			
			//set "caption" 
			Element tableCaption = document.createElement("caption");
			table.insertBefore(tableCaption, table.getFirstChild());
			
			//parse and set text in table "label" element
			Element tableName = customMethods.getPreviousElement(table);
			Element tableLabel = document.createElement("label");
			table.insertBefore(tableLabel, table.getFirstChild());
			Pattern k3 = Pattern.compile("^.+?(?=\\.)"); // all before first dot
			Matcher m3 = k3.matcher(tableName.getTextContent());
			while (m3.find()) {
		    	tableLabel.setTextContent(m3.group()); 
		    }
			
			//parse and set caption children element "title", renaming previous "p" element
			Node tableTitle = customMethods.getPreviousElement(table);
			tableCaption.appendChild(document.renameNode(tableTitle, null, "title"));
			Pattern k1 = Pattern.compile("\\.(.*)"); //group is after the first dote
			Matcher m1 = k1.matcher(tableTitle.getTextContent());
			while (m1.find()) {
				tableTitle.setTextContent(m1.group(1).trim());
			}
			
			//parse and set caption children element "p" (table commentary)
			Element tableCommentary = customMethods.getNextElement(table);
			Pattern k2 = Pattern.compile("^\\s*\\*"); // first * symbol ignoring whitespaces
			if (tableCommentary != null) {
				Matcher m2 = k2.matcher(tableCommentary.getTextContent());
				while (m2.find() && tableCommentary.getTextContent() != null) {
				tableCaption.appendChild(tableCommentary);
			}
			
			}
			
		}
	}

}
