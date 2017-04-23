package doc.transformation.xml;

/**
 * @file /src/doc/transformation/xml/docIngestion.java
 *
 * Copyright (c) 2017 Vitaliy Bezsheiko
 * 
 * Distributed under the GNU GPL v3.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


 
public class docIngestion {
 
    public static void main(String[] args) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, IOException, ParserConfigurationException, SAXException, CustomExceptions, URISyntaxException {
    	String docxInputFile = args[0];
    	if (!(docxInputFile.endsWith("docx"))) {
    		throw new CustomExceptions("Input file extension must be .docx");
    	}
    	String newJATSOutput = args[1];
    	
    	ingestionAndTransform(docxInputFile, newJATSOutput);
       
    }

	private static void ingestionAndTransform(String docxInputFile, String newJATSOutput)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException,
			XPathExpressionException, FileNotFoundException, URISyntaxException {
		
		/* Fixing path. Really don't know
		 * how to do it with apache ant 
		 */
		CodeSource codeSource = docIngestion.class.getProtectionDomain().getCodeSource();
		File jarFile = new File(codeSource.getLocation().toURI().getPath());
		String jarDir = jarFile.getParentFile().getPath();
		
		
		// fixing filepath to tei.xsl
		Pattern p = Pattern.compile("[A-Z]:");
		Matcher m = p.matcher(docxInputFile);
		if (!m.find()) {
			URL url = ClassLoader.getSystemResource(docxInputFile);
			if (url != null) {
			    File file = new File(url.toURI());
			    docxInputFile = file.getAbsolutePath();
			}
		} 
    	
		File buildFile = new File(jarDir + "/stylesheets/docx/build-from.xml");
    	Project project = new Project();
    		
    	project.setUserProperty("ant.file", buildFile.getAbsolutePath());     
    	DefaultLogger consoleLogger = new DefaultLogger();
    	consoleLogger.setErrorPrintStream(System.err);
    	consoleLogger.setOutputPrintStream(System.out);
    	consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    	project.addBuildListener(consoleLogger);
    	String teiOutput = "../temp/tei.xml";
    	try {
    	    project.fireBuildStarted();
    	    project.init();
    	    ProjectHelper helper = ProjectHelper.getProjectHelper();
    	    project.addReference("ant.projectHelper", helper);
    	   
    	    helper.parse(project, buildFile);
    	   
    	    project.setUserProperty("inputFile", docxInputFile);
    	    project.setUserProperty("outputFile", teiOutput);
    	    
    	    project.executeTarget(project.getDefaultTarget());
    	    project.fireBuildFinished(null);
    	    
    	   
    	} catch (BuildException e) {
    	    project.fireBuildFinished(e);
    	}
    	
    	
        
    	System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
    	TransformerFactory tFactory = TransformerFactory.newInstance();
    	DOMResult teiResult = new DOMResult();
    	
        Transformer transformer = tFactory.newTransformer(new StreamSource(new File(jarDir + "/stylesheets/nlm/tei_to_nlm.xsl")));
        transformer.transform(new StreamSource(new File(jarDir + "/stylesheets/temp/" + teiOutput)),
                              teiResult);
    
    	Document document = (Document) teiResult.getNode();
	            
	    transformerArticleBack.transformerArticleBackMethod(document);
	    transformerBiblAMA.transformerBiblFinder(document);
	    transformerFigures.transformerFiguresImpl(document);
	    transformerTables.transformerTablesImpl(document);
	    transformerInTextCit.textTransformCitations(document);
	    transformerFigRefs.transformerFigRefsImpl(document);
	    transformerTableRefs.transformerTableRefsImpl(document);
	    transformerMeta.textTransformMetaImpl(document);
	    transformerAbstractKey.transformerAbstractKeyImpl(document);
	            
	    Transformer tr = TransformerFactory.newInstance().newTransformer();
	    tr.setOutputProperty(OutputKeys.INDENT, "yes");
	    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	    DOMSource source = new DOMSource(document);
	    FileOutputStream fos = new FileOutputStream(newJATSOutput);
	    StreamResult result = new StreamResult(fos);
	    tr.transform(source, result);
	}    
    
   
}