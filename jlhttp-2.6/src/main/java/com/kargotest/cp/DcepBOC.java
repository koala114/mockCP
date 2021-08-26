package com.kargotest.cp;

import net.freeutils.httpserver.HTTPServer;
import com.kargotest.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
Public Key: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOeX7043CnC10wPGEmyGiKrvh2K48Y2jgP1Hez7zAbb84DrM75Y/L7leqY0+Rg/XqwIVjWuOu1jC4nCmlh6HEH5tEToSoITv3g/t4YgYi65sG5ZJBxEgXMkxeUPPOXZQh1NxR3q+OtoW3gkn5uDWA7IbFfJfdNOoXrwiCw1igpDQIDAQAB
Private KEY: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI55fvTjcKcLXTA8YSbIaIqu+HYrjxjaOA/Ud7PvMBtvzgOszvlj8vuV6pjT5GD9erAhWNa467WMLicKaWHocQfm0ROhKghO/eD+3hiBiLrmwblkkHESBcyTF5Q885dlCHU3FHer462hbeCSfm4NYDshsV8l9006hevCILDWKCkNAgMBAAECgYAH2aPoJFwBqDujO8BynZo9AfUItc+1o9hw7tkFngk9icSFKVFiJmKuAA2kDBDKU00eRUga1KeQIkAswIMb9O29Vt6OMAuEnusOlPbJlN85iyUqvFLk7YsjgSLyK7ROymXJeIX6ZPyHsDDY8GTOIoLysDD+ADzngVQIcvEwl58DFQJBAPt0WwCn3+4cixG2DhCLt8LwvS0WKuD6Tsszj59yYzRNWMVAD4XmKA6lS8wEzVsJXnjgwFsChpgXEUdMI4M/OxcCQQCRDNGuWo0o01V/4b7kWO7WfbFRPRMLrcPyZTyljYEhuzD7Z3KkryKyApBKtiJXF5witIhNjO8HIsyoOnXfBoN7AkBP69WYycK0zcGt6W1i+OV5Qkb+c5NBWo18rHCzvwmk8AiM+SV164dD0Gnc+JHEo5+xT84TnyPkZ0CIpryK1KgVAkAowdBdTIZjlVZt85G++hpKXNEpQZ8LZg0sHDQ5VJSXVNEDo2K8UZXPLRc8Vwc5L6Iowk+WcuZrO+UD1EFwpJTnAkEA9K5rhKqxBHFBqxbNVREJBQHJHX5WkeNDcTYmHJqAd/GCJ1SeYri9HoIlqvKYx9aG/QnvW94LAO+d/BPK78Bmsg==
zip -d jlhttp-2.6_test.jar 'META-INF/.SF' 'META-INF/.RSA' 'META-INF/*SF'
 */

public class DcepBOC implements HTTPServer.ContextHandler {
    private static final Logger logger = LogManager.getLogger(DcepBOC.class);
    private final String BOCPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI55fvTjcKcLXTA8YSbIaIqu+HYrjxjaOA/Ud7PvMBtvzgOszvlj8vuV6pjT5GD9erAhWNa467WMLicKaWHocQfm0ROhKghO/eD+3hiBiLrmwblkkHESBcyTF5Q885dlCHU3FHer462hbeCSfm4NYDshsV8l9006hevCILDWKCkNAgMBAAECgYAH2aPoJFwBqDujO8BynZo9AfUItc+1o9hw7tkFngk9icSFKVFiJmKuAA2kDBDKU00eRUga1KeQIkAswIMb9O29Vt6OMAuEnusOlPbJlN85iyUqvFLk7YsjgSLyK7ROymXJeIX6ZPyHsDDY8GTOIoLysDD+ADzngVQIcvEwl58DFQJBAPt0WwCn3+4cixG2DhCLt8LwvS0WKuD6Tsszj59yYzRNWMVAD4XmKA6lS8wEzVsJXnjgwFsChpgXEUdMI4M/OxcCQQCRDNGuWo0o01V/4b7kWO7WfbFRPRMLrcPyZTyljYEhuzD7Z3KkryKyApBKtiJXF5witIhNjO8HIsyoOnXfBoN7AkBP69WYycK0zcGt6W1i+OV5Qkb+c5NBWo18rHCzvwmk8AiM+SV164dD0Gnc+JHEo5+xT84TnyPkZ0CIpryK1KgVAkAowdBdTIZjlVZt85G++hpKXNEpQZ8LZg0sHDQ5VJSXVNEDo2K8UZXPLRc8Vwc5L6Iowk+WcuZrO+UD1EFwpJTnAkEA9K5rhKqxBHFBqxbNVREJBQHJHX5WkeNDcTYmHJqAd/GCJ1SeYri9HoIlqvKYx9aG/QnvW94LAO+d/BPK78Bmsg==";

