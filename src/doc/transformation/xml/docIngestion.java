package doc.transformation.xml;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import doc.transformation.docx.docxTransform;

 
public class docIngestion {
 
    public static void main(String[] args) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, IOException, ParserConfigurationException, SAXException {
    	String outputFile = "../../2.xml";
    	
    	    
    	docxTransform.antFileTransform("../../example.docx", outputFile);
    	//docxTransform.teiFileTransform(outputFile, "Stylesheets/nlm/tei_to_nlm.xsl", teiOutput);
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse(outputFile);
            
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
            FileOutputStream fos = new FileOutputStream("other.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
       
    }    
    
}