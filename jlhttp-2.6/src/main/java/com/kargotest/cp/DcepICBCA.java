package com.kargotest.cp;

import net.freeutils.httpserver.HTTPServer;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DcepICBCA {
    String jsonText;
    String res = "";

    String patternDate = "yyyy-MM-dd";
    String patternTime = "hh:mm:ss";

    SimpleDateFormat DateFormat = new SimpleDateFormat(patternDate);
    SimpleDateFormat timeFormat = new SimpleDateFormat(patternTime);
    String date = DateFormat.format(new Date());
    String time = timeFormat.format(new Date());
    JSONParser parser = new JSONParser();

    private String qrcode;
    int tradeState;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getQrcode() { return qrcode; }

    JSONObject bizConetntJson;

    public DcepICBCA(JSONObject bizContent) {
        bizConetntJson = bizContent;
            if (!Objects.isNull(bizConetntJson.get("amount"))) {
                this.setAmount(Integer.valueOf(bizConetntJson.get("amount").toString()));
           }
    }

    public String revealResponse() {
        JSONObject reveal = new JSONObject();
        jsonText = reveal.toString();

        return jsonText;
    }

    public String redempResponse() {
        JSONObject redemp = new JSONObject();
        JSONObject bizContent = new JSONObject();
        qrcode = bizConetntJson.get("qrcode").toString();

        bizContent.put("err_code", "1010");
        bizContent.put("return_msg", "调用RPC服务成功");

        String returnCode = qrcode.substring(qrcode.length() - 3);
        Character resultCode = qrcode.charAt(15);

        if (!returnCode.equals("110") && !returnCode.equals("119")) {
            if (resultCode == '1') {
                jsonText = " {\"response_biz_content\":{\"err_code\":\"1010\",\"return_msg\":\"调用RPC服务成功\",\"result_code\":\"1\",\"err_code_des\":\"未知错误\",\"term_id\":\"\",\"merchant_id\":\"M00000124\",\"chantype\":126,\"return_code\":\"0\",\"sub_merchant_id\":\"S102644007\"},\"sign\":\"K2+1qhUYm6QqnFIxcy+OtNvpscIn1k1fXojVlbJb5g+KJXZbPDX4ZUoMWNiv66DMZivgbJuQM0rcyaZxHmxa3jJnkeLKDmluHBzV2BaEqmH7IjHu33ZP7/FQz497q30EISP7hROr0/q9Ch04i4k+9KmcuAIlDq3W+8cU3ka6q6A=\"}";

            }
            else if (resultCode == '2') {
                jsonText = " {\"response_biz_content\":{\"err_code\":\"1010\",\"return_msg\":\"调用RPC服务成功\",\"result_code\":\"2\",\"err_code_des\":\"输密中\",\"term_id\":\"\",\"merchant_id\":\"M00000124\",\"chantype\":126,\"return_code\":\"0\",\"sub_merchant_id\":\"S102644007\"},\"sign\":\"K2+1qhUYm6QqnFIxcy+OtNvpscIn1k1fXojVlbJb5g+KJXZbPDX4ZUoMWNiv66DMZivgbJuQM0rcyaZxHmxa3jJnkeLKDmluHBzV2BaEqmH7IjHu33ZP7/FQz497q30EISP7hROr0/q9Ch04i4k+9KmcuAIlDq3W+8cU3ka6q6A=\"}";

            }
            else {
                JSONObject responseJson;
                String responseSample = "{\"amount\":\"1\",\"cash_amount\":\"1\",\"promotion_detail\":\"\",\"openid\":\"M00000124|WT20210707500094656031\",\"custom_bank_code\":\"C1010211000012\",\"return_msg\":\"调用RPC服务成功\",\"date_end\":\"2021-07-07\",\"chantype\":126,\"term_id\":\"\",\"merchant_id\":\"M00000124\",\"custom_bank_name\":\"中国工商银行\",\"sub_merchant_id\":\"S102644007\",\"sub_openid\":\"S102644007|WT20210707500094656031\",\"out_trade_no\":\"38621000049-000820974215\",\"settlement_amount\":\"1\",\"trade_type\":\"MIRCOPAY\",\"result_code\":\"0\",\"time_end\":\"13:45:18\",\"attach\":\"KargoTest\",\"return_code\":\"0\",\"order_id\":\"20210707500110200324\",\"currtype\":\"CNY\"}";
                try {
                    this.setAmount(Integer.valueOf(bizConetntJson.get("amount").toString()));
                    responseJson = (JSONObject) parser.parse(responseSample);
                    responseJson.put("return_code", "0");
                    responseJson.put("result_code", "0");
                    responseJson.put("date_end", date);
                    responseJson.put("time_end", time);
                    responseJson.put("order_id", date.replaceAll("-","") + "0000" + (int)(Math.random() * 10000000 + 1));
                    responseJson.put("refund_success_date", date);
                    responseJson.put("out_trade_no", bizConetntJson.get("out_trade_no"));
                    responseJson.put("settlement_refund_amount", amount);
                    responseJson.put("amount", amount);
                    responseJson.put("cash_amount", amount);

                    redemp.put("response_biz_content", responseJson);
                    redemp.put("sign", "qYFtkO47KmCnSwnNKtF8GHMm1MI3p4X4fg1yvkwkPW6qSMAgdvNoSDCDxSSLKImf5wkB6m8D9Ksgiepz6mpQNVzyNRrCIz+Vk65cwrqk3eGfl4yJz1JlUxVDjgjLwIrpZ2tOgPLL7D6L9RbA9+QETTR/MY51p01l5AbOcQLbyuM=");
                } catch (ParseException pe){
                    System.out.println(pe.getMessage());
                }

                jsonText = redemp.toString();

            }


            return jsonText;
        }

        if (returnCode.equals("110")) {
            jsonText = "{\"sign\":\"m4Ch3f43O8h7eW4hyQo94Mk0L\",\"response_biz_content\":{ \"return_code\":10650001,\"return_ms\":\"系统内部错误\",\"msg_id\":\"urcnl24ciutr9\"}}";
        }
        if (returnCode.equals("119")) {
            jsonText = "{\"sign\":\"m4Ch3f43O8h7eW4hyQo94Mk0L\",\"response_biz_content\":{ \"return_code\":10650001,\"return_ms\":\"未知错误\",\"msg_id\":\"urcnl24ciutr9\"}}";
        }

        return jsonText;
    }

    public String queryResponse() {
        JSONObject query = new JSONObject();
        JSONObject response_biz_content = new JSONObject();
        amount = Integer.valueOf(bizConetntJson.get("amount").toString());

        if (amount > 7 || amount == 1) {
            tradeState = 1;
            response_biz_content.put("merchant_bank_code", "C1010211000012");
            response_biz_content.put("commodity", "KargoTestGoods");
            response_biz_content.put("ordervalidtime", time);
            response_biz_content.put("cash_amount", amount);
            response_biz_content.put("trade_state", tradeState); // 1-支付成功; 2-未支付; 3-已关闭; 4-支付失败; 5-已撤销; 6-用户支付中; 7-转入退款;
            response_biz_content.put("coupon_count", 0);
            response_biz_content.put("custom_bank_code", "C1010511003703");
            response_biz_content.put("return_msg", "调用RPC服务成功");
            response_biz_content.put("merchant_id", bizConetntJson.get("merchant_id"));
            response_biz_content.put("sub_merchant_id", bizConetntJson.get("sub_merchant_id"));
            response_biz_content.put("merchant_short_name", "大连罗森便利店有限公司");
            response_biz_content.put("order_amount", amount);
            response_biz_content.put("trade_state_desc", "");
            response_biz_content.put("alread_ref_amount", 0);
            response_biz_content.put("pay_type", "TT01");
            response_biz_content.put("attach", "KargoTest");
            response_biz_content.put("time_end", time);
            response_biz_content.put("coupon_amount", 0);
            response_biz_content.put("amount", amount);
            response_biz_content.put("order_date_time", date + time);
            response_biz_content.put("merchant_order", bizConetntJson.get("out_trade_no"));
            response_biz_content.put("merchant_wallet_name", "大连罗森便利店有限公司");
            response_biz_content.put("merchant_name", "大连罗森便利店有限公司");
            response_biz_content.put("date_end", date);
            response_biz_content.put("term_id", "");
            response_biz_content.put("cr_qrcode", "");
            response_biz_content.put("chantype", 126);
            response_biz_content.put("ordervaliddate", "2021-07-05");
            response_biz_content.put("custom_bank_name", "中国建设银行");
            response_biz_content.put("out_trade_no", "38621000049-000820559617");
            response_biz_content.put("trade_type", "MIRCOPAY");
            response_biz_content.put("settlement_amount", amount);
            response_biz_content.put("result_code", "0");
            response_biz_content.put("return_code", "0");
            response_biz_content.put("order_id", date.replaceAll("-", "") + "0000" + (int) (Math.random() * 10000000 + 1));
            response_biz_content.put("cash_currtype", "CNY");
            response_biz_content.put("currtype", "CNY");
            response_biz_content.put("status", 3);

            query.put("response_biz_content", response_biz_content);
            query.put("sign", "feTE/L+Dh2rsFl7jSh22YsWNJNWTGGvUJYTdox4QhtLEteR7DoTzGspfJP6zOWGv2wcFBYp4jmSuhnnaK7VZZA9iXCS1x0gMcX5y9xXt6yXSt+BZ7X+A9khx6RI9AuhTRpTCFRzW68S0tAXioFTc0449Ldb5e6279qH3V17H3SQ=");


            jsonText = query.toString();

            return jsonText;
        }
        else {
            response_biz_content.put("result_code", "0");
            response_biz_content.put("return_code", "0");
            response_biz_content.put("merchant_order", bizConetntJson.get("out_trade_no"));
            response_biz_content.put("trade_state", amount);

            query.put("response_biz_content", response_biz_content);
            query.put("sign", "feTE/L+Dh2rsFl7jSh22YsWNJNWTGGvUJYTdox4QhtLEteR7DoTzGspfJP6zOWGv2wcFBYp4jmSuhnnaK7VZZA9iXCS1x0gMcX5y9xXt6yXSt+BZ7X+A9khx6RI9AuhTRpTCFRzW68S0tAXioFTc0449Ldb5e6279qH3V17H3SQ=");

            jsonText = query.toString();

            return jsonText;


        }

    }

    public String refundResponse() {
        JSONObject refund = new JSONObject();
        String responseSample = "{\"cash_amount\":359,\"custom_bank_code\":\"C1010211000012\",\"coupon_refund_amount\":0,\"return_msg\":\"调用RPC服务成功\",\"merchant_id\":\"M00000124\",\"sub_merchant_id\":\"S102644007\",\"cash_refund_amount\":359,\"order_amount\":359,\"refund_amount\":359,\"settlement_total_amount\":359,\"out_refund_no\":\"000820996831\",\"chantype\":126,\"refund_id\":\"20210709000140315319\",\"custom_bank_name\":\"中国工商银行\",\"coupon_refund_list\":[],\"settlement_refund_amount\":359,\"out_trade_no\":\"38621000049-000820996731\",\"result_code\":\"0\",\"refund_success_date\":\"2021-07-09\",\"coupon_refund_count\":0,\"return_code\":\"0\",\"order_id\":\"20210709000140315319\",\"cash_currtype\":\"CNY\",\"currtype\":\"CNY\"}";
        try {
            JSONObject responseJson = (JSONObject) parser.parse(responseSample);
            amount = Integer.valueOf(bizConetntJson.get("amount").toString());
            responseJson.put("cash_amount", amount);
            responseJson.put("cash_refund_amount", amount);
            responseJson.put("order_amount", amount);
            responseJson.put("refund_amount", amount);
            responseJson.put("settlement_total_amount", amount);
            responseJson.put("out_trade_no", bizConetntJson.get("out_trade_no"));
            responseJson.put("out_refund_no", bizConetntJson.get("out_refund_no"));
            responseJson.put("refund_success_date", date);
            responseJson.put("order_id", date.replaceAll("-","") + "0000" + (int)(Math.random() * 10000000 + 1));

            refund.put("response_biz_content", responseJson);
            refund.put("sign", "EgDoPqL76qAw0hh0/87/Gd4QT7+JJETbzWeXOeUgn7n47FpEQ5bv2xAPsUlatBJywk2xlPy78yK9uWQMOG/fcaZ1V8UuKscOOWc3iaB1o8qGpEdu6x3hWbcqUMpAFdqyU+8rRqStDWXqDVJ24eEd/XHW3j6GiLDFQmjJMF+PxmE=");
            jsonText = refund.toString();

        }catch (ParseException pe){
            System.out.println(pe.getMessage());
        }

        return jsonText;
    }

    public String getRes() {
        return res;
    }

}
