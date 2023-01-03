package com.tencent.wxcloudrun.utils;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Random;

public class JsapiTicketUtil {

    /***
     * 进行get请求
     * @param url
     * @param charset
     * @param timeout
     * @return
     */
    public static String sendGet(String url, String charset, int timeout)
    {
        String result = "";
        try
        {
            URL u = new URL(url);
            try
            {
                URLConnection conn = u.openConnection();
                conn.connect();
                conn.setConnectTimeout(timeout);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                String line="";
                while ((line = in.readLine()) != null)
                {

                    result = result + line;
                }
                in.close();
            } catch (IOException e) {
                return result;
            }
        }
        catch (MalformedURLException e)
        {
            return result;
        }

        return result;
    }

    /**
     * 获取access_token
     * @return
     */
    public static String getAccessToken(){
        String appid="wxdf2bfef7aaa15a33";
        String appSecret="e80cbf0165f8b400c4fac2d070127ce5";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret + "";
        String backData = JsapiTicketUtil.sendGet(url, "utf-8", 10000);
        String accessToken = (String) JSONObject.fromObject(backData).get("access_token");
        return accessToken;
    }

    /**
     * 获取jsapiTicket
     * @return
     */
    public static String getJSApiTicket(String accessToken){
        // 获取jsapiTicket
        //String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";
        String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
        String backData = JsapiTicketUtil.sendGet(urlStr, "utf-8", 10000);
        String ticket = (String) JSONObject.fromObject(backData).get("ticket");
        return ticket;

    }

    /**
     * 随机生成26位签名nonceStr
     * @return
     */
    public static String getNonceStr() {
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int number = random.nextInt(26);
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

    /**
     * 获取当前时间戳timestamp
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }


    /**
     * 进行sha1签名加密
     * @return
     */
    public static String getSha1(String str) throws Exception {
        if (null == str || str.length() == 0) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}