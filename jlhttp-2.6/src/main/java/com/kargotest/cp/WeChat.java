package com.kargotest.cp;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
/pay/micropay
<xml>
    <appid>wx14061433c3a0c284</appid>
    <attach>KC WeChat payment</attach>
    <auth_code>134663003197578976</auth_code>
    <body>BuzzTimeKargo15F消费</body>
    <device_info>40420888802</device_info>
    <mch_id>1344479201</mch_id>
    <nonce_str>vtlc54gv9nu0chskzc9e</nonce_str>
    <out_trade_no>000823138415</out_trade_no>
    <spbill_create_ip>10.161.168.50</spbill_create_ip>
    <sub_mch_id>1401872502</sub_mch_id>
    <time_start>20210722113134</time_start>
    <time_expire>20210723113134</time_expire>
    <total_fee>1</total_fee>
    <sign>2BC45DEE08CBB3B16435437B1629F79F</sign>
</xml>

<xml>
    <appid>wx5f969321cf58a9d5</appid>
    <attach>KC WeChat payment</attach>
    <auth_code>134558555135007985</auth_code>
    <body>创愿测试平台公众号3Qmart-测试总店消费</body>
    <deevice_info>20130603033</device_info>
    <mch_id>1246033102</mch_id>
    <nonce_str>k8kxf7b7n46wz011698h</nonce_str>
    <out_trade_no>000820346312</out_trade_no>
    <spbill_create_ip>10.161.168.50</spbill_create_ip>
    <time_start>20210621134413</time_start>
    <time_expire>20210622134413</time_expire>
    <total_fee>1</total_fee>
    <sign>2CC58B8A7689A1DE6693ADDA20304E59</sign>
</xml>

 */

public class WeChat {
    private StringWriter sw = new StringWriter();
    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    public static Map<String, Object> saveRequestObject = new HashMap<>();
    public static Map<String, Integer> limitQueryTime = new HashMap<>();

    public Document getPayDoc(Map<String, String> map) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = timeFormat.format(new Date());

        Map<String, String> elements = Stream.of(new String[][]{
                {"return_code", "SUCCESS"},
                {"return_msg", "OK"},
                {"appid", map.get("appid")},
                {"mch_id", map.get("mch_id")},
                {"device_info", map.get("device_info")},
                {"nonce_str", "z4fr8COR95DHtOX4"},
                {"sign", "174F0D6E4A405112D71413793940C011"},
                {"result_code", "SUCCESS"},
                {"openid", "oplZpvwvMnm1E04jVlXU8nqRpOr0"},
                {"is_subscribe", "Y"},
                {"trade_type", "MICROPAY"},
                {"total_fee", map.get("total_fee")},
                {"fee_type", "CNY"},
                {"transaction_id", "4200001025"+ date + "6576"},
                {"out_trade_no", map.get("out_trade_no")},
                {"attach", "KC WeChat payment"},
                {"time_end", map.get("time_expire")},
                {"cash_fee", map.get("total_fee")},
                {"cash_fee_type", "CNY"}

        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        for (Map.Entry<String, String> entry : elements.entrySet()
        ) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element subElement = document.createElement(key);
            if(subElement.getTagName().equals("total_fee")||subElement.getTagName().equals("cash_fee")) {
                subElement.setTextContent(value);
            }
            else{
                Node vElement= document.createCDATASection(value);
                subElement.appendChild(vElement);
            }
            root.appendChild(subElement);
        }
        return document;
    }

    public Document getQueryResult(Map<String, String> map) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = timeFormat.format(new Date());

        Map<String, String> elements = Stream.of(new String[][]{
                {"return_code", "SUCCESS"},
                {"return_msg", "OK"},
                {"appid", map.get("appid")},
                {"mch_id", map.get("mch_id")},
                {"device_info", map.get("device_info")},
                {"nonce_str", "z4fr8COR95DHtOX4"},
                {"sign", "174F0D6E4A405112D71413793940C011"},
                {"result_code", "SUCCESS"},
                {"openid", "oplZpvwvMnm1E04jVlXU8nqRpOr0"},
                {"is_subscribe", "Y"},
                {"trade_type", "MICROPAY"},
                {"bank_type", "CFT"},
                {"total_fee", map.get("total_fee")},
                {"fee_type", "CNY"},
                {"transaction_id", "4200001025"+ date + "6576"},
                {"out_trade_no", map.get("out_trade_no")},
                {"attach", "KC WeChat payment"},
                {"time_end", map.get("time_expire")},
                {"cash_fee", map.get("total_fee")},
                {"trade_state", "SUCCESS"},
                {"trade_state_desc", "支付成功"}

        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        for (Map.Entry<String, String> entry : elements.entrySet()
        ) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element subElement = document.createElement(key);
            if(subElement.getTagName().equals("total_fee")||subElement.getTagName().equals("cash_fee")) {
                subElement.setTextContent(value);
            }
            else{
                Node vElement= document.createCDATASection(value);
                subElement.appendChild(vElement);
            }
            root.appendChild(subElement);
        }
        return document;
    }

