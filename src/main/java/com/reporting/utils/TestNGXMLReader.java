package com.reporting.utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class TestNGXMLReader {
	public static void read(String paramString) {
		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
			Document localDocument = localDocumentBuilder.parse(paramString);
			localDocument.getDocumentElement().normalize();
			NodeList localNodeList1 = localDocument.getElementsByTagName("atu");
			if (localNodeList1.getLength() > 0) {
				Node localNode = localNodeList1.item(0);
				if (localNode.getNodeType() == 1) {
					Element localElement = (Element) localNode;
					NodeList localNodeList2 = localElement.getElementsByTagName("dir");
					if (localNodeList2.getLength() > 0) {
						// localObject =
						// ((Element)localNodeList2.item(0)).getAttribute("value");
					}
					Object localObject = localElement.getElementsByTagName("header");
					if (((NodeList) localObject).getLength() > 0) {
						String str1 = ((Element) ((NodeList) localObject).item(0))
								.getAttribute("text");
						String str2 = ((Element) ((NodeList) localObject).item(0))
								.getAttribute("logo");
					}
				}
			}
		} catch (ParserConfigurationException localParserConfigurationException) {
		} catch (IOException localIOException) {
		} catch (SAXException localSAXException) {
		}
	}
}

/*
 * Location: D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * 
 * Qualified Name: atu.testng.reports.utils.TestNGXMLReader
 * 
 * JD-Core Version: 0.7.0.1
 */