    JSONParser parser = new JSONParser();

    // values in Header
    private Map<String, String> bocHeaders = new HashMap<>();
    private String AESKey;
    private String offset;
    private String decryptedBOCMsg;

    private String path;
    private String merId;
    private String termId;
    private String tranAmt;
    private String refundAmt;
    private String merOrderNo;
    private String oldMerOrderNo;
    private String result;
    private String authCode;

    private static Map<String, JSONObject> BOCPayMap = new HashMap<>();
    private static Map<String, Integer> limitQueryTime = new HashMap<>();


    String patternDate = "yyyyMMdd";
    String patternTime = "hhmmss";

    SimpleDateFormat DateFormat = new SimpleDateFormat(patternDate);
    SimpleDateFormat timeFormat = new SimpleDateFormat(patternTime);
    String date = DateFormat.format(new Date());
    String time = timeFormat.format(new Date());

    public int serve(HTTPServer.Request req, HTTPServer.Response resp) throws IOException {
        String encryptedBOCMsg = HTTPServer.convert(req.getBody(), Charset.forName("UTF-8"), false);
        logger.info("DCEP-BOC Request: {}", encryptedBOCMsg);
        // remove duplication
        //String encryptedBOCMsgStart = encryptedBOCMsg.substring(0,61);
        //int startAt = encryptedBOCMsg.indexOf(encryptedBOCMsgStart, 1);
        //if (startAt != -1)
        //    encryptedBOCMsg = encryptedBOCMsg.substring(startAt);

        // get headers
        Iterator<HTTPServer.Header> headers = req.getHeaders().iterator();
        while(headers.hasNext()){
            HTTPServer.Header header = headers.next();
            logger.info("Request Headers: {} -> {}", header.getName(), header.getValue());
            bocHeaders.put(header.getName(), header.getValue());
        }

        // decrypted SecurityKey
        AESKey = Utils.decryptAESKey(bocHeaders.get("SecurityKey"), Utils.getPrivateKey(BOCPrivateKey));
        offset = bocHeaders.get("Offset");
        // decrypted BOC Request Body
        try{
            logger.info("AESKey -> {} | Offset -> {}", AESKey, offset);
            decryptedBOCMsg = Utils.decryptedMessage(encryptedBOCMsg, AESKey, bocHeaders.get("Offset"));
            logger.info("after decrypted: {}", decryptedBOCMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        Pattern pattern = Pattern.compile("/v200/(.*)/(.*)");
        Matcher matcher = pattern.matcher(req.getPath());
        matcher.find();
        path = matcher.group(1);
        */
        path = req.getPath();

        if (path.contains("payment")){
            // convert to JSON
            try{
                JSONObject BocReq = (JSONObject)parser.parse(decryptedBOCMsg);
                merId = (String) BocReq.get("merId");
                termId = (String) BocReq.get("termId");
                tranAmt = (String) BocReq.get("tranAmt");
                merOrderNo = (String) BocReq.get("merOrderNo");
                authCode = (String) BocReq.get("authCode");
                if (authCode.length() != 19 || !authCode.startsWith("0"))
                    return 0;
                if (merId==null || termId==null || tranAmt==null || merOrderNo==null)
                    return 0;
                else if(tranAmt.length() != 12)
                    return 0;

            }catch (ParseException e){
                e.printStackTrace();
            }
            result = doPayment(authCode);
        }
        else if (path.contains("query")){
            try{
                JSONObject BocReq = (JSONObject)parser.parse(decryptedBOCMsg);
                merId = (String) BocReq.get("merId");
                termId = (String) BocReq.get("termId");
                oldMerOrderNo = (String) BocReq.get("oldMerOrderNo");
                if (merId==null || termId==null || oldMerOrderNo==null)
                    return 0;
            }catch (ParseException e){
                e.printStackTrace();
            }
            result = doQuery(limitQueryTime.get(oldMerOrderNo));
        }
        else if (path.contains("revoke")){
            try{
                JSONObject BocReq = (JSONObject)parser.parse(decryptedBOCMsg);
                merId = (String) BocReq.get("merId");
                termId = (String) BocReq.get("termId");
                tranAmt = (String) BocReq.get("tranAmt");
                merOrderNo = (String) BocReq.get("merOrderNo");
                oldMerOrderNo = (String) BocReq.get("oldMerOrderNo");
                if (!tranAmt.startsWith("0"))
                    return 0;
                if (merId==null || termId==null || tranAmt==null || merOrderNo==null || oldMerOrderNo==null)
                    return 0;
                else if(tranAmt.length() != 12)
                    return 0;
            }catch (ParseException e){
                e.printStackTrace();
            }
            result = doRevoke();
        }
        else if (path.contains("refund")){
            try{
                JSONObject BocReq = (JSONObject)parser.parse(decryptedBOCMsg);
                merId = (String) BocReq.get("merId");
                termId = (String) BocReq.get("termId");
                refundAmt = (String) BocReq.get("refundAmt");
                merOrderNo = (String) BocReq.get("merOrderNo");
                oldMerOrderNo = (String) BocReq.get("oldMerOrderNo");
                if (!refundAmt.startsWith("0"))
                    return 0;
                if (merId==null || termId==null || refundAmt==null || merOrderNo==null || oldMerOrderNo==null)
                    return 0;
                else if(refundAmt.length() != 12)
                    return 0;
            }catch (ParseException e){
                e.printStackTrace();
            }
            result = doRefund();

        }

        resp.getHeaders().add("Content-Type", "application/json");
        logger.info("Response -> {}", result);
        resp.send(200, result);
        return 0;
    }

    public String doRefund(){
        JSONObject oldPaymnet = BOCPayMap.get(oldMerOrderNo);
        JSONObject refund = new JSONObject();

        refund.put("merId", merId);
        refund.put("termId", termId);
        refund.put("netCode", "1000");
        refund.put("netMsg", "Auto Testing REFUND");
        refund.put("respCode", "000000");
        refund.put("respMsg", "Auto Testing REFUND SUCCESS");
        refund.put("merOrderNo", merOrderNo);
        refund.put("localOrderNo", "14000001" + date + time + "000422");
        refund.put("bankDate", date);
        refund.put("bankTime", time);
        refund.put("oldLocalOrderNo", oldPaymnet.get("localOrderNo"));
        refund.put("oldPayType", "DZZF");
        refund.put("refundOrderNo", "DR1400000100172" + time);
        refund.put("oldBankOrderNo", "DP1400000100171" + time);
        refund.put("oldBankDate", oldPaymnet.get("bankDate"));
        refund.put("oldMerOrderNo", oldMerOrderNo);
        refund.put("refundAmt", refundAmt);
        refund.put("merchantNo", merId);

        String plainText = refund.toString();
        logger.info("Before encrypted -> {}", plainText);
        result = getEncryptedResult(plainText, AESKey, bocHeaders.get("Offset"));

        return result;
    }

    public String doRevoke(){
        JSONObject oldPaymnet = BOCPayMap.get(oldMerOrderNo);
        JSONObject revoke = new JSONObject();
        revoke.put("merId", merId);
        revoke.put("termId", termId);
        revoke.put("netCode", "1000");
        revoke.put("netMsg", "Auto Testing RSVAL");
        revoke.put("respCode", "000000");
        revoke.put("respMsg", "Auto Testing RSVAL SUCCESS");
        revoke.put("merOrderNo", merOrderNo);
        revoke.put("localOrderNo", "14000001" + date + time + "000402");
        revoke.put("bankDate", date);
        revoke.put("bankTime", time);
        revoke.put("oldLocalOrderNo", oldPaymnet.get("localOrderNo"));
        revoke.put("oldPayType", "DZZF");
        revoke.put("refundOrderNo", "DR1400000100167" + time);
        revoke.put("oldBankOrderNo", "DP1400000100167" + time);
        revoke.put("merchantNo", merId);

        String plainText = revoke.toString();
        logger.info("Before encrypted -> {}", plainText);
        result = getEncryptedResult(plainText, AESKey, bocHeaders.get("Offset"));

        return result;
    }

    public String doQuery(int counter){
        JSONObject oldPaymnet = BOCPayMap.get(oldMerOrderNo);
        JSONObject query = new JSONObject();

        query.put("merId", merId);
        query.put("termId", termId);
        query.put("netCode", "1000");
        query.put("netMsg", "Auto Testing Query");
        query.put("respCode", "000000");
        query.put("respMsg", "Auto Testing Query SUCCESS");
        query.put("bankDate", date);
        query.put("bankTime", time);
        query.put("oldLocalOrderNo", oldPaymnet.get("localOrderNo"));
        query.put("oldTranAmt", oldPaymnet.get("tranAmt"));
        query.put("oldCcyCode", "156");
        query.put("oldPayType", "DZZF");
        if (counter > 3){
            query.put("oldRespCode", "000000");
            query.put("oldRespMsg", "Auto Testing REDEMP SUCCESS");
        }
        else {
            query.put("oldRespCode", "888888");
            query.put("oldRespMsg", "waiting for a buyer input ");
        }

        query.put("oldBankDate", oldPaymnet.get("bankDate"));
        query.put("oldBankTime", oldPaymnet.get("bankTime"));
        query.put("oldMerOrderNo", oldPaymnet.get("merOrderNo"));
        query.put("oldBankOrderNo", oldPaymnet.get("bankOrderNo"));
        query.put("oldOthTradeNo", oldPaymnet.get("othTradeNo"));
        query.put("oldDisPrice", "000000000000");
        query.put("oldCashAmount", oldPaymnet.get("cashAmount"));
        query.put("merchantNo", merId);

        System.out.println("Query time " + counter);
        limitQueryTime.put(oldMerOrderNo, ++counter);

        String plainText = query.toString();
        logger.info("Before encrypted -> {}", plainText);

        result = getEncryptedResult(plainText, AESKey, bocHeaders.get("Offset"));

        return result;
    }

    public String doPayment(String authCode) {
        JSONObject payment = new JSONObject();
        payment.put("merId", merId);
        payment.put("termId", termId);
        payment.put("netCode", "1000");
        payment.put("netMsg", "Auto Testing REDEMP");
        payment.put("respCode", "888888");
        payment.put("respMsg", "Auto Testing REDEMP SUCCESS");
        payment.put("tranAmt", tranAmt);
        payment.put("ccyCode", "156");
        payment.put("merOrderNo", merOrderNo);
        payment.put("localOrderNo", "14000001" + date + time + "000421");
        payment.put("payType", "DZZF");
        payment.put("bankDate", date);
        payment.put("bankTime", time);
        payment.put("bankOrderNo", "DP1400000" + date + time);
        payment.put("othTradeNo", "041" + date + time + "0009717561");
        payment.put("disPrice", "000000000000");
        payment.put("cashAmount", tranAmt);
        payment.put("merchantNo", merId);
        BOCPayMap.put(merOrderNo, payment);
        limitQueryTime.put(merOrderNo, 0);

        String plainText = payment.toString();
        logger.info("Before encrypted -> {}", plainText);

        result = getEncryptedResult(plainText, AESKey, bocHeaders.get("Offset"));
        return result;
    }


    private String getEncryptedResult(String input, String aeskey, String iv){
            try{
                return Utils.encryptedMessage(input, aeskey, iv);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            return null;
    }
}
