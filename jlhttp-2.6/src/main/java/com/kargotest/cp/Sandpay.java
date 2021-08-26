package com.kargotest.cp;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandpay {
    JSONObject sandpayJsonData;
    JSONObject reqDatabody;

    public Sandpay(String sandpayData){
        JSONParser parser = new JSONParser();
        try {
            sandpayJsonData = (JSONObject)parser.parse(sandpayData); // data filed
            reqDatabody = (JSONObject)sandpayJsonData.get("body"); // data -> body
        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
        }
    }

    public String sandPayResponse() {
        String responseStr = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = timeFormat.format(new Date());
        JSONObject head = new JSONObject();
        head.put("respTime", date);
        head.put("respMsg", "成功");
        head.put("version", "1.0");
        head.put("respCode", "000000");

        JSONObject body = new JSONObject();
        body.put("totalAmount", reqDatabody.get("totalAmount"));
        body.put("clearDate", date);
        body.put("tradeNo", date.substring(2) + Math.round(Math.random()*100));
        body.put("payTime", date);
        body.put("buyerPayAmount", reqDatabody.get("totalAmount"));
        body.put("cardBalance", "000000015826");
        body.put("orderCode", reqDatabody.get("orderCode"));
        body.put("discAmount", "0");

        JSONObject data = new JSONObject();
        data.put("head", head);
        data.put("body", body);

        responseStr = "charset=UTF-8&signType=01&sign=tH 4yf5gf7pJXnE2ua9XcdPthNnmmJBn jWCJ6R9M/jHCPZX ikOmfJ8zbIrQrDbFsjZW3RHpGUWlrs/aJQfUUcJfkIVqPhJlJP/R/ZdSvyiSTe90Z23UqiEjgveHvK 8HocnnQfLEdpPv2YoG64RyS7Dam2IUKwTLcdBNPXjJMRv25kIJBXB0ppP4qNxtcRFZ/DrSVQKnOOCQcQAZphdzLThAh8zFx7hZ1Fe0FuKl7Xf4q2ndjQCGonxLUGMaWIGxM9uX/LlqPZZFVCrJRz0eJeY8cX/bqnL5exATlLKI9n5tySEkE3f qkj7jS6YUSqvFradiOBxQ zbjs8U19OQ==&data=";

        return responseStr + data.toJSONString();
    }

    public String sandCancelResponse() {
        String responseStr = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String datetime = timeFormat.format(new Date());
        JSONObject head = new JSONObject();
        head.put("respTime", datetime);
        head.put("respMsg", "成功");
        head.put("version", "1.0");
        head.put("respCode", "000000");

        JSONObject body = new JSONObject();
        String date = datetime.substring(0,8);
        body.put("extend", "");
        body.put("clearDate", datetime.substring(0,8));
        body.put("tradeNo", date.substring(2) + Math.round(Math.random()*100));
        body.put("cancelAmount", reqDatabody.get("oriTotalAmount"));
        body.put("orderCode", reqDatabody.get("orderCode"));

        JSONObject data = new JSONObject();
        data.put("head", head);
        data.put("body", body);

        responseStr = "charset=UTF-8&signType=01&sign=tH 4yf5gf7pJXnE2ua9XcdPthNnmmJBn jWCJ6R9M/jHCPZX ikOmfJ8zbIrQrDbFsjZW3RHpGUWlrs/aJQfUUcJfkIVqPhJlJP/R/ZdSvyiSTe90Z23UqiEjgveHvK 8HocnnQfLEdpPv2YoG64RyS7Dam2IUKwTLcdBNPXjJMRv25kIJBXB0ppP4qNxtcRFZ/DrSVQKnOOCQcQAZphdzLThAh8zFx7hZ1Fe0FuKl7Xf4q2ndjQCGonxLUGMaWIGxM9uX/LlqPZZFVCrJRz0eJeY8cX/bqnL5exATlLKI9n5tySEkE3f qkj7jS6YUSqvFradiOBxQ zbjs8U19OQ==&data=";

        return responseStr + data.toJSONString();
    }

    public String sandRefundResponse() {
        String responseStr = "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String datetime = timeFormat.format(new Date());
        JSONObject head = new JSONObject();
        head.put("respTime", datetime);
        head.put("respMsg", "成功");
        head.put("version", "1.0");
        head.put("respCode", "000000");

        JSONObject body = new JSONObject();
        String date = datetime.substring(0,8);
        body.put("extend", "");
        body.put("clearDate", datetime.substring(0,8));
        body.put("tradeNo", datetime.substring(2) + Math.round(Math.random()*100));
        body.put("refundTime", date);
        body.put("orderCode", reqDatabody.get("orderCode"));
        body.put("surplusAmount", null);
        body.put("batchSerial", "1" + date + "86550204165" + Math.round(Math.random()*10000));
        body.put("refundAmount", "+" + reqDatabody.get("refundAmount"));

        JSONObject data = new JSONObject();
        data.put("head", head);
        data.put("body", body);

        responseStr = "charset=UTF-8&signType=01&sign=tH 4yf5gf7pJXnE2ua9XcdPthNnmmJBn jWCJ6R9M/jHCPZX ikOmfJ8zbIrQrDbFsjZW3RHpGUWlrs/aJQfUUcJfkIVqPhJlJP/R/ZdSvyiSTe90Z23UqiEjgveHvK 8HocnnQfLEdpPv2YoG64RyS7Dam2IUKwTLcdBNPXjJMRv25kIJBXB0ppP4qNxtcRFZ/DrSVQKnOOCQcQAZphdzLThAh8zFx7hZ1Fe0FuKl7Xf4q2ndjQCGonxLUGMaWIGxM9uX/LlqPZZFVCrJRz0eJeY8cX/bqnL5exATlLKI9n5tySEkE3f qkj7jS6YUSqvFradiOBxQ zbjs8U19OQ==&data=";

        return responseStr + data.toJSONString();
    }
}
