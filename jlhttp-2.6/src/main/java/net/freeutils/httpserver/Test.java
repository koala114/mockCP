package net.freeutils.httpserver;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {

    public void createWeChatXml(){

        try{
            String xmlStr = "<xml><appid>wx5f969321cf58a9d5</appid><attach>208888</attach><auth_code>134605723733336326</auth_code><body>创愿测平台公众号2南京西路店消费</body><device_info>00062006371</device_info><goods_tag>refund_test121</goods_tag><mch_id>1246033102</mch_id><nonce_str>pq692eaenmgrpq692eaenmgrqc85vug9</nonce_str><out_trade_no>00082444063912</out_trade_no><spbill_create_ip>10.161.168.50</spbill_create_ip><time_start>20210823094012</time_start><time_expire>20210824094012</time_expire><total_fee>1</total_fee><version>1.0</version><sign>7F5CF196D7C89666FE35BB167A2A7CD5</sign></xml>";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlStr));
            document = docBuilder.parse(is);
            //document.normalizeDocument();
            Element elements = (Element)document.getElementsByTagName("xml").item(0);
            NodeList children = elements.getChildNodes();
            for (int i=0;i<children.getLength();i++){
                Element element = (Element) children.item(i);
                System.out.println("element is: " + element.getTagName() + element.getTextContent());

            }

            OutputFormat format = new OutputFormat(document);

            format.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(System.out, format);

            serializer.serialize(document);
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String request() throws Exception {
        String responseCP = "";
        String req = "http://127.0.0.1:9000/gateway.do?_input_charset=utf-8&body=AlipayTest2%E5%8D%97%E4%BA%AC%E8%A5%BF%E8%B7%AF%E5%BA%97%E6%B6%88%E8%B4%B9&dynamic_id=281977278430745091&dynamic_id_type=bar_code&extend_params={\"AGENT_ID\":\"KargoCard\",\"STORE_TYPE\":\"1\",\"STORE_ID\":\"208888\",\"TERMINAL_ID\":\"00062006371\"}&it_b_pay=5m&out_trade_no=000824064410&partner=2088121336952778&product_code=BARCODE_PAY_OFFLINE&seller_id=2088121336952778&service=alipay.acquire.createandpay&subject=AlipayTest2%E5%8D%97%E4%BA%AC%E8%A5%BF%E8%B7%AF%E5%BA%97%E6%B6%88%E8%B4%B9&total_fee=0.01&sign_type=MD5&sign=f9fb298392c829c79376a9610f1f9142";
        String body = URLEncoder.encode(req, "UTF-8");

        responseCP = sendHttpRequest(req, 60000, false);
        return responseCP;
        }

        public String sendHttpRequest(String url, int timeout, boolean isCertPath) throws Exception {
            String response = null;
            URL postUrl = null;
            HttpURLConnection connection = null;
            try {
                System.out.println(url);
                postUrl = new URL("" + url);
                connection = (HttpURLConnection) postUrl.openConnection();

                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");  //GET or POST
                connection.setUseCaches(false);
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);

                connection.setInstanceFollowRedirects(true);

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; text/html; charset=utf-8");

                int returnCode = connection.getResponseCode();

                if (returnCode == HttpURLConnection.HTTP_OK)
                {
                    InputStreamReader in = new InputStreamReader(connection.getInputStream(),"utf-8");
                    BufferedReader reader = new BufferedReader(in);//设置编码,否则中文乱码
                    String line="";
                    StringBuffer strBuf=new StringBuffer();
                    while ((line = reader.readLine()) != null){
                        strBuf.append(line);
                    }
                    reader.close();
                    response = strBuf.toString();
                } else {
                    throw new Exception("Http ERROR : Http Status is " + returnCode);
                }

                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return response;
        }

    public static void main(String[] args){
        Test t = new Test();
        try {
            t.request();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
