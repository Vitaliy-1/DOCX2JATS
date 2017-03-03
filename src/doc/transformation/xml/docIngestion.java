package doc.transformation.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import javax.xml.parsers.ParserConfigurationException;
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
 
    public static void main(String[] args) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, IOException, ParserConfigurationException, SAXException {
    	String docxInputFile = args[0];
    	String newJATSOutput = args[1];
    	
    	ingestionAndTransform(docxInputFile, newJATSOutput);
       
    }

	private static void ingestionAndTransform(String docxInputFile, String newJATSOutput)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException,
			XPathExpressionException, FileNotFoundException {
		File buildFile = new File("Stylesheets/docx/build-from.xml");
    	Project project = new Project();
    		
    	project.setUserProperty("ant.file", buildFile.getAbsolutePath());     
    	DefaultLogger consoleLogger = new DefaultLogger();
    	consoleLogger.setErrorPrintStream(System.err);
    	consoleLogger.setOutputPrintStream(System.out);
    	consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    	project.addBuildListener(consoleLogger);
    	String teiOutput = "E:/Workspace/Test/1.xml";
    	
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
        Transformer transformer = tFactory.newTransformer(new StreamSource(new File("Stylesheets/nlm/tei_to_nlm.xsl")));
        transformer.transform(new StreamSource(new File(teiOutput)),
                              teiResult);
    
    	Document document = (Document) teiResult.getNode();
	            
	    transformerArticleBack.transformerArticleBack(document);
	    transformerBiblAMA.transformerBiblFinder(document);
	    transformerFigures.transformerFiguresImpl(document);
	    transformerTables.transformerTablesImpl(document);
	    transformerInTextCit.textTransformCitations(document);
	    transformerFigRefs.transformerFigRefsImpl(document);
	    transformerTableRefs.transformerTableRefsImpl(document);
	    transformerMeta.textTransformMetaImpl(document);
	    transformerAbstractKey.transformerAbstractKeyImpl(document);
	            
	    Transformer tr = TransformerFactory.newInstance().newTransformer();
	    DOMSource source = new DOMSource(document);
	    FileOutputStream fos = new FileOutputStream(newJATSOutput);
	    StreamResult result = new StreamResult(fos);
	    tr.transform(source, result);
	}    
    
   
}