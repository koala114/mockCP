package com.kargotest.cp;

import org.json.simple.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class BaiLianOK {
    String jsonText;
    String res = "";

    String patternDate = "yyyyMMdd";
    String patternTime = "hhmmss";

    SimpleDateFormat DateFormat = new SimpleDateFormat(patternDate);
    SimpleDateFormat timeFormat = new SimpleDateFormat(patternTime);
    String date = DateFormat.format(new Date());
    String time = timeFormat.format(new Date());

    private String returnCode;
    private String balanceAmt;
    private String realPay;
    private String tradeNo;
    private String payTime;
    private String returnMessage;
    private String sign;
    private String txnTime;
    private String pan;
    private String txnDate;
    private String nonceStr;

    class OKCard {
        public OKCard(String service, JSONObject jsonObj) {
            Iterator<String> keys = jsonObj.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObj.get(key);
                if (Objects.isNull(value) || value.equals("")) {
                    service = "BadRequest";
                }
            }

            System.out.println(service);
            if (service.equals("okcardConsume")) {
                Long totalAmount = (Long)jsonObj.get("totalAmount");
                if (totalAmount < 99999999)
                    res = this.redempResponse(totalAmount, false);
                else
                    res = this.redempResponse(totalAmount, true);
            }
            else if (service.equals("okcardReversal"))
                res = this.revealResponse();
            else if (service.equals("okcardRefund")) {
                Long refundAmount = (Long)jsonObj.get("refundAmount");
                res = this.refundResponse(refundAmount);
            }
            else if (service.equals("BadRequest"))
                res = this.checkReqData();
        }

        public String checkReqData() {
            JSONObject checkData = new JSONObject();

            checkData.put("returnCode", "EK1075");
            checkData.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            checkData.put("payTime", date + time);
            checkData.put("returnMessage", "请求字段中有NULL或者空");
            checkData.put("sign", "hpYcN4kLsDCBAYL+GoYzDqupYl9w1Bpnq2qqVj7YqKQZQ1v4R+WG+AUII6VlT3L8eS9wFo3Q6P2a1T39PFF14IxA3HTLNfm/K3QcgHypHLY/DRYzQo2zi1UcaSkZt6VjJGflijtSyN/uuUWI7U7wU+uhf0FVir4KztZqbElct+I=");
            checkData.put("txnTime", time);
            checkData.put("txnDate", date);
            checkData.put("nonceStr", "e1b7d903e7574d7b8c67af37613d3a21");

            jsonText = checkData.toString();

            return jsonText;
        }

        public String revealResponse() {
            JSONObject reveal = new JSONObject();

            reveal.put("returnCode", "000000");
            reveal.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            reveal.put("payTime", date + time);
            reveal.put("returnMessage", "交易成功");
            reveal.put("sign", "hpYcN4kLsDCBAYL+GoYzDqupYl9w1Bpnq2qqVj7YqKQZQ1v4R+WG+AUII6VlT3L8eS9wFo3Q6P2a1T39PFF14IxA3HTLNfm/K3QcgHypHLY/DRYzQo2zi1UcaSkZt6VjJGflijtSyN/uuUWI7U7wU+uhf0FVir4KztZqbElct+I=");
            reveal.put("txnTime", time);
            reveal.put("txnDate", date);
            reveal.put("nonceStr", "e1b7d903e7574d7b8c67af37613d3a21");

            jsonText = reveal.toString();

            return jsonText;
        }

        public String redempResponse(Long totalAmt, boolean isOverFlowMaxAmount) {
            JSONObject redemp = new JSONObject();

            if (isOverFlowMaxAmount){
                redemp.put("returnCode", "MTP00008");
                redemp.put("realPay", 0);
                redemp.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
                redemp.put("payTime", date + time);
                redemp.put("returnMessage", "该笔订单被用户取消，请重新下单");
                redemp.put("sign", "PtkGpJz2g2V5CNd7UeAGuw5s8dG/lNgLpgpzoWiNO++BaF8IrkXPPrOGRbXPMeh6jvnyLBuV11NJHLhY1UBzRgQQZ6NIim5k5lBS0W0aPcpEBENJK59owLTIjZ4hPf6KYTQoAi0dRDRGnQMUbcaVh/prroHR4B+slMMi2lqWzeg=");
                redemp.put("txnTime", time);
                redemp.put("txnDate", date);
                redemp.put("nonceStr", "12675ff5dc1f4afb84346f53190ae948");

                jsonText = redemp.toString();

                return jsonText;
            }

            redemp.put("returnCode", "000000");
            redemp.put("balanceAmt", 15180);
            redemp.put("realPay", totalAmt);
            redemp.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            redemp.put("payTime", date + time);
            redemp.put("returnMessage", "交易成功");
            redemp.put("sign", "pehSLrhaMtlX0XGQ6NLek782/rHD9RE8/l/2tfiHwQE+0VaSiSyQfqKOy2aoCts3TR6jVkMeeZtYDBK+Uzkp7o3w8nn29JmprlieYuAkThBaWebEkymX5jUxcBYdT0uWecHhbwtR4Xnac9IeCeASv8wmefkxkS9y9+T3UBiS6UU=");
            redemp.put("txnTime", time);
            redemp.put("pan", "3005" + time);
            redemp.put("txnDate", date);
            redemp.put("nonceStr", "12675ff5dc1f4afb84346f53190ae948");

            jsonText = redemp.toString();

            return jsonText;
        }

        public String refundResponse(Long refundAmount) {
            JSONObject refund = new JSONObject();

            refund.put("returnCode", "000000");
            refund.put("balanceAmt", 0);
            refund.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            refund.put("realRefund", refundAmount);
            refund.put("payTime", date + time);
            refund.put("returnMessage", "交易成功");
            refund.put("sign", "WMomYCGVkM41Bfn3iFxhXaynOSijh4vwOQlLlct24jANh1R+GCK/rtKmWguIeAh/q4Q618AKJOWeaKDqA/4x46b5UpkuyGx3Fp1uFdX9bfGTM9JixddFpvmAUIFTAn5t/BTIV7K/ZiZHmPVGdaVEoejuLO9aQ70HB6Ixt8lTXPw=");
            refund.put("txnTime", time);
            refund.put("pan", "3005" + time);
            refund.put("txnDate", date);
            refund.put("nonceStr", "36a4f158ab3a466cadd66be5c225d044");

            jsonText = refund.toString();

            return jsonText;

        }
    }

    class OKPay {
        public OKPay(String service, JSONObject jsonObj) {
            System.out.println(service);
            if (service.equals("okpayConsume")) {
                Long totalAmount = (Long)jsonObj.get("totalAmount");
                res = this.redmpResponse(totalAmount);
            }
            else if (service.equals("okpayRefund")) {
                Long refundAmount = (Long)jsonObj.get("refundAmount");
                res = this.revealResponse(refundAmount);
            }
            else if (service.equals("okpayRefund")) {
                Long refundAmount = (Long)jsonObj.get("refundAmount");
                res = this.refundResponse(refundAmount);
            }
        }

        public String revealResponse(Long realPay) {
            JSONObject reveal = new JSONObject();

            reveal.put("returnCode", "000000");
            reveal.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            reveal.put("realRefund", realPay);
            reveal.put("returnMessage", "SUCCESS");
            reveal.put("sign", "CBLW56IqyD6dyQGp3xPv46RUMt8oGBJtzpznyQ046V+AcVIfzlpncrDw5ICV2gxXMLCWCgV0ak4PdfATNjQYm5Fs7/PQXoCuOAGre0be9EKo1WpPIqj/ijC6MzEB8dfnaMpWfovzwTST2aSgcd4GurocYot1lYvtrjPt4ZDwE6U=");
            reveal.put("txnTime", time);
            reveal.put("txnDate", date);
            reveal.put("nonceStr", "e1b7d903e7574d7b8c67af37613d3a21");

            jsonText = reveal.toString();

            return jsonText;
        }

        public String redmpResponse(Long totalAmt) {
            JSONObject redemp = new JSONObject();

            redemp.put("returnCode", "000000");
            redemp.put("realPay", totalAmt);
            redemp.put("tradeNo", date + (int)(Math.random() * 10000000 + 1));
            redemp.put("payTime", date + time);
            redemp.put("returnMessage", "SUCCESS");
            redemp.put("sign", "iAO3Rz8LNxFtqtce9jznmkYyo7JhFbXtpjMjOnDJVHP5l/pGhIqzx65hgKoz/N5hHBDBDONlnoYapAfe/2ZzR+Kq/kA6afv2SmyHeHMNsMRT4fSqyc9srnLccfLBAsfYlOEQWHTasrK70tlY02D01QNEa/oMKsM1JCgM+76JiZk=");
            redemp.put("txnTime", time);
            redemp.put("txnDate", date);
            redemp.put("nonceStr", "804d755d0ed34be9a31825e35371c443");

            jsonText = redemp.toString();

            return jsonText;
        }

        public String refundResponse(Long refundAmount) {
            jsonText = revealResponse(refundAmount);

            return jsonText;

        }
    }

    public BaiLianOK(String reqStr) {
        JSONObject jsonObj = (JSONObject) JSONValue.parse(reqStr);
        String service = (String)jsonObj.get("service");


        if (service.startsWith("okcard")) {
            OKCard okCard = new OKCard(service, jsonObj);
        }
        else if (service.startsWith("okpay")) {
            OKPay okPay = new OKPay(service, jsonObj);
        }

    }

    public String getRes() {
        return res;
    }
}
