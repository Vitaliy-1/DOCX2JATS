package doc.transformation.docx;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class docxTransform {
	
	public static void antFileTransform(String inputFile, String jatsxml) throws TransformerException {
	File buildFile = new File("Stylesheets/docx/build-from.xml");
	Project project = new Project();
		
	project.setUserProperty("ant.file", buildFile.getAbsolutePath());     
	DefaultLogger consoleLogger = new DefaultLogger();
	consoleLogger.setErrorPrintStream(System.err);
	consoleLogger.setOutputPrintStream(System.out);
	consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
	project.addBuildListener(consoleLogger);
	String outputFile = "E:/Workspace/Test/1.xml";
	
	try {
	    project.fireBuildStarted();
	    project.init();
	    ProjectHelper helper = ProjectHelper.getProjectHelper();
	    project.addReference("ant.projectHelper", helper);
	   
	    helper.parse(project, buildFile);
	  
	    
	    project.setUserProperty("inputFile", inputFile);
	    project.setUserProperty("outputFile", outputFile);
	  
	    project.executeTarget(project.getDefaultTarget());
	    project.fireBuildFinished(null);
	} catch (BuildException e) {
	    project.fireBuildFinished(e);
	}
	
	System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
    teiFileTransform(outputFile, "Stylesheets/nlm/tei_to_nlm.xsl", jatsxml);
}

public static void teiFileTransform(String inputPath, String xsltPath, String outputPath) throws TransformerException {
    TransformerFactory tFactory = TransformerFactory.newInstance();
   
        Transformer transformer = tFactory.newTransformer(new StreamSource(new File(xsltPath)));
        transformer.transform(new StreamSource(new File(inputPath)),
                              new StreamResult(new File(outputPath)));
    
}

}
