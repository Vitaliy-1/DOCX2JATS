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

 
public class docIngestion {
 
    public static void main(String[] args) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, IOException, ParserConfigurationException, SAXException {
        
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse("../forparsing/tarasov_for_jats.xml");
            
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
            FileOutputStream fos = new FileOutputStream("../forparsing/other.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
       
    }    
    
}