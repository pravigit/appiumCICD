package com.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class TestUtils {

    public static final long NORMAL_WAIT = 10;

    public Map<String, String> parseStringXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {

        HashMap<String, String> stringMap = new HashMap<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("string");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                stringMap.put(element.getAttribute("name"), element.getTextContent());
            }
        }
        return stringMap;
    }

    public String dateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }
}