    public Document getRefundDoc(Map<String, String> map) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = timeFormat.format(new Date());

        Map<String, String> elements = Stream.of(new String[][]{
                {"return_code", "SUCCESS"},
                {"return_msg", "OK"},
                {"appid", map.get("appid")},
                {"mch_id", map.get("mch_id")},
                {"device_info", map.get("device_info")},
                {"nonce_str", "z4fr8COR95DHtOX4"},
                {"sign", "174F0D6E4A405112D71413793940C011"},
                {"result_code", "SUCCESS"},
                {"transaction_id", "4200001025"+ date + "6576"},
                {"out_trade_no", map.get("out_trade_no")},
                {"out_refund_no", map.get("out_refund_no")},
                {"refund_id", "5030100941"+ date +"16599"},
                {"refund_channel", "AAA"},
                {"refund_fee", map.get("refund_fee")},
                {"coupon_refund_fee", "0"},
                {"total_fee", map.get("total_fee")},
                {"cash_fee", map.get("total_fee")},
                {"coupon_refund_count", "0"},
                {"cash_refund_fee", map.get("refund_fee")}

        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        for (Map.Entry<String, String> entry : elements.entrySet()
        ) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element subElement = document.createElement(key);
            List<String> doNotCDATASection = new ArrayList<String>();
            doNotCDATASection.add("total_fee");
            doNotCDATASection.add("cash_fee");
            doNotCDATASection.add("refund_fee");
            doNotCDATASection.add("coupon_refund_fee");
            doNotCDATASection.add("coupon_refund_count");
            doNotCDATASection.add("cash_refund_fee");
            if(doNotCDATASection.contains(subElement.getTagName())) {
                subElement.setTextContent(value);
            }
            else{
                Node vElement= document.createCDATASection(value);
                subElement.appendChild(vElement);
            }
            root.appendChild(subElement);
        }
        return document;
    }

    public Document getRVSALDoc(Map<String, String> map) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);

        Map<String, String> elements = Stream.of(new String[][]{
                {"return_code", "SUCCESS"},
                {"return_msg", "OK"},
                {"appid", map.get("appid")},
                {"mch_id", map.get("mch_id")},
                {"nonce_str", "z4fr8COR95DHtOX4"},
                {"sign", "174F0D6E4A405112D71413793940C011"},
                {"result_code", "SUCCESS"},
                {"recall", "N"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        for (Map.Entry<String, String> entry : elements.entrySet()
        ) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element subElement = document.createElement(key);
            Node vElement= document.createCDATASection(value);
            subElement.appendChild(vElement);

            root.appendChild(subElement);
        }
        return document;
    }

    public Document getQueryDoc(Map<String, String> map) throws ParserConfigurationException {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);

        Map<String, String> elements = Stream.of(new String[][]{
                {"return_code", "SUCCESS"},
                {"return_msg", "OK"},
                {"appid", map.get("appid")},
                {"mch_id", map.get("mch_id")},
                {"nonce_str", "z4fr8COR95DHtOX4"},
                {"sign", "174F0D6E4A405112D71413793940C011"},
                {"result_code", "SUCCESS"},
                {"out_trade_no", map.get("out_trade_no")},
                {"attach", ""},
                {"trade_state_desc", "需要用户输入支付密码"},
                {"trade_state", "USERPAYING"}

        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        for (Map.Entry<String, String> entry : elements.entrySet()
        ) {
            String key = entry.getKey();
            String value = entry.getValue();
            Element subElement = document.createElement(key);
            Node vElement= document.createCDATASection(value);
            subElement.appendChild(vElement);

            root.appendChild(subElement);
        }
        return document;
    }

    public String toXMLString(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(sw);
            transformer.transform(domSource, streamResult);

        } catch (TransformerException tfe) {
            System.out.println(tfe.getStackTrace());
        }
        return sw.toString();
    }

    public Map<String, String> parseXMLString(String xmlStr) {
        Map<String, String> xml2Map = new HashMap<>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlStr));
            document = docBuilder.parse(is);
            Element elements = (Element)document.getElementsByTagName("xml").item(0);
            NodeList children = elements.getChildNodes();

            for (int i=0;i<children.getLength();i++){
                Element element = (Element) children.item(i);
                xml2Map.put(element.getTagName(), element.getTextContent());

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml2Map;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
