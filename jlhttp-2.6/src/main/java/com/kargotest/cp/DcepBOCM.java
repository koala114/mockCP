package com.kargotest.cp;

import com.bocom.api.BocomApiException;
import com.bocom.api.utils.BocomSignature;
import com.bocom.api.utils.enums.AlgorithmType;
import net.freeutils.httpserver.HTTPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
zip -d jlhttp-2.6-BOCM2.jar 'META-INF/.SF' 'META-INF/.RSA' 'META-INF/*SF'
/api/walletpay/misQueryOrder/v2 REQ: trans_type=01消费结果查询 trans_type=02 退款结果查询
                                RES: txn_state=01:订单创建 02:用户支付中 03:已关闭 04:部分退款 05:全部退款 06:交易成功
                                     trans_state=N-交易成功 E-交易失败 S-交易未决
/api/walletpay/misRefund/v2     RES: biz_state=

apiwgPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu+N+JVG0xzDgF4002DD2zpL70YbfN/CRfAefOizY6X/PMt/x6feykeTBV19fEj3MYTVEoY1pakuVgC2AOND1c7ay29dVQgp6pRKZ5pFoaeHUeZAjcnGy25RB+zkqPpapfMXbt3G7ktExcr1AWMXDiuZBPRLGjDKDOu/iztoAtaKSRGmp0m051Jm/Q8EVc9cTZniZLWoAopayet9i4B9Qxuxzz0yZ//lqSkgQSQU8O8QhMhlb8r6Jeh4N2kzSbPB+twHzFu+ZZ/g1E31NE0Mqt4twCnBMtVMLW8ZM39XKIq0omJWwXV128vgJqWEdu25Zfqbb5fVv63JKqvBD2xt9yQIDAQAB"
apiwgPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC7434lUbTHMOAXjTTYMPbOkvvRht838JF8B586LNjpf88y3/Hp97KR5MFXX18SPcxhNUShjWlqS5WALYA40PVztrLb11VCCnqlEpnmkWhp4dR5kCNycbLblEH7OSo+lql8xdu3cbuS0TFyvUBYxcOK5kE9EsaMMoM67+LO2gC1opJEaanSbTnUmb9DwRVz1xNmeJktagCilrJ632LgH1DG7HPPTJn/+WpKSBBJBTw7xCEyGVvyvol6Hg3aTNJs8H63AfMW75ln+DUTfU0TQyq3i3AKcEy1Uwtbxkzf1coirSiYlbBdXXby+AmpYR27bll+ptvl9W/rckqq8EPbG33JAgMBAAECggEAPUWG0ay/g2XQ7l1CKm5tThhovn95M8jj6Mqjhlkxs5PWggS940q/lQxAcLnNSymUCO9SHkz7X4qeilpMQjsCkGW4FjE2wH6iWEqknag1oLHglHQI2z1w0mKa0c5mlFqVkvcDG+OfA9XshjppTYnQpu9Hvx7sWLPwT7wC/ung6Vo2aw/SabocA0a31edQY+Ty0RdjVrAXP9T0JilWH7qRHZxlT/WlFEjs9zQUiGPJudbObut6XkkBRlEf8UohxMv0FuzogfdTa9UynpHshNGZEk3JDNSPOUc8RWgC0pTDKLZbwEoEF/X+DO1xNb6rEFbMFo/dKnmvxpjBxhQ/toZbsQKBgQD7pm95WdNHO7cWtGIiD9JpdCC57UVMFlgaFvWj6lqthVO8fW4aRjqs3ONEMT/2+ss86zWIINs94Q0f/wbZC+hKHU6J5OKzFAS/pdHU7TNTEiY/CtyXfjCJTdv6GzT/MJmes0PkvjNwRkqV7mIJzfssxaUIlnsUrJywNPNvJk8A1wKBgQC/IujTvZcNUO9dtlWgzELe3cMYuLMgvJYiAemx0a5lWa3DSzjBP2YV69838JLjcYy/mkirT+gwDlQsERpWJthIHU5qsb2uwj1b/UJPlM/OyIeBRPve1HPAC9oHtHyAbEjcRlKOs0+Emm2/+FOih2ulr+d5+OmhuOklwiUzQOuCXwKBgAvG1zKrpHqR7diPKoSDjwpGV/27f+G2rfrSlj5Mil/SfH+2sv9hx/8s+ynG0EKDrB02uLOdLgVwUcfsjGp95yoIwxMq9f0Bc9NwNCitzRgXIlkS7g3c5vKWBTRoL4u9v4Kwyv2adRfNAlKP7GCfFiEbQsTrHelyxoZsg/PwrAPTAoGATEJ9ap3mOqXkGxu4pmNY+tq4EJEAxzr3G7Jvr0bdsgpJzfWhO1k0PeLSONt/f8e6RGgmPlOvbB0LcFmSjHULLhqjQuaPq75MBPvTDTVuhC52Ahmn9IwHcsRHxXM5iXOqzlgwcEcSnvGOgF1v4RTu2jiIvp2VebTxMON5PC3WyTkCgYArnpIWscGMJHJHcdvuRE7M1XJ4LjTpzHGnuX1AbXbFxt7CtaZydAMJJ2flMo5cLmZKJ/HRNLw2E1IQekZrDyjWv/dJw71eM+GkBw21M5R7Sd4+/YU8Y5vyDDJ5oJw+YXbNoLgYKm9Py/jg8v2pZ9Wf1EO8yT13ZaeqvgknOH0j9g=="
 */
