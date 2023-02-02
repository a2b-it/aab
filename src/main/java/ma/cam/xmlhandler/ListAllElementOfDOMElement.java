package ma.cam.xmlhandler;


import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class ListAllElementOfDOMElement {
    static Logger logger = LoggerFactory.getLogger(ListAllElementOfDOMElement.class);
    int i=0;
    public static void main(String[] args) {
        String data="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><SCPIPayment><msgId>20210623094440</msgId><creDtTm>2021-06-23T09:44:40</creDtTm><SCPIPaymentTypes><SCPIPaymentType><LclInstrmCode>CEE</LclInstrmCode><LclInstrmNm>Virements Europe</LclInstrmNm><LclInstrmSts>ACTF</LclInstrmSts><LclInstrmLimits><LclInstrmLimit><LIMaxAmtJO>16000.00</LIMaxAmtJO><LIMaxAmtJF>32000.00</LIMaxAmtJF><LIMaxAmtCcy>MAD</LIMaxAmtCcy></LclInstrmLimit><LclInstrmLimit><LIMaxAmtJO>0.00</LIMaxAmtJO><LIMaxAmtJF>0.00</LIMaxAmtJF><LIMaxAmtCcy>EUR</LIMaxAmtCcy></LclInstrmLimit><LclInstrmLimit><LIMaxAmtJO>20000.00</LIMaxAmtJO><LIMaxAmtJF>40000.00</LIMaxAmtJF><LIMaxAmtCcy>CAD</LIMaxAmtCcy></LclInstrmLimit></LclInstrmLimits></SCPIPaymentType></SCPIPaymentTypes></SCPIPayment>";
        ListAllElementOfDOMElement inst = new ListAllElementOfDOMElement();
        try {
            Map<String, Object> mapData = inst.getMapXmlKeyValue(data);
            System.out.println(mapData.size());
            Iterator iter = mapData.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> elemenet = (Map.Entry<String, Object>) iter.next();
                System.out.println(elemenet.getKey()+":"+elemenet.getValue());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getMapXmlKeyValue(String xmlData) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        InputSource is = new InputSource(new StringReader(xmlData));

        Document doc = db.parse(is);


        Element rootNode = doc.getDocumentElement();

        NodeList entries = doc.getElementsByTagName(rootNode.getNodeName());

        int num = entries.getLength();

        for (int i = 0; i < num; i++) {
            Element node = (Element) entries.item(i);
            listAllAttributes(map, node);
        }

        return map;

    }

    public static Map<String, Object> listAllAttributes(Map<String, Object> vmap, Element element) {
        String valueTag = "";

        if (element.getNodeType() == Node.ELEMENT_NODE) {
            NodeList list = element.getChildNodes();
            for (int temp = 0; temp < list.getLength(); temp++) {
                org.w3c.dom.Node data = list.item(temp);
                try {
                    valueTag = null;
                    valueTag = data.getNodeValue().trim();
                } catch (Exception e) {
                    //logger.error(e.getMessage());
                }

                if (valueTag != null && valueTag.length() > 0) {
                    getpathTag(vmap, data);
                }

                if (data.getNodeType() == Node.ELEMENT_NODE) {
                    listAllAttributes(vmap, (Element) data);
                }
            }
        }
        return vmap;
    }

    public static void getpathTag(Map<String, Object> vmap1, org.w3c.dom.Node elem) {
        org.w3c.dom.Node currentElem = elem;
        StringBuilder dataStr = new StringBuilder();
        String strvalue = null;
        try {
            strvalue = elem.getNodeValue().trim();
        } catch (Exception e) {
        }

        while (true) {
            try {
                elem = elem.getParentNode();
            } catch (Exception e) {
                break;
            }

            if (elem == null) {
                break;
            }
            try {
                if (elem.getNodeType() == Node.DOCUMENT_NODE) {
                    break;
                }
            } catch (Exception e) {
                break;
            }

            try {
                dataStr.insert(0, "<" + elem.getNodeName() + ">");
            } catch (Exception e) {
                break;
            }
        }

        // get a map containing the attributes of this node
        NamedNodeMap attributes = currentElem.getParentNode().getAttributes();
        // get the number of nodes in this map
        if (attributes != null) {
            int numAttrs = attributes.getLength();
            for (int i = 0; i < numAttrs; i++) {
                Attr attr = (Attr) attributes.item(i);
                String attrName = attr.getNodeName();
                String attrValue = attr.getNodeValue();
                vmap1.put(dataStr.toString() + "<" + attrName + ">", attrValue);
            }
        }

        boolean isExist=vmap1.containsKey(dataStr.toString());
        int i=1;
        while (isExist) {
            while (isExist) {
                isExist = vmap1.containsKey(dataStr.toString()+ "[" + (i++) + "]");
            }
            dataStr = new StringBuilder(dataStr.toString() + "[" + (i-1) + "]");
        }


        vmap1.put(dataStr.toString(), strvalue);
    }
}