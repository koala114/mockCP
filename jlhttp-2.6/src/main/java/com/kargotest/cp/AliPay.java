package com.kargotest.cp;

import net.freeutils.httpserver.HTTPServer.*;
import net.freeutils.httpserver.HTTPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.simple.*;


public class AliPay implements HTTPServer.ContextHandler {
    private static final Logger logger = LogManager.getLogger(AliPay.class);
    public static Map<String, Double> aliPayRefund = new HashMap<>();
    private String reqStr = "";
    Map<String, String> reqData = new LinkedHashMap<String, String>();
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat paymentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = timeFormat.format(new Date());
    JSONObject extend_params = new JSONObject();

    public int serve(Request req, Response resp) throws IOException {
        String result = "";
        List<String[]> reqParams = null;
        try{
            reqParams = req.getParamsList();
            logger.info("Alipay Request: " + reqParams.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        reqData  = HTTPServer.toMap(reqParams);
        if (reqData.get("extend_params") != null)
            extend_params = (JSONObject) JSONValue.parse(reqData.get("extend_params"));

        if (reqData.get("service").equals("alipay.acquire.createandpay")){
            Document res = createandpay();
            result = toXMLString(res);
            logger.info("Alipay REDEMP Response: " + result);
            if(reqData.get("total_fee") != null){
                double total_fee = Double.valueOf(reqData.get("total_fee"));
                aliPayRefund.put(reqData.get("out_trade_no"), 0.0);
            }
        } else if (reqData.get("service").equals("alipay.acquire.cancel")){
            Document res = reversal();
            result = toXMLString(res);
            logger.info("Alipay RSVAL Response: " + result);
        } else if(reqData.get("service").equals("alipay.acquire.refund")){
            Document res = refund();
            result = toXMLString(res);
            logger.info("Alipay REFUND Response: " + result);
        }
        resp.getHeaders().add("Content-Type", "application/xml");
        resp.send(200, result);
        return 0;
    }

    public Document createandpay(){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("alipay");
            document.appendChild(root);

            Map<String, String> elements = Stream.of(new String[][]{
                    {"is_success", "T"},
                    {"request", ""},
                    {"response", ""},
                    {"sign", "42ea73520baaadbcb1b6ab174f2791f3"},
                    {"sign_type", "MD5"},
            }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

            System.out.println(elements);
            for (Map.Entry<String, String> entry : elements.entrySet()
            ) {

                String key = entry.getKey();
                String value = entry.getValue();
                Element subElement = document.createElement(key);
                if(subElement.getTagName().equals("request")) {
                    for(Map.Entry<String, String> e : reqData.entrySet()){
                        Element param = document.createElement("param");
                        param.setAttribute("name", e.getKey());
                        param.setTextContent(e.getValue());
                        subElement.appendChild(param);
                    }
                    root.appendChild(subElement);
                } else if(subElement.getTagName().equals("response")){
                    Element responseElement = document.createElement("alipay");
                    Map<String, String> alipayElements = new HashMap<>();
                    alipayElements.put("buyer_logon_id", "for***@" + date + ".com");
                    alipayElements.put("buyer_user_id", "2088" + date);
                    alipayElements.put("fund_change", "Y");
                    alipayElements.put("gmt_refund_pay", paymentFormat.format(new Date()));
                    alipayElements.put("out_trade_no", reqData.get("out_trade_no"));
                    alipayElements.put("result_code", "ORDER_SUCCESS_PAY_SUCCESS");
                    alipayElements.put("total_fee", reqData.get("total_fee"));
                    alipayElements.put("trade_no", date + "41");
                    for(Map.Entry<String, String> e : alipayElements.entrySet()){
                        Element subAlipayElement = document.createElement(e.getKey());
                        subAlipayElement.setTextContent(e.getValue());
                        responseElement.appendChild(subAlipayElement);
                    }
                    Element fund_bill_list = document.createElement("refund_detail_item_list");
                    Element tradeFundBill = document.createElement("TradeFundBill");
                    Map<String, String> subTradeFundBill = new HashMap<>();
                    subTradeFundBill.put("amount", reqData.get("total_fee"));
                    subTradeFundBill.put("force_use_cash", "F");
                    subTradeFundBill.put("fund_channel", "PCREDIT");
                    for(Map.Entry<String, String> e : subTradeFundBill.entrySet()){
                        Element subTradeFundBillElement = document.createElement(e.getKey());
                        subTradeFundBillElement.setTextContent(e.getValue());
                        tradeFundBill.appendChild(subTradeFundBillElement);
                    }
                    fund_bill_list.appendChild(tradeFundBill);
                    responseElement.appendChild(fund_bill_list);
                    subElement.appendChild(responseElement);
                    root.appendChild(subElement);
                }
                else{
                    subElement.setTextContent(value);
                    root.appendChild(subElement);
                }
            }
            return document;
        }catch (ParserConfigurationException parserConfigurationException){
            System.out.println(parserConfigurationException.getStackTrace());
        }
    return null;
    }

    public Document refund(){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("alipay");
            document.appendChild(root);

            Map<String, String> elements = Stream.of(new String[][]{
                    {"is_success", "T"},
                    {"request", ""},
                    {"response", ""},
                    {"sign", "40bca394a3d25d22c5ef6e8cf1240a2a"},
                    {"sign_type", "MD5"},
            }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

            System.out.println(elements);
            for (Map.Entry<String, String> entry : elements.entrySet()
            ) {

                String key = entry.getKey();
                String value = entry.getValue();
                Element subElement = document.createElement(key);
                if(subElement.getTagName().equals("request")) {
                    for(Map.Entry<String, String> e : reqData.entrySet()){
                        Element param = document.createElement("param");
                        param.setAttribute("name", e.getKey());
                        param.setTextContent(e.getValue());
                        subElement.appendChild(param);
                    }
                    root.appendChild(subElement);
                } else if(subElement.getTagName().equals("response")){
                    String out_trade_no = reqData.get("out_trade_no");
                    Element responseElement = document.createElement("alipay");
                    Map<String, String> alipayElements = new HashMap<>();
                    alipayElements.put("buyer_logon_id", "for***@" + date + ".com");
                    alipayElements.put("buyer_user_id", "2088" + date);
                    alipayElements.put("gmt_payment", paymentFormat.format(new Date()));
                    alipayElements.put("out_trade_no", reqData.get("out_trade_no"));
                    alipayElements.put("result_code", "SUCCESS");
                    double total_refund = Double.valueOf(reqData.get("refund_amount")) + aliPayRefund.get(out_trade_no);
                    alipayElements.put("refund_fee", Double.toString(total_refund));
                    aliPayRefund.put(out_trade_no, total_refund);
                    alipayElements.put("trade_no", date + "41");
                    for(Map.Entry<String, String> e : alipayElements.entrySet()){
                        Element subAlipayElement = document.createElement(e.getKey());
                        subAlipayElement.setTextContent(e.getValue());
                        responseElement.appendChild(subAlipayElement);
                    }
                    Element fund_bill_list = document.createElement("fund_bill_list");
                    Element tradeFundBill = document.createElement("TradeFundBill");
                    Map<String, String> subTradeFundBill = new HashMap<>();
                    subTradeFundBill.put("amount", reqData.get("refund_amount"));
                    subTradeFundBill.put("force_use_cash", "F");
                    subTradeFundBill.put("fund_channel", "90");
                    for(Map.Entry<String, String> e : subTradeFundBill.entrySet()){
                        Element subTradeFundBillElement = document.createElement(e.getKey());
                        subTradeFundBillElement.setTextContent(e.getValue());
                        tradeFundBill.appendChild(subTradeFundBillElement);
                    }
                    fund_bill_list.appendChild(tradeFundBill);
                    responseElement.appendChild(fund_bill_list);
                    subElement.appendChild(responseElement);
                    root.appendChild(subElement);
                }
                else{
                    subElement.setTextContent(value);
                    root.appendChild(subElement);
                }
            }
            return document;
        }catch (ParserConfigurationException parserConfigurationException){
            System.out.println(parserConfigurationException.getStackTrace());
        }
        return null;
    }

    public Document reversal(){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("alipay");
            document.appendChild(root);

            Map<String, String> elements = Stream.of(new String[][]{
                    {"is_success", "T"},
                    {"request", ""},
                    {"response", ""},
                    {"sign", "40bca394a3d25d22c5ef6e8cf1240a2a"},
                    {"sign_type", "MD5"},
            }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

            System.out.println(elements);
            for (Map.Entry<String, String> entry : elements.entrySet()
            ) {

                String key = entry.getKey();
                String value = entry.getValue();
                Element subElement = document.createElement(key);
                if(subElement.getTagName().equals("request")) {
                    for(Map.Entry<String, String> e : reqData.entrySet()){
                        Element param = document.createElement("param");
                        param.setAttribute("name", e.getKey());
                        param.setTextContent(e.getValue());
                        subElement.appendChild(param);
                    }
                    root.appendChild(subElement);
                } else if(subElement.getTagName().equals("response")){
                    Element responseElement = document.createElement("alipay");
                    Map<String, String> alipayElements = new HashMap<>();
                    alipayElements.put("action", "refund");
                    alipayElements.put("out_trade_no", reqData.get("out_trade_no"));
                    alipayElements.put("result_code", "SUCCESS");
                    alipayElements.put("retry_flag", "N");
                    alipayElements.put("trade_no", date + "41");
                    for(Map.Entry<String, String> e : alipayElements.entrySet()){
                        Element subAlipayElement = document.createElement(e.getKey());
                        subAlipayElement.setTextContent(e.getValue());
                        responseElement.appendChild(subAlipayElement);
                    }
                    subElement.appendChild(responseElement);
                    root.appendChild(subElement);
                }
                else{
                    subElement.setTextContent(value);
                    root.appendChild(subElement);
                }
            }
            return document;
        }catch (ParserConfigurationException parserConfigurationException){
            System.out.println(parserConfigurationException.getStackTrace());
        }
        return null;
    }
    public String toXMLString(Document document) {
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(sw);
            transformer.transform(domSource, streamResult);

        } catch (TransformerException tfe) {
            System.out.println(tfe.getStackTrace());
        }
        String result = sw.toString();
        sw.getBuffer().trimToSize();
        return result;
    }
}