public class DcepBOCM implements HTTPServer.ContextHandler{
    private static final Logger logger = LogManager.getLogger(DcepBOCM.class);
    private static Map<String, trans> transMap = new HashMap<>();
    private static Map<String, ArrayList> trans4Refund = new HashMap<>();
    private static Map<String, Integer> limitQueryTime = new HashMap<>();


    // https://www.devglan.com/online-tools/rsa-encryption-decryption 2048 Bit
    private final String BOCMApiWGPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC7434lUbTHMOAXjTTYMPbOkvvRht838JF8B586LNjpf88y3/Hp97KR5MFXX18SPcxhNUShjWlqS5WALYA40PVztrLb11VCCnqlEpnmkWhp4dR5kCNycbLblEH7OSo+lql8xdu3cbuS0TFyvUBYxcOK5kE9EsaMMoM67+LO2gC1opJEaanSbTnUmb9DwRVz1xNmeJktagCilrJ632LgH1DG7HPPTJn/+WpKSBBJBTw7xCEyGVvyvol6Hg3aTNJs8H63AfMW75ln+DUTfU0TQyq3i3AKcEy1Uwtbxkzf1coirSiYlbBdXXby+AmpYR27bll+ptvl9W/rckqq8EPbG33JAgMBAAECggEAPUWG0ay/g2XQ7l1CKm5tThhovn95M8jj6Mqjhlkxs5PWggS940q/lQxAcLnNSymUCO9SHkz7X4qeilpMQjsCkGW4FjE2wH6iWEqknag1oLHglHQI2z1w0mKa0c5mlFqVkvcDG+OfA9XshjppTYnQpu9Hvx7sWLPwT7wC/ung6Vo2aw/SabocA0a31edQY+Ty0RdjVrAXP9T0JilWH7qRHZxlT/WlFEjs9zQUiGPJudbObut6XkkBRlEf8UohxMv0FuzogfdTa9UynpHshNGZEk3JDNSPOUc8RWgC0pTDKLZbwEoEF/X+DO1xNb6rEFbMFo/dKnmvxpjBxhQ/toZbsQKBgQD7pm95WdNHO7cWtGIiD9JpdCC57UVMFlgaFvWj6lqthVO8fW4aRjqs3ONEMT/2+ss86zWIINs94Q0f/wbZC+hKHU6J5OKzFAS/pdHU7TNTEiY/CtyXfjCJTdv6GzT/MJmes0PkvjNwRkqV7mIJzfssxaUIlnsUrJywNPNvJk8A1wKBgQC/IujTvZcNUO9dtlWgzELe3cMYuLMgvJYiAemx0a5lWa3DSzjBP2YV69838JLjcYy/mkirT+gwDlQsERpWJthIHU5qsb2uwj1b/UJPlM/OyIeBRPve1HPAC9oHtHyAbEjcRlKOs0+Emm2/+FOih2ulr+d5+OmhuOklwiUzQOuCXwKBgAvG1zKrpHqR7diPKoSDjwpGV/27f+G2rfrSlj5Mil/SfH+2sv9hx/8s+ynG0EKDrB02uLOdLgVwUcfsjGp95yoIwxMq9f0Bc9NwNCitzRgXIlkS7g3c5vKWBTRoL4u9v4Kwyv2adRfNAlKP7GCfFiEbQsTrHelyxoZsg/PwrAPTAoGATEJ9ap3mOqXkGxu4pmNY+tq4EJEAxzr3G7Jvr0bdsgpJzfWhO1k0PeLSONt/f8e6RGgmPlOvbB0LcFmSjHULLhqjQuaPq75MBPvTDTVuhC52Ahmn9IwHcsRHxXM5iXOqzlgwcEcSnvGOgF1v4RTu2jiIvp2VebTxMON5PC3WyTkCgYArnpIWscGMJHJHcdvuRE7M1XJ4LjTpzHGnuX1AbXbFxt7CtaZydAMJJ2flMo5cLmZKJ/HRNLw2E1IQekZrDyjWv/dJw71eM+GkBw21M5R7Sd4+/YU8Y5vyDDJ5oJw+YXbNoLgYKm9Py/jg8v2pZ9Wf1EO8yT13ZaeqvgknOH0j9g==";
    String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu+N+JVG0xzDgF4002DD2zpL70YbfN/CRfAefOizY6X/PMt/x6feykeTBV19fEj3MYTVEoY1pakuVgC2AOND1c7ay29dVQgp6pRKZ5pFoaeHUeZAjcnGy25RB+zkqPpapfMXbt3G7ktExcr1AWMXDiuZBPRLGjDKDOu/iztoAtaKSRGmp0m051Jm/Q8EVc9cTZniZLWoAopayet9i4B9Qxuxzz0yZ//lqSkgQSQU8O8QhMhlb8r6Jeh4N2kzSbPB+twHzFu+ZZ/g1E31NE0Mqt4twCnBMtVMLW8ZM39XKIq0omJWwXV128vgJqWEdu25Zfqbb5fVv63JKqvBD2xt9yQIDAQAB";

