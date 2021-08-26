package com.kargotest.cp;

/*
{
	"communicationKey": "DZlq16oYdRlxPwC5H8kZYvGs69ZdMpPg",
	"accessToken": "WLul+HP3NMaCVd4pCOn1l4Th41j1Wy8Aae3QwP0qUnpSYaNiYqzTpBXbjidpfBPEJxcac097M6QI6DFpuEFPntGVGlA6hLxkLMSvxwNly1PGIk2x2ctgifvdmvO3IZSzvh+Y6mw/VZgORb6MFlVFfjt92Z4Sa+wSxPFb1IJf75NglEM4z4FH7CtdpqOUjSuO4U4lcyF/g9TY/PUjOAR+RQtBiixGdtLe/WD3ikYJ/LZFj7cRVmz33JomRJmalqrQ1e8jw4u2GriIjeQo9QwNqw==",
	"ftServer": "https://dev1-eng.kargotest.com:9443/NCCB/MMER11FTMainPlat"
}
 */

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DcepCCB {
    public String redempResponse(){
        String patternDate = "yyyyMMddhhmmss";
        SimpleDateFormat DateFormat = new SimpleDateFormat(patternDate);
        String date = DateFormat.format(new Date());

        JSONObject redemp = new JSONObject();
        JSONObject detailElement= new JSONObject();
        JSONArray detail = new JSONArray();

        redemp.put("SUCCESS", "true");
        detailElement.put ("STATUSCODE", "00");
        detailElement.put("ORDERID", "000824012734");
        detailElement.put("AMOUNT", "0.02");
        detailElement.put("ORDERDATE", date);
        detailElement.put("ACCNAME", "");
        detailElement.put("Pref_Amt", "0.00");
        detailElement.put("CmAvy_Cntnt", "");
        detail.add(detailElement);
        redemp.put("Detail_Grp", detail);

        return redemp.toString();
    }

    public String queryResponse(){
        JSONObject query = new JSONObject();
        query.put("RESULT", "U");
        query.put("ORDERID", "000824012734");
        query.put("AMOUNT", "0.02");
        query.put("TRACEID", "101011X3316291766994" + Math.round(Math.random()*100000));
        query.put("TfrOut_Acba", "");
        query.put("ERRORCODE", "");
        query.put("ERRORMSG", "");

        return query.toString();
    }

    public String queryRefund(){
        String result = "qWwcBrjJbgUPy70xs2Lhp/xKQ4LLZNXFcylZ4pTVdAuu2I+V7NRW6jXc/fMlqvMvg/kCgbFAF6kPKyKThIJ1F3XmLfgTJTJcEFb1+d/4q00PQ8eT1I7SEJ69+3ETrTQB7mA/puxpr+3CoGWv26H+LC+KFkPKLiCZrUcjnu91N8SWuKQJmLLqcrNcMGgQNcFpshKIpe/8wPTmenInZEis1qOPSNIjRSLvU1VaKs88ZeGRvSzcoQssXg6s4SlhvYDLK0mwwsHL0tZZ9iG2Dy28wWMgwgq1PR44QT/TquuSLWHtwSfKEgrjY6u4jWF17HUQvul/CIdC7EP2CkMhHrbL1yyPxz34ZkJ8fxHW1gjpMivF3nVw8XleARzsoL3CuE+7f8Aa6Y5AljP5mIEnjKcK1Q==";

        return result;
    }

}