    private Map<String, String> urlDecoded = new HashMap<>();
    private JSONParser parser = new JSONParser();
    private JSONObject BOCMReq;
    private String patternDate = "yyyyMMdd";
    private String patternTime = "hhmmss";
    private JSONObject rsp_biz_content;
    private String settleAmount = "";

    private SimpleDateFormat DateFormat = new SimpleDateFormat(patternDate);
    private SimpleDateFormat timeFormat = new SimpleDateFormat(patternTime);
    private String date = DateFormat.format(new Date());
    private String time = timeFormat.format(new Date());

    public DcepBOCM() {
        rsp_biz_content = new JSONObject();
        rsp_biz_content.put("biz_state", "S");
        rsp_biz_content.put("rsp_code", null);
        rsp_biz_content.put("rsp_msg", null);
    }

    private String result;

    public int serve(HTTPServer.Request req, HTTPServer.Response resp) throws IOException {
        String BOCMMsg = HTTPServer.convert(req.getBody(), Charset.forName("UTF-8"), true);
        urlDecoded = HTTPServer.splitString(BOCMMsg);

        try{
            BOCMReq = (JSONObject)parser.parse(urlDecoded.get("biz_content"));
            logger.info("BOCMReq: {}", BOCMMsg);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String path = req.getPath().split("/")[3];
        logger.info("PATH: {}", path);

        if(path.equals("misPayOrder")){
            result = doPayment(BOCMReq);
        }
        else if(path.equals("misQueryOrder")){
            result = doQuery(BOCMReq);
        }
        else if(path.equals("misRefund")){
            result = doRefund(BOCMReq);
        }
        resp.getHeaders().add("Content-Type", "application/json");
        resp.send(200, result);
        return 0;
    }

    public String doPayment(JSONObject biz_content){
        JSONObject req_body = (JSONObject)biz_content.get("req_body");
        JSONObject rsp_body = new JSONObject();
        String amount = String.format("%1$" + 12 + "s", req_body.get("total_amount")).replace(' ', '0');
        if(req_body.get("total_amount").equals("100")){
            // 如果消费1元时返回的settle_amount值设置成80分
            settleAmount = String.format("%1$" + 12 + "s", "80").replace(' ', '0');
        }
        else
            settleAmount = amount;
        JSONObject root = new JSONObject();
        JSONObject rsp_head = new JSONObject();
        rsp_head.put("transcode", "CIPP120101");
        rsp_head.put("term_trans_time", date + time);
        if (Integer.parseInt(amount) > 50000){
            rsp_head.put("response_code", "CIPP0004PY0001");
            rsp_head.put("response_msg", "(假冒)交易未决");
        }
        else {
            rsp_head.put("response_code", "CIPP0004PY0000");
            rsp_head.put("response_msg", "(假冒)交易成功");
        }
        rsp_head.put("remark", null);
        rsp_biz_content.put("rsp_head", rsp_head);
        rsp_body.put("total_amount", amount);
        rsp_body.put("amount_type", "156");
        rsp_body.put("sys_order_no", "0501" + date + time + "0049630030");
        rsp_body.put("coupon_fee", null);
        rsp_body.put("detail", null);
        rsp_body.put("settle_amount", settleAmount);
        rsp_body.put("extend_info", null);
        rsp_body.put("mcht_order_no", req_body.get("mcht_order_no"));
        rsp_body.put("notify_url", null);
        rsp_body.put("pay_dsct_name", null);
        rsp_body.put("pay_dsct_id", null);
        rsp_biz_content.put("rsp_body", rsp_body);
        root.put("rsp_biz_content", rsp_biz_content);

        String doPaymentResult = getSignStr(root);
        logger.info("Response: {}", doPaymentResult);
        String txnId = (String) req_body.get("mcht_order_no");
        trans t = new trans((String)req_body.get("total_amount"));
        transMap.put(txnId, t);
        return doPaymentResult;
    }

    public String doQuery(JSONObject biz_content){
        JSONObject req_head = (JSONObject)biz_content.get("req_head");
        JSONObject req_body = (JSONObject)biz_content.get("req_body");

        JSONObject root = new JSONObject();
        JSONObject rsp_head = new JSONObject();
        rsp_head.put("transcode", "CIPP120001");
        rsp_head.put("term_trans_time", date + time);
        rsp_head.put("response_code", "CIPP0004PY0000");
        rsp_head.put("response_msg", "(假冒)交易成功");
        rsp_head.put("remark", null);
        rsp_biz_content.put("rsp_head", rsp_head);

        JSONObject rsp_body = new JSONObject();
        String trans_type = (String) req_body.get("trans_type");
        rsp_body.put("sys_order_no", "0501" + date + time + "0049630033");
        rsp_body.put("amount_type", "156");
        rsp_body.put("sys_trace_time", date + time);
        rsp_body.put("sys_trace_no", date + time + "0501114630030");
        rsp_body.put("coupon_fee", null);
        rsp_body.put("extend_info", null);
        rsp_body.put("pay_dsct_name", null);
        rsp_body.put("pay_dsct_id", null);
        rsp_head.put("response_code", "CIPP0004PY0000");
        rsp_biz_content.put("rsp_body", rsp_body);

        root.put("rsp_biz_content", rsp_biz_content);

        String trace_no = (String) req_head.get("trace_no");
        trans t = null;

        if (trans_type.equals("01")) {
            t = transMap.get(trace_no);
            int amount = t.getTotal_amount();
            String amountLeadingZero = String.format("%1$" + 12 + "s", String.valueOf(amount)).replace(' ', '0');;
            rsp_body.put("trans_amount", amountLeadingZero);
            rsp_body.put("total_amount", amountLeadingZero);
            rsp_body.put("done_refund_amount", 0);
            rsp_body.put("settle_amount", settleAmount.equals("000000000080")?"80":null);

            int counter = t.getQueryTimes();
            if (amount >50000 && counter != 0){
                rsp_body.put("txn_state", "02");
                rsp_body.put("trans_state", "S");
                t.setQueryTimes(--counter);
                transMap.put(trace_no, t);
            }
            else{
                rsp_body.put("txn_state", "06");
                rsp_body.put("trans_state", "N");
            }
        }
        else if (trans_type.equals("02")){ // 退款查询
            for (Map.Entry<String, trans> entry : transMap.entrySet()) {
                Map e = entry.getValue().getRefunds();
                if(e.containsKey(trace_no))
                    t = entry.getValue();
            }
            int refundAmt = 0;
            if (t.getRefunds().containsKey(trace_no)) {
                refundAmt = t.getRefunds().get(trace_no);
                rsp_body.put("trans_amount", getLeadingZeroAmt(String.valueOf(refundAmt)));
                rsp_body.put("total_amount", getLeadingZeroAmt(String.valueOf(t.getTotal_amount())));
                rsp_body.put("done_refund_amount", getLeadingZeroAmt(String.valueOf(t.getDoneRefundAmt())));
                if (t.getTotal_amount() == t.getDoneRefundAmt())
                    rsp_body.put("txn_state", "05");
                else
                    rsp_body.put("txn_state", "04");

            }
            rsp_body.put("trans_state", "N");
        }

        String doQueryResult = getSignStr(root);
        logger.info("Response: {}", doQueryResult);
        return doQueryResult;
    }

    public String doRefund(JSONObject biz_content){
        JSONObject req_head = (JSONObject)biz_content.get("req_head");
        JSONObject req_body = (JSONObject)biz_content.get("req_body");

        JSONObject root = new JSONObject();
        JSONObject rsp_body = new JSONObject();
        String trace_no = (String) req_head.get("trace_no");
        String refund_amount = (String) req_body.get("refund_amount");
        String orig_mcht_order_no = (String) req_body.get("orig_mcht_order_no");
        trans t = transMap.get(orig_mcht_order_no);
        t.setRefunds(trace_no, Integer.parseInt(refund_amount));
        rsp_body.put("done_refund_amount", getLeadingZeroAmt(String.valueOf(t.getDoneRefundAmt())));
        rsp_body.put("refund_amount", getLeadingZeroAmt(refund_amount));
        rsp_body.put("amount_type", null);
        rsp_body.put("pay_dsct_name", null);
        rsp_body.put("coupon_refund_amount", null);
        rsp_body.put("real_refund_amount", null);
        rsp_body.put("pay_dsct_id", null);
        rsp_body.put("sys_order_no", "0501" + date + time + "0049640031");
        rsp_body.put("refund_trace_no", date + time + "05012640012");
        rsp_biz_content.put("rsp_body", rsp_body);

        JSONObject rsp_head = new JSONObject();
        rsp_head.put("response_code", "CIPP0004PY0000");
        rsp_head.put("term_trans_time", date + time);
        rsp_head.put("transcode", null);
        rsp_head.put("remark", null);
        rsp_head.put("trace_no", trace_no);
        rsp_head.put("response_msg", "(假冒)交易成功");
        rsp_biz_content.put("rsp_head", rsp_head);

        root.put("rsp_biz_content", rsp_biz_content);

        String doRefundResult = getSignStr(root);
        transMap.put(orig_mcht_order_no, t);
        return doRefundResult;
    }

    private String getSignStr(JSONObject root){
        String sign = null;
        JSONObject rsp_biz_content = (JSONObject) root.get("rsp_biz_content");
        try {
            sign = BocomSignature.sign(AlgorithmType.IDEA, rsp_biz_content.toString(), BOCMApiWGPrivateKey, "UTF-8","SHA256WithRSA" );
            boolean t = BocomSignature.verify(AlgorithmType.IDEA, rsp_biz_content.toString(), pubKey, "UTF-8", sign);
            logger.info("Sign of rsp_biz_content Content: {}", rsp_biz_content.toString());
            logger.info("Sign: {}", sign);
        } catch (BocomApiException e) {
            e.printStackTrace();
        }
        String rootStr = root.toString();
        String doQueryResult = rootStr.substring(0, rootStr.length()-1) + ",\"sign\":\"" + sign + "\"}";
        logger.info("Response: {}", doQueryResult);
        return doQueryResult;
    }

    public static String getLeadingZeroAmt(String s) {
        return String.format("%1$" + 12 + "s", s).replace(' ', '0');
    }

    class trans {
        int total_amount = 0;
        HashMap<String, Integer> refunds = new HashMap<>();
        int queryTimes = 3;

        public trans(String total_amount) {
            this.total_amount = Integer.parseInt(total_amount);
        }

        public int getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(int total_amount) {
            this.total_amount = total_amount;
        }

        public HashMap<String, Integer> getRefunds() {
            return refunds;
        }

        public void setRefunds(String txnId, int refundAmt) {
            this.refunds.put(txnId, refundAmt);
        }

        public int getQueryTimes() {
            return queryTimes;
        }

        public void setQueryTimes(int queryTimes) {
            this.queryTimes = queryTimes;
        }

        public int getDoneRefundAmt(){
            int doneRefundAmt = 0;
            for (Map.Entry<String, Integer> entry : this.refunds.entrySet())
                doneRefundAmt += entry.getValue();
            return doneRefundAmt;
        }
    }
